package com.zachary.bifromq.inbox.client;

import com.zachary.bifromq.baserpc.IRPCClient;
import com.zachary.bifromq.inbox.rpc.proto.InboxMessagePack;
import com.zachary.bifromq.inbox.rpc.proto.InboxServiceGrpc;
import com.zachary.bifromq.inbox.rpc.proto.SendReply;
import com.zachary.bifromq.inbox.rpc.proto.SendRequest;
import com.zachary.bifromq.inbox.rpc.proto.SendResult;
import com.zachary.bifromq.plugin.subbroker.DeliveryPack;
import com.zachary.bifromq.plugin.subbroker.DeliveryResult;
import com.zachary.bifromq.plugin.subbroker.IDeliverer;
import com.google.common.collect.Iterables;
import com.zachary.bifromq.type.SubInfo;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;

class DeliveryPipeline implements IDeliverer {
    private final IRPCClient.IRequestPipeline<SendRequest, SendReply> ppln;

    DeliveryPipeline(String delivererKey, IRPCClient rpcClient) {
        ppln = rpcClient.createRequestPipeline("", null, delivererKey,
            emptyMap(), InboxServiceGrpc.getReceiveMethod());
    }

    @Override
    public CompletableFuture<Map<SubInfo, DeliveryResult>> deliver(Iterable<DeliveryPack> packs) {
        long reqId = System.nanoTime();
        return ppln.invoke(SendRequest.newBuilder()
                .setReqId(reqId)
                .addAllInboxMsgPack(Iterables.transform(packs,
                    e -> InboxMessagePack.newBuilder()
                        .setMessages(e.messagePack)
                        .addAllSubInfo(e.inboxes)
                        .build()))
                .build())
            .thenApply(sendReply -> sendReply.getResultList().stream()
                .collect(Collectors.toMap(SendResult::getSubInfo, e -> {
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
