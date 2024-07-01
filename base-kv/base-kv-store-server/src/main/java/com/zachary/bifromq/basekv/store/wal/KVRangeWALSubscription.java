package com.zachary.bifromq.basekv.store.wal;

import com.zachary.bifromq.basekv.proto.KVRangeSnapshot;
import com.zachary.bifromq.basekv.raft.proto.LogEntry;
import com.zachary.bifromq.basekv.store.util.AsyncRunner;
import com.google.protobuf.InvalidProtocolBufferException;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@Slf4j
class KVRangeWALSubscription implements IKVRangeWALSubscription {
    private final long maxFetchBytes;
    private final IKVRangeWAL wal;
    private final Executor executor;
    private final AsyncRunner fetchRunner;
    private final AsyncRunner applyRunner;
    private final IKVRangeWALSubscriber subscriber;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final AtomicBoolean fetching = new AtomicBoolean();
    private final AtomicBoolean stopped = new AtomicBoolean();
    private volatile long lastFetchedIdx;
    private volatile long commitIdx = -1;

    KVRangeWALSubscription(long maxFetchBytes,
                           IKVRangeWAL wal,
                           Observable<Long> commitIndex,
                           long startIndex,
                           IKVRangeWALSubscriber subscriber,
                           Executor executor) {
        this.maxFetchBytes = maxFetchBytes;
        this.wal = wal;
        this.executor = executor;
        this.fetchRunner = new AsyncRunner(executor);
        this.applyRunner = new AsyncRunner(executor);
        this.subscriber = subscriber;
        this.lastFetchedIdx = startIndex - 1;
        this.subscriber.onSubscribe(this);
        disposables.add(wal.snapshotInstallTask()
            .subscribe(task -> fetchRunner.add(() -> {
                try {
                    KVRangeSnapshot snap = KVRangeSnapshot.parseFrom(task.snapshot);
                    lastFetchedIdx = snap.getLastAppliedIndex();
                    commitIdx = -1;
                    applyRunner.cancelAll();
                    applyRunner.add(applySnapshot(snap, task.onDone));
                } catch (InvalidProtocolBufferException e) {
                    task.onDone.completeExceptionally(e);
                }
            })));
        disposables.add(commitIndex
            .subscribe(c -> fetchRunner.add(() -> {
                commitIdx = c;
                scheduleFetchWAL();
            })));
    }

    @Override
    public void stop() {
        if (stopped.compareAndSet(false, true)) {
            disposables.dispose();
            fetchRunner.cancelAll();
            applyRunner.cancelAll();
            fetchRunner.awaitDone().toCompletableFuture().join();
            applyRunner.awaitDone().toCompletableFuture().join();
        }
    }

    private void scheduleFetchWAL() {
        if (!stopped.get() && fetching.compareAndSet(false, true)) {
            fetchRunner.add(this::fetchWAL);
        }
    }

    private CompletableFuture<Void> fetchWAL() {
        if (lastFetchedIdx < commitIdx) {
            return wal.retrieveCommitted(lastFetchedIdx + 1, maxFetchBytes)
                .handleAsync((logEntries, e) -> {
                    if (e != null) {
                        log.error("Failed to retrieve log from wal", e);
                        fetching.set(false);
                        scheduleFetchWAL();
                    } else {
                        fetchRunner.add(() -> {
                            LogEntry entry = null;
                            while (logEntries.hasNext()) {
                                // no restore task interrupted
                                entry = logEntries.next();
                                applyRunner.add(applyLog(entry));
                            }
                            if (entry != null) {
                                lastFetchedIdx = entry.getIndex();
                            }
                            fetching.set(false);
                            if (lastFetchedIdx < commitIdx) {
                                scheduleFetchWAL();
                            }
                        });
                    }
                    return null;
                }, executor);
        } else {
            fetching.set(false);
            if (lastFetchedIdx < commitIdx) {
                scheduleFetchWAL();
            }
            return CompletableFuture.completedFuture(null);
        }
    }

    private Supplier<CompletableFuture<Void>> applyLog(LogEntry logEntry) {
        return () -> {
            CompletableFuture<Void> onDone = new CompletableFuture<>();
            if (onDone.isCancelled()) {
                return CompletableFuture.completedFuture(null);
            }
            CompletableFuture<Void> applyFuture = subscriber.apply(logEntry);
            onDone.whenComplete((v, e) -> {
                if (onDone.isCancelled()) {
                    applyFuture.cancel(true);
                }
            });
            applyFuture.whenCompleteAsync((v, e) -> fetchRunner.add(() -> {
                // always examine state and submit application task sequentially
                if (!onDone.isCancelled()) {
                    if (e != null) {
                        // reapply
                        applyRunner.addFirst(applyLog(logEntry));
                    }
                }
                onDone.complete(null);
            }), executor);
            return onDone;
        };
    }

    private Supplier<CompletableFuture<Void>> applySnapshot(KVRangeSnapshot snapshot, CompletableFuture<Void> onDone) {
        return () -> {
            if (onDone.isCancelled()) {
                return CompletableFuture.completedFuture(null);
            }
            CompletableFuture<Void> applyFuture = subscriber.apply(snapshot);
            onDone.whenComplete((v, e) -> {
                if (onDone.isCancelled()) {
                    applyFuture.cancel(true);
                }
            });
            applyFuture.whenCompleteAsync((v, e) -> {
                if (e != null) {
                    onDone.completeExceptionally(e);
                } else {
                    onDone.complete(null);
                }
            }, executor);
            return onDone;
        };
    }
}
