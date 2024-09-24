package com.zachary.bifromq.mqtt.inbox;

import com.zachary.bifromq.baserpc.IRPCClient;
import com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxReply;
import com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxRequest;
import com.zachary.bifromq.mqtt.inbox.rpc.proto.OnlineInboxBrokerGrpc;
import com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteReply;
import com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteRequest;
import com.zachary.bifromq.mqtt.inbox.util.DeliveryGroupKeyUtil;
import com.zachary.bifromq.plugin.subbroker.DeliveryPack;
import com.zachary.bifromq.plugin.subbroker.DeliveryResult;
import com.zachary.bifromq.plugin.subbroker.IDeliverer;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.zachary.bifromq.type.SubInfo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;

@Slf4j
class MqttBrokerClient implements IMqttBrokerClient {
    private final AtomicBoolean hasStopped = new AtomicBoolean();
    private final IRPCClient rpcClient;

    MqttBrokerClient(@NonNull IRPCClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public IDeliverer open(String delivererKey) {
        Preconditions.checkState(!hasStopped.get());
        return new DeliveryPipeline(delivererKey);
    }

    @Override
    public CompletableFuture<Boolean> hasInbox(long reqId, String tenantId, String inboxId, String delivererKey) {
        Preconditions.checkState(!hasStopped.get());
        return rpcClient.invoke(tenantId, DeliveryGroupKeyUtil.parseServerId(delivererKey),
                HasInboxRequest.newBuilder().setReqId(reqId).setInboxId(inboxId).build(),
                OnlineInboxBrokerGrpc.getHasInboxMethod())
            .thenApply(HasInboxReply::getResult);
    }


    @Override
    public void close() {
        if (hasStopped.compareAndSet(false, true)) {
            log.info("Closing MQTT broker client");
            log.debug("Stopping rpc client");
            rpcClient.stop();
            log.info("MQTT broker client closed");
        }
    }

    private class DeliveryPipeline implements IDeliverer {
        private final IRPCClient.IRequestPipeline<WriteRequest, WriteReply> ppln;

        DeliveryPipeline(String deliveryGroupKey) {
            ppln = rpcClient.createRequestPipeline("", DeliveryGroupKeyUtil.parseServerId(deliveryGroupKey), "",
                emptyMap(), OnlineInboxBrokerGrpc.getWriteMethod());
        }

        @Override
        public CompletableFuture<Map<SubInfo, DeliveryResult>> deliver(Iterable<DeliveryPack> packs) {
            Preconditions.checkState(!hasStopped.get());
            long reqId = System.nanoTime();
            return ppln.invoke(WriteRequest.newBuilder()
                    .setReqId(reqId)
                    .addAllDeliveryPack(Iterables.transform(packs, e -> com.zachary.bifromq.mqtt.inbox.rpc.proto.DeliveryPack.newBuilder()
                        .setMessagePack(e.messagePack)
                        .addAllSubscriber(e.inboxes)
                        .build()))
                    .build())
                .thenApply(writeReply -> writeReply.getResultList().stream()
                    .collect(Collectors.toMap(e -> e.getSubInfo(), e -> {
                        switch (e.getResult()) {
                            case NO_INBOX:
                                return DeliveryResult.NO_INBOX;
                            case OK:
                            default:
                                return DeliveryResult.OK;
                        }
                    })));
        }

        @Override
        public void close() {
            ppln.close();
        }
    }
}
