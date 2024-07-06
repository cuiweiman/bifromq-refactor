package com.zachary.bifromq.inbox.store.benchmark;

import com.zachary.bifromq.basecluster.AgentHostOptions;
import com.zachary.bifromq.basecluster.IAgentHost;
import com.zachary.bifromq.basecrdt.service.CRDTServiceOptions;
import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basekv.localengine.RocksDBKVEngineConfigurator;
import com.zachary.bifromq.basekv.store.option.KVRangeStoreOptions;
import com.zachary.bifromq.basekv.store.proto.KVRangeROReply;
import com.zachary.bifromq.basekv.store.proto.KVRangeRORequest;
import com.zachary.bifromq.basekv.store.proto.KVRangeRWReply;
import com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest;
import com.zachary.bifromq.basekv.store.proto.ReplyCode;
import com.zachary.bifromq.inbox.storage.proto.CreateParams;
import com.zachary.bifromq.inbox.storage.proto.CreateReply;
import com.zachary.bifromq.inbox.storage.proto.CreateRequest;
import com.zachary.bifromq.inbox.storage.proto.HasReply;
import com.zachary.bifromq.inbox.storage.proto.HasRequest;
import com.zachary.bifromq.inbox.storage.proto.InboxInsertReply;
import com.zachary.bifromq.inbox.storage.proto.InboxInsertRequest;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceROCoProcInput;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceROCoProcOutput;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceRWCoProcInput;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceRWCoProcOutput;
import com.zachary.bifromq.inbox.storage.proto.MessagePack;
import com.zachary.bifromq.inbox.storage.proto.TouchReply;
import com.zachary.bifromq.inbox.storage.proto.TouchRequest;
import com.zachary.bifromq.inbox.store.IInboxStore;
import com.zachary.bifromq.plugin.eventcollector.Event;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.zachary.bifromq.inbox.util.KeyUtil.scopedInboxId;
import static com.zachary.bifromq.inbox.util.MessageUtil.buildBatchInboxInsertRequest;
import static com.zachary.bifromq.inbox.util.MessageUtil.buildCreateRequest;
import static com.zachary.bifromq.inbox.util.MessageUtil.buildHasRequest;
import static com.zachary.bifromq.inbox.util.MessageUtil.buildTouchRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Slf4j
abstract class InboxStoreState {
    private static final String DB_NAME = "testDB";
    private static final String DB_CHECKPOINT_DIR_NAME = "testDB_cp";

    private static final String DB_WAL_NAME = "testWAL";
    private static final String DB_WAL_CHECKPOINT_DIR = "testWAL_cp";
    public Path dbRootDir;
    protected IBaseKVStoreClient storeClient;
    private IAgentHost agentHost;
    private ICRDTService crdtService;
    protected IInboxStore testStore;

    private IEventCollector eventCollector = new IEventCollector() {
        @Override
        public void report(Event<?> event) {

        }
    };

    public InboxStoreState() {
        try {
            dbRootDir = Files.createTempDirectory("");
        } catch (IOException e) {
        }

        String uuid = UUID.randomUUID().toString();
        AgentHostOptions agentHostOpts = AgentHostOptions.builder()
            .addr("127.0.0.1")
            .baseProbeInterval(Duration.ofSeconds(10))
            .joinRetryInSec(5)
            .joinTimeout(Duration.ofMinutes(5))
            .build();
        agentHost = IAgentHost.newInstance(agentHostOpts);
        agentHost.start();
        crdtService = ICRDTService.newInstance(CRDTServiceOptions.builder().build());
        crdtService.start(agentHost);

        KVRangeStoreOptions options = new KVRangeStoreOptions();
//        options.setWalEngineConfigurator(new InMemoryKVEngineConfigurator());
//        options.setDataEngineConfigurator(new InMemoryKVEngineConfigurator());
        ((RocksDBKVEngineConfigurator) options.getDataEngineConfigurator())
            .setDbCheckpointRootDir(Paths.get(dbRootDir.toString(), DB_CHECKPOINT_DIR_NAME, uuid)
                .toString())
            .setDbRootDir(Paths.get(dbRootDir.toString(), DB_NAME, uuid).toString());
        ((RocksDBKVEngineConfigurator) options.getWalEngineConfigurator())
            .setDbCheckpointRootDir(Paths.get(dbRootDir.toString(), DB_WAL_CHECKPOINT_DIR, uuid)
                .toString())
            .setDbRootDir(Paths.get(dbRootDir.toString(), DB_WAL_NAME, uuid).toString());

        storeClient = IBaseKVStoreClient
            .inProcClientBuilder()
            .clusterId(IInboxStore.CLUSTER_NAME)
            .crdtService(crdtService)
            .build();
        testStore = IInboxStore.
            inProcBuilder()
            .agentHost(agentHost)
            .crdtService(crdtService)
            .storeClient(storeClient)
            .eventCollector(eventCollector)
            .purgeDelay(Duration.ZERO)
            .clock(Clock.systemUTC())
            .kvRangeStoreOptions(options)
            .build();
    }

    @Setup(Level.Trial)
    public void setup() {
        testStore.start(true);
        storeClient.join();
        afterSetup();
        log.info("Setup finished, and start testing");
    }

    abstract void afterSetup();

    @TearDown
    public void teardown() {
        log.info("Finish testing, and tearing down");
        beforeTeardown();
        storeClient.stop();
        testStore.stop();
        crdtService.stop();
        agentHost.shutdown();
        try {
            Files.walk(dbRootDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        } catch (IOException e) {
            log.error("Failed to delete db root dir", e);
        }
    }

    abstract void beforeTeardown();

    protected HasReply requestHas(String tenantId, String inboxId) {
        try {
            long reqId = ThreadLocalRandom.current().nextInt();
            ByteString scopedInboxId = scopedInboxId(tenantId, inboxId);
            KVRangeSetting s = storeClient.findByKey(scopedInboxId).get();
            HasRequest request = HasRequest.newBuilder().addScopedInboxId(scopedInboxId).build();
            InboxServiceROCoProcInput input = buildHasRequest(reqId, request);
            KVRangeROReply reply = storeClient.linearizedQuery(s.leader, KVRangeRORequest.newBuilder()
                .setReqId(reqId)
                .setVer(s.ver)
                .setKvRangeId(s.id)
                .setRoCoProcInput(input.toByteString())
                .build()).join();
            assertEquals(reply.getReqId(), reqId);
            assertEquals(reply.getCode(), ReplyCode.Ok);
            InboxServiceROCoProcOutput output = InboxServiceROCoProcOutput.parseFrom(reply.getRoCoProcResult());
            assertTrue(output.getReply().hasHas());
            assertEquals(output.getReply().getReqId(), reqId);
            return output.getReply().getHas();
        } catch (InvalidProtocolBufferException e) {
            throw new AssertionError(e);
        }
    }

    protected CreateReply requestCreate(String tenantId, String inboxId, int limit, int expireSeconds,
                                        boolean dropOldest) {
        try {
            long reqId = ThreadLocalRandom.current().nextInt();
            ByteString scopedInboxId = scopedInboxId(tenantId, inboxId);
            KVRangeSetting s = storeClient.findByKey(scopedInboxId).get();
            CreateRequest request = CreateRequest.newBuilder()
                .putInboxes(scopedInboxId.toStringUtf8(), CreateParams.newBuilder()
                    .setExpireSeconds(expireSeconds)
                    .setLimit(limit)
                    .setDropOldest(dropOldest)
                    .build())
                .build();
            InboxServiceRWCoProcInput input = buildCreateRequest(reqId, request);
            KVRangeRWReply reply = storeClient.execute(s.leader, KVRangeRWRequest.newBuilder()
                .setReqId(reqId)
                .setVer(s.ver)
                .setKvRangeId(s.id)
                .setRwCoProc(input.toByteString())
                .build()).join();
            assertEquals(reply.getReqId(), reqId);
            assertEquals(reply.getCode(), ReplyCode.Ok);
            InboxServiceRWCoProcOutput output = InboxServiceRWCoProcOutput.parseFrom(reply.getRwCoProcResult());
            assertTrue(output.getReply().hasCreateInbox());
            assertEquals(output.getReply().getReqId(), reqId);
            return output.getReply().getCreateInbox();
        } catch (InvalidProtocolBufferException e) {
            throw new AssertionError(e);
        }
    }

    protected TouchReply requestDelete(String tenantId, String inboxId) {
        try {
            long reqId = ThreadLocalRandom.current().nextInt();
            ByteString scopedInboxId = scopedInboxId(tenantId, inboxId);
            KVRangeSetting s = storeClient.findByKey(scopedInboxId).get();
            TouchRequest request = TouchRequest.newBuilder()
                .putScopedInboxId(scopedInboxId.toStringUtf8(), false)
                .build();
            InboxServiceRWCoProcInput input = buildTouchRequest(reqId, request);
            KVRangeRWReply reply = storeClient.execute(s.leader, KVRangeRWRequest.newBuilder()
                .setReqId(reqId)
                .setVer(s.ver)
                .setKvRangeId(s.id)
                .setRwCoProc(input.toByteString())
                .build()).join();
            assertEquals(reply.getReqId(), reqId);
            assertEquals(reply.getCode(), ReplyCode.Ok);
            InboxServiceRWCoProcOutput output = InboxServiceRWCoProcOutput.parseFrom(reply.getRwCoProcResult());
            assertTrue(output.getReply().hasTouch());
            assertEquals(output.getReply().getReqId(), reqId);
            return output.getReply().getTouch();
        } catch (InvalidProtocolBufferException e) {
            throw new AssertionError(e);
        }
    }

    protected InboxInsertReply requestInsert(String tenantId, String inboxId, InboxInsertRequest request) {
        try {
            long reqId = ThreadLocalRandom.current().nextInt();
            ByteString scopedInboxId = scopedInboxId(tenantId, inboxId);
            KVRangeSetting s = storeClient.findByKey(scopedInboxId).get();
            InboxServiceRWCoProcInput input = buildBatchInboxInsertRequest(reqId, request);
            KVRangeRWReply reply = storeClient.execute(s.leader, KVRangeRWRequest.newBuilder()
                .setReqId(reqId)
                .setVer(s.ver)
                .setKvRangeId(s.id)
                .setRwCoProc(input.toByteString())
                .build()).join();
            assertEquals(reply.getReqId(), reqId);
            assertEquals(reply.getCode(), ReplyCode.Ok);
            InboxServiceRWCoProcOutput output = InboxServiceRWCoProcOutput.parseFrom(reply.getRwCoProcResult());
            assertTrue(output.getReply().hasInsert());
            assertEquals(output.getReply().getInsert(), reqId);
            return output.getReply().getInsert();
        } catch (InvalidProtocolBufferException e) {
            throw new AssertionError(e);
        }
    }

    protected InboxInsertReply requestInsert(String tenantId, String inboxId, String topic, Message... messages) {

        try {
            InboxInsertRequest.Builder builder = InboxInsertRequest.newBuilder();
            InboxInsertRequest request = builder
                .addSubMsgPack(MessagePack.newBuilder()
                    .setSubInfo(SubInfo.newBuilder()
                        .setTenantId(tenantId)
                        .setInboxId(inboxId)
                        .setSubQoS(messages[0].getPubQoS())
                        .build())
                    .addMessages(TopicMessagePack.newBuilder()
                        .setTopic(topic)
                        .addMessage(TopicMessagePack.PublisherPack.newBuilder()
                            .addAllMessage(Arrays.stream(messages).collect(Collectors.toList()))
                            .build())
                        .build())
                    .build())
                .build();
            long reqId = ThreadLocalRandom.current().nextInt();
            ByteString scopedInboxId = scopedInboxId(tenantId, inboxId);
            KVRangeSetting s = storeClient.findByKey(scopedInboxId).get();
            InboxServiceRWCoProcInput input = buildBatchInboxInsertRequest(reqId, request);
            KVRangeRWReply reply = storeClient.execute(s.leader, KVRangeRWRequest.newBuilder()
                .setReqId(reqId)
                .setVer(s.ver)
                .setKvRangeId(s.id)
                .setRwCoProc(input.toByteString())
                .build(), inboxId).join();
            assertEquals(reply.getReqId(), reqId);
            assertEquals(reply.getCode(), ReplyCode.Ok);
            InboxServiceRWCoProcOutput output = InboxServiceRWCoProcOutput.parseFrom(reply.getRwCoProcResult());
            assertTrue(output.getReply().hasInsert());
            assertEquals(output.getReply().getReqId(), reqId);
            return output.getReply().getInsert();
        } catch (InvalidProtocolBufferException e) {
            throw new AssertionError(e);
        }
    }

    protected Message message(QoS qos, String payload) {
        return Message.newBuilder()
            .setMessageId(System.nanoTime())
            .setPubQoS(qos)
            .setPayload(ByteString.copyFromUtf8(payload))
            .build();
    }
}
