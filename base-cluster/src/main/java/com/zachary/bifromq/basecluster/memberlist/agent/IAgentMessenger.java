package com.zachary.bifromq.basecluster.memberlist.agent;

import com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr;
import com.zachary.bifromq.basecluster.agent.proto.AgentMessage;
import com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.CompletableFuture;

public interface IAgentMessenger {
    /**
     * Send message to the receiver, send message to self is allowed
     *
     * @param message
     * @param receiver
     * @param reliable
     * @return
     */
    CompletableFuture<Void> send(AgentMessage message, AgentMemberAddr receiver, boolean reliable);

    /**
     * Receiving messages
     *
     * @return
     */
    Observable<AgentMessageEnvelope> receive();
}
