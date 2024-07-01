package com.zachary.bifromq.basekv.balance;

import com.zachary.bifromq.baseenv.EnvProvider;
import com.zachary.bifromq.basehookloader.BaseHookLoader;
import com.zachary.bifromq.basekv.balance.command.BalanceCommand;
import com.zachary.bifromq.basekv.balance.command.CommandType;
import com.zachary.bifromq.basekv.balance.option.KVRangeBalanceControllerOptions;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basekv.proto.KVRangeStoreDescriptor;
import com.google.common.util.concurrent.MoreExecutors;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Timer.Sample;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
public class KVRangeBalanceController {

    private enum State {
        Init,
        Started,
        Closed
    }

    private final KVRangeBalanceControllerOptions options;
    private final IBaseKVStoreClient storeClient;
    private final CommandRunner commandRunner;
    private final List<StoreBalancer> balancers = new ArrayList<>();
    private final ScheduledExecutorService executor;
    private final AtomicBoolean scheduling = new AtomicBoolean();
    private final AtomicReference<State> state = new AtomicReference<>(State.Init);
    private final boolean executorOwner;

    private MetricManager metricsManager;
    private Disposable descriptorSub;
    private ScheduledFuture scheduledFuture;

    public KVRangeBalanceController(IBaseKVStoreClient storeClient,
                                    KVRangeBalanceControllerOptions balancerOptions,
                                    ScheduledExecutorService executor) {
        this.options = balancerOptions.toBuilder()
            .balancers(
                balancerOptions.getBalancers().stream()
                    .distinct()
                    .collect(Collectors.toList())
            )
            .build();
        this.storeClient = storeClient;
        this.commandRunner = new CommandRunner(storeClient);
        executorOwner = executor == null;
        if (executor == null) {
            this.executor = ExecutorServiceMetrics.monitor(Metrics.globalRegistry, new ScheduledThreadPoolExecutor(1,
                EnvProvider.INSTANCE.newThreadFactory("balance-executor")), "balanceExecutor");
        } else {
            this.executor = executor;
        }
    }

    public void start(String localStoreId) {
        if (state.compareAndSet(State.Init, State.Started)) {
            Map<String, IStoreBalancerFactory> balancerFactoryMap = BaseHookLoader.load(IStoreBalancerFactory.class);
            for (String factoryName : options.getBalancers()) {
                if (!balancerFactoryMap.containsKey(factoryName)) {
                    log.warn("There is no balancer factory named: {}", factoryName);
                    continue;
                }
                StoreBalancer balancer = balancerFactoryMap.get(factoryName).newBalancer(localStoreId);
                balancers.add(balancer);
            }
            this.metricsManager = new MetricManager(localStoreId);
            log.info("KVRangeBalanceController start to balance in store: {}", localStoreId);
            descriptorSub = this.storeClient.describe()
                .distinctUntilChanged()
                .subscribe(sds -> executor.execute(() -> updateStoreDescriptors(sds)));
            scheduleLater();
        }
    }

    public void stop() {
        if (state.compareAndSet(State.Started, State.Closed)) {
            descriptorSub.dispose();
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
            if (executorOwner) {
                MoreExecutors.shutdownAndAwaitTermination(executor, 5, TimeUnit.SECONDS);
            }
        }
    }

    private void updateStoreDescriptors(Set<KVRangeStoreDescriptor> descriptors) {
        for (StoreBalancer balancer : balancers) {
            balancer.update(descriptors);
        }
        scheduleLater();
    }

    private void scheduleLater() {
        if (state.get() == State.Started && scheduling.compareAndSet(false, true)) {
            scheduledFuture =
                executor.schedule(this::scheduleNow, options.getScheduleIntervalInMs(), TimeUnit.MILLISECONDS);
        }
    }

    private void scheduleNow() {
        metricsManager.scheduleCount.increment();
        for (StoreBalancer fromBalancer : balancers) {
            Optional<BalanceCommand> commandOpt = fromBalancer.balance();
            if (commandOpt.isPresent()) {
                BalanceCommand commandToRun = commandOpt.get();
                log.debug("Run command: {}", commandToRun);
                String balancerName = fromBalancer.getClass().getSimpleName();
                String cmdName = commandToRun.getClass().getSimpleName();
                Sample start = Timer.start();
                CommandType commandType = commandToRun.type();
                commandRunner.run(commandToRun)
                    .whenCompleteAsync((r, e) -> {
                        MetricManager.CommandMetrics metrics = metricsManager.getCommandMetrics(balancerName, cmdName);
                        if (r == CommandRunner.Result.Succeed) {
                            metrics.cmdSucceedCounter.increment();
                            start.stop(metrics.cmdRunTimer);
                            // Always schedule later after recovery command
                            if (commandType == CommandType.RECOVERY) {
                                scheduling.set(false);
                                scheduleLater();
                            } else {
                                scheduleNow();
                            }
                        } else {
                            scheduling.set(false);
                            if (e != null) {
                                log.error("Should not be here, error when run command", e);
                            }
                            metrics.cmdFailedCounter.increment();
                            scheduleLater();
                        }
                    }, executor);
                return;
            }
        }
        // no command to run
        scheduling.set(false);
    }

    static class MetricManager {

        private final Tags tags;
        private final Counter scheduleCount;
        private final Map<MetricsKey, CommandMetrics> metricsMap = new HashMap<>();

        public MetricManager(String storeId) {
            tags = Tags.of("storeId", storeId);
            scheduleCount = Counter.builder("basekv.balance.scheduled")
                .tags(tags)
                .register(Metrics.globalRegistry);
        }

        public CommandMetrics getCommandMetrics(String fromBalancer, String command) {
            MetricsKey metricsKey = MetricsKey.builder()
                .balancer(fromBalancer)
                .cmdName(command)
                .build();
            return metricsMap.computeIfAbsent(metricsKey,
                k -> new CommandMetrics(tags.and("balancer", k.balancer).and("cmd", k.cmdName)));
        }

        public void close() {
            Metrics.globalRegistry.remove(scheduleCount);
            metricsMap.values().forEach(CommandMetrics::clear);
        }

        @Builder
        private static class MetricsKey {
            private String balancer;
            private String cmdName;
        }

        static class CommandMetrics {
            Counter cmdSucceedCounter;
            Counter cmdFailedCounter;
            Timer cmdRunTimer;

            private CommandMetrics(Tags tags) {
                cmdSucceedCounter = Counter.builder("basekv.balance.cmd.succeed")
                    .tags(tags)
                    .register(Metrics.globalRegistry);
                cmdFailedCounter = Counter.builder("basekv.balance.cmd.failed")
                    .tags(tags)
                    .register(Metrics.globalRegistry);
                cmdRunTimer = Timer.builder("basekv.balance.cmd.run")
                    .tags(tags)
                    .register(Metrics.globalRegistry);
            }

            private void clear() {
                Metrics.globalRegistry.remove(cmdSucceedCounter);
                Metrics.globalRegistry.remove(cmdFailedCounter);
                Metrics.globalRegistry.remove(cmdRunTimer);
            }
        }


    }
}
