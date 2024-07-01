package com.zachary.bifromq.inbox.server.benchmark;

import com.zachary.bifromq.plugin.subbroker.DeliveryPack;
import com.zachary.bifromq.plugin.subbroker.IDeliverer;
import com.zachary.bifromq.type.ClientInfo;
import com.zachary.bifromq.type.Message;
import com.zachary.bifromq.type.SubInfo;
import com.zachary.bifromq.type.TopicMessagePack;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static com.zachary.bifromq.type.QoS.AT_LEAST_ONCE;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

@Slf4j
@State(Scope.Benchmark)
public class QoS1InsertState extends InboxServiceState {
    private static final String tenantId = "testTraffic";
    private TopicMessagePack msgs;
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
        TopicMessagePack.Builder builder = TopicMessagePack.newBuilder().setTopic("greeting");
        IntStream.range(0, 10).forEach(j -> builder
            .addMessage(TopicMessagePack.PublisherPack.newBuilder()
                .addMessage(Message.newBuilder()
                    .setPubQoS(AT_LEAST_ONCE)
                    .setPayload(ByteString.copyFromUtf8("hello"))
                    .build())
                .build())
        );
        msgs = builder.build();
    }

    @Override
    protected void beforeTeardown() {
        inboxWriter.close();
    }

    public void insert() {
        inboxWriter.deliver(singleton(new DeliveryPack(msgs, singletonList(SubInfo.newBuilder()
            .setTenantId(tenantId)
            .setInboxId(ThreadLocalRandom.current().nextInt(0, inboxCount) + "")
            .setTopicFilter("greeting")
            .setSubQoS(AT_LEAST_ONCE)
            .build())))).join();
    }
}
