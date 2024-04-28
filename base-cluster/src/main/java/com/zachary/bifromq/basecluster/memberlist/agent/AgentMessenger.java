package com.zachary.bifromq.basecluster.memberlist.agent;

import com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr;
import com.zachary.bifromq.basecluster.agent.proto.AgentMessage;
import com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope;
import com.zachary.bifromq.basecluster.memberlist.IHostAddressResolver;
import com.zachary.bifromq.basecluster.messenger.IMessenger;
import com.zachary.bifromq.basecluster.proto.ClusterMessage;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Timed;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;

public class AgentMessenger implements IAgentMessenger {
    private final String agentId;
    private final IHostAddressResolver addressResolver;
    private final IMessenger messenger;

    public AgentMessenger(String agentId, IHostAddressResolver addressResolver, IMessenger messenger) {
        this.agentId = agentId;
        this.addressResolver = addressResolver;
        this.messenger = messenger;
    }

    @Override
    public CompletableFuture<Void> send(AgentMessage message, AgentMemberAddr receiver, boolean reliable) {
        InetSocketAddress memberAddress = addressResolver.resolve(receiver.getEndpoint());
        if (memberAddress != null) {
            return messenger.send(ClusterMessage.newBuilder()
                    .setAgentMessage(AgentMessageEnvelope.newBuilder()
                            .setAgentId(agentId)
                            .setReceiver(receiver)
                            .setMessage(message)
                            .build())
                    .build(), memberAddress, reliable);
        } else {
            // ignore temporary unreachable agent member
            return CompletableFuture.failedFuture(new UnknownHostException("Unknown host"));
        }
    }

    @Override
    public Observable<AgentMessageEnvelope> receive() {
        return messenger.receive()
                .map(Timed::value)
                .filter(msg -> msg.message.hasAgentMessage() &&
                        msg.message.getAgentMessage().getAgentId().equals(agentId))
                .map(msg -> msg.message.getAgentMessage());

    }
}
