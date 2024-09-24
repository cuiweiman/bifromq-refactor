package com.zachary.bifromq.inbox.server.scheduler;

import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.zachary.bifromq.basescheduler.BatchCallBuilder;
import com.zachary.bifromq.basescheduler.IBatchCall;
import com.zachary.bifromq.inbox.rpc.proto.CommitReply;
import com.zachary.bifromq.inbox.rpc.proto.CommitRequest;
import com.zachary.bifromq.inbox.storage.proto.InboxCommit;
import com.zachary.bifromq.inbox.storage.proto.InboxCommitRequest;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceRWCoProcInput;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceRWCoProcOutput;
import com.zachary.bifromq.inbox.storage.proto.UpdateRequest;
import com.google.common.collect.Maps;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zachary.bifromq.type.ClientInfo;
import com.zachary.bifromq.type.QoS;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static com.zachary.bifromq.inbox.util.KeyUtil.scopedInboxId;
import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.INBOX_MAX_INBOXES_PER_COMMIT;

@Slf4j
public class InboxCommitScheduler extends InboxUpdateScheduler<CommitRequest, CommitReply> {
    private final IBaseKVStoreClient kvStoreClient;
    private final int maxInboxesPerCommit;

    public InboxCommitScheduler(IBaseKVStoreClient kvStoreClient) {
        super(kvStoreClient, "inbox_server_commit");
        this.kvStoreClient = kvStoreClient;
        maxInboxesPerCommit = INBOX_MAX_INBOXES_PER_COMMIT.get();
    }

    @Override
    protected ByteString rangeKey(CommitRequest request) {
        return scopedInboxId(request.getClientInfo().getTenantId(), request.getInboxId());
    }

    @Override
    protected BatchCallBuilder<CommitRequest, CommitReply> newBuilder(String name, int maxInflights,
                                                                      KVRangeSetting rangeSetting) {
        return new BatchCommitBuilder(name, maxInflights, rangeSetting, kvStoreClient);
    }

    private class BatchCommitBuilder extends BatchCallBuilder<CommitRequest, CommitReply> {
        private class BatchCommit implements IBatchCall<CommitRequest, CommitReply> {
            private final AtomicInteger inboxCount = new AtomicInteger();

            // key: scopedInboxIdUtf8, value: [qos0, qos1, qos2]
            private final Map<String, Long[]> inboxCommits = new NonBlockingHashMap<>();

            // key: scopedInboxIdUtf8
            private final Map<CommitRequest, CompletableFuture<CommitReply>> onInboxCommitted =
                new NonBlockingHashMap<>();

            @Override
            public boolean isEmpty() {
                return inboxCommits.isEmpty();
            }

            @Override
            public CompletableFuture<CommitReply> add(CommitRequest request) {
                ClientInfo clientInfo = request.getClientInfo();
                String scopedInboxIdUtf8 = scopedInboxId(clientInfo.getTenantId(),
                    request.getInboxId()).toStringUtf8();
                Long[] upToSeqs = inboxCommits.computeIfAbsent(scopedInboxIdUtf8, k -> {
                    inboxCount.incrementAndGet();
                    return new Long[3];
                });
                QoS qos = request.getQos();
                upToSeqs[qos.ordinal()] = upToSeqs[qos.ordinal()] == null ?
                    request.getUpToSeq() : Math.max(upToSeqs[qos.ordinal()], request.getUpToSeq());
                return onInboxCommitted.computeIfAbsent(request, k -> new CompletableFuture<>());
            }

            @Override
            public void reset() {
                inboxCount.set(0);
                inboxCommits.clear();
                onInboxCommitted.clear();
            }

            @Override
            public CompletableFuture<Void> execute() {
                UpdateRequest updateRequest = UpdateRequest.newBuilder()
                    .setReqId(System.nanoTime())
                    .setCommit(InboxCommitRequest.newBuilder()
                        .putAllInboxCommit(Maps.transformValues(inboxCommits,
                            v -> {
                                InboxCommit.Builder cb = InboxCommit.newBuilder();
                                if (v[0] != null) {
                                    cb.setQos0UpToSeq(v[0]);
                                }
                                if (v[1] != null) {
                                    cb.setQos1UpToSeq(v[1]);
                                }
                                if (v[2] != null) {
                                    cb.setQos2UpToSeq(v[2]);
                                }
                                return cb.build();
                            }))
                        .build())
                    .build();
                batchInboxCount.record(inboxCount.get());
                Timer.Sample start = Timer.start();
                return kvStoreClient.execute(range.leader,
                        KVRangeRWRequest.newBuilder()
                            .setReqId(updateRequest.getReqId())
                            .setVer(range.ver)
                            .setKvRangeId(range.id)
                            .setRwCoProc(InboxServiceRWCoProcInput.newBuilder()
                                .setRequest(updateRequest)
                                .build().toByteString())
                            .build())
                    .thenApply(reply -> {
                        start.stop(batchCommitTimer);
                        switch (reply.getCode()) {
                            case Ok:
                                try {
                                    return InboxServiceRWCoProcOutput
                                        .parseFrom(
                                            reply.getRwCoProcResult())
                                        .getReply().getCommit();
                                } catch (InvalidProtocolBufferException e) {
                                    log.error("Unable to parse rw co-proc output", e);
                                    throw new RuntimeException(e);
                                }
                            default:
                                log.warn("Failed to exec rw co-proc[code={}]", reply.getCode());
                                throw new RuntimeException();
                        }
                    })
                    .handle((v, e) -> {
                        for (CommitRequest request : onInboxCommitted.keySet()) {
                            if (e != null) {
                                onInboxCommitted.get(request)
                                    .complete(CommitReply.newBuilder()
                                        .setReqId(request.getReqId())
                                        .setResult(CommitReply.Result.ERROR)
                                        .build());
                            } else {
                                onInboxCommitted.get(request)
                                    .complete(CommitReply.newBuilder()
                                        .setReqId(request.getReqId())
                                        .setResult(v.getResultMap()
                                            .get(scopedInboxId(
                                                request.getClientInfo().getTenantId(),
                                                request.getInboxId()).toStringUtf8()) ?
                                            CommitReply.Result.OK : CommitReply.Result.ERROR)
                                        .build());
                            }
                        }
                        return null;
                    });

            }

            @Override
            public boolean isEnough() {
                return inboxCount.get() > maxInboxesPerCommit;
            }
        }

        private final IBaseKVStoreClient kvStoreClient;
        private final KVRangeSetting range;
        private final DistributionSummary batchInboxCount;
        private final Timer batchCommitTimer;

        BatchCommitBuilder(String name, int maxInflights,
                                   KVRangeSetting range, IBaseKVStoreClient kvStoreClient) {
            super(name, maxInflights);
            this.range = range;
            this.kvStoreClient = kvStoreClient;
            Tags tags = Tags.of("rangeId", KVRangeIdUtil.toShortString(range.id));
            batchInboxCount = DistributionSummary.builder("inbox.server.commit.inboxes")
                .tags(tags)
                .register(Metrics.globalRegistry);
            batchCommitTimer = Timer.builder("inbox.server.commit.latency")
                .tags(tags)
                .register(Metrics.globalRegistry);
        }

        @Override
        public BatchCommit newBatch() {
            return new BatchCommit();
        }

        @Override
        public void close() {
            Metrics.globalRegistry.remove(batchInboxCount);
            Metrics.globalRegistry.remove(batchCommitTimer);
        }
    }
}
