package com.zachary.bifromq.mqtt;

import com.zachary.bifromq.mqtt.client.MqttMsg;
import com.zachary.bifromq.mqtt.client.MqttTestClient;
import com.zachary.bifromq.plugin.authprovider.type.MQTT3AuthData;
import com.zachary.bifromq.plugin.authprovider.type.MQTT3AuthResult;
import com.zachary.bifromq.plugin.authprovider.type.MQTTAction;
import com.zachary.bifromq.plugin.authprovider.type.Ok;
import com.zachary.bifromq.plugin.eventcollector.Event;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.type.ClientInfo;
import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

@Slf4j
public class MQTTFanOutTest extends MQTTTest {
    private String tenantId = "testTraffic";
    private String deviceKey = "testDevice";

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        super.setup();
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
//            Event event = invocationOnMock.getArgument(0);
//            log.info("event: {}", event);
            return null;
        }).when(eventCollector).report(any(Event.class));
    }

    @Test(groups = "integration")
    public void fanout() {
        fanout(0);
        fanout(1);
        fanout(2);
    }

    public void fanout(int pubQoS) {
        String topic = "/a/" + pubQoS;
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(tenantId + "/" + deviceKey);

        MqttTestClient pubClient = new MqttTestClient(brokerURI, "pubClient");
        pubClient.connect(connOpts);

        MqttTestClient subClient1 = new MqttTestClient(brokerURI, "subClient1");
        subClient1.connect(connOpts);
        MqttTestClient subClient2 = new MqttTestClient(brokerURI, "subClient2");
        subClient2.connect(connOpts);
        MqttTestClient subClient3 = new MqttTestClient(brokerURI, "subClient3");
        subClient3.connect(connOpts);

        Observable<MqttMsg> topicSub1 = subClient1.subscribe("#", 0);
        Observable<MqttMsg> topicSub2 = subClient2.subscribe(topic, 1);
        Observable<MqttMsg> topicSub3 = subClient3.subscribe("/a/+", 2);

        pubClient.publish(topic, pubQoS, ByteString.copyFromUtf8("hello"), false);

        MqttMsg msg1 = topicSub1.blockingFirst();
        assertEquals(msg1.topic, topic);
        assertEquals(msg1.qos, Math.min(0, pubQoS));
        assertFalse(msg1.isDup);
        assertFalse(msg1.isRetain);
        assertEquals(msg1.payload, ByteString.copyFromUtf8("hello"));

        MqttMsg msg2 = topicSub2.blockingFirst();
        assertEquals(msg2.topic, topic);
        assertEquals(msg2.qos, Math.min(1, pubQoS));
        assertFalse(msg2.isDup);
        assertFalse(msg2.isRetain);
        assertEquals(msg2.payload, ByteString.copyFromUtf8("hello"));

        MqttMsg msg3 = topicSub3.blockingFirst();
        assertEquals(msg3.topic, topic);
        assertEquals(msg3.qos, Math.min(2, pubQoS));
        assertFalse(msg3.isDup);
        assertFalse(msg3.isRetain);
        assertEquals(msg3.payload, ByteString.copyFromUtf8("hello"));

        // TODO: verify event collected

        pubClient.disconnect();
        subClient1.disconnect();
        subClient2.disconnect();
        subClient3.disconnect();

        pubClient.close();
        subClient1.close();
        subClient2.close();
        subClient3.close();
    }
}
