package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.proto.State;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProc;
import com.zachary.bifromq.basekv.store.api.IKVRangeReader;
import com.zachary.bifromq.basekv.store.exception.KVRangeException;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.zachary.bifromq.basekv.proto.State.StateType.Merged;
import static com.zachary.bifromq.basekv.proto.State.StateType.Purged;
import static com.zachary.bifromq.basekv.proto.State.StateType.Removed;
import static java.util.concurrent.CompletableFuture.completedFuture;

@Slf4j
class KVRangeQueryRunner implements IKVRangeQueryRunner {
    private interface QueryFunction<Req, Resp> {
        CompletableFuture<Resp> apply(IKVRangeReader rangeReader);
    }

    private final IKVRangeState kvRangeAccessor;
    private final IKVRangeCoProc coProc;
    private final Executor executor;
    private final Set<CompletableFuture<?>> runningQueries = Sets.newConcurrentHashSet();
    private final IKVRangeQueryLinearizer linearizer;
    //    private final ThreadLocal<IKVRangeReader> threaLocalRangeReader;
//    private final LoadingCache<Thread, IKVRangeReader> threaLocalRangeReader;
    private final AtomicBoolean closed = new AtomicBoolean();

    KVRangeQueryRunner(IKVRangeState kvRangeAccessor,
                       IKVRangeCoProc coProc,
                       Executor executor,
                       IKVRangeQueryLinearizer linearizer) {
        this.kvRangeAccessor = kvRangeAccessor;
        this.coProc = coProc;
        this.executor = executor;
        this.linearizer = linearizer;
    }

    // Execute a ROCommand
    @Override
    public CompletionStage<Boolean> exist(long ver, ByteString key, boolean linearized) {
        return submit(ver, rangeReader -> completedFuture(rangeReader.kvReader().exist(key)), linearized);
    }


    @Override
    public CompletionStage<Optional<ByteString>> get(long ver, ByteString key, boolean linearized) {
        return submit(ver, rangeReader -> completedFuture(rangeReader.kvReader().get(key)), linearized);
    }

    @Override
    public CompletableFuture<ByteString> queryCoProc(long ver, ByteString query, boolean linearized) {
        return submit(ver, rangeReader -> coProc.query(query, rangeReader.kvReader())
            .exceptionally(e -> {
                throw new InternalError("Query CoProc failed", e);
            }), linearized);
    }

    // Close the executor, the returned future will be completed when running commands finished and pending tasks
    // will be canceled
    public void close() {
        if (closed.compareAndSet(false, true)) {
            runningQueries.forEach(f -> f.cancel(true));
        }
    }

    private <ReqT, ResultT> CompletableFuture<ResultT> submit(long ver, QueryFunction<ReqT, ResultT> queryFn,
                                                              boolean linearized) {
        CompletableFuture<ResultT> onDone = new CompletableFuture<>();
        runningQueries.add(onDone);
        Runnable queryTask = () -> {
            if (onDone.isDone()) {
                return;
            }
            if (closed.get()) {
                onDone.cancel(true);
                return;
            }
            if (linearized) {
                linearizer.linearize()
                    .thenComposeAsync(v -> doQuery(ver, queryFn), executor)
                    .whenCompleteAsync((r, e) -> {
                        if (e != null) {
                            onDone.completeExceptionally(e);
                        } else {
                            onDone.complete(r);
                        }
                    }, executor);
            } else {
                doQuery(ver, queryFn).whenCompleteAsync((v, e) -> {
                    if (e != null) {
                        onDone.completeExceptionally(e);
                    } else {
                        onDone.complete(v);
                    }
                }, executor);
            }
        };
        onDone.whenComplete((v, e) -> runningQueries.remove(onDone));
        executor.execute(queryTask);
        return onDone;
    }

    private <ReqT, ResultT> CompletableFuture<ResultT> doQuery(long ver, QueryFunction<ReqT, ResultT> queryFn) {
        CompletableFuture onDone = new CompletableFuture();
        IKVRangeReader rangeReader = kvRangeAccessor.borrow();
        // return the borrowed reader when future completed
        onDone.whenComplete((v, e) -> kvRangeAccessor.returnBorrowed(rangeReader));
        State state = rangeReader.state();
        if (ver != rangeReader.ver()) {
            onDone.completeExceptionally(
                new KVRangeException.BadVersion("Version Mismatch: expect=" + rangeReader.ver() + ", actual=" + ver));
            return onDone;
        }
        if (state.getType() == Merged || state.getType() == Removed || state.getType() == Purged) {
            onDone.completeExceptionally(
                new KVRangeException.TryLater("Range has been " + state.getType().name().toLowerCase()));
            return onDone;
        }
        try {
            return queryFn.apply(rangeReader).whenCompleteAsync((v, e) -> {
                if (e != null) {
                    onDone.completeExceptionally(e);
                } else {
                    onDone.complete(v);
                }
            }, executor);
        } catch (Throwable e) {
            return CompletableFuture.failedFuture(new KVRangeException.InternalException(e.getMessage()));
        }
    }
}
