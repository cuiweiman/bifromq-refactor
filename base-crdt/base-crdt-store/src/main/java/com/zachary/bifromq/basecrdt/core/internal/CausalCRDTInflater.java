package com.zachary.bifromq.basecrdt.core.internal;

import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;
import com.zachary.bifromq.basecrdt.core.api.ICausalCRDT;
import com.zachary.bifromq.basecrdt.core.exception.CRDTEngineException;
import com.zachary.bifromq.basecrdt.core.util.Log;
import com.zachary.bifromq.basecrdt.proto.Replacement;
import com.zachary.bifromq.basecrdt.proto.Replica;
import com.zachary.bifromq.basecrdt.proto.StateLattice;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
abstract class CausalCRDTInflater<T extends IDotStore, O extends ICRDTOperation, C extends ICausalCRDT<O>> {
    private final AtomicBoolean inflationScheduled = new AtomicBoolean(false);
    private final AtomicBoolean compactionScheduled = new AtomicBoolean(false);
    private final AtomicBoolean taskExecuting = new AtomicBoolean(false);
    private final AtomicReference<CompletableFuture<Void>> stopSignal = new AtomicReference<>();
    private final Replica replica;
    private final IReplicaStateLattice replicaStateLattice;
    private final ScheduledExecutorService executor;
    private final Duration inflationInterval;
    private final T dotStore;
    private final C crdt;
    private final ConcurrentLinkedQueue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();
    private final MetricManager metricManager;
    private volatile ScheduledFuture compactionTask;

    private volatile OperationExecTask currentOp = null;
    private volatile JoinTask currentJoin = null;

    CausalCRDTInflater(long engineId, Replica replica, IReplicaStateLattice stateLattice,
                       ScheduledExecutorService executor,
                       Duration inflationInterval) {
        this.replica = replica;
        this.replicaStateLattice = stateLattice;
        this.executor = executor;
        this.inflationInterval = inflationInterval;
        this.metricManager = new MetricManager(Tags.of("store.id", Long.toUnsignedString(engineId))
                .and("replica.uri", replica.getUri())
                .and("replica.id", Base64.getEncoder().encodeToString(replica.getId().toByteArray())));
        try {
            dotStore = dotStoreType().getDeclaredConstructor().newInstance();
            crdt = newCRDT(replica, dotStore, this::execute);
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException("Unknown dot store implementation", e);
        }
        // build aggregated view of existing events
        stateLattice.lattices().forEachRemaining(causalState -> ((DotStore) dotStore).add(causalState));
    }

    protected abstract C newCRDT(Replica replica, T dotStore, CausalCRDT.CRDTOperationExecutor<O> executor);

    final C getCRDT() {
        return crdt;
    }

    final Replica id() {
        return replica;
    }

    final CompletableFuture<Void> stop() {
        CompletableFuture<Void> onStop = stopSignal.updateAndGet(current -> {
            if (current == null) {
                return new CompletableFuture();
            }
            return current;
        });
        scheduleInflation();
        metricManager.close();
        return onStop;
    }

    final CompletableFuture<Void> join(Iterable<Replacement> delta) {
        if (stopSignal.get() != null) {
            // silently drop the request
            return CompletableFuture.completedFuture(null);
        }
        CompletableFuture<Void> ret;
        synchronized (this) {
            if (currentJoin == null) {
                currentJoin = new JoinTask(delta, new CompletableFuture<>());
            } else {
                currentJoin.add(delta);
            }
            ret = currentJoin.onDone;
        }
        scheduleInflation();
        return ret;
    }

    CompletableFuture<Optional<Iterable<Replacement>>> delta(Map<ByteString,
            NavigableMap<Long, Long>> coveredLatticeEvents,
                                                             Map<ByteString,
                                                                     NavigableMap<Long, Long>> coveredHistoryEvents,
                                                             int maxEvents) {
        CompletableFuture<Optional<Iterable<Replacement>>> onDone = new CompletableFuture<>();
        submitTask(() -> {
            Timer.Sample sample = Timer.start();
            onDone.complete(replicaStateLattice
                    .delta(coveredLatticeEvents, coveredHistoryEvents, maxEvents));
            sample.stop(metricManager.deltaTimer);
        });
        return onDone;
    }

    Map<ByteString, NavigableMap<Long, Long>> latticeEvents() {
        return replicaStateLattice.latticeIndex();
    }

    Map<ByteString, NavigableMap<Long, Long>> historyEvents() {
        return replicaStateLattice.historyIndex();
    }

    protected abstract ICoalesceOperation<T, O> startCoalescing(O op);

    protected abstract Class<? extends T> dotStoreType();

    private CompletableFuture<Void> execute(O op) {
        if (stopSignal.get() != null) {
            // silently drop the request
            return CompletableFuture.failedFuture(CRDTEngineException.CRDT_IS_CLOSE);
        }
        CompletableFuture<Void> ret;
        synchronized (this) {
            if (currentOp == null) {
                currentOp = new OperationExecTask(op, new CompletableFuture<>());
            } else {
                currentOp.coalesce(op);
            }
            ret = currentOp.onDone;
        }
        scheduleInflation();
        return ret;
    }

    private void submitTask(Runnable task) {
        taskQueue.add(task);
        startExecutor();
    }

    private void startExecutor() {
        if (taskExecuting.compareAndSet(false, true)) {
            executor.execute(() -> {
                try {
                    while (!taskQueue.isEmpty()) {
                        Runnable task = taskQueue.poll();
                        task.run();
                        Thread.yield();
                    }
                } catch (Throwable e) {
                    Log.error(log, "Failed to execute inflater[{}] task", replica, e);
                }
                taskExecuting.set(false);
                if (!taskQueue.isEmpty()) {
                    startExecutor();
                }
            });
        }
    }

    private void scheduleInflation() {
        if (inflationScheduled.compareAndSet(false, true)) {
            Runnable task = () -> submitTask(() -> {
                try {
                    inflate();
                    scheduleCompaction();
                } catch (Throwable e) {
                    Log.error(log, "Inflation[{}] error: {}", replica, e);
                } finally {
                    inflationScheduled.set(false);
                }
                if (currentOp != null || currentJoin != null) {
                    scheduleInflation();
                } else if (stopSignal.get() != null) {
                    if (compactionTask != null) {
                        compactionTask.cancel(true);
                    }
                    stopSignal.get().complete(null);
                }
            });
            executor.schedule(task, inflationInterval.toMillis(), TimeUnit.MILLISECONDS);
        }
    }

    private void scheduleCompaction() {
        if (compactionScheduled.compareAndSet(false, true)) {
            compactionTask = executor.schedule(() -> submitTask(() -> {
                Timer.Sample sample = Timer.start();
                if (replicaStateLattice.compact() && stopSignal.get() == null) {
                    compactionScheduled.set(false);
                    scheduleCompaction();
                } else {
                    compactionScheduled.set(false);
                }
                sample.stop(metricManager.compactionTimer);
            }), replicaStateLattice.historyDuration().toMillis(), TimeUnit.MILLISECONDS);
        }
    }

    private void inflate() {
        Timer.Sample sample = Timer.start();
        List<Replacement> deltas = null;
        Optional<JoinTask> opTask = buildOpTask();
        if (opTask.isPresent()) {
            deltas = opTask.get().deltas;
        }
        JoinTask joinTask;
        synchronized (this) {
            joinTask = currentJoin;
            currentJoin = null;
        }
        if (joinTask != null) {
            if (deltas == null) {
                deltas = joinTask.deltas;
            } else {
                deltas.addAll(joinTask.deltas);
            }
        }

        if (deltas != null) {
            // join delta first
            IReplicaStateLattice.JoinDiff joinDiff = replicaStateLattice.join(deltas);

            // inflate the dotStore
            List<StateLattice> adds = Lists.newArrayList();
            List<StateLattice> rems = Lists.newArrayList();
            for (StateLattice lattice : joinDiff.adds()) {
                if (((DotStore) dotStore).add(lattice)) {
                    adds.add(lattice);
                }
            }
            for (StateLattice lattice : joinDiff.removes()) {
                if (((DotStore) dotStore).remove(lattice)) {
                    rems.add(lattice);
                }
            }
            if (!adds.isEmpty() || !rems.isEmpty()) {
                // tell CRDT about what happened in the dot store
                ((CausalCRDT<T, O>) crdt).afterInflation(adds, rems);
            }
        }
        sample.stop(metricManager.inflationTimer);
        if (opTask.isPresent()) {
            opTask.get().onDone.complete(null);
        }
        if (joinTask != null) {
            joinTask.onDone.complete(null);
        }
    }

    private Optional<JoinTask> buildOpTask() {
        OperationExecTask task;
        synchronized (this) {
            task = currentOp;
            currentOp = null;
        }
        if (task != null) {
            JoinTask joinTask = new JoinTask(task.op.delta(dotStore, replicaStateLattice::nextEvent),
                    new CompletableFuture<>());
            joinTask.onDone.whenComplete((v, e) -> task.onDone.complete(null));
            return Optional.of(joinTask);
        }
        return Optional.empty();
    }

    private static class JoinTask {
        public final List<Replacement> deltas = Lists.newLinkedList();
        public final CompletableFuture<Void> onDone;

        JoinTask(Iterable<Replacement> delta, CompletableFuture<Void> onDone) {
            delta.forEach(deltas::add);
            this.onDone = onDone;
        }

        public void add(Iterable<Replacement> next) {
            next.forEach(deltas::add);
        }
    }

    private class OperationExecTask {
        private final CompletableFuture<Void> onDone;
        private final ICoalesceOperation<T, O> op;

        OperationExecTask(O op, CompletableFuture<Void> onDone) {
            this.op = startCoalescing(op);
            this.onDone = onDone;
        }

        public void coalesce(O next) {
            op.coalesce(next);
        }
    }

    private class MetricManager {
        public final Timer inflationTimer;
        public final Timer deltaTimer;
        public final Timer compactionTimer;
        public final Gauge eventSizeGauge;

        MetricManager(Tags tags) {
            inflationTimer = Metrics.timer("basecrdt.inflation.time", tags);
            deltaTimer = Metrics.timer("basecrdt.delta.time", tags);
            compactionTimer = Metrics.timer("basecrdt.compact.time", tags);
            eventSizeGauge = Gauge.builder("basecrdt.event.size", replicaStateLattice::size)
                    .tags(tags)
                    .register(Metrics.globalRegistry);
        }

        void close() {
            Metrics.globalRegistry.remove(inflationTimer.getId());
            Metrics.globalRegistry.remove(deltaTimer.getId());
            Metrics.globalRegistry.remove(compactionTimer.getId());
            Metrics.globalRegistry.remove(eventSizeGauge);
        }
    }
}
