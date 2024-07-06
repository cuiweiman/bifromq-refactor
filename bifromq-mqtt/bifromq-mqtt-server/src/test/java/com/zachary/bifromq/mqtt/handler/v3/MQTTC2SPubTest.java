/*
 * Copyright (c) 2023. Baidu, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zachary.bifromq.mqtt.handler.v3;


import com.zachary.bifromq.mqtt.handler.BaseMQTTTest;
import com.zachary.bifromq.mqtt.utils.MQTTMessageUtils;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.plugin.eventcollector.EventType.CLIENT_CONNECTED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.DISCARD;
import static com.zachary.bifromq.plugin.eventcollector.EventType.INVALID_TOPIC;
import static com.zachary.bifromq.plugin.eventcollector.EventType.MALFORMED_TOPIC;
import static com.zachary.bifromq.plugin.eventcollector.EventType.NO_PUB_PERMISSION;
import static com.zachary.bifromq.plugin.eventcollector.EventType.PROTOCOL_VIOLATION;
import static com.zachary.bifromq.plugin.eventcollector.EventType.PUB_ACKED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.PUB_ACK_DROPPED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.PUB_ACTION_DISALLOW;
import static com.zachary.bifromq.plugin.eventcollector.EventType.PUB_RECED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.PUB_REC_DROPPED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.QOS0_DIST_ERROR;
import static com.zachary.bifromq.plugin.eventcollector.EventType.QOS1_DIST_ERROR;
import static com.zachary.bifromq.plugin.eventcollector.EventType.QOS2_DIST_ERROR;
import static com.zachary.bifromq.plugin.settingprovider.Setting.MsgPubPerSec;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Slf4j
public class MQTTC2SPubTest extends BaseMQTTTest {

    @Test
    public void qoS0Pub() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistDist(true);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishQoS0Message("testTopic", 123);
        channel.writeInbound(publishMessage);
        verifyEvent(1, CLIENT_CONNECTED);
        verify(distClient, times(1)).pub(anyLong(), anyString(), any(QoS.class), any(ByteBuffer.class), anyInt(),
            any(ClientInfo.class));
    }


    @Test
    public void qoS0PubBadMessage() {
        connectAndVerify(true);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishQoS0DupMessage("testTopic", 123);
        channel.writeInbound(publishMessage);
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.runPendingTasks();
        assertFalse(channel.isActive());
        verifyEvent(2, CLIENT_CONNECTED, PROTOCOL_VIOLATION);
    }

    @Test
    public void qoS0PubDistFailed() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistDist(false);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishQoS0Message("testTopic", 123);
        channel.writeInbound(publishMessage);
        verifyEvent(2, CLIENT_CONNECTED, QOS0_DIST_ERROR);
    }

    @Test
    public void qos0PubDistDrop() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistDrop();
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishQoS0Message("testTopic", 123);
        channel.writeInbound(publishMessage);
        verifyEvent(2, CLIENT_CONNECTED, QOS0_DIST_ERROR);
    }

    @Test
    public void qoS0PubAuthFailed() {
        // not by pass
        connectAndVerify(true);
        mockAuthCheck(false);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishQoS0Message("testTopic", 123);
        channel.writeInbound(publishMessage);
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.runPendingTasks();
        assertFalse(channel.isActive());
        verifyEvent(3, CLIENT_CONNECTED, PUB_ACTION_DISALLOW, NO_PUB_PERMISSION);
    }

    @Test
    public void qoS1Pub() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistDist(true);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishQoS1Message("testTopic", 123);
        channel.writeInbound(publishMessage);
        MqttPubAckMessage ackMessage = channel.readOutbound();
        assertEquals(ackMessage.variableHeader().messageId(), 123);
        verifyEvent(2, CLIENT_CONNECTED, PUB_ACKED);
    }

    @Test
    public void qoS1PubDistFailed() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistDist(false);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishQoS1Message("testTopic", 123);
        channel.writeInbound(publishMessage);
        verifyEvent(2, CLIENT_CONNECTED, QOS1_DIST_ERROR);
    }

    @Test
    public void qos1PubDistDrop() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistDrop();
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishQoS1Message("testTopic", 123);
        channel.writeInbound(publishMessage);
        verifyEvent(2, CLIENT_CONNECTED, QOS1_DIST_ERROR);
    }


    @Test
    public void qoS1PubAckWithUnWritable() {
        connectAndVerify(true);
        mockAuthCheck(true);
        CompletableFuture<Void> distResult = new CompletableFuture<>();
        when(distClient.pub(anyLong(), anyString(), any(QoS.class), any(ByteBuffer.class), anyInt(),
            any(ClientInfo.class))).thenReturn(distResult);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishQoS1Message("testTopic", 123);
        channel.writeInbound(publishMessage);
        // make channel unWritable
        channel.writeOneOutbound(MQTTMessageUtils.largeMqttMessage(300 * 1024));
        assertFalse(channel.isWritable());
        distResult.complete(null);
        channel.runPendingTasks();
        verifyEvent(2, CLIENT_CONNECTED, PUB_ACK_DROPPED);
    }

    @Test
    public void qoS1PubAuthFailed() {
        // not by pass
        connectAndVerify(true);
        mockAuthCheck(false);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishQoS1Message("testTopic", 123);
        channel.writeInbound(publishMessage);
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.runPendingTasks();
        assertFalse(channel.isActive());
        verifyEvent(3, CLIENT_CONNECTED, PUB_ACTION_DISALLOW, NO_PUB_PERMISSION);
    }

    @Test
    public void qoS2Pub() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistDist(true);
        // publish
        channel.writeInbound(MQTTMessageUtils.publishQoS2Message("testTopic", 123));
        MqttMessage mqttMessage = channel.readOutbound();
        assertEquals(mqttMessage.fixedHeader().messageType(), MqttMessageType.PUBREC);
        assertEquals(((MqttMessageIdVariableHeader) mqttMessage.variableHeader()).messageId(), 123);
        assertTrue(sessionContext.isConfirming(tenantId, channel.id().asLongText(), 123));
        // publish release
        channel.writeInbound(MQTTMessageUtils.publishRelMessage(123));
        mqttMessage = channel.readOutbound();
        assertEquals(mqttMessage.fixedHeader().messageType(), MqttMessageType.PUBCOMP);
        assertEquals(((MqttMessageIdVariableHeader) mqttMessage.variableHeader()).messageId(), 123);
        verifyEvent(2, CLIENT_CONNECTED, PUB_RECED);
        assertFalse(sessionContext.isConfirming(tenantId, channel.id().asLongText(), 123));
    }

    @Test
    public void qoS2PubDistFailed() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistDist(false);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishQoS2Message("testTopic", 123);
        channel.writeInbound(publishMessage);
        verifyEvent(2, CLIENT_CONNECTED, QOS2_DIST_ERROR);
        assertFalse(sessionContext.isConfirming(tenantId, channel.id().asLongText(), 123));
    }

    @Test
    public void qoS2PubDistDrop() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistDrop();
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishQoS2Message("testTopic", 123);
        channel.writeInbound(publishMessage);
        verifyEvent(2, CLIENT_CONNECTED, QOS2_DIST_ERROR);
        assertFalse(sessionContext.isConfirming(tenantId, channel.id().asLongText(), 123));
    }


    @Test
    public void qoS2PubAckWithUnWritable() {
        connectAndVerify(true);
        mockAuthCheck(true);
        CompletableFuture<Void> distResult = new CompletableFuture<>();
        when(distClient.pub(anyLong(), anyString(), any(QoS.class), any(ByteBuffer.class), anyInt(),
            any(ClientInfo.class))).thenReturn(distResult);
        channel.writeInbound(MQTTMessageUtils.publishQoS2Message("testTopic", 123));

        // make channel unWritable and drop PubRec
        channel.writeOneOutbound(MQTTMessageUtils.largeMqttMessage(300 * 1024));
        assertFalse(channel.isWritable());
        distResult.complete(null);
        channel.runPendingTasks();
        verifyEvent(2, CLIENT_CONNECTED, PUB_REC_DROPPED);
        assertTrue(sessionContext.isConfirming(tenantId, channel.id().asLongText(), 123));

        // flush channel
        channel.flush();
        channel.readOutbound();
        assertTrue(channel.isWritable());

        // client did not receive PubRec, resend pub and receive PubRec
        channel.writeInbound(MQTTMessageUtils.publishQoS2Message("testTopic", 123));
        channel.runPendingTasks();
        verifyEvent(2, CLIENT_CONNECTED, PUB_REC_DROPPED);
        MqttMessage mqttMessage = channel.readOutbound();
        assertEquals(mqttMessage.fixedHeader().messageType(), MqttMessageType.PUBREC);
        assertEquals(((MqttMessageIdVariableHeader) mqttMessage.variableHeader()).messageId(), 123);

        // continue to publish PubRel
        channel.writeInbound(MQTTMessageUtils.publishRelMessage(123));
        mqttMessage = channel.readOutbound();
        assertEquals(mqttMessage.fixedHeader().messageType(), MqttMessageType.PUBCOMP);
        assertEquals(((MqttMessageIdVariableHeader) mqttMessage.variableHeader()).messageId(), 123);
        verifyEvent(2, CLIENT_CONNECTED, PUB_REC_DROPPED);
        assertFalse(sessionContext.isConfirming(tenantId, channel.id().asLongText(), 123));
    }

    @Test
    public void qoS2PubAuthFailed() {
        // not by pass
        connectAndVerify(true);
        mockAuthCheck(false);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishQoS2Message("testTopic", 123);
        channel.writeInbound(publishMessage);
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.runPendingTasks();
        assertFalse(channel.isActive());
        verifyEvent(3, CLIENT_CONNECTED, PUB_ACTION_DISALLOW, NO_PUB_PERMISSION);
        assertFalse(sessionContext.isConfirming(tenantId, channel.id().asLongText(), 123));
    }

    @Test
    public void malformedTopic() {
        connectAndVerify(true);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishQoS0Message("topic\u0000", 123);
        channel.writeInbound(publishMessage);
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.runPendingTasks();
        assertFalse(channel.isActive());
        verifyEvent(2, CLIENT_CONNECTED, MALFORMED_TOPIC);
    }

    @Test
    public void invalidTopic() {
        connectAndVerify(true);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishQoS0Message("$share/g/testTopic", 123);
        channel.writeInbound(publishMessage);
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.runPendingTasks();
        assertFalse(channel.isActive());
        verifyEvent(2, CLIENT_CONNECTED, INVALID_TOPIC);
    }

    @Test
    public void pubTooFast() {
        when(settingProvider.provide(eq(MsgPubPerSec), anyString())).thenReturn(1);
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistDist(true);
        MqttPublishMessage publishMessage = MQTTMessageUtils.publishQoS1Message("testTopic", 1);
        MqttPublishMessage publishMessage2 = MQTTMessageUtils.publishQoS1Message("testTopic", 2);
        channel.writeInbound(publishMessage);
        channel.writeInbound(publishMessage2);
        MqttPubAckMessage ackMessage = channel.readOutbound();
        assertEquals(ackMessage.variableHeader().messageId(), 1);
        verifyEvent(3, CLIENT_CONNECTED, PUB_ACKED, DISCARD);
    }

}
