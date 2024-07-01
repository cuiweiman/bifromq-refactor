package com.zachary.bifromq.inbox.server;

import com.zachary.bifromq.inbox.client.IInboxReaderClient;
import com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply;
import com.zachary.bifromq.inbox.storage.proto.Fetched;
import com.zachary.bifromq.plugin.subbroker.DeliveryPack;
import com.zachary.bifromq.plugin.subbroker.DeliveryResult;
import com.zachary.bifromq.plugin.subbroker.IDeliverer;
import com.zachary.bifromq.type.ClientInfo;
import com.zachary.bifromq.type.Message;
import com.zachary.bifromq.type.QoS;
import com.zachary.bifromq.type.SubInfo;
import com.zachary.bifromq.type.TopicMessagePack;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Collections.singletonList;
import static org.testng.Assert.assertEquals;

public class InboxInsertTest extends InboxServiceTest {
    @Test(groups = "integration")
    public void insert() throws InterruptedException {
        String tenantId = "tenantA";
        String inboxId = "inbox1";
        String delivererKey = "deliverer1";
        ClientInfo clientInfo = ClientInfo.newBuilder().setTenantId(tenantId).build();
        long reqId = System.nanoTime();
        CreateInboxReply createInboxReply = inboxReaderClient.create(reqId, inboxId, clientInfo).join();
        assertEquals(createInboxReply.getReqId(), reqId);
        assertEquals(createInboxReply.getResult(), CreateInboxReply.Result.OK);

        IDeliverer writer = inboxBrokerClient.open(delivererKey);
        Message msg = Message.newBuilder()
            .setPubQoS(QoS.AT_LEAST_ONCE)
            .build();
        TopicMessagePack.PublisherPack publisherPack = TopicMessagePack.PublisherPack
            .newBuilder()
            .addMessage(msg)
            .build();
        TopicMessagePack pack = TopicMessagePack.newBuilder()
            .setTopic("topic")
            .addMessage(publisherPack)
            .build();
        SubInfo subInfo = SubInfo.newBuilder()
            .setTenantId(tenantId)
            .setInboxId(inboxId)
            .setTopicFilter("topic")
            .setSubQoS(QoS.AT_LEAST_ONCE)
            .build();
        List<DeliveryPack> msgPack = new LinkedList<>();
        msgPack.add(new DeliveryPack(pack, singletonList(subInfo)));
        Map<SubInfo, DeliveryResult> result = writer.deliver(msgPack).join();
        assertEquals(result.get(subInfo), DeliveryResult.OK);

        IInboxReaderClient.IInboxReader reader = inboxReaderClient.openInboxReader(inboxId, delivererKey, clientInfo);

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Fetched> fetchedRef = new AtomicReference<>();
        reader.fetch(fetched -> {
            fetchedRef.set(fetched);
            latch.countDown();
        });
        latch.await();
        assertEquals(fetchedRef.get().getResult(), Fetched.Result.OK);
        assertEquals(fetchedRef.get().getQos1MsgCount(), 1);
        assertEquals(fetchedRef.get().getQos1Msg(0).getMsg().getMessage(), msg);

        reader.close();
        writer.close();
    }

    @Test(groups = "integration")
    public void insertMultiMsgPackWithSameInbox() throws InterruptedException {
        String tenantId = "trafficA";
        String inboxId = "inbox1";
        String delivererKey = "deliverer1";
        ClientInfo clientInfo = ClientInfo.newBuilder().setTenantId(tenantId).build();
        long reqId = System.nanoTime();
        CreateInboxReply createInboxReply = inboxReaderClient.create(reqId, inboxId, clientInfo).join();
        assertEquals(createInboxReply.getReqId(), reqId);
        assertEquals(createInboxReply.getResult(), CreateInboxReply.Result.OK);

        IDeliverer writer = inboxBrokerClient.open(delivererKey);
        Message msg = Message.newBuilder()
            .setPubQoS(QoS.AT_LEAST_ONCE)
            .build();
        TopicMessagePack.PublisherPack publisherPack = TopicMessagePack.PublisherPack
            .newBuilder()
            .addMessage(msg)
            .build();
        TopicMessagePack pack1 = TopicMessagePack.newBuilder()
            .setTopic("topic")
            .addMessage(publisherPack)
            .build();
        TopicMessagePack pack2 = TopicMessagePack.newBuilder()
            .setTopic("topic")
            .addMessage(publisherPack)
            .build();
        SubInfo subInfo = SubInfo.newBuilder()
            .setTenantId(tenantId)
            .setInboxId(inboxId)
            .setTopicFilter("topic")
            .setSubQoS(QoS.AT_LEAST_ONCE)
            .build();
        List<DeliveryPack> msgPack1 = new LinkedList<>();
        msgPack1.add(new DeliveryPack(pack1, singletonList(subInfo)));
        List<DeliveryPack> msgPack2 = new LinkedList<>();
        msgPack2.add(new DeliveryPack(pack2, singletonList(subInfo)));
        CompletableFuture<Map<SubInfo, DeliveryResult>> writeFuture1 = writer.deliver(msgPack1);
        CompletableFuture<Map<SubInfo, DeliveryResult>> writeFuture2 = writer.deliver(msgPack2);
        CompletableFuture<Map<SubInfo, DeliveryResult>> writeFuture3 = writer.deliver(msgPack2);

        Map<SubInfo, DeliveryResult> result1 = writeFuture1.join();
        assertEquals(result1.get(subInfo), DeliveryResult.OK);
        Map<SubInfo, DeliveryResult> result2 = writeFuture2.join();
        assertEquals(result2.get(subInfo), DeliveryResult.OK);
        Map<SubInfo, DeliveryResult> result3 = writeFuture3.join();
        assertEquals(result3.get(subInfo), DeliveryResult.OK);

        IInboxReaderClient.IInboxReader reader = inboxReaderClient.openInboxReader(inboxId, delivererKey, clientInfo);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Fetched> fetchedRef = new AtomicReference<>();
        reader.fetch(fetched -> {
            fetchedRef.set(fetched);
            latch.countDown();
        });
        latch.await();
        assertEquals(fetchedRef.get().getResult(), Fetched.Result.OK);
        assertEquals(fetchedRef.get().getQos1MsgCount(), 3);
        assertEquals(fetchedRef.get().getQos1Msg(0).getMsg().getMessage(), msg);

        reader.close();
        writer.close();
    }
}
