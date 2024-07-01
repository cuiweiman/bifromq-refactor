package com.zachary.bifromq.inbox.server.scheduler;

import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest;
import com.zachary.bifromq.basekv.store.proto.ReplyCode;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.zachary.bifromq.basescheduler.BatchCallBuilder;
import com.zachary.bifromq.basescheduler.IBatchCall;
import com.zachary.bifromq.inbox.rpc.proto.SendResult;
import com.zachary.bifromq.inbox.rpc.proto.SendResult.Result;
import com.zachary.bifromq.inbox.storage.proto.InboxInsertRequest;
import com.zachary.bifromq.inbox.storage.proto.InboxInsertResult;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceRWCoProcInput;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceRWCoProcOutput;
import com.zachary.bifromq.inbox.storage.proto.MessagePack;
import com.zachary.bifromq.inbox.storage.proto.UpdateRequest;
import com.zachary.bifromq.inbox.util.KeyUtil;
import com.zachary.bifromq.type.SubInfo;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.INBOX_MAX_BYTES_PER_INSERT;
import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.INBOX_MAX_INBOXES_PER_INSERT;

@Slf4j
public class InboxInsertScheduler extends InboxUpdateScheduler<MessagePack, SendResult.Result> {
    private final IBaseKVStoreClient kvStoreClient;

    private final int maxInboxPerBatch;

    private final int maxSizePerBatch;

    public InboxInsertScheduler(IBaseKVStoreClient kvStoreClient) {
        super(kvStoreClient, "inbox_server_insert");
        this.kvStoreClient = kvStoreClient;
        maxInboxPerBatch = INBOX_MAX_INBOXES_PER_INSERT.get();
        maxSizePerBatch = INBOX_MAX_BYTES_PER_INSERT.get();
    }

    @Override
    protected ByteString rangeKey(MessagePack request) {
        return KeyUtil.scopedInboxId(request.getSubInfo().getTenantId(), request.getSubInfo().getInboxId());
    }

    @Override
    protected BatchCallBuilder<MessagePack, Result> newBuilder(String name, int maxInflights,
                                                               KVRangeSetting rangeSetting) {
        return new BatchInsertBuilder(name, maxInflights, rangeSetting, kvStoreClient);
    }

    private final class BatchInsertBuilder extends BatchCallBuilder<MessagePack, Result> {
        private class BatchInsert implements IBatchCall<MessagePack, Result> {
            private final AtomicInteger msgSize = new AtomicInteger();

            // key: scopedInboxIdUtf8
            private final Queue<InsertTask> inboxInserts = new ConcurrentLinkedQueue<>();

            @Override
            public boolean isEmpty() {
                return inboxInserts.isEmpty();
            }

            @Override
            public CompletableFuture<SendResult.Result> add(MessagePack request) {
                InsertTask task = new InsertTask(request);
                inboxInserts.add(task);
                msgSize.addAndGet(request.getSerializedSize());
                return task.onDone;
            }

            @Override
            public void reset() {
                msgSize.set(0);
                inboxInserts.clear();
            }

            @Override
            public CompletableFuture<Void> execute() {
                UpdateRequest updateRequest = UpdateRequest.newBuilder()
                    .setReqId(System.nanoTime())
                    .setInsert(InboxInsertRequest.newBuilder()
                        .addAllSubMsgPack(inboxInserts.stream()
                            .map(t -> t.request)
                            .collect(Collectors.toList()))
                        .build())
                    .build();
                batchInboxCount.record(inboxInserts.size());
                batchMsgCount.record(msgSize.get());
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
                        start.stop(batchInsertTimer);
                        if (reply.getCode() == ReplyCode.Ok) {
                            try {
                                return InboxServiceRWCoProcOutput.parseFrom(reply.getRwCoProcResult())
                                    .getReply().getInsert();
                            } catch (InvalidProtocolBufferException e) {
                                log.error("Unable to parse rw co-proc output", e);
                                throw new RuntimeException(e);
                            }
                        }
                        log.warn("Failed to exec rw co-proc[code={}]", reply.getCode());
                        throw new RuntimeException();
                    })
                    .handle((v, e) -> {
                        if (e != null) {
                            // TODO: report insert rpc error?
                            while (!inboxInserts.isEmpty()) {
                                InsertTask task = inboxInserts.poll();
                                task.onDone.complete(Result.OK);
                            }
                        } else {
                            Map<SubInfo, SendResult.Result> insertResults = new HashMap<>();
                            for (InboxInsertResult result : v.getResultsList()) {
                                if (result.getResult() == InboxInsertResult.Result.NO_INBOX) {
                                    insertResults.put(result.getSubInfo(), Result.NO_INBOX);
                                } else {
                                    insertResults.put(result.getSubInfo(), Result.OK);
                                }
                            }
                            while (!inboxInserts.isEmpty()) {
                                InsertTask task = inboxInserts.poll();
                                task.onDone.complete(insertResults.get(task.request.getSubInfo()));
                            }
                        }
                        return null;
                    });
            }

            @Override
            public boolean isEnough() {
                return inboxInserts.size() > maxInboxPerBatch || msgSize.get() > maxSizePerBatch;
            }
        }

        private final IBaseKVStoreClient kvStoreClient;

        private final KVRangeSetting range;

        private final DistributionSummary batchInboxCount;

        private final DistributionSummary batchMsgCount;

        private final Timer batchInsertTimer;

        BatchInsertBuilder(String name, int maxInflights,
                           KVRangeSetting range, IBaseKVStoreClient kvStoreClient) {
            super(name, maxInflights);
            this.range = range;
            this.kvStoreClient = kvStoreClient;
            Tags tags = Tags.of("rangeId", KVRangeIdUtil.toShortString(range.id));
            batchInboxCount = DistributionSummary.builder("inbox.server.insert.inboxes")
                .tags(tags)
                .register(Metrics.globalRegistry);
            batchMsgCount = DistributionSummary.builder("inbox.server.insert.msgs")
                .tags(tags)
                .register(Metrics.globalRegistry);
            batchInsertTimer = Timer.builder("inbox.server.insert.latency")
                .tags(tags)
                .register(Metrics.globalRegistry);
        }

        @Override
        public BatchInsert newBatch() {
            return new BatchInsert();
        }

        @Override
        public void close() {
            Metrics.globalRegistry.remove(batchInboxCount);
            Metrics.globalRegistry.remove(batchMsgCount);
            Metrics.globalRegistry.remove(batchInsertTimer);
        }
    }

    @AllArgsConstructor
    public static final class InsertTask {
        final MessagePack request;
        final CompletableFuture<SendResult.Result> onDone = new CompletableFuture<>();
    }
}
