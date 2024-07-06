package com.zachary.bifromq.dist.client;

import com.zachary.bifromq.basehlc.HLC;
import com.zachary.bifromq.baserpc.IRPCClient;
import com.zachary.bifromq.dist.client.scheduler.ClientCall;
import com.zachary.bifromq.dist.client.scheduler.DistServerCallScheduler;
import com.zachary.bifromq.dist.rpc.proto.ClearRequest;
import com.zachary.bifromq.dist.rpc.proto.DistServiceGrpc;
import com.zachary.bifromq.dist.rpc.proto.SubReply;
import com.zachary.bifromq.dist.rpc.proto.SubRequest;
import com.zachary.bifromq.dist.rpc.proto.UnsubRequest;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.protobuf.UnsafeByteOperations.unsafeWrap;

@Slf4j
class DistClient implements IDistClient {
    private final DistServerCallScheduler reqScheduler;
    private final IRPCClient rpcClient;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    DistClient(@NonNull IRPCClient rpcClient) {
        this.rpcClient = rpcClient;
        reqScheduler = new DistServerCallScheduler(rpcClient);
    }

    @Override
    public Observable<IRPCClient.ConnState> connState() {
        return rpcClient.connState();
    }

    @Override
    public CompletableFuture<Void> pub(long reqId, String topic, QoS qos, ByteBuffer payload,
                                       int expirySeconds, ClientInfo publisher) {
        long now = HLC.INST.getPhysical();
        long expiry = expirySeconds == Integer.MAX_VALUE ? Long.MAX_VALUE :
            now + TimeUnit.MILLISECONDS.convert(expirySeconds, TimeUnit.SECONDS);
        return reqScheduler.schedule(new ClientCall(publisher, topic, Message.newBuilder()
            .setMessageId(reqId)
            .setPubQoS(qos)
            .setPayload(unsafeWrap(payload))
            .setTimestamp(now)
            .setExpireTimestamp(expiry)
            .build()));
    }

    @Override
    public CompletableFuture<Integer> sub(long reqId, String tenantId, String topicFilter, QoS qos, String inboxId,
                                          String delivererKey, int subBrokerId) {
        SubRequest request = SubRequest.newBuilder()
            .setReqId(reqId)
            .setTenantId(tenantId)
            .setTopicFilter(topicFilter)
            .setSubQoS(qos)
            .setInboxId(inboxId)
            .setDelivererKey(delivererKey)
            .setBroker(subBrokerId)
            .build();
        log.trace("Handling sub request:\n{}", request);
        return rpcClient.invoke(tenantId, null, request, DistServiceGrpc.getSubMethod())
            .handle((v, e) -> {
                if (e != null) {
                    log.debug("Sub request failed: reqId={}, tenantId={}", request.getReqId(), tenantId, e);
                    return SubReply.SubResult.Failure.getNumber();
                }
                log.trace("Finish handling sub request:\n{}, reply:\n{}", request, v);
                return v.getResult().getNumber();
            });

    }

    @Override
    public CompletableFuture<Boolean> unsub(long reqId, String tenantId, String topicFilter, String inbox,
                                            String delivererKey, int subBrokerId) {
        UnsubRequest request = UnsubRequest.newBuilder()
            .setReqId(reqId)
            .setTenantId(tenantId)
            .setTopicFilter(topicFilter)
            .setInboxId(inbox)
            .setDelivererKey(delivererKey)
            .setBroker(subBrokerId)
            .build();
        log.trace("Handling unsub request:\n{}", request);
        return rpcClient.invoke(tenantId, null, request, DistServiceGrpc.getUnsubMethod())
            .thenApply(v -> {
                log.trace("Finish handling unsub request:\n{}", request);
                return true;
            });
    }

    @Override
    public CompletableFuture<Void> clear(long reqId, String tenantId, String inboxId, String delivererKey,
                                         int subBrokerId) {
        log.trace("Requesting clear: reqId={}", reqId);
        ClearRequest request = ClearRequest.newBuilder()
            .setReqId(reqId)
            .setTenantId(tenantId)
            .setInboxId(inboxId)
            .setDelivererKey(delivererKey)
            .setBroker(subBrokerId)
            .build();
        return rpcClient.invoke(tenantId, null, request, DistServiceGrpc.getClearMethod())
            .thenApply(v -> {
                log.trace("Got clear reply: request={}, reply={}", request, v);
                return null;
            });
    }

    @Override
    public void stop() {
        // close tenant logger and drain logs before closing the dist client
        if (closed.compareAndSet(false, true)) {
            log.info("Stopping dist client");
            log.debug("Closing request scheduler");
            reqScheduler.close();
            log.debug("Stopping rpc client");
            rpcClient.stop();
            log.info("Dist client stopped");
        }
    }
}
