package com.zachary.bifromq.inbox.server.scheduler;


import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basekv.store.proto.KVRangeRORequest;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.zachary.bifromq.basescheduler.BatchCallBuilder;
import com.zachary.bifromq.basescheduler.IBatchCall;
import com.zachary.bifromq.inbox.storage.proto.FetchParams;
import com.zachary.bifromq.inbox.storage.proto.Fetched;
import com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest;
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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.INBOX_FETCH_QUEUES_PER_RANGE;
import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.INBOX_MAX_INBOXES_PER_FETCH;

@Slf4j
public class InboxFetchScheduler extends InboxQueryScheduler<InboxFetchScheduler.InboxFetch, Fetched> {
    private final IBaseKVStoreClient kvStoreClient;
    private final int maxInboxesPerFetch;


    public InboxFetchScheduler(IBaseKVStoreClient kvStoreClient) {
        super(INBOX_FETCH_QUEUES_PER_RANGE.get(), kvStoreClient,
            "inbox_server_fetch");
        this.kvStoreClient = kvStoreClient;
        maxInboxesPerFetch = INBOX_MAX_INBOXES_PER_FETCH.get();
    }

    @Override
    protected int selectQueue(int queueNum, InboxFetch request) {
        int idx = request.scopedInboxId.hashCode() % queueNum;
        if (idx < 0) {
            idx += queueNum;
        }
        return idx;
    }

    @Override
    protected ByteString rangeKey(InboxFetch request) {
        return request.scopedInboxId;
    }

    @Override
    protected BatchCallBuilder<InboxFetch, Fetched> newBuilder(String name, int maxInflights, BatchKey batchKey) {
        return new BatchFetchBuilder(name, maxInflights, batchKey.rangeSetting, kvStoreClient);
    }

    @AllArgsConstructor
    public static class InboxFetch {
        final ByteString scopedInboxId;
        final FetchParams params;
    }

    private class BatchFetchBuilder extends BatchCallBuilder<InboxFetch, Fetched> {
        private class BatchFetch implements IBatchCall<InboxFetch, Fetched> {
            // key: scopedInboxIdUtf8
            private final Map<String, FetchParams> inboxFetches = new NonBlockingHashMap<>();

            // key: scopedInboxIdUtf8
            private final Map<String, CompletableFuture<Fetched>> onInboxFetched = new NonBlockingHashMap<>();

            @Override
            public boolean isEmpty() {
                return inboxFetches.isEmpty();
            }

            @Override
            public boolean isEnough() {
                return inboxFetches.size() > maxInboxesPerFetch;
            }

            @Override
            public CompletableFuture<Fetched> add(InboxFetch request) {
                inboxFetches.compute(request.scopedInboxId.toStringUtf8(), (k, v) -> {
                    if (v == null) {
                        return request.params;
                    }
                    FetchParams.Builder b = v.toBuilder();
                    if (request.params.hasQos0StartAfter()) {
                        b.setQos0StartAfter(request.params.getQos1StartAfter());
                    }
                    if (request.params.hasQos1StartAfter()) {
                        b.setQos1StartAfter(request.params.getQos1StartAfter());
                    }
                    if (request.params.hasQos2StartAfter()) {
                        b.setQos2StartAfter(request.params.getQos2StartAfter());
                    }
                    b.setMaxFetch(request.params.getMaxFetch());
                    return b.build();
                });
                return onInboxFetched.computeIfAbsent(request.scopedInboxId.toStringUtf8(),
                    k -> new CompletableFuture<>());
            }

            @Override
            public void reset() {
                inboxFetches.clear();
                onInboxFetched.clear();
            }

            @Override
            public CompletableFuture<Void> execute() {
                QueryRequest request = QueryRequest.newBuilder()
                    .setReqId(System.nanoTime())
                    .setFetch(InboxFetchRequest.newBuilder()
                        .putAllInboxFetch(inboxFetches)
                        .build())
                    .build();
                batchInboxCount.record(inboxFetches.size());
                Timer.Sample start = Timer.start();
                return kvStoreClient.linearizedQuery(rangeStoreId,
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
                        start.stop(batchFetchTimer);
                        switch (v.getCode()) {
                            case Ok:
                                try {
                                    QueryReply reply = InboxServiceROCoProcOutput.parseFrom(
                                        v.getRoCoProcResult()).getReply();
                                    assert reply.getReqId() == request.getReqId();
                                    return reply.getFetch();
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
                            for (String scopedInboxIdUtf8 : onInboxFetched.keySet()) {
                                onInboxFetched.get(scopedInboxIdUtf8).completeExceptionally(e);
                            }
                        } else {
                            for (String scopedInboxIdUtf8 : onInboxFetched.keySet()) {
                                onInboxFetched.get(scopedInboxIdUtf8)
                                    .complete(v.getResultMap().get(scopedInboxIdUtf8));
                            }
                        }
                        return null;
                    });
            }
        }

        private final String rangeStoreId;

        private final String orderKey;

        private final KVRangeSetting range;

        private final IBaseKVStoreClient kvStoreClient;

        private final DistributionSummary batchInboxCount;

        private final Timer batchFetchTimer;

        BatchFetchBuilder(String name, int maxInflights, KVRangeSetting range, IBaseKVStoreClient kvStoreClient) {
            super(name, maxInflights);
            this.range = range;
            this.kvStoreClient = kvStoreClient;
            rangeStoreId = selectStore(range);
            orderKey = this.hashCode() + "";
            Tags tags = Tags.of("rangeId", KVRangeIdUtil.toShortString(range.id));
            batchInboxCount = DistributionSummary.builder("inbox.server.fetch.inboxes")
                .tags(tags)
                .register(Metrics.globalRegistry);
            batchFetchTimer = Timer.builder("inbox.server.fetch.latency")
                .tags(tags)
                .register(Metrics.globalRegistry);
        }

        @Override
        public BatchFetch newBatch() {
            return new BatchFetch();
        }

        @Override
        public void close() {
            Metrics.globalRegistry.remove(batchInboxCount);
            Metrics.globalRegistry.remove(batchFetchTimer);
        }

        private String selectStore(KVRangeSetting setting) {
            return setting.allReplicas.get(ThreadLocalRandom.current().nextInt(setting.allReplicas.size()));
        }
    }
}
