package com.zachary.bifromq.basecrdt.core.api;

import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.proto.Replica;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.CompletableFuture;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/20 17:08
 */
public interface ICausalCRDT<O extends ICRDTOperation> {

    /**
     * The identity of the CRDT replica
     */
    Replica id();

    /**
     * The type of the replica
     * 分片类型
     */
    CausalCRDTType type();

    /**
     * Execute an CRDT operation asynchronously
     * 异步执行 CRDT 的操作
     *
     * @param op 操作
     * @return 异步执行结果
     */
    CompletableFuture<Void> execute(O op);


    /**
     * The observable of inflation happens to the CRDT state overtime
     * {@link Observable} 观察者模式
     *
     * @return Observable
     */
    Observable<Long> inflation();
}
