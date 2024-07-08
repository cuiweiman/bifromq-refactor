package com.zachary.bifromq.mqtt;

import com.zachary.bifromq.mqtt.client.MqttTestClient;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.channelclosed.IdentifierRejected;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.testng.annotations.Test;

import static org.eclipse.paho.client.mqttv3.MqttException.REASON_CODE_INVALID_CLIENT_ID;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

@Slf4j
public class MQTTBadConnectMessageTest extends MQTTTest {
    @Test(groups = "integration")
    public void testCleanSessionFalseAndEmptyClientIdentifier() {
        MqttTestClient mqttClient = new MqttTestClient(brokerURI, "");

        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setMqttVersion(4);
        connOpts.setCleanSession(false);
        connOpts.setWill("/abc", new byte[] {}, 0, false);
        MqttException e = TestUtils.expectThrow(() -> mqttClient.connect(connOpts));
        assertEquals(e.getReasonCode(), REASON_CODE_INVALID_CLIENT_ID);

        verify(eventCollector).report(argThat(event -> event instanceof IdentifierRejected));
        mqttClient.close();
    }
}
