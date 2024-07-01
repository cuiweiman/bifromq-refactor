package com.zachary.bifromq.basekv.server;

import com.zachary.bifromq.basecluster.IAgentHost;
import com.zachary.bifromq.basecluster.memberlist.agent.IAgent;
import com.zachary.bifromq.basecluster.memberlist.agent.IAgentMember;
import com.zachary.bifromq.basekv.proto.KVRangeMessage;
import com.zachary.bifromq.basekv.proto.StoreMessage;
import com.zachary.bifromq.basekv.store.IStoreMessenger;
import com.google.protobuf.InvalidProtocolBufferException;
import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
class AgentHostStoreMessenger implements IStoreMessenger {
    static String agentId(String clusterId) {
        return "BaseKV:" + clusterId;
    }

    private final AtomicBoolean stopped = new AtomicBoolean();
    private final IAgentHost agentHost;
    private final IAgent agent;
    private final IAgentMember agentMember;
    private final String clusterId;
    private final String storeId;

    AgentHostStoreMessenger(IAgentHost agentHost, String clusterId, String storeId) {
        this.agentHost = agentHost;
        this.clusterId = clusterId;
        this.storeId = storeId;
        this.agent = agentHost.host(agentId(clusterId));
        this.agentMember = agent.register(storeId);
    }

    @Override
    public void send(StoreMessage message) {
        if (message.getPayload().hasHostStoreId()) {
            agentMember.multicast(message.getPayload().getHostStoreId(), message.toByteString(), true);
        } else {
            agentMember.broadcast(message.toByteString(), true);
        }
    }

    @Override
    public Observable<StoreMessage> receive() {
        return agentMember.receive()
            .mapOptional(agentMessage -> {
                try {
                    StoreMessage message = StoreMessage.parseFrom(agentMessage.getPayload());
                    KVRangeMessage payload = message.getPayload();
                    if (!payload.hasHostStoreId()) {
                        // this is a broadcast message
                        message = message.toBuilder().setPayload(payload.toBuilder()
                            .setHostStoreId(storeId)
                            .build()).build();
                    }
                    return Optional.of(message);
                } catch (InvalidProtocolBufferException e) {
                    log.warn("Unable to parse store message", e);
                    return Optional.empty();
                }
            });
    }

    @Override
    public void close() {
        if (stopped.compareAndSet(false, true)) {
            agent.deregister(agentMember).join();
        }
    }
}
