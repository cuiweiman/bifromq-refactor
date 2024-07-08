package com.zachary.bifromq.mqtt.handler.v3;

import com.zachary.bifromq.basehlc.HLC;
import com.zachary.bifromq.mqtt.session.v3.IMQTT3TransientSession;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.pushhandling.DropReason;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.pushhandling.PushEvent;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.pushhandling.QoS0Dropped;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.pushhandling.QoS0Pushed;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.pushhandling.QoS1Dropped;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.pushhandling.QoS2Dropped;
import com.zachary.bifromq.type.ClientInfo;
import com.zachary.bifromq.type.Message;
import com.zachary.bifromq.type.QoS;
import com.zachary.bifromq.type.SubInfo;
import com.zachary.bifromq.type.TopicMessagePack;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttTopicSubscription;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

import static com.zachary.bifromq.mqtt.inbox.util.DeliveryGroupKeyUtil.toDelivererKey;
import static com.zachary.bifromq.mqtt.utils.AuthUtil.buildSubAction;
import static com.zachary.bifromq.plugin.eventcollector.ThreadLocalEventPool.getLocal;

@Slf4j
public final class MQTT3TransientSessionHandler extends MQTT3SessionHandler implements IMQTT3TransientSession {
    private final AtomicLong seqNum = new AtomicLong();
    private boolean clearOnDisconnect = false;

    @Builder
    public MQTT3TransientSessionHandler(ClientInfo clientInfo,
                                        int keepAliveTimeSeconds,
                                        boolean sessionPresent,
                                        WillMessage willMessage) {
        super(clientInfo, keepAliveTimeSeconds, true, sessionPresent, willMessage);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        super.handlerAdded(ctx);
        resumeChannelRead();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        super.channelInactive(ctx);
        if (clearOnDisconnect) {
            submitBgTask(() -> sessionCtx.distClient.clear(System.nanoTime(), clientInfo().getTenantId(), channelId(),
                    toDelivererKey(channelId(), sessionCtx.serverId), 0)
                .whenComplete((clearResult, e) -> {
                    if (e != null) {
                        log.warn("Subscription clean error for client:\n{}", clientInfo());
                    } else {
                        log.trace("Subscription cleaned for client:\n{}", clientInfo());
                    }
                }));
        }
        ctx.fireChannelInactive();
    }

    @Override
    protected void onDistQoS1MessageConfirmed(int messageId, long seq, String topic, Message message,
                                              boolean delivered) {
    }

    @Override
    protected void onDistQoS2MessageConfirmed(int messageId, long seq, String topic, Message message,
                                              boolean delivered) {
    }

    @Override
    protected CompletableFuture<MqttQoS> doSubscribe(long reqId, MqttTopicSubscription topicSub) {
        return sessionCtx.distClient.sub(reqId, clientInfo().getTenantId(), topicSub.topicName(),
                QoS.forNumber(topicSub.qualityOfService().value()), channelId(),
                toDelivererKey(channelId(), sessionCtx.serverId), 0)
            .thenApplyAsync(subResult -> {
                MqttQoS qos = MqttQoS.valueOf(subResult);
                if (qos != MqttQoS.FAILURE) {
                    clearOnDisconnect = true;
                }
                return qos;
            }, ctx.channel().eventLoop());
    }

    @Override
    protected CompletableFuture<Boolean> doUnsubscribe(long reqId, String topicFilter) {
        return sessionCtx.distClient.unsub(reqId, clientInfo().getTenantId(), topicFilter, channelId(),
                toDelivererKey(channelId(), sessionCtx.serverId), 0)
            .exceptionally(e -> true);
    }

    @Override
    public void publish(SubInfo subInfo, TopicMessagePack topicMsgPack) {
        String topic = topicMsgPack.getTopic();
        List<TopicMessagePack.PublisherPack> publisherPacks = topicMsgPack.getMessageList();
        String topicFilter = subInfo.getTopicFilter();
        QoS subQoS = subInfo.getSubQoS();
        cancelOnInactive(authProvider.check(clientInfo(), buildSubAction(topicFilter, subQoS)))
            .thenAcceptAsync(allow -> {
                if (allow) {
                    long timestamp = HLC.INST.getPhysical();
                    for (int i = 0; i < publisherPacks.size(); i++) {
                        TopicMessagePack.PublisherPack senderMsgPack = publisherPacks.get(i);
                        ClientInfo publisher = senderMsgPack.getPublisher();
                        List<Message> messages = senderMsgPack.getMessageList();
                        for (int j = 0; j < messages.size(); j++) {
                            Message message = messages.get(j);
                            QoS finalQoS =
                                QoS.forNumber(Math.min(message.getPubQoS().getNumber(), subQoS.getNumber()));
                            boolean flush = i + 1 == publisherPacks.size() && j + 1 == messages.size();
                            switch (finalQoS) {
                                case AT_MOST_ONCE:
                                    if (bufferCapacityHinter.hasCapacity()) {
                                        if (sendQoS0TopicMessage(topic, message, false, flush, timestamp)) {
                                            if (debugMode) {
                                                eventCollector.report(
                                                    getLocal(QoS0Pushed.class)
                                                        .reqId(message.getMessageId())
                                                        .isRetain(false)
                                                        .sender(publisher)
                                                        .topic(topic)
                                                        .matchedFilter(topicFilter)
                                                        .size(message.getPayload().size())
                                                        .clientInfo(clientInfo()));
                                            }
                                        } else {
                                            eventCollector.report(
                                                getLocal(QoS0Dropped.class)
                                                    .reason(DropReason.ChannelClosed)
                                                    .reqId(message.getMessageId())
                                                    .isRetain(false)
                                                    .sender(publisher)
                                                    .topic(topic)
                                                    .matchedFilter(topicFilter)
                                                    .size(message.getPayload().size())
                                                    .clientInfo(clientInfo()));
                                        }
                                    } else {
                                        flush(true);
                                        eventCollector.report(getLocal(QoS0Dropped.class)
                                            .reason(DropReason.Overflow)
                                            .reqId(message.getMessageId())
                                            .isRetain(false)
                                            .sender(publisher)
                                            .topic(topic)
                                            .matchedFilter(topicFilter)
                                            .size(message.getPayload().size())
                                            .clientInfo(clientInfo()));
                                    }
                                    break;
                                case AT_LEAST_ONCE:
                                    if (bufferCapacityHinter.hasCapacity()) {
                                        int messageId = sendQoS1TopicMessage(seqNum.incrementAndGet(),
                                            topicFilter, topic, message, publisher, false, flush, timestamp);
                                        if (messageId < 0) {
                                            log.error("MessageId exhausted");
                                        }
                                    } else {
                                        flush(true);
                                        eventCollector.report(getLocal(QoS1Dropped.class)
                                            .reason(DropReason.Overflow)
                                            .reqId(message.getMessageId())
                                            .isRetain(false)
                                            .sender(publisher)
                                            .topic(topic)
                                            .matchedFilter(topicFilter)
                                            .size(message.getPayload().size())
                                            .clientInfo(clientInfo()));
                                    }
                                    break;
                                case EXACTLY_ONCE:
                                    if (bufferCapacityHinter.hasCapacity()) {
                                        int messageId = sendQoS2TopicMessage(seqNum.incrementAndGet(),
                                            topicFilter, topic, message, publisher, false, flush, timestamp);
                                        if (messageId < 0) {
                                            log.error("MessageId exhausted");
                                        }
                                    } else {
                                        flush(true);
                                        eventCollector.report(getLocal(QoS2Dropped.class)
                                            .reason(DropReason.Overflow)
                                            .reqId(message.getMessageId())
                                            .isRetain(false)
                                            .sender(publisher)
                                            .topic(topic)
                                            .matchedFilter(topicFilter)
                                            .size(message.getPayload().size())
                                            .clientInfo(clientInfo()));
                                    }
                                    break;
                            }
                        }
                    }
                } else {
                    for (TopicMessagePack.PublisherPack senderMsgPack : publisherPacks) {
                        for (Message message : senderMsgPack.getMessageList()) {
                            PushEvent<?> dropEvent;
                            switch (message.getPubQoS()) {
                                case AT_LEAST_ONCE -> dropEvent = getLocal(QoS1Dropped.class)
                                    .reason(DropReason.NoSubPermission);
                                case EXACTLY_ONCE -> dropEvent = getLocal(QoS2Dropped.class)
                                    .reason(DropReason.NoSubPermission);
                                default -> dropEvent = getLocal(QoS0Dropped.class)
                                    .reason(DropReason.NoSubPermission);
                            }
                            eventCollector.report(dropEvent
                                .reqId(message.getMessageId())
                                .isRetain(false)
                                .sender(senderMsgPack.getPublisher())
                                .topic(topic)
                                .matchedFilter(topicFilter)
                                .size(message.getPayload().size())
                                .clientInfo(clientInfo()));
                        }
                    }
                    // just do unsub once
                    submitBgTask(() -> doUnsubscribe(0, topicFilter).thenAccept(
                        v -> {
                        })
                    );
                }
            }, ctx.channel().eventLoop());
    }
}
