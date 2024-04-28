package com.zachary.bifromq.basecluster;


import com.zachary.bifromq.basecluster.memberlist.agent.IAgent;
import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import io.reactivex.rxjava3.core.Observable;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface IAgentHost {
    static IAgentHost newInstance(AgentHostOptions options) {
        return new AgentHost(options);
    }

    HostEndpoint local();

    /**
     * Join the cluster as a running node by communicating with some existing running node of the cluster as the seeds
     *
     * @param seeds
     * @return
     */
    CompletableFuture<Void> join(Set<InetSocketAddress> seeds);

    /**
     * An observable of the live membership of host cluster
     *
     * @return
     */
    Observable<Set<HostEndpoint>> cluster();

    /**
     * Host an agent locally
     *
     * @param agentId
     * @return
     */
    IAgent host(String agentId);

    /**
     * unhost the agent
     *
     * @param agentId
     */
    CompletableFuture<Void> stopHosting(String agentId);

    /**
     * An observable of agent host membership
     *
     * @return
     */
    Observable<Set<HostEndpoint>> membership();

    /**
     * Start the host
     */
    void start();

    /**
     * Shutdown the host
     */
    void shutdown();
}
