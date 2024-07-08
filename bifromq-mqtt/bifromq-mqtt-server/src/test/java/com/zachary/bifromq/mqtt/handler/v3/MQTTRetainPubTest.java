package com.zachary.bifromq.mqtt.handler.v3;


import com.zachary.bifromq.mqtt.handler.BaseMQTTTest;
import com.zachary.bifromq.mqtt.utils.MQTTMessageUtils;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static com.zachary.bifromq.plugin.eventcollector.EventType.CLIENT_CONNECTED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.MSG_RETAINED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.MSG_RETAINED_ERROR;
import static com.zachary.bifromq.plugin.eventcollector.EventType.PUB_ACKED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.RETAIN_MSG_CLEARED;
import static com.zachary.bifromq.retain.rpc.proto.RetainReply.Result.CLEARED;
import static com.zachary.bifromq.retain.rpc.proto.RetainReply.Result.ERROR;
import static com.zachary.bifromq.retain.rpc.proto.RetainReply.Result.RETAINED;

@Slf4j
public class MQTTRetainPubTest extends BaseMQTTTest {

    @Test
    public void qoS1PubRetain() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistDist(true);
        mockRetainPipeline(RETAINED);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishRetainQoS1Message("testTopic", 123);
        channel.writeInbound(publishMessage);
        verifyEvent(3, CLIENT_CONNECTED, MSG_RETAINED, PUB_ACKED);
    }

    @Test
    public void qoS1PubRetainClear() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistDist(true);
        mockRetainPipeline(CLEARED);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishRetainQoS1Message("testTopic", 123);
        channel.writeInbound(publishMessage);
        verifyEvent(3, CLIENT_CONNECTED, RETAIN_MSG_CLEARED, PUB_ACKED);
    }

    @Test
    public void qoS1PubRetainFailed() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistDist(true);
        mockRetainPipeline(ERROR);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishRetainQoS1Message("testTopic", 123);
        channel.writeInbound(publishMessage);
        verifyEvent(2, CLIENT_CONNECTED, MSG_RETAINED_ERROR);
    }

    @Test
    public void qoS2PubRetainFailed() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistDist(true);
        mockRetainPipeline(ERROR);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishRetainQoS2Message("testTopic", 123);
        channel.writeInbound(publishMessage);
        verifyEvent(2, CLIENT_CONNECTED, MSG_RETAINED_ERROR);
    }
}
