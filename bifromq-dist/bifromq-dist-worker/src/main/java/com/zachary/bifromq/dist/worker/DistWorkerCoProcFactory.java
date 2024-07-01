package com.zachary.bifromq.dist.worker;

import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProc;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProcFactory;
import com.zachary.bifromq.basekv.store.api.IKVRangeReader;
import com.zachary.bifromq.dist.client.IDistClient;
import com.zachary.bifromq.dist.worker.scheduler.DeliveryScheduler;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import com.zachary.bifromq.plugin.subbroker.ISubBrokerManager;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.DIST_MATCH_PARALLELISM;

@Slf4j
public class DistWorkerCoProcFactory implements IKVRangeCoProcFactory {
    private final IDistClient distClient;
    private final ISettingProvider settingProvider;
    private final IEventCollector eventCollector;
    private final ISubBrokerManager subBrokerManager;
    private final DeliveryScheduler scheduler;
    private final ExecutorService matchExecutor;

    public DistWorkerCoProcFactory(IDistClient distClient,
                                   ISettingProvider settingProvider,
                                   IEventCollector eventCollector,
                                   ISubBrokerManager subBrokerManager) {
        this.distClient = distClient;
        this.settingProvider = settingProvider;
        this.eventCollector = eventCollector;
        this.subBrokerManager = subBrokerManager;
        scheduler = new DeliveryScheduler(subBrokerManager);

        matchExecutor = ExecutorServiceMetrics.monitor(Metrics.globalRegistry,
            new ForkJoinPool(DIST_MATCH_PARALLELISM.get(), new ForkJoinPool.ForkJoinWorkerThreadFactory() {
                final AtomicInteger index = new AtomicInteger(0);

                @Override
                public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
                    ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
                    worker.setName(String.format("topic-matcher-%d", index.incrementAndGet()));
                    worker.setDaemon(false);
                    return worker;
                }
            }, null, false), "topic-matcher");
    }

    @Override
    public IKVRangeCoProc create(KVRangeId id, Supplier<IKVRangeReader> rangeReaderProvider) {
        return new DistWorkerCoProc(id, rangeReaderProvider, eventCollector, settingProvider, distClient,
            subBrokerManager, scheduler, matchExecutor);
    }

    public void close() {
        scheduler.close();
        matchExecutor.shutdown();
    }
}
