package com.zachary.bifromq.retain.client;

import com.zachary.bifromq.baserpc.IRPCClient;
import com.zachary.bifromq.retain.rpc.proto.MatchReply;
import com.zachary.bifromq.retain.rpc.proto.MatchRequest;
import com.zachary.bifromq.retain.rpc.proto.RetainReply;
import com.zachary.bifromq.retain.rpc.proto.RetainRequest;
import com.zachary.bifromq.retain.rpc.proto.RetainServiceGrpc;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.zachary.bifromq.retain.utils.PipelineUtil.PIPELINE_ATTR_KEY_CLIENT_INFO;
import static com.zachary.bifromq.retain.utils.PipelineUtil.encode;
import static com.google.protobuf.UnsafeByteOperations.unsafeWrap;

@Slf4j
class RetainServiceClient implements IRetainServiceClient {
    private final IRPCClient rpcClient;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    RetainServiceClient(@NonNull IRPCClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public void stop() {
        if (closed.compareAndSet(false, true)) {
            log.info("Stopping retain client");
            log.debug("Stopping rpc client");
            rpcClient.stop();
            log.debug("Retain client stopped");
        }
    }

    @Override
    public Observable<IRPCClient.ConnState> connState() {
        return rpcClient.connState();
    }

    @Override
    public IClientPipeline open(ClientInfo clientInfo) {
        Map<String, String> pipelineAttrs = new HashMap<>() {{
            put(PIPELINE_ATTR_KEY_CLIENT_INFO, encode(clientInfo));
        }};
        return new IClientPipeline() {
            private final IRPCClient.IRequestPipeline<RetainRequest, RetainReply> ppln =
                rpcClient.createRequestPipeline(clientInfo.getTenantId(), null,
                    null, pipelineAttrs, RetainServiceGrpc.getRetainMethod());

            @Override
            public CompletableFuture<RetainReply> retain(long reqId, String topic,
                                                         QoS qos, ByteBuffer payload, int expirySeconds) {
                long now = System.currentTimeMillis();
                long expiry = expirySeconds == Integer.MAX_VALUE ? Long.MAX_VALUE : now +
                    TimeUnit.MILLISECONDS.convert(expirySeconds, TimeUnit.SECONDS);
                return ppln.invoke(RetainRequest.newBuilder()
                        .setReqId(reqId)
                        .setQos(qos)
                        .setTopic(topic)
                        .setTimestamp(now)
                        .setExpireTimestamp(expiry)
                        .setPayload(unsafeWrap(payload))
                        .build())
                    .exceptionally(e -> RetainReply.newBuilder()
                        .setReqId(reqId)
                        .setResult(RetainReply.Result.ERROR)
                        .build());
            }

            @Override
            public void close() {
                ppln.close();
            }
        };
    }

    @Override
    public CompletableFuture<MatchReply> match(long reqId, String tenantId,
                                               String topicFilter, int limit, ClientInfo clientInfo) {
        Map<String, String> pipelineAttrs = new HashMap<>() {{
            put(PIPELINE_ATTR_KEY_CLIENT_INFO, encode(clientInfo));
        }};
        log.trace("Handling match request: reqId={}, topicFilter={}", reqId, topicFilter);
        return rpcClient.invoke(tenantId, null, MatchRequest.newBuilder()
                .setReqId(reqId)
                .setTopicFilter(topicFilter)
                .setLimit(limit)
                .build(), pipelineAttrs, RetainServiceGrpc.getMatchMethod())
            .whenComplete((v, e) -> {
                if (e != null) {
                    log.trace("Finish handling match request with error: reqId={}, topicFilter={}",
                        reqId, topicFilter, e);
                } else {
                    log.trace("Finish handling match request: reqId={}, topicFilter={}, reply={}",
                        reqId, topicFilter, v);
                }
            });
    }
}
