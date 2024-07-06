package com.zachary.bifromq.dist.client.scheduler;

import com.zachary.bifromq.baserpc.IRPCClient;
import com.zachary.bifromq.basescheduler.BatchCallBuilder;
import com.zachary.bifromq.basescheduler.BatchCallScheduler;
import com.zachary.bifromq.basescheduler.IBatchCall;
import com.zachary.bifromq.dist.rpc.proto.DistReply;
import com.zachary.bifromq.dist.rpc.proto.DistRequest;
import com.zachary.bifromq.dist.rpc.proto.DistServiceGrpc;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.DIST_CLIENT_MAX_INFLIGHT_CALLS_PER_QUEUE;
import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.DIST_MAX_TOPICS_IN_BATCH;
import static java.util.Collections.emptyMap;

@Slf4j
public class DistServerCallScheduler
    extends BatchCallScheduler<ClientCall, Void, DistServerCallScheduler.BatchKey> {
    private final int maxBatchedTopics;
    private final IRPCClient rpcClient;

    public DistServerCallScheduler(IRPCClient rpcClient) {
        super("dist_client_send_batcher", DIST_CLIENT_MAX_INFLIGHT_CALLS_PER_QUEUE.get());
        maxBatchedTopics = DIST_MAX_TOPICS_IN_BATCH.get();
        this.rpcClient = rpcClient;
    }

    @Override
    protected BatchCallBuilder<ClientCall, Void> newBuilder(String name, int maxInflights, BatchKey batchKey) {
        return new DistServerCallBuilder(name, maxInflights,
            rpcClient.createRequestPipeline(batchKey.tenantId, null, null, emptyMap(),
                DistServiceGrpc.getDistMethod()));
    }

    @Override
    protected Optional<BatchKey> find(ClientCall message) {
        return Optional.of(new BatchKey(message.publisher.getTenantId(), Thread.currentThread().getId()));
    }

    private class DistServerCallBuilder extends BatchCallBuilder<ClientCall, Void> {
        private class DistServerCall implements IBatchCall<ClientCall, Void> {
            private final Map<ClientInfo, Map<String, PublisherMessagePack.TopicPack.Builder>> clientMsgPack =
                new HashMap<>(maxBatchedTopics);
            private final List<CompletableFuture<Void>> tasks = new ArrayList<>();

            @Override
            public boolean isEmpty() {
                return tasks.isEmpty();
            }

            @Override
            public boolean isEnough() {
                return clientMsgPack.size() > maxBatchedTopics;
            }

            @Override
            public CompletableFuture<Void> add(ClientCall request) {
                CompletableFuture<Void> onDone = new CompletableFuture<>();
                tasks.add(onDone);
                clientMsgPack.computeIfAbsent(request.publisher, k -> new HashMap<>())
                    .computeIfAbsent(request.topic, k -> PublisherMessagePack.TopicPack.newBuilder().setTopic(k))
                    .addMessage(request.message);
                return onDone;
            }

            @Override
            public void reset() {
                clientMsgPack.clear();
                tasks.clear();
            }

            @Override
            public CompletableFuture<Void> execute() {
                DistRequest.Builder requestBuilder = DistRequest.newBuilder().setReqId(System.nanoTime());
                clientMsgPack.forEach((k, v) -> {
                    PublisherMessagePack.Builder senderMsgPackBuilder =
                        PublisherMessagePack.newBuilder().setPublisher(k);
                    for (PublisherMessagePack.TopicPack.Builder packBuilder : v.values()) {
                        senderMsgPackBuilder.addMessagePack(packBuilder);
                    }
                    requestBuilder.addMessages(senderMsgPackBuilder.build());
                });
                DistRequest request = requestBuilder.build();
                log.debug("Sending dist request: reqId={}", request.getReqId());
                return ppln.invoke(request).handle((v, e) -> {
                    if (e != null) {
                        log.error("Request failed", e);
                        tasks.forEach(taskOnDone -> taskOnDone.completeExceptionally(e));
                    } else {
                        log.debug("Got dist reply: reqId={}", v.getReqId());
                        tasks.forEach(taskOnDone -> taskOnDone.complete(null));
                    }
                    return null;
                });
            }
        }

        private final IRPCClient.IRequestPipeline<DistRequest, DistReply> ppln;

        DistServerCallBuilder(String name, int maxInflights,
                              IRPCClient.IRequestPipeline<DistRequest, DistReply> ppln) {
            super(name, maxInflights);
            this.ppln = ppln;
        }

        @Override
        public DistServerCall newBatch() {
            return new DistServerCall();
        }

        @Override
        public void close() {
            ppln.close();
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    static class BatchKey {
        final String tenantId;
        final long threadId;
    }
}
