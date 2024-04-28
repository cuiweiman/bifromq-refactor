package com.zachary.bifromq.basecluster.memberlist;

import com.zachary.bifromq.basecluster.memberlist.agent.IAgent;
import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import com.zachary.bifromq.basecluster.membership.proto.HostMember;
import io.reactivex.rxjava3.core.Observable;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/24 11:05
 */
public interface IHostMemberList {
    /**
     * The member from local
     *
     * @return
     */
    HostMember local();

    boolean isZombie(HostEndpoint endpoint);

    /**
     * Quit local host from the member list, after quit the memberlist instance should never be used
     */
    CompletableFuture<Void> stop();

    /**
     * An hot observable about members
     *
     * @return
     */
    Observable<Map<HostEndpoint, Integer>> members();

    /**
     * Host the provided agent in local host. If the agent is already hosted, nothing will happen, otherwise
     * other hosts which are residing same agent will get notified.
     *
     * @param agentId
     */
    IAgent host(String agentId);

    /**
     * Stop hosting the agent. If the agent is not a resident, nothing will happen. The agent object is not expected
     * to be used after calling this method.
     *
     * @param agentId
     */
    CompletableFuture<Void> stopHosting(String agentId);

    /**
     * The agents currently are residing in local host
     *
     * @return
     */
    Set<String> agents();
}
