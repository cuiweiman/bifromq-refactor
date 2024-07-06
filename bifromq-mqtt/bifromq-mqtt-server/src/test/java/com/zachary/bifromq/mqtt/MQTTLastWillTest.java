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

package com.zachary.bifromq.mqtt;

import com.zachary.bifromq.mqtt.client.MqttMsg;
import com.zachary.bifromq.mqtt.client.MqttTestClient;
import com.zachary.bifromq.plugin.authprovider.type.MQTT3AuthData;
import com.zachary.bifromq.plugin.authprovider.type.MQTT3AuthResult;
import com.zachary.bifromq.plugin.authprovider.type.MQTTAction;
import com.zachary.bifromq.plugin.authprovider.type.Ok;
import com.zachary.bifromq.plugin.eventcollector.Event;
import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.testng.annotations.Test;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Slf4j
public class MQTTLastWillTest extends MQTTTest {

    @Test(groups = "integration")
    public void lastWillQoS1() {
        String tenantId = "ashdsha";
        String deviceKey = "testDevice";
        String userName = tenantId + "/" + deviceKey;
        String willTopic = "willTopic";
        ByteString willPayload = ByteString.copyFromUtf8("bye");
        when(authProvider.auth(any(MQTT3AuthData.class)))
            .thenReturn(CompletableFuture.completedFuture(MQTT3AuthResult.newBuilder()
                .setOk(Ok.newBuilder()
                    .setTenantId(tenantId)
                    .setUserId(deviceKey)
                    .build())
                .build()));
        when(authProvider.check(any(ClientInfo.class), any(MQTTAction.class)))
            .thenReturn(CompletableFuture.completedFuture(true));

        doAnswer(invocationOnMock -> {
            Event event = invocationOnMock.getArgument(0);
            log.info("event: {}", event);
            return null;
        }).when(eventCollector).report(any(Event.class));

        MqttConnectOptions lwtPubConnOpts = new MqttConnectOptions();
        lwtPubConnOpts.setCleanSession(true);
        lwtPubConnOpts.setWill(willTopic, willPayload.toByteArray(), 1, false);
        lwtPubConnOpts.setUserName(userName);
        MqttTestClient lwtPubClient = new MqttTestClient(brokerURI, "lwtPubclient");
        lwtPubClient.connect(lwtPubConnOpts);

        MqttConnectOptions lwtSubConnOpts = new MqttConnectOptions();
        lwtSubConnOpts.setCleanSession(true);
        lwtSubConnOpts.setUserName(userName);

        MqttTestClient lwtSubClient = new MqttTestClient(brokerURI, "lwtSubClient");
        lwtSubClient.connect(lwtSubConnOpts);
        Observable<MqttMsg> topicSub = lwtSubClient.subscribe(willTopic, 1);

        log.info("Kill client");
        sessionDictClient.kill(System.nanoTime(), tenantId, deviceKey,
            "lwtPubclient", ClientInfo.getDefaultInstance()).join();

        MqttMsg msg = topicSub.blockingFirst();
        assertEquals(msg.topic, willTopic);
        assertEquals(msg.qos, 1);
        assertEquals(msg.payload, willPayload);
        assertFalse(msg.isRetain);
    }

    @Test(groups = "integration")
    public void lastWillQoS1Retained() {
        String tenantId = "ashdsha";
        String deviceKey = "testDevice";
        String userName = tenantId + "/" + deviceKey;
        String willTopic = "willTopic";
        ByteString willPayload = ByteString.copyFromUtf8("bye");
        when(authProvider.auth(any(MQTT3AuthData.class)))
            .thenReturn(CompletableFuture.completedFuture(MQTT3AuthResult.newBuilder()
                .setOk(Ok.newBuilder()
                    .setTenantId(tenantId)
                    .setUserId(deviceKey)
                    .build())
                .build()));
        when(authProvider.check(any(ClientInfo.class), any(MQTTAction.class)))
            .thenReturn(CompletableFuture.completedFuture(true));

        doAnswer(invocationOnMock -> {
            Event event = invocationOnMock.getArgument(0);
            log.info("event: {}", event.type());
            return null;
        }).when(eventCollector).report(any(Event.class));

        MqttConnectOptions lwtPubConnOpts = new MqttConnectOptions();
        lwtPubConnOpts.setCleanSession(true);
        lwtPubConnOpts.setWill(willTopic, willPayload.toByteArray(), 1, true);
        lwtPubConnOpts.setUserName(userName);
        MqttTestClient lwtPubClient = new MqttTestClient(brokerURI, "lwtPubclient");
        lwtPubClient.connect(lwtPubConnOpts);
        sessionDictClient.kill(System.nanoTime(), tenantId, deviceKey,
            "lwtPubclient", ClientInfo.getDefaultInstance()).join();

        MqttConnectOptions lwtSubConnOpts = new MqttConnectOptions();
        lwtSubConnOpts.setCleanSession(true);
        lwtSubConnOpts.setUserName(userName);

        MqttTestClient lwtSubClient = new MqttTestClient(brokerURI, "lwtSubClient");
        lwtSubClient.connect(lwtSubConnOpts);
        Observable<MqttMsg> topicSub = lwtSubClient.subscribe(willTopic, 1);

        MqttMsg msg = topicSub.blockingFirst();
        assertEquals(msg.topic, willTopic);
        assertEquals(msg.qos, 1);
        assertEquals(msg.payload, willPayload);
        assertTrue(msg.isRetain);
    }

    @Test(groups = "integration")
    public void lastWillQoS2() {
        String tenantId = "ashdsha";
        String deviceKey = "testDevice";
        String userName = tenantId + "/" + deviceKey;
        String willTopic = "willTopic";
        ByteString willPayload = ByteString.copyFromUtf8("bye");
        when(authProvider.auth(any(MQTT3AuthData.class)))
            .thenReturn(CompletableFuture.completedFuture(MQTT3AuthResult.newBuilder()
                .setOk(Ok.newBuilder()
                    .setTenantId(tenantId)
                    .setUserId(deviceKey)
                    .build())
                .build()));
        when(authProvider.check(any(ClientInfo.class), any(MQTTAction.class)))
            .thenReturn(CompletableFuture.completedFuture(true));

        doAnswer(invocationOnMock -> {
            Event event = invocationOnMock.getArgument(0);
            log.info("event: {}", event.type());
            return null;
        }).when(eventCollector).report(any(Event.class));

        MqttConnectOptions lwtPubConnOpts = new MqttConnectOptions();
        lwtPubConnOpts.setCleanSession(true);
        lwtPubConnOpts.setWill(willTopic, willPayload.toByteArray(), 2, false);
        lwtPubConnOpts.setUserName(userName);
        MqttTestClient lwtPubClient = new MqttTestClient(brokerURI, "lwtPubclient");
        lwtPubClient.connect(lwtPubConnOpts);

        MqttConnectOptions lwtSubConnOpts = new MqttConnectOptions();
        lwtSubConnOpts.setCleanSession(true);
        lwtSubConnOpts.setUserName(userName);

        MqttTestClient lwtSubClient = new MqttTestClient(brokerURI, "lwtSubClient");
        lwtSubClient.connect(lwtSubConnOpts);
        Observable<MqttMsg> topicSub = lwtSubClient.subscribe(willTopic, 2);

        sessionDictClient.kill(System.nanoTime(), tenantId, deviceKey,
            "lwtPubclient", ClientInfo.getDefaultInstance()).join();

        MqttMsg msg = topicSub.blockingFirst();
        assertEquals(msg.topic, willTopic);
        assertEquals(msg.qos, 2);
        assertEquals(msg.payload, willPayload);
        assertFalse(msg.isRetain);
    }

    @Test(groups = "integration")
    public void lastWillQoS2Retained() {
        String tenantId = "ashdsha";
        String deviceKey = "testDevice";
        String userName = tenantId + "/" + deviceKey;
        String willTopic = "willTopic";
        ByteString willPayload = ByteString.copyFromUtf8("bye");
        when(authProvider.auth(any(MQTT3AuthData.class)))
            .thenReturn(CompletableFuture.completedFuture(MQTT3AuthResult.newBuilder()
                .setOk(Ok.newBuilder()
                    .setTenantId(tenantId)
                    .setUserId(deviceKey)
                    .build())
                .build()));
        when(authProvider.check(any(ClientInfo.class), any(MQTTAction.class)))
            .thenReturn(CompletableFuture.completedFuture(true));

        doAnswer(invocationOnMock -> {
            Event event = invocationOnMock.getArgument(0);
            log.info("event: {}", event.type());
            return null;
        }).when(eventCollector).report(any(Event.class));

        MqttConnectOptions lwtPubConnOpts = new MqttConnectOptions();
        lwtPubConnOpts.setCleanSession(true);
        lwtPubConnOpts.setWill(willTopic, willPayload.toByteArray(), 2, true);
        lwtPubConnOpts.setUserName(userName);
        MqttTestClient lwtPubClient = new MqttTestClient(brokerURI, "lwtPubclient");
        lwtPubClient.connect(lwtPubConnOpts);
        sessionDictClient.kill(System.nanoTime(), tenantId, deviceKey,
            "lwtPubclient", ClientInfo.getDefaultInstance()).join();

        MqttConnectOptions lwtSubConnOpts = new MqttConnectOptions();
        lwtSubConnOpts.setCleanSession(true);
        lwtSubConnOpts.setUserName(userName);

        MqttTestClient lwtSubClient = new MqttTestClient(brokerURI, "lwtSubClient");
        lwtSubClient.connect(lwtSubConnOpts);
        Observable<MqttMsg> topicSub = lwtSubClient.subscribe(willTopic, 2);

        MqttMsg msg = topicSub.blockingFirst();
        assertEquals(msg.topic, willTopic);
        assertEquals(msg.qos, 2);
        assertEquals(msg.payload, willPayload);
        assertTrue(msg.isRetain);
    }
}
