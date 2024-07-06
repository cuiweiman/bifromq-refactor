package com.zachary.bifromq.inbox.server.scheduler;

import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.zachary.bifromq.basescheduler.BatchCallBuilder;
import com.zachary.bifromq.basescheduler.IBatchCall;
import com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply;
import com.zachary.bifromq.inbox.rpc.proto.CreateInboxRequest;
import com.zachary.bifromq.inbox.storage.proto.CreateParams;
import com.zachary.bifromq.inbox.storage.proto.CreateRequest;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceRWCoProcInput;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceRWCoProcOutput;
import com.zachary.bifromq.inbox.storage.proto.UpdateRequest;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
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
import static com.zachary.bifromq.plugin.settingprovider.Setting.OfflineExpireTimeSeconds;
import static com.zachary.bifromq.plugin.settingprovider.Setting.OfflineOverflowDropOldest;
import static com.zachary.bifromq.plugin.settingprovider.Setting.OfflineQueueSize;
import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.INBOX_MAX_INBOXES_PER_CREATE;

@Slf4j
public class InboxCreateScheduler extends InboxUpdateScheduler<CreateInboxRequest, CreateInboxReply> {
    private final IBaseKVStoreClient kvStoreClient;

    private final ISettingProvider settingProvider;
    private final int maxInboxesPerCreate;

    public InboxCreateScheduler(IBaseKVStoreClient kvStoreClient, ISettingProvider settingProvider) {
        super(kvStoreClient, "inbox_server_create");
        this.kvStoreClient = kvStoreClient;
        this.settingProvider = settingProvider;
        maxInboxesPerCreate = INBOX_MAX_INBOXES_PER_CREATE.get();

    }

    @Override
    protected BatchCallBuilder<CreateInboxRequest, CreateInboxReply> newBuilder(String name, int maxInflights,
                                                                                KVRangeSetting rangeSetting) {
        return new BatchCreateBuilder(name, maxInflights, rangeSetting, kvStoreClient);
    }

    @Override
    protected ByteString rangeKey(CreateInboxRequest request) {
        return scopedInboxId(request.getClientInfo().getTenantId(), request.getInboxId());
    }

    private class BatchCreateBuilder extends BatchCallBuilder<CreateInboxRequest, CreateInboxReply> {
        private class BatchCreate implements IBatchCall<CreateInboxRequest, CreateInboxReply> {
            // key: scopedInboxIdUtf8
            private final Map<String, CreateParams> inboxCreates = new NonBlockingHashMap<>();

            // key: scopedInboxIdUtf8
            private final Map<CreateInboxRequest, CompletableFuture<CreateInboxReply>> onInboxCreated =
                new NonBlockingHashMap<>();

            @Override
            public boolean isEmpty() {
                return inboxCreates.isEmpty();
            }

            @Override
            public CompletableFuture<CreateInboxReply> add(CreateInboxRequest request) {
                ClientInfo client = request.getClientInfo();
                String tenantId = client.getTenantId();
                String scopedInboxIdUtf8 = scopedInboxId(tenantId, request.getInboxId()).toStringUtf8();
                inboxCreates.computeIfAbsent(scopedInboxIdUtf8, k -> CreateParams.newBuilder()
                    .setExpireSeconds(settingProvider.provide(OfflineExpireTimeSeconds, tenantId))
                    .setLimit(settingProvider.provide(OfflineQueueSize, tenantId))
                    .setDropOldest(settingProvider.provide(OfflineOverflowDropOldest, tenantId))
                    .setClient(client)
                    .build());
                return onInboxCreated.computeIfAbsent(request, k -> new CompletableFuture<>());
            }

            @Override
            public void reset() {
                inboxCreates.clear();
                onInboxCreated.clear();
            }

            @Override
            public CompletableFuture<Void> execute() {
                UpdateRequest updateRequest = UpdateRequest.newBuilder()
                    .setReqId(System.nanoTime())
                    .setCreateInbox(CreateRequest.newBuilder()
                        .putAllInboxes(inboxCreates)
                        .build())
                    .build();
                batchInboxCount.record(inboxCreates.size());
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
                        start.stop(batchCreateTimer);
                        switch (reply.getCode()) {
                            case Ok:
                                try {
                                    return InboxServiceRWCoProcOutput.parseFrom(reply.getRwCoProcResult())
                                        .getReply().getCreateInbox();
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
                        for (CreateInboxRequest request : onInboxCreated.keySet()) {
                            onInboxCreated.get(request).complete(CreateInboxReply.newBuilder()
                                .setReqId(request.getReqId())
                                .setResult(e == null ? CreateInboxReply.Result.OK :
                                    CreateInboxReply.Result.ERROR)
                                .build());
                        }
                        return null;
                    });

            }

            @Override
            public boolean isEnough() {
                return inboxCreates.size() > maxInboxesPerCreate;
            }
        }

        private final IBaseKVStoreClient kvStoreClient;

        private final KVRangeSetting range;

        private final DistributionSummary batchInboxCount;
        private final Timer batchCreateTimer;

        BatchCreateBuilder(String name, int maxInflights, KVRangeSetting range,
                           IBaseKVStoreClient kvStoreClient) {
            super(name, maxInflights);
            this.range = range;
            this.kvStoreClient = kvStoreClient;
            Tags tags = Tags.of("rangeId", KVRangeIdUtil.toShortString(range.id));
            batchInboxCount = DistributionSummary.builder("inbox.server.create.inboxes")
                .tags(tags)
                .register(Metrics.globalRegistry);
            batchCreateTimer = Timer.builder("inbox.server.create.latency")
                .tags(tags)
                .register(Metrics.globalRegistry);
        }

        @Override
        public BatchCreate newBatch() {
            return new BatchCreate();
        }

        @Override
        public void close() {
            Metrics.globalRegistry.remove(batchInboxCount);
            Metrics.globalRegistry.remove(batchCreateTimer);
        }
    }
}
