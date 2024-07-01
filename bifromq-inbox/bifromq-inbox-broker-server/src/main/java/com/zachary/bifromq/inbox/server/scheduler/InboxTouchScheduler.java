package com.zachary.bifromq.inbox.server.scheduler;

import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.zachary.bifromq.basescheduler.BatchCallBuilder;
import com.zachary.bifromq.basescheduler.IBatchCall;
import com.zachary.bifromq.inbox.rpc.proto.DeleteInboxRequest;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceRWCoProcInput;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceRWCoProcOutput;
import com.zachary.bifromq.inbox.storage.proto.TouchRequest;
import com.zachary.bifromq.inbox.storage.proto.UpdateRequest;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.zachary.bifromq.inbox.util.KeyUtil.scopedInboxId;
import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.INBOX_MAX_INBOXES_PER_TOUCH;

@Slf4j
public class InboxTouchScheduler extends InboxUpdateScheduler<InboxTouchScheduler.Touch, Void> {
    private final IBaseKVStoreClient kvStoreClient;
    private final int maxInboxesPerTouch;

    public InboxTouchScheduler(IBaseKVStoreClient kvStoreClient) {
        super(kvStoreClient, "inbox_server_touch");
        this.kvStoreClient = kvStoreClient;
        maxInboxesPerTouch = INBOX_MAX_INBOXES_PER_TOUCH.get();
    }

    @Override
    protected ByteString rangeKey(Touch request) {
        return ByteString.copyFromUtf8(request.scopedInboxIdUtf8);
    }

    @Override
    protected BatchCallBuilder<Touch, Void> newBuilder(String name, int maxInflights, KVRangeSetting rangeSetting) {
        return new BatchTouchBuilder(name, maxInflights, rangeSetting, kvStoreClient);
    }

    private class BatchTouchBuilder extends BatchCallBuilder<Touch, Void> {
        private class BatchTouch implements IBatchCall<Touch, Void> {
            // key: scopedInboxIdUtf8
            private final Map<String, Boolean> inboxTouches = new NonBlockingHashMap<>();

            // key: scopedInboxIdUtf8
            private final Map<Touch, CompletableFuture<Void>> onInboxTouched = new NonBlockingHashMap<>();

            @Override
            public boolean isEmpty() {
                return inboxTouches.isEmpty();
            }

            @Override
            public CompletableFuture<Void> add(Touch request) {
                inboxTouches.compute(request.scopedInboxIdUtf8, (k, v) -> {
                    if (v == null) {
                        return request.keep;
                    }
                    return v && request.keep;
                });
                return onInboxTouched.computeIfAbsent(request, k -> new CompletableFuture<>());
            }

            @Override
            public void reset() {
                inboxTouches.clear();
                onInboxTouched.clear();
            }

            @Override
            public CompletableFuture<Void> execute() {
                UpdateRequest updateRequest = UpdateRequest.newBuilder()
                    .setReqId(System.nanoTime())
                    .setTouch(TouchRequest.newBuilder()
                        .putAllScopedInboxId(inboxTouches)
                        .build())
                    .build();
                batchInboxCount.record(inboxTouches.size());
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
                        start.stop(batchTouchTimer);
                        switch (reply.getCode()) {
                            case Ok:
                                try {
                                    return InboxServiceRWCoProcOutput.parseFrom(reply.getRwCoProcResult())
                                        .getReply().getTouch();
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
                        for (Touch request : onInboxTouched.keySet()) {
                            if (e != null) {
                                onInboxTouched.get(request).completeExceptionally(e);
                            } else {
                                onInboxTouched.get(request).complete(null);
                            }
                        }
                        return null;
                    });
            }

            @Override
            public boolean isEnough() {
                return inboxTouches.size() > maxInboxesPerTouch;
            }
        }

        private final IBaseKVStoreClient kvStoreClient;
        private final KVRangeSetting range;

        private final DistributionSummary batchInboxCount;
        private final Timer batchTouchTimer;

        BatchTouchBuilder(String name, int maxInflights,
                                  KVRangeSetting range, IBaseKVStoreClient kvStoreClient) {
            super(name, maxInflights);
            this.range = range;
            this.kvStoreClient = kvStoreClient;
            Tags tags = Tags.of("rangeId", KVRangeIdUtil.toShortString(range.id));
            batchInboxCount = DistributionSummary.builder("inbox.server.touch.inboxes")
                .tags(tags)
                .register(Metrics.globalRegistry);
            batchTouchTimer = Timer.builder("inbox.server.touch.latency")
                .tags(tags)
                .register(Metrics.globalRegistry);
        }

        @Override
        public BatchTouch newBatch() {
            return new BatchTouch();
        }

        @Override
        public void close() {
            Metrics.globalRegistry.remove(batchInboxCount);
            Metrics.globalRegistry.remove(batchTouchTimer);
        }
    }

    public static class Touch {
        final String scopedInboxIdUtf8;
        final boolean keep;

        public Touch(DeleteInboxRequest req) {
            scopedInboxIdUtf8 = scopedInboxId(req.getClientInfo().getTenantId(), req.getInboxId()).toStringUtf8();
            keep = false;
        }

        public Touch(ByteString scopedInboxId) {
            scopedInboxIdUtf8 = scopedInboxId.toStringUtf8();
            keep = true;
        }
    }
}
