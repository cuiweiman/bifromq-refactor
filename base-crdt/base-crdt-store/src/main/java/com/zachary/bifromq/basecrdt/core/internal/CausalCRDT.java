
package com.zachary.bifromq.basecrdt.core.internal;

import com.google.common.collect.Sets;
import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;
import com.zachary.bifromq.basecrdt.core.api.ICausalCRDT;
import com.zachary.bifromq.basecrdt.proto.Replica;
import com.zachary.bifromq.basecrdt.proto.StateLattice;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @description: 因果CRDT
 * @author: cuiweiman
 * @date: 2024/9/23 17:59
 */
@Slf4j
abstract class CausalCRDT<T extends IDotStore, O extends ICRDTOperation> implements ICausalCRDT<O> {
    /**
     * 点存储访问器
     *
     * @param <T> 泛型
     */
    interface DotStoreAccessor<T extends IDotStore> {
        T fetch();
    }

    interface CRDTOperationExecutor<O extends ICRDTOperation> {
        CompletableFuture<Void> submit(O op);
    }

    private final Set<ObservableEmitter> emitters = Sets.newConcurrentHashSet();

    protected final Replica replica;
    protected final CRDTOperationExecutor<O> executor;
    protected final DotStoreAccessor<T> dotStoreAccessor;

    CausalCRDT(Replica replica, DotStoreAccessor<T> dotStoreAccessor, CRDTOperationExecutor<O> executor) {
        this.replica = replica;
        this.dotStoreAccessor = dotStoreAccessor;
        this.executor = executor;
    }

    @Override
    public Replica id() {
        return replica;
    }

    @Override
    public final CompletableFuture<Void> execute(O op) {
        return executor.submit(op);
    }

    @Override
    public final Observable<Long> inflation() {
        return Observable.create(emitter -> {
            emitters.add(emitter);
            emitter.setCancellable(() -> emitters.remove(emitter));
        });
    }

    /**
     * A notification from inflater about the batch changes made to the dot store
     *
     * @param addEvents
     * @param removeEvents
     */
    final void afterInflation(Iterable<StateLattice> addEvents, Iterable<StateLattice> removeEvents) {
        handleInflation(addEvents, removeEvents);
        long ts = System.nanoTime();
        emitters.forEach(e -> e.onNext(ts));
    }

    protected void handleInflation(Iterable<StateLattice> addEvents, Iterable<StateLattice> removeEvents) {
    }

    @Override
    public String toString() {
        return dotStoreAccessor.fetch().toString();
    }
}
