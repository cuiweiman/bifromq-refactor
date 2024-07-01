package com.zachary.bifromq.inbox.server.scheduler;

import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basekv.store.proto.KVRangeRORequest;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.zachary.bifromq.basescheduler.BatchCallBuilder;
import com.zachary.bifromq.basescheduler.IBatchCall;
import com.zachary.bifromq.inbox.rpc.proto.HasInboxReply;
import com.zachary.bifromq.inbox.rpc.proto.HasInboxRequest;
import com.zachary.bifromq.inbox.storage.proto.HasRequest;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceROCoProcInput;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceROCoProcOutput;
import com.zachary.bifromq.inbox.storage.proto.QueryReply;
import com.zachary.bifromq.inbox.storage.proto.QueryRequest;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;
import org.jctools.maps.NonBlockingHashSet;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import static com.zachary.bifromq.inbox.util.KeyUtil.scopedInboxId;
import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.INBOX_CHECK_QUEUES_PER_RANGE;
import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.INBOX_MAX_INBOXES_PER_CHECK;

@Slf4j
public class InboxCheckScheduler extends InboxQueryScheduler<HasInboxRequest, HasInboxReply> {
    private final IBaseKVStoreClient kvStoreClient;

    private final int maxInboxesPerCheck;


    public InboxCheckScheduler(IBaseKVStoreClient kvStoreClient) {
        super(INBOX_CHECK_QUEUES_PER_RANGE.get(), kvStoreClient,
            "inbox_server_check");
        this.kvStoreClient = kvStoreClient;
        maxInboxesPerCheck = INBOX_MAX_INBOXES_PER_CHECK.get();
    }

    @Override
    protected int selectQueue(int queueNum, HasInboxRequest request) {
        return ThreadLocalRandom.current().nextInt(0, queueNum);
    }

    @Override
    protected ByteString rangeKey(HasInboxRequest request) {
        return scopedInboxId(request.getClientInfo().getTenantId(), request.getInboxId());
    }

    @Override
    protected BatchCallBuilder<HasInboxRequest, HasInboxReply> newBuilder(String name, int maxInflights,
                                                                          BatchKey batchKey) {
        return new BatchCheckBuilder(name, maxInflights, batchKey.rangeSetting, kvStoreClient);
    }

    private class BatchCheckBuilder extends BatchCallBuilder<HasInboxRequest, HasInboxReply> {
        private class BatchCheck implements IBatchCall<HasInboxRequest, HasInboxReply> {
            private final Set<ByteString> checkInboxes = new NonBlockingHashSet<>();
            private final Map<HasInboxRequest, CompletableFuture<HasInboxReply>> onInboxChecked =
                new NonBlockingHashMap<>();

            @Override
            public boolean isEmpty() {
                return checkInboxes.isEmpty();
            }

            @Override
            public boolean isEnough() {
                return checkInboxes.size() > maxInboxesPerCheck;
            }

            @Override
            public CompletableFuture<HasInboxReply> add(HasInboxRequest request) {
                checkInboxes.add(scopedInboxId(request.getClientInfo().getTenantId(), request.getInboxId()));
                return onInboxChecked.computeIfAbsent(request, k -> new CompletableFuture<>());
            }

            @Override
            public void reset() {
                checkInboxes.clear();
                onInboxChecked.clear();
            }

            @Override
            public CompletableFuture<Void> execute() {
                QueryRequest request = QueryRequest.newBuilder()
                    .setReqId(System.nanoTime())
                    .setHas(HasRequest.newBuilder()
                        .addAllScopedInboxId(checkInboxes)
                        .build())
                    .build();
                batchInboxCount.record(checkInboxes.size());
                Timer.Sample start = Timer.start();
                return kvStoreClient.linearizedQuery(range.leader,
                        KVRangeRORequest.newBuilder()
                            .setReqId(request.getReqId())
                            .setVer(range.ver)
                            .setKvRangeId(range.id)
                            .setRoCoProcInput(InboxServiceROCoProcInput.newBuilder()
                                .setRequest(request)
                                .build()
                                .toByteString())
                            .build(), orderKey)
                    .thenApply(v -> {
                        start.stop(batchCheckTimer);
                        switch (v.getCode()) {
                            case Ok:
                                try {
                                    QueryReply reply = InboxServiceROCoProcOutput.parseFrom(
                                        v.getRoCoProcResult()).getReply();
                                    assert reply.getReqId() == request.getReqId();
                                    return reply.getHas();
                                } catch (InvalidProtocolBufferException e) {
                                    log.error("Unable to parse rw co-proc output", e);
                                    throw new RuntimeException("Unable to parse rw co-proc output", e);
                                }
                            default:
                                log.warn("Failed to exec rw co-proc[code={}]", v.getCode());
                                throw new RuntimeException("Failed to exec rw co-proc");
                        }
                    })
                    .handle((v, e) -> {
                        if (e != null) {
                            onInboxChecked.forEach((req, f) -> f.completeExceptionally(e));
                        } else {
                            onInboxChecked.forEach((req, f) -> {
                                Boolean exists = v.getExistsMap()
                                    .get(scopedInboxId(req.getClientInfo().getTenantId(),
                                        req.getInboxId()).toStringUtf8());
                                // if query result doesn't contain the scoped inboxId, reply error
                                if (exists == null) {
                                    f.completeExceptionally(new RuntimeException("Inbox not found"));
                                } else {
                                    f.complete(HasInboxReply.newBuilder()
                                        .setReqId(req.getReqId())
                                        .setResult(exists)
                                        .build());
                                }
                            });
                        }
                        return null;
                    });
            }
        }

        private final String orderKey;

        private final KVRangeSetting range;

        private final IBaseKVStoreClient kvStoreClient;

        private final DistributionSummary batchInboxCount;

        private final Timer batchCheckTimer;

        BatchCheckBuilder(String name, int maxInflights, KVRangeSetting range, IBaseKVStoreClient kvStoreClient) {
            super(name, maxInflights);
            this.range = range;
            this.kvStoreClient = kvStoreClient;
            orderKey = this.hashCode() + "";
            Tags tags = Tags.of("rangeId", KVRangeIdUtil.toShortString(range.id));
            batchInboxCount = DistributionSummary.builder("inbox.server.check.inboxes")
                .tags(tags)
                .register(Metrics.globalRegistry);
            batchCheckTimer = Timer.builder("inbox.server.check.latency")
                .tags(tags)
                .register(Metrics.globalRegistry);
        }

        @Override
        public BatchCheck newBatch() {
            return new BatchCheck();
        }

        @Override
        public void close() {
            Metrics.globalRegistry.remove(batchInboxCount);
            Metrics.globalRegistry.remove(batchCheckTimer);
        }
    }
}
