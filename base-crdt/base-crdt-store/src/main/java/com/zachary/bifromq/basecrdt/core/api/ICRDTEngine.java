
package com.zachary.bifromq.basecrdt.core.api;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.internal.InMemCRDTEngine;
import com.zachary.bifromq.basecrdt.proto.Replacement;
import com.zachary.bifromq.basecrdt.proto.Replica;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ICRDTEngine {
    /**
     * 创建实例
     */
    static ICRDTEngine newInstance(CRDTEngineOptions crdtEngineOptions) {
        return new InMemCRDTEngine(crdtEngineOptions);
    }

    long id();

    Iterator<Replica> hosting();

    /**
     * Host a CRDT replica under specified URI if not host yet
     *
     * @param crdtURI
     * @return
     */
    Replica host(String crdtURI);

    /**
     * Host a CRDT replica using specified replicaId or return existing hosted replica. It's caller's duty to ensure the
     * replicaId provided is unique within the cluster
     *
     * @param crdtURI
     * @param replicaId
     * @return
     */
    Replica host(String crdtURI, ByteString replicaId);

    CompletableFuture<Void> stopHosting(String crdtURI);

    <C extends ICausalCRDT> Optional<C> get(String crdtURI);

    Optional<Map<ByteString, NavigableMap<Long, Long>>> latticeEvents(String crdtURI);

    Optional<Map<ByteString, NavigableMap<Long, Long>>> historyEvents(String crdtURI);

    CompletableFuture<Void> join(String crdtURI, Iterable<Replacement> delta);

    CompletableFuture<Optional<Iterable<Replacement>>> delta(String crdtURI,
                                                             Map<ByteString, NavigableMap<Long, Long>>
                                                                     coveredLatticeEvents,
                                                             Map<ByteString, NavigableMap<Long, Long>>
                                                                     coveredHistoryEvents,
                                                             int maxEvents);

    void start();

    void stop();
}
