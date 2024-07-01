package com.zachary.bifromq.inbox.store;

import com.zachary.bifromq.inbox.storage.proto.InboxFetchReply;
import com.zachary.bifromq.inbox.storage.proto.MessagePack;
import com.zachary.bifromq.inbox.util.KeyUtil;
import com.zachary.bifromq.type.QoS;
import com.zachary.bifromq.type.SubInfo;
import com.zachary.bifromq.type.TopicMessagePack;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class BatchInsertTest extends InboxStoreTest {

    @Test(groups = "integration")
    public void insertQoS012() {
        String tenantId = "tenantA";
        String inboxId = "inboxId";
        SubInfo subInfo1 = SubInfo.newBuilder()
            .setTenantId(tenantId)
            .setInboxId(inboxId)
            .setSubQoS(QoS.AT_MOST_ONCE)
            .setTopicFilter("greeting")
            .build();
        SubInfo subInfo2 = SubInfo.newBuilder()
            .setTenantId(tenantId)
            .setInboxId(inboxId)
            .setSubQoS(QoS.AT_LEAST_ONCE)
            .setTopicFilter("greeting")
            .build();
        SubInfo subInfo3 = SubInfo.newBuilder()
            .setTenantId(tenantId)
            .setInboxId(inboxId)
            .setSubQoS(QoS.EXACTLY_ONCE)
            .setTopicFilter("greeting")
            .build();

        String scopedInboxIdUtf8 = KeyUtil.scopedInboxId(tenantId, inboxId).toStringUtf8();

        TopicMessagePack.PublisherPack msg0 = message(QoS.AT_MOST_ONCE, "hello");
        TopicMessagePack.PublisherPack msg1 = message(QoS.AT_LEAST_ONCE, "world");
        TopicMessagePack.PublisherPack msg2 = message(QoS.EXACTLY_ONCE, "!!!!!");
        requestCreate(tenantId, inboxId, 3, 600, false);
        requestInsert(tenantId, inboxId,
            MessagePack.newBuilder()
                .setSubInfo(subInfo1)
                .addMessages(TopicMessagePack.newBuilder()
                    .setTopic("greeting")
                    .addMessage(msg0)
                    .build())
                .build(),
            MessagePack.newBuilder()
                .setSubInfo(subInfo2)
                .addMessages(TopicMessagePack.newBuilder()
                    .setTopic("greeting")
                    .addMessage(msg1)
                    .build())
                .build(),
            MessagePack.newBuilder()
                .setSubInfo(subInfo3)
                .addMessages(TopicMessagePack.newBuilder()
                    .setTopic("greeting")
                    .addMessage(msg2)
                    .build())
                .build());

        InboxFetchReply reply0 = requestFetchQoS0(tenantId, inboxId, 10);
        assertEquals(reply0.getResultMap().get(scopedInboxIdUtf8).getQos0SeqCount(), 1);

        assertEquals(reply0.getResultMap().get(scopedInboxIdUtf8).getQos0MsgCount(), 1);
        assertEquals(reply0.getResultMap().get(scopedInboxIdUtf8).getQos0Msg(0).getMsg().getMessage(),
            msg0.getMessage(0));

        InboxFetchReply reply1 = requestFetchQoS1(tenantId, inboxId, 10);
        assertEquals(reply1.getResultMap().get(scopedInboxIdUtf8).getQos1SeqCount(), 1);
        assertEquals(reply1.getResultMap().get(scopedInboxIdUtf8).getQos1Seq(0), 0);

        assertEquals(reply1.getResultMap().get(scopedInboxIdUtf8).getQos1MsgCount(), 1);
        assertEquals(reply1.getResultMap().get(scopedInboxIdUtf8).getQos1Msg(0).getMsg().getMessage(),
            msg1.getMessage(0));

        InboxFetchReply reply2 = requestFetchQoS2(tenantId, inboxId, 10, null);
        assertEquals(reply2.getResultMap().get(scopedInboxIdUtf8).getQos2SeqCount(), 1);
        assertEquals(reply2.getResultMap().get(scopedInboxIdUtf8).getQos2Seq(0), 0);

        assertEquals(reply2.getResultMap().get(scopedInboxIdUtf8).getQos2MsgCount(), 1);
        assertEquals(reply2.getResultMap().get(scopedInboxIdUtf8).getQos2Msg(0).getMsg().getMessage(),
            msg2.getMessage(0));
    }

    @Test(groups = "integration")
    public void testInsertSameQoS2MultipleTimes() {
        String tenantId = "tenantId";
        String inboxId = "inboxId";
        SubInfo subInfo = SubInfo.newBuilder()
            .setTenantId(tenantId)
            .setInboxId(inboxId)
            .setSubQoS(QoS.EXACTLY_ONCE)
            .setTopicFilter("greeting")
            .build();

        String scopedInboxIdUtf8 = KeyUtil.scopedInboxId(tenantId, inboxId).toStringUtf8();

        TopicMessagePack.PublisherPack msg = message(QoS.EXACTLY_ONCE, "hello world");
        requestCreate(tenantId, inboxId, 10, 600, false);
        requestInsert(tenantId, inboxId,
            MessagePack.newBuilder()
                .setSubInfo(subInfo)
                .addMessages(TopicMessagePack.newBuilder()
                    .setTopic("greeting")
                    .addMessage(msg)
                    .build())
                .build(),
            MessagePack.newBuilder()
                .setSubInfo(subInfo)
                .addMessages(TopicMessagePack.newBuilder()
                    .setTopic("greeting")
                    .addMessage(msg)
                    .build())
                .build(),
            MessagePack.newBuilder()
                .setSubInfo(subInfo)
                .addMessages(TopicMessagePack.newBuilder()
                    .setTopic("greeting")
                    .addMessage(msg)
                    .build())
                .build());

        InboxFetchReply reply0 = requestFetchQoS0(tenantId, inboxId, 10);
        assertEquals(reply0.getResultMap().get(scopedInboxIdUtf8).getQos0SeqCount(), 0);
        assertEquals(reply0.getResultMap().get(scopedInboxIdUtf8).getQos0MsgCount(), 0);

        InboxFetchReply reply1 = requestFetchQoS1(tenantId, inboxId, 10);
        assertEquals(reply1.getResultMap().get(scopedInboxIdUtf8).getQos1SeqCount(), 0);
        assertEquals(reply1.getResultMap().get(scopedInboxIdUtf8).getQos1MsgCount(), 0);

        InboxFetchReply reply2 = requestFetchQoS2(tenantId, inboxId, 10, null);
        assertEquals(reply2.getResultMap().get(scopedInboxIdUtf8).getQos2SeqCount(), 1);
        assertEquals(reply2.getResultMap().get(scopedInboxIdUtf8).getQos2Seq(0), 0);
        assertEquals(reply2.getResultMap().get(scopedInboxIdUtf8).getQos2Msg(0).getMsg().getMessage(),
            msg.getMessage(0));
    }
}
