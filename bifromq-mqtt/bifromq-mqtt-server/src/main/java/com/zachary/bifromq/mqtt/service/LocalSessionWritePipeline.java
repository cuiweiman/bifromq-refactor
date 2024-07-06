package com.zachary.bifromq.mqtt.service;

import com.zachary.bifromq.baserpc.ResponsePipeline;
import com.zachary.bifromq.mqtt.inbox.rpc.proto.DeliveryPack;
import com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteReply;
import com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteRequest;
import com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteResult;
import com.zachary.bifromq.mqtt.session.v3.IMQTT3TransientSession;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
class LocalSessionWritePipeline extends ResponsePipeline<WriteRequest, WriteReply> {
    private final Map<String, IMQTT3TransientSession> sessionMap;

    public LocalSessionWritePipeline(Map<String, IMQTT3TransientSession> sessionMap,
                                     StreamObserver<WriteReply> responseObserver) {
        super(responseObserver);
        this.sessionMap = sessionMap;
    }

    @Override
    protected CompletableFuture<WriteReply> handleRequest(String tenantId, WriteRequest request) {
        log.trace("Handle inbox write request: \n{}", request);
        WriteReply.Builder replyBuilder = WriteReply.newBuilder().setReqId(request.getReqId());
        Set<SubInfo> ok = new HashSet<>();
        Set<SubInfo> noInbox = new HashSet<>();
        for (DeliveryPack deliveryPack : request.getDeliveryPackList()) {
            TopicMessagePack topicMsgPack = deliveryPack.getMessagePack();
            List<SubInfo> subInfos = deliveryPack.getSubscriberList();
            Map<IMQTT3TransientSession, SubInfo> inboxes = new HashMap<>();
            for (SubInfo subInfo : subInfos) {
                IMQTT3TransientSession session = sessionMap.get(subInfo.getInboxId());
                if (!noInbox.contains(subInfo) && session != null) {
                    ok.add(subInfo);
                    inboxes.put(session, subInfo);
                } else {
                    noInbox.add(subInfo);
                }
            }
            inboxes.forEach((session, subInfo) -> session.publish(subInfo, topicMsgPack));
        }
        ok.forEach(subInfo -> replyBuilder.addResult(WriteResult.newBuilder()
            .setSubInfo(subInfo)
            .setResult(WriteResult.Result.OK)
            .build()));
        noInbox.forEach(subInfo -> replyBuilder.addResult(WriteResult.newBuilder()
            .setSubInfo(subInfo)
            .setResult(WriteResult.Result.NO_INBOX)
            .build()));
        return CompletableFuture.completedFuture(replyBuilder.build());
    }
}
