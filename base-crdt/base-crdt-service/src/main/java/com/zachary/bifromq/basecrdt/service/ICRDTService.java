package com.zachary.bifromq.basecrdt.service;

import com.zachary.bifromq.basecluster.IAgentHost;
import com.zachary.bifromq.basecrdt.core.api.ICausalCRDT;
import com.zachary.bifromq.basecrdt.proto.Replica;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface ICRDTService {

    /**
     * Construct a new instance
     *
     * @param options
     * @return
     */
    static ICRDTService newInstance(@NonNull CRDTServiceOptions options) {
        return new CRDTService(options);
    }

    long id();

    Replica host(String uri);

    Optional<ICausalCRDT> get(String uri);

    CompletableFuture<Void> stopHosting(String uri);

    Observable<Set<Replica>> aliveReplicas(String uri);

    boolean isStarted();

    /**
     * Start the store by providing agentHost
     */
    void start(IAgentHost agentHost);

    /**
     * Stop the store
     */
    void stop();

}
