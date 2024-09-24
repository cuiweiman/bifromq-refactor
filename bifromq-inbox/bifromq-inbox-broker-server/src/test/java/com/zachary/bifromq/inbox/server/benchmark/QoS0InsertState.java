package com.zachary.bifromq.inbox.server.benchmark;

import com.zachary.bifromq.plugin.subbroker.DeliveryPack;
import com.zachary.bifromq.plugin.subbroker.DeliveryResult;
import com.zachary.bifromq.plugin.subbroker.IDeliverer;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.type.ClientInfo;
import com.zachary.bifromq.type.Message;
import com.zachary.bifromq.type.SubInfo;
import com.zachary.bifromq.type.TopicMessagePack;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static com.zachary.bifromq.type.QoS.AT_MOST_ONCE;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

@Slf4j
@State(Scope.Benchmark)
public class QoS0InsertState extends InboxServiceState {
    private static final String tenantId = "testTraffic";
    private final TopicMessagePack msg = TopicMessagePack.newBuilder()
        .setTopic("greeting")
        .addMessage(TopicMessagePack.PublisherPack.newBuilder()
            .addMessage(Message.newBuilder()
                .setPubQoS(AT_MOST_ONCE)
                .setPayload(ByteString.copyFromUtf8("hello"))
                .build())
            .build())
        .build();
    private static final int inboxCount = 100;

    private IDeliverer inboxWriter;

    @Override
    protected void afterSetup() {
        int i = 0;
        while (i < inboxCount) {
            inboxReaderClient.create(System.nanoTime(), i + "", ClientInfo.newBuilder()
                .setTenantId(tenantId)
                .build()).join();
            i++;
        }
        inboxWriter = inboxBrokerClient.open("deliverer1");
    }

    @Override
    protected void beforeTeardown() {
        inboxWriter.close();
    }

    public Map<SubInfo, DeliveryResult> insert() {
        return inboxWriter.deliver(singleton(new DeliveryPack(msg, singletonList(SubInfo.newBuilder()
            .setTenantId(tenantId)
            .setInboxId(ThreadLocalRandom.current().nextInt(0, 100) + "")
            .setTopicFilter("greeting")
            .setSubQoS(AT_MOST_ONCE)
            .build())))).join();
    }
}
