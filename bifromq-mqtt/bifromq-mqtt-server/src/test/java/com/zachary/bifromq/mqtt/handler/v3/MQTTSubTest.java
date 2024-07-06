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
import io.netty.handler.codec.mqtt.MqttSubAckMessage;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.plugin.eventcollector.EventType.CLIENT_CONNECTED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.INVALID_TOPIC_FILTER;
import static com.zachary.bifromq.plugin.eventcollector.EventType.MALFORMED_TOPIC_FILTER;
import static com.zachary.bifromq.plugin.eventcollector.EventType.PROTOCOL_VIOLATION;
import static com.zachary.bifromq.plugin.eventcollector.EventType.SUB_ACKED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.SUB_ACTION_DISALLOW;
import static com.zachary.bifromq.plugin.eventcollector.EventType.TOO_LARGE_SUBSCRIPTION;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@Slf4j
public class MQTTSubTest extends BaseMQTTTest {

    private boolean shouldCleanSubs = false;

    @AfterMethod
    public void clean() {
        if (shouldCleanSubs) {
            when(distClient.clear(anyLong(), anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(CompletableFuture.completedFuture(null));
            channel.close();
            verify(distClient, times(1))
                .clear(anyLong(), anyString(), anyString(), anyString(), anyInt());
        } else {
            channel.close();
        }
        shouldCleanSubs = false;
    }

    @Test
    public void transientQoS0Sub() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistSub(QoS.AT_MOST_ONCE, true);
        mockRetainMatch();
        int[] qos = {0, 0, 0};
        MqttSubscribeMessage subMessage = MQTTMessageUtils.qoSMqttSubMessages(qos);
        channel.writeInbound(subMessage);
        MqttSubAckMessage subAckMessage = channel.readOutbound();
        verifySubAck(subAckMessage, qos);
        verifyEvent(2, CLIENT_CONNECTED, SUB_ACKED);
        shouldCleanSubs = true;
    }

    @Test
    public void transientQoS1Sub() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistSub(QoS.AT_LEAST_ONCE, true);
        mockRetainMatch();
        int[] qos = {1, 1, 1};
        MqttSubscribeMessage subMessage = MQTTMessageUtils.qoSMqttSubMessages(qos);
        channel.writeInbound(subMessage);
        MqttSubAckMessage subAckMessage = channel.readOutbound();
        verifySubAck(subAckMessage, qos);
        verifyEvent(2, CLIENT_CONNECTED, SUB_ACKED);
        shouldCleanSubs = true;
    }

    @Test
    public void transientQoS2Sub() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistSub(QoS.EXACTLY_ONCE, true);
        mockRetainMatch();
        int[] qos = {2, 2, 2};
        MqttSubscribeMessage subMessage = MQTTMessageUtils.qoSMqttSubMessages(qos);
        channel.writeInbound(subMessage);
        MqttSubAckMessage subAckMessage = channel.readOutbound();
        verifySubAck(subAckMessage, qos);
        verifyEvent(2, CLIENT_CONNECTED, SUB_ACKED);
        shouldCleanSubs = true;
    }

    @Test
    public void transientMixedSub() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistSub(QoS.AT_MOST_ONCE, true);
        mockDistSub(QoS.AT_LEAST_ONCE, true);
        mockDistSub(QoS.EXACTLY_ONCE, true);
        mockRetainMatch();
        int[] qos = {0, 1, 2};
        MqttSubscribeMessage subMessage = MQTTMessageUtils.qoSMqttSubMessages(qos);
        channel.writeInbound(subMessage);
        MqttSubAckMessage subAckMessage = channel.readOutbound();
        verifySubAck(subAckMessage, qos);
        verifyEvent(2, CLIENT_CONNECTED, SUB_ACKED);
        shouldCleanSubs = true;
    }

    @Test
    public void transientMixedSubWithDistSubFailed() {
        connectAndVerify(true);
        mockAuthCheck(true);
        mockDistSub(QoS.AT_MOST_ONCE, true);
        mockDistSub(QoS.AT_LEAST_ONCE, true);
        mockDistSub(QoS.EXACTLY_ONCE, false);
        mockRetainMatch();
        int[] qos = {0, 1, 2};
        MqttSubscribeMessage subMessage = MQTTMessageUtils.qoSMqttSubMessages(qos);
        channel.writeInbound(subMessage);
        MqttSubAckMessage subAckMessage = channel.readOutbound();
        verifySubAck(subAckMessage, new int[] {0, 1, 128});
        verifyEvent(2, CLIENT_CONNECTED, SUB_ACKED);
        shouldCleanSubs = true;
    }

    @Test
    public void persistentQoS0Sub() {
        connectAndVerify(false);
        mockAuthCheck(true);
        mockDistSub(QoS.AT_MOST_ONCE, true);
        mockRetainMatch();
        int[] qos = {0, 0, 0};
        MqttSubscribeMessage subMessage = MQTTMessageUtils.qoSMqttSubMessages(qos);
        channel.writeInbound(subMessage);
        MqttSubAckMessage subAckMessage = channel.readOutbound();
        verifySubAck(subAckMessage, qos);
        verifyEvent(2, CLIENT_CONNECTED, SUB_ACKED);
    }

    @Test
    public void persistentQoS1Sub() {
        connectAndVerify(false);
        mockAuthCheck(true);
        mockDistSub(QoS.AT_LEAST_ONCE, true);
        mockRetainMatch();
        int[] qos = {1, 1, 1};
        MqttSubscribeMessage subMessage = MQTTMessageUtils.qoSMqttSubMessages(qos);
        channel.writeInbound(subMessage);
        MqttSubAckMessage subAckMessage = channel.readOutbound();
        verifySubAck(subAckMessage, qos);
        verifyEvent(2, CLIENT_CONNECTED, SUB_ACKED);
    }

    @Test
    public void persistentQoS2Sub() {
        connectAndVerify(false);
        mockAuthCheck(true);
        mockDistSub(QoS.EXACTLY_ONCE, true);
        mockRetainMatch();
        int[] qos = {2, 2, 2};
        MqttSubscribeMessage subMessage = MQTTMessageUtils.qoSMqttSubMessages(qos);
        channel.writeInbound(subMessage);
        MqttSubAckMessage subAckMessage = channel.readOutbound();
        verifySubAck(subAckMessage, qos);
        verifyEvent(2, CLIENT_CONNECTED, SUB_ACKED);
    }

    @Test
    public void persistentMixedSub() {
        connectAndVerify(false);
        mockAuthCheck(true);
        mockDistSub(QoS.AT_MOST_ONCE, true);
        mockDistSub(QoS.AT_LEAST_ONCE, true);
        mockDistSub(QoS.EXACTLY_ONCE, true);
        mockRetainMatch();
        int[] qos = {0, 1, 2};
        MqttSubscribeMessage subMessage = MQTTMessageUtils.qoSMqttSubMessages(qos);
        channel.writeInbound(subMessage);
        MqttSubAckMessage subAckMessage = channel.readOutbound();
        verifySubAck(subAckMessage, qos);
        verifyEvent(2, CLIENT_CONNECTED, SUB_ACKED);
    }

    @Test
    public void subWithEmptyTopicList() {
        connectAndVerify(true);
        MqttSubscribeMessage subMessage = MQTTMessageUtils.badQoS0MqttSubMessageWithoutTopic();
        channel.writeInbound(subMessage);
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.writeInbound();
        verifyEvent(2, CLIENT_CONNECTED, PROTOCOL_VIOLATION);
    }

    @Test
    public void subWithTooLargeTopicList() {
        connectAndVerify(true);
        int[] qos = new int[100];
        Arrays.fill(qos, 1);
        MqttSubscribeMessage subMessage = MQTTMessageUtils.qoSMqttSubMessages(qos);
        channel.writeInbound(subMessage);
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.writeInbound();
        verifyEvent(2, CLIENT_CONNECTED, TOO_LARGE_SUBSCRIPTION);
    }

    @Test
    public void subWithMalformedTopic() {
        connectAndVerify(true);
        MqttSubscribeMessage subMessage = MQTTMessageUtils.topicMqttSubMessages("/topic\u0000");
        channel.writeInbound(subMessage);
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.writeInbound();
        verifyEvent(2, CLIENT_CONNECTED, MALFORMED_TOPIC_FILTER);
    }

    @Test
    public void subWithInvalidTopic() {
        connectAndVerify(true);
        MqttSubscribeMessage subMessage = MQTTMessageUtils.badTopicMqttSubMessages();
        channel.writeInbound(subMessage);
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.writeInbound();
        verifyEvent(2, CLIENT_CONNECTED, INVALID_TOPIC_FILTER);
    }

    @Test
    public void subWithAuthFailed() {
        connectAndVerify(true);
        mockAuthCheck(false);
        int[] qos = {1, 1, 1};
        MqttSubscribeMessage subMessage = MQTTMessageUtils.qoSMqttSubMessages(qos);
        channel.writeInbound(subMessage);
        MqttSubAckMessage subAckMessage = channel.readOutbound();
        verifySubAck(subAckMessage, new int[] {128, 128, 128});
        verifyEvent(5, CLIENT_CONNECTED, SUB_ACTION_DISALLOW, SUB_ACTION_DISALLOW, SUB_ACTION_DISALLOW, SUB_ACKED);
    }

    private void verifySubAck(MqttSubAckMessage subAckMessage, int[] expectedQos) {
        assertEquals(subAckMessage.payload().grantedQoSLevels().size(), expectedQos.length);
        for (int i = 0; i < expectedQos.length; i++) {
            assertEquals(expectedQos[i], (int) subAckMessage.payload().grantedQoSLevels().get(i));
        }
    }

}
