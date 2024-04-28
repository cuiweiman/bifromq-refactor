package com.zachary.bifromq.basecluster.memberlist.agent;

import com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr;
import com.zachary.bifromq.basecluster.agent.proto.AgentMemberMetadata;
import com.zachary.bifromq.basecluster.agent.proto.AgentMessage;
import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.CompletableFuture;

public interface IAgentMember {
    AgentMemberAddr address();

    /**
     * Broadcast a message among the agent members
     *
     * @param message
     * @param reliable
     * @return
     */
    CompletableFuture<Void> broadcast(ByteString message, boolean reliable);

    /**
     * Send a message to another member located in given endpoint
     *
     * @param targetMemberAddr
     * @param message
     * @param reliable
     * @return
     */
    CompletableFuture<Void> send(AgentMemberAddr targetMemberAddr, ByteString message, boolean reliable);

    /**
     * Send a message to all endpoints where target member name is registered
     *
     * @param targetMemberName
     * @param message
     * @param reliable
     * @return
     */
    CompletableFuture<Void> multicast(String targetMemberName, ByteString message, boolean reliable);

    /**
     * Get current associated metadata
     *
     * @return
     */
    AgentMemberMetadata metadata();

    /**
     * Update associated metadata
     *
     * @param value
     */
    void metadata(ByteString value);

    /**
     * An observable of incoming messages
     *
     * @return
     */
    Observable<AgentMessage> receive();
}
