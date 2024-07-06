package com.zachary.bifromq.mqtt.handler.v3;

import com.zachary.bifromq.basehlc.HLC;
import com.zachary.bifromq.inbox.client.IInboxReaderClient;
import com.zachary.bifromq.inbox.storage.proto.Fetched;
import com.zachary.bifromq.inbox.storage.proto.InboxMessage;
import com.zachary.bifromq.mqtt.session.v3.IMQTT3PersistentSession;
import com.zachary.bifromq.mqtt.utils.AuthUtil;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.clientdisconnect.InboxTransientError;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.clientdisconnect.SessionCreateError;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.pushhandling.DropReason;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.pushhandling.QoS0Dropped;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.pushhandling.QoS0Pushed;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.pushhandling.QoS1Dropped;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.pushhandling.QoS2Dropped;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttTopicSubscription;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

import static com.zachary.bifromq.mqtt.handler.v3.MQTTSessionIdUtil.userSessionId;
import static com.zachary.bifromq.plugin.eventcollector.ThreadLocalEventPool.getLocal;
import static com.zachary.bifromq.type.MQTTClientInfoConstants.MQTT_CLIENT_ID_KEY;
import static com.zachary.bifromq.type.QoS.AT_LEAST_ONCE;
import static com.zachary.bifromq.type.QoS.EXACTLY_ONCE;

@Slf4j
public class MQTT3PersistentSessionHandler extends MQTT3SessionHandler implements IMQTT3PersistentSession {
    // key: the seq that wait for confirm, value: the seq for triggering sending confirm to inbox
    private final SortedMap<Long, Long> qos1ConfirmSeqMap = new TreeMap<>();
    private final SortedMap<Long, Long> qos2ConfirmSeqMap = new TreeMap<>();
    private boolean qos1Confirming = false;
    private boolean qos2Confirming = false;
    private long qos1ConfirmUpToSeq;
    private long qos2ConfirmUpToSeq;
    private IInboxReaderClient.IInboxReader inboxReader;

    @Builder
    public MQTT3PersistentSessionHandler(ClientInfo clientInfo, int keepAliveTimeSeconds, WillMessage willMessage,
                                         boolean sessionPresent) {
        super(clientInfo, keepAliveTimeSeconds, false, sessionPresent, willMessage);
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        super.handlerAdded(ctx);
        if (sessionPresent()) {
            setupInboxPipeline();
        } else {
            cancelOnInactive(sessionCtx.inboxClient.create(System.nanoTime(), userSessionId(clientInfo()),
                    clientInfo())).thenAcceptAsync(createInboxReply -> {
                switch (createInboxReply.getResult()) {
                    case OK:
                        setupInboxPipeline();
                        break;
                    case ERROR:
                    default:
                        closeConnectionWithSomeDelay(getLocal(SessionCreateError.class).clientInfo(clientInfo()));
                }
            }, ctx.channel().eventLoop());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        super.channelInactive(ctx);
        if (inboxReader != null) {
            inboxReader.close();
        }
        ctx.fireChannelInactive();
    }

    @Override
    protected void onDistQoS1MessageConfirmed(int messageId, long seq, String topic, Message message,
                                              boolean delivered) {
        // thread the commit using messageId as the reqId
        bufferCapacityHinter.onConfirm();
        assert qos1ConfirmSeqMap.containsKey(seq);
        long triggerSeq = qos1ConfirmSeqMap.remove(seq);
        if (qos1ConfirmSeqMap.isEmpty() || qos1ConfirmSeqMap.firstKey() > triggerSeq) {
            // all seq < triggerSeq has been confirmed
            log.trace("Committing qos1 up to seq: tenantId={}, inboxId={}, seq={}", clientInfo().getTenantId(),
                    clientInfo().getMetadataOrThrow(MQTT_CLIENT_ID_KEY), triggerSeq);
            qos1ConfirmUpToSeq = triggerSeq;
            if (!qos1Confirming) {
                confirmQoS1();
            }
        }
    }

    @Override
    protected void onDistQoS2MessageConfirmed(int messageId, long seq, String topic, Message message,
                                              boolean delivered) {
        // thread the commit using messageId as the reqId
        bufferCapacityHinter.onConfirm();
        assert qos2ConfirmSeqMap.containsKey(seq);
        long triggerSeq = qos2ConfirmSeqMap.remove(seq);
        if (qos2ConfirmSeqMap.isEmpty() || qos2ConfirmSeqMap.firstKey() > triggerSeq) {
            // all seq < triggerSeq has been confirmed
            log.trace("Committing qos2 up to seq: tenantId={}, inboxId={}, seq={}", clientInfo().getTenantId(),
                    clientInfo().getMetadataOrThrow(MQTT_CLIENT_ID_KEY), triggerSeq);
            qos2ConfirmUpToSeq = triggerSeq;
            if (!qos2Confirming) {
                confirmQoS2();
            }
        }
    }

    @Override
    protected CompletableFuture<MqttQoS> doSubscribe(long reqId, MqttTopicSubscription topicSub) {
        String inboxId = userSessionId(clientInfo());
        return sessionCtx.distClient.sub(reqId, clientInfo().getTenantId(), topicSub.topicName(),
                        QoS.forNumber(topicSub.qualityOfService().value()), inboxId,
                        sessionCtx.inboxClient.getDelivererKey(inboxId, clientInfo()), 1)
                .thenApply(MqttQoS::valueOf);
    }

    @Override
    protected CompletableFuture<Boolean> doUnsubscribe(long reqId, String topicFilter) {
        String inboxId = userSessionId(clientInfo());
        return sessionCtx.distClient.unsub(reqId, clientInfo().getTenantId(), topicFilter, inboxId,
                        sessionCtx.inboxClient.getDelivererKey(inboxId, clientInfo()), 1)
                .exceptionally(e -> true);
    }

    private void confirmQoS1() {
        long upToSeq = qos1ConfirmUpToSeq;
        tearDownTasks(inboxReader.commit(System.nanoTime(), QoS.AT_LEAST_ONCE, upToSeq))
                .whenCompleteAsync((v, e) -> {
                    if (upToSeq < qos1ConfirmUpToSeq) {
                        confirmQoS1();
                    } else {
                        qos1Confirming = false;
                    }
                }, ctx.channel().eventLoop());
    }

    private void confirmQoS2() {
        long upToSeq = qos2ConfirmUpToSeq;
        tearDownTasks(inboxReader.commit(System.nanoTime(), QoS.EXACTLY_ONCE, upToSeq))
                .whenCompleteAsync((v, e) -> {
                    if (upToSeq < qos2ConfirmUpToSeq) {
                        confirmQoS2();
                    } else {
                        qos2Confirming = false;
                    }
                }, ctx.channel().eventLoop());
    }

    private void setupInboxPipeline() {
        if (!ctx.channel().isActive()) {
            return;
        }
        String inboxId = userSessionId(clientInfo());
        inboxReader = sessionCtx.inboxClient.openInboxReader(inboxId,
                sessionCtx.inboxClient.getDelivererKey(inboxId, clientInfo()), clientInfo());

        inboxReader.fetch(this::consume);
        bufferCapacityHinter.hint(inboxReader::hint);
        // resume channel read after inbox being setup
        resumeChannelRead();
    }

    private void consume(Fetched fetched) {
        log.trace("Got fetched : tenantId={}, inboxId={}, qos0={}, qos1={}, qos2={}", clientInfo().getTenantId(),
                clientInfo().getMetadataOrThrow(MQTT_CLIENT_ID_KEY), fetched.getQos0SeqCount(),
                fetched.getQos1SeqCount(), fetched.getQos2SeqCount());
        ctx.channel().eventLoop().execute(() -> {
            switch (fetched.getResult()) {
                case OK:
                    long timestamp = HLC.INST.getPhysical();
                    // deal with qos0
                    assert fetched.getQos0SeqCount() == fetched.getQos0MsgCount();
                    for (int i = 0; i < fetched.getQos0SeqCount(); i++) {
                        InboxMessage msg = fetched.getQos0Msg(i);
                        pubQoS0Message(msg.getTopicFilter(), msg.getMsg(), i + 1 == fetched.getQos0MsgCount(),
                                timestamp);
                    }
                    // deal with qos1
                    assert fetched.getQos1SeqCount() == fetched.getQos1MsgCount();
                    if (fetched.getQos1SeqCount() > 0) {
                        long triggerSeq = fetched.getQos1Seq(fetched.getQos1SeqCount() - 1);
                        for (int i = 0; i < fetched.getQos1SeqCount(); i++) {
                            long seq = fetched.getQos1Seq(i);
                            log.trace("QoS1ConfirmSeqMap: {}-{}", seq, triggerSeq);
                            qos1ConfirmSeqMap.put(seq, triggerSeq);
                            InboxMessage distMsg = fetched.getQos1Msg(i);
                            pubQoS1Message(seq, distMsg.getTopicFilter(), distMsg.getMsg(),
                                    i + 1 == fetched.getQos1SeqCount(), timestamp);
                        }
                    }
                    // deal with qos2
                    assert fetched.getQos2SeqCount() == fetched.getQos2MsgCount();
                    if (fetched.getQos2SeqCount() > 0) {
                        long triggerSeq = fetched.getQos2Seq(fetched.getQos2SeqCount() - 1);
                        for (int i = 0; i < fetched.getQos2SeqCount(); i++) {
                            long seq = fetched.getQos2Seq(i);
                            qos2ConfirmSeqMap.put(seq, triggerSeq);
                            InboxMessage distMsg = fetched.getQos2Msg(i);
                            TopicMessage topicMsg = distMsg.getMsg();
                            pubQoS2Message(seq, distMsg.getTopicFilter(), topicMsg, i + 1 == fetched.getQos2SeqCount(),
                                    timestamp);
                        }
                    }
                    break;
                case NO_INBOX:
                    // fall through
                case ERROR:
                    // fall through
                default:
                    closeConnectionWithSomeDelay(getLocal(InboxTransientError.class).clientInfo(clientInfo()));
                    break;
            }
        });
    }

    protected void pubQoS0Message(String topicFilter, TopicMessage topicMsg, boolean flush, long timestamp) {
        String topic = topicMsg.getTopic();
        Message message = topicMsg.getMessage();
        cancelOnInactive(authProvider.check(clientInfo(), AuthUtil.buildSubAction(topicFilter, QoS.AT_MOST_ONCE)))
                .thenAcceptAsync(allow -> {
                    if (allow) {
                        if (sendQoS0TopicMessage(topic, message, false, flush, timestamp)) {
                            if (debugMode) {
                                eventCollector.report(getLocal(QoS0Pushed.class)
                                        .reqId(message.getMessageId())
                                        .isRetain(false)
                                        .sender(topicMsg.getPublisher())
                                        .topic(topic)
                                        .matchedFilter(topicFilter)
                                        .size(message.getPayload().size())
                                        .clientInfo(clientInfo()));
                            }
                        } else {
                            eventCollector.report(getLocal(QoS0Dropped.class)
                                    .reason(DropReason.ChannelClosed)
                                    .reqId(message.getMessageId())
                                    .isRetain(false)
                                    .sender(topicMsg.getPublisher())
                                    .topic(topic)
                                    .matchedFilter(topicFilter)
                                    .size(message.getPayload().size())
                                    .clientInfo(clientInfo()));
                        }
                    } else {
                        eventCollector.report(getLocal(QoS0Dropped.class)
                                .reason(DropReason.NoSubPermission)
                                .reqId(message.getMessageId())
                                .isRetain(false)
                                .sender(topicMsg.getPublisher())
                                .topic(topic)
                                .matchedFilter(topicFilter)
                                .size(message.getPayload().size())
                                .clientInfo(clientInfo()));
                        submitBgTask(
                                () -> doUnsubscribe(message.getMessageId(), topicFilter).thenAccept(
                                        v -> {
                                        }));
                    }
                }, ctx.channel().eventLoop());
    }

    protected void pubQoS1Message(long seq, String topicFilter, TopicMessage topicMsg, boolean flush, long timestamp) {
        String topic = topicMsg.getTopic();
        Message message = topicMsg.getMessage();
        cancelOnInactive(authProvider.check(clientInfo(), AuthUtil.buildSubAction(topicFilter, AT_LEAST_ONCE)))
                .thenAcceptAsync(allow -> {
                    if (allow) {
                        int messageId = sendQoS1TopicMessage(seq, topicFilter, topic, message, topicMsg.getPublisher(),
                                false, flush, timestamp);
                        if (messageId < 0) {
                            log.error("MessageId exhausted");
                        }
                    } else {
                        eventCollector.report(getLocal(QoS1Dropped.class)
                                .reason(DropReason.NoSubPermission)
                                .reqId(message.getMessageId())
                                .isRetain(false)
                                .sender(topicMsg.getPublisher())
                                .topic(topic)
                                .matchedFilter(topicFilter)
                                .size(message.getPayload().size())
                                .clientInfo(clientInfo()));
                        submitBgTask(() -> doUnsubscribe(message.getMessageId(), topicFilter)
                                .thenAccept(v -> {
                                }));
                    }
                }, ctx.channel().eventLoop());
    }

    protected void pubQoS2Message(long seq,
                                  String topicFilter,
                                  TopicMessage topicMsg,
                                  boolean flush,
                                  long timestamp) {
        String topic = topicMsg.getTopic();
        Message message = topicMsg.getMessage();
        cancelOnInactive(authProvider.check(clientInfo(), AuthUtil.buildSubAction(topicFilter, EXACTLY_ONCE)))
                .thenAcceptAsync(allow -> {
                    if (allow) {
                        int messageId = sendQoS2TopicMessage(seq, topicFilter, topic, message, topicMsg.getPublisher(),
                                false, flush, timestamp);
                        if (messageId < 0) {
                            log.error("MessageId exhausted");
                        }
                    } else {
                        eventCollector.report(getLocal(QoS2Dropped.class)
                                .reason(DropReason.NoSubPermission)
                                .reqId(message.getMessageId())
                                .isRetain(false)
                                .sender(topicMsg.getPublisher())
                                .topic(topic)
                                .matchedFilter(topicFilter)
                                .size(message.getPayload().size())
                                .clientInfo(clientInfo()));
                        submitBgTask(() -> doUnsubscribe(message.getMessageId(), topicFilter).thenAccept(
                                v -> {
                                }));
                    }
                }, ctx.channel().eventLoop());
    }
}
