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

package com.zachary.bifromq.mqtt.handler;


import com.zachary.bifromq.mqtt.utils.MQTTMessageUtils;
import com.zachary.bifromq.plugin.authprovider.type.Reject;
import com.zachary.bifromq.plugin.eventcollector.Event;
import com.zachary.bifromq.plugin.eventcollector.EventType;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.extern.slf4j.Slf4j;
import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.plugin.eventcollector.EventType.PING_REQ;
import static com.zachary.bifromq.plugin.eventcollector.EventType.SESSION_CHECK_ERROR;
import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_ACCEPTED;
import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD;
import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED;
import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_REFUSED_SERVER_UNAVAILABLE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
public class MQTTConnectTest extends BaseMQTTTest {

    @Test
    public void transientSessionWithoutInbox() {
        connectAndVerify(true, false);
        verifyEvent(1, EventType.CLIENT_CONNECTED);
    }

    @Test
    public void transientSessionWithInbox() {
        connectAndVerify(true, true);
        verifyEvent(1, EventType.CLIENT_CONNECTED);
    }

    @Test
    public void transientSessionWithInbox2() {
        // clear failed
        mockAuthPass();
        mockSessionReg();
        when(inboxClient.has(anyLong(), anyString(), any(ClientInfo.class)))
            .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Mock exception")));
        MqttConnectMessage connectMessage = MQTTMessageUtils.mqttConnectMessage(true);
        channel.writeInbound(connectMessage);
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.runPendingTasks();
        MqttConnAckMessage ackMessage = channel.readOutbound();
        // verifications
        Assert.assertEquals(CONNECTION_REFUSED_SERVER_UNAVAILABLE, ackMessage.variableHeader().connectReturnCode());
        verifyEvent(1, SESSION_CHECK_ERROR);
    }

    @Test
    public void transientSessionClearSubError() {
        // clear failed
        mockAuthPass();
        mockSessionReg();
        mockInboxDelete(true);
        mockInboxHas(true);
        mockDistClear(false);
        MqttConnectMessage connectMessage = MQTTMessageUtils.mqttConnectMessage(true);
        channel.writeInbound(connectMessage);
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.runPendingTasks();
        MqttConnAckMessage ackMessage = channel.readOutbound();
        // verifications
        Assert.assertEquals(CONNECTION_REFUSED_SERVER_UNAVAILABLE, ackMessage.variableHeader().connectReturnCode());
        verifyEvent(1, EventType.SESSION_CLEANUP_ERROR);
    }

    @Test
    public void persistentSessionWithoutInbox() {
        connectAndVerify(false, false);
        verifyEvent(1, EventType.CLIENT_CONNECTED);
    }

    @Test
    public void persistentSessionCreateInboxError() {
        // create inbox failed
        mockAuthPass();
        mockSessionReg();
        mockInboxHas(false);
        mockInboxCreate(false);
        MqttConnectMessage connectMessage = MQTTMessageUtils.mqttConnectMessage(false);
        channel.writeInbound(connectMessage);
        MqttConnAckMessage ackMessage = channel.readOutbound();
        // verifications
        Assert.assertEquals(CONNECTION_ACCEPTED, ackMessage.variableHeader().connectReturnCode());
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.runPendingTasks();
        verifyEvent(2, EventType.CLIENT_CONNECTED, EventType.SESSION_CREATE_ERROR);
    }

    @Test
    public void persistentSessionCheckInboxError() {
        // create inbox failed
        mockAuthPass();
        mockSessionReg();
        when(inboxClient.has(anyLong(), anyString(), any(ClientInfo.class)))
            .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Mock exception")));
        MqttConnectMessage connectMessage = MQTTMessageUtils.mqttConnectMessage(false);
        channel.writeInbound(connectMessage);
        // verifications
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.runPendingTasks();
        MqttConnAckMessage ackMessage = channel.readOutbound();
        Assert.assertEquals(CONNECTION_REFUSED_SERVER_UNAVAILABLE, ackMessage.variableHeader().connectReturnCode());
        verifyEvent(1, SESSION_CHECK_ERROR);
    }

    @Test
    public void persistentSessionWithInbox() {
        connectAndVerify(false, true);
        verifyEvent(1, EventType.CLIENT_CONNECTED);
    }

    @Test
    public void authBanned() {
        mockAuthReject(Reject.Code.NotAuthorized, "");
        MqttConnectMessage connectMessage = MQTTMessageUtils.mqttConnectMessage(true);
        channel.writeInbound(connectMessage);
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.runPendingTasks();
        MqttConnAckMessage ackMessage = channel.readOutbound();
        // verifications
        Assert.assertEquals(CONNECTION_REFUSED_NOT_AUTHORIZED, ackMessage.variableHeader().connectReturnCode());
        verifyEvent(1, EventType.NOT_AUTHORIZED_CLIENT);
    }

    @Test
    public void authNotPass() {
        mockAuthReject(Reject.Code.BadPass, "");
        MqttConnectMessage connectMessage = MQTTMessageUtils.mqttConnectMessage(true);
        channel.writeInbound(connectMessage);
        // verifications
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.runPendingTasks();
        MqttConnAckMessage ackMessage = channel.readOutbound();
        Assert.assertEquals(CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD,
            ackMessage.variableHeader().connectReturnCode());
        verifyEvent(1, EventType.UNAUTHENTICATED_CLIENT);
    }

    @Test
    public void authError() {
        mockAuthReject(Reject.Code.Error, "");
        MqttConnectMessage connectMessage = MQTTMessageUtils.mqttConnectMessage(true);
        channel.writeInbound(connectMessage);
        channel.advanceTimeBy(disconnectDelay, TimeUnit.MILLISECONDS);
        channel.runPendingTasks();
        MqttConnAckMessage ackMessage = channel.readOutbound();
        // verifications
        Assert.assertEquals(CONNECTION_REFUSED_SERVER_UNAVAILABLE, ackMessage.variableHeader().connectReturnCode());
        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventCollector, times(1)).report(eventArgumentCaptor.capture());
        verifyEvent(1, EventType.AUTH_ERROR);
    }

    @Test
    public void validWillTopic() {
        mockAuthPass();
        mockSessionReg();
        mockInboxHas(false);
        MqttConnectMessage connectMessage = MQTTMessageUtils.qoSWillMqttConnectMessage(1, true);
        channel.writeInbound(connectMessage);
        MqttConnAckMessage ackMessage = channel.readOutbound();
        // verifications
        Assert.assertEquals(CONNECTION_ACCEPTED, ackMessage.variableHeader().connectReturnCode());
        verifyEvent(1, EventType.CLIENT_CONNECTED);
    }

    @Test
    public void pingAndPingResp() {
        connectAndVerify(true, false);
        channel.writeInbound(MqttMessage.PINGREQ);
        MqttMessage pingResp = channel.readOutbound();
        Assert.assertEquals(MqttMessage.PINGRESP, pingResp);
        verifyEvent(2, EventType.CLIENT_CONNECTED, PING_REQ);
    }
}
