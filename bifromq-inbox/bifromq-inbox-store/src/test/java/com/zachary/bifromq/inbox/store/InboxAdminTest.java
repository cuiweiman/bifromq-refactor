package com.zachary.bifromq.inbox.store;

import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.store.proto.KVRangeROReply;
import com.zachary.bifromq.basekv.store.proto.KVRangeRORequest;
import com.zachary.bifromq.inbox.storage.proto.HasReply;
import com.zachary.bifromq.type.QoS;
import com.zachary.bifromq.type.SubInfo;
import com.zachary.bifromq.type.TopicMessagePack;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Clock;

import static com.zachary.bifromq.inbox.util.KeyUtil.scopedInboxId;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class InboxAdminTest extends InboxStoreTest {
    @Mock
    private Clock clock;

    @BeforeMethod(groups = "integration")
    public void setup() throws IOException {
        super.setup();
        when(clock.millis()).thenReturn(0L);
    }

    @Override
    protected Clock getClock() {
        return clock;
    }

    @Test(groups = "integration")
    public void createAndHasCheck() {
        String tenantId = "tenantId";
        String inboxId = "inboxId";
        HasReply has = requestHas(tenantId, inboxId);
        assertFalse(has.getExistsMap().get(scopedInboxId(tenantId, inboxId).toStringUtf8()));
        requestCreate(tenantId, inboxId, 10, 100, false);
        has = requestHas(tenantId, inboxId);
        assertTrue(has.getExistsMap().get(scopedInboxId(tenantId, inboxId).toStringUtf8()));
    }

    @Test(groups = "integration")
    public void expireAndHasCheck() {
        String tenantId = "tenantId";
        String inboxId = "inboxId";
        HasReply has = requestHas(tenantId, inboxId);
        assertFalse(has.getExistsMap().get(scopedInboxId(tenantId, inboxId).toStringUtf8()));
        requestCreate(tenantId, inboxId, 10, 2, false);
        has = requestHas(tenantId, inboxId);
        assertTrue(has.getExistsMap().get(scopedInboxId(tenantId, inboxId).toStringUtf8()));
        when(clock.millis()).thenReturn(2100L);
        has = requestHas(tenantId, inboxId);
        assertFalse(has.getExistsMap().get(scopedInboxId(tenantId, inboxId).toStringUtf8()));
    }

    @Test(groups = "integration")
    public void createAndDelete() {
        String tenantId = "tenantId";
        String inboxId1 = "inboxId1";
        String inboxId2 = "inboxId2";
        requestCreate(tenantId, inboxId1, 10, 10, false);
        requestCreate(tenantId, inboxId2, 10, 10, false);
        assertTrue(requestHas(tenantId, inboxId1).getExistsMap()
            .get(scopedInboxId(tenantId, inboxId1).toStringUtf8()));
        requestDelete(tenantId, inboxId1);

        assertFalse(requestHas(tenantId, inboxId1).getExistsMap()
            .get(scopedInboxId(tenantId, inboxId1).toStringUtf8()));
        assertTrue(requestHas(tenantId, inboxId2).getExistsMap()
            .get(scopedInboxId(tenantId, inboxId2).toStringUtf8()));
    }

    @Test(groups = "integration")
    public void deleteNonExist() {
        String tenantId = "tenantId";
        String inboxId = "inboxId";
        requestDelete(tenantId, inboxId);
        HasReply has = requestHas(tenantId, inboxId);
        assertFalse(has.getExistsMap().get(scopedInboxId(tenantId, inboxId).toStringUtf8()));
    }

    @Test(groups = "integration")
    public void testGC() {
        String tenantId = "tenantId";
        String inboxId = "inboxId";
        String topic = "greeting";
        TopicMessagePack.PublisherPack msg0 = message(QoS.AT_MOST_ONCE, "hello");
        TopicMessagePack.PublisherPack msg1 = message(QoS.AT_LEAST_ONCE, "world");
        TopicMessagePack.PublisherPack msg2 = message(QoS.EXACTLY_ONCE, "!!!!!");
        requestCreate(tenantId, inboxId, 3, 1, true);
        SubInfo subInfo = SubInfo.newBuilder()
            .setTenantId(tenantId)
            .setInboxId(inboxId)
            .setSubQoS(QoS.AT_MOST_ONCE)
            .setTopicFilter("greeting")
            .build();
        requestInsert(subInfo, topic, msg0, msg1, msg2);
        // advance to gc'able
        when(clock.millis()).thenReturn(1100L);
        KVRangeSetting s = storeClient.findByKey(scopedInboxId(tenantId, inboxId)).get();
        testStore.gcRange(s).join();

        KVRangeROReply reply = storeClient.query(s.leader, KVRangeRORequest.newBuilder()
            .setReqId(System.nanoTime())
            .setKvRangeId(s.id)
            .setVer(s.ver)
            .setExistKey(scopedInboxId(tenantId, inboxId))
            .build()).join();
        assertFalse(reply.getExistResult());
    }
}
