package com.zachary.bifromq.basecluster.memberlist.agent;

import com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr;
import com.zachary.bifromq.basecluster.agent.proto.AgentMemberMetadata;
import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import io.reactivex.rxjava3.core.Observable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface IAgent {
    String id();

    HostEndpoint endpoint();

    /**
     * A hot observable of agent membership
     *
     * @return
     */
    Observable<Map<AgentMemberAddr, AgentMemberMetadata>> membership();

    /**
     * Register a local agent member.
     * It's allowed to register same member name in same logical agent from different agent hosts
     *
     * @param memberName
     */
    IAgentMember register(String memberName);

    /**
     * Deregister a member instance, the caller should never hold the reference to the instance after deregistered
     *
     * @param member
     */
    CompletableFuture<Void> deregister(IAgentMember member);
}
