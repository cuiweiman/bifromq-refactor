package com.zachary.bifromq.mqtt.handler;

import com.zachary.bifromq.mqtt.utils.MQTTMessageUtils;
import com.zachary.bifromq.sessiondict.rpc.proto.Quit;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.zachary.bifromq.plugin.eventcollector.EventType.CLIENT_CONNECTED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.KICKED;
import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_ACCEPTED;

public class MQTTKickTest extends BaseMQTTTest {

    @Test
    public void testKick() {
        mockAuthPass();
        mockSessionReg();
        mockInboxHas(false);
        MqttConnectMessage connectMessage = MQTTMessageUtils.mqttConnectMessage(true);
        channel.writeInbound(connectMessage);
        MqttConnAckMessage ackMessage = channel.readOutbound();
        Assert.assertEquals(CONNECTION_ACCEPTED, ackMessage.variableHeader().connectReturnCode());

        // kick
        kickSubject.onNext(Quit.newBuilder().build());
        channel.runPendingTasks();
        Assert.assertFalse(channel.isActive());
        verifyEvent(2, CLIENT_CONNECTED, KICKED);
    }
}
