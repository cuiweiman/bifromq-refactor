package com.zachary.bifromq.mqtt.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageBuilders;
import io.netty.handler.codec.mqtt.MqttVersion;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@Slf4j
public class MQTTMessageDebounceHandlerTest extends BaseMQTTTest {

    @Override
    protected ChannelInitializer<EmbeddedChannel> channelInitializer() {
        return new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel embeddedChannel) {
                ChannelPipeline pipeline = embeddedChannel.pipeline();
                pipeline.addLast("decoder", new MqttDecoder(256 * 1024)); //256kb
                pipeline.addLast("encoder", MqttEncoder.INSTANCE);
                pipeline.addLast(MQTTMessageDebounceHandler.NAME, new MQTTMessageDebounceHandler());
            }
        };
    }

    @Test
    public void testReadManually() {
        channel.config().setAutoRead(false);
        MqttMessage connMsg = MqttMessageBuilders.connect()
            .protocolVersion(MqttVersion.MQTT_3_1_1).keepAlive(30).build();
        MqttMessage pingReqMsg = MqttMessage.PINGREQ;
        MqttMessage connAckMsg = MqttMessageBuilders.connAck()
            .returnCode(MqttConnectReturnCode.CONNECTION_ACCEPTED).build();

        channel.writeInbound(connMsg, pingReqMsg, connAckMsg);
        assertNull(channel.readInbound());

        channel.read();
        assertEquals(channel.readInbound(), connMsg);
        assertNull(channel.readInbound());

        channel.read();
        assertEquals(channel.readInbound(), pingReqMsg);
        assertNull(channel.readInbound());

        channel.read();
        assertEquals(channel.readInbound(), connAckMsg);
        assertNull(channel.readInbound());

        channel.read();
        assertNull(channel.readInbound());
    }

    @Test
    public void testAutoRead() {
        channel.config().setAutoRead(true);
        MqttMessage connMsg = MqttMessageBuilders.connect()
            .protocolVersion(MqttVersion.MQTT_3_1_1).keepAlive(30).build();
        MqttMessage connAckMsg = MqttMessageBuilders.connAck()
            .returnCode(MqttConnectReturnCode.CONNECTION_ACCEPTED).build();
        channel.writeInbound(connMsg, connAckMsg);
        assertEquals(channel.readInbound(), connMsg);
        assertEquals(channel.readInbound(), connAckMsg);
    }

    @Test
    public void testAutoReadToManualRead() {
        channel.config().setAutoRead(true);
        MqttMessage connMsg = MqttMessageBuilders.connect()
            .protocolVersion(MqttVersion.MQTT_3_1_1).keepAlive(30).build();
        channel.writeInbound(connMsg);
        assertEquals(channel.readInbound(), connMsg);

        channel.config().setAutoRead(false);
        MqttMessage pingReqMsg = MqttMessage.PINGREQ;
        channel.writeInbound(pingReqMsg);
        assertNull(channel.readInbound());

        channel.read();
        assertEquals(channel.readInbound(), pingReqMsg);
        assertNull(channel.readInbound());
    }

    @Test
    public void testManualReadToAutoRead() {
        channel.config().setAutoRead(false);
        MqttMessage connMsg = MqttMessageBuilders.connect()
            .protocolVersion(MqttVersion.MQTT_3_1_1).keepAlive(30).build();
        channel.writeInbound(connMsg);
        assertNull(channel.readInbound());
        channel.read();
        assertEquals(channel.readInbound(), connMsg);

        channel.config().setAutoRead(true);
        MqttMessage pingReqMsg = MqttMessage.PINGREQ;
        MqttMessage connAckMsg = MqttMessageBuilders.connAck()
            .returnCode(MqttConnectReturnCode.CONNECTION_ACCEPTED).build();
        channel.writeInbound(pingReqMsg, connAckMsg);
        assertEquals(channel.readInbound(), pingReqMsg);
        assertEquals(channel.readInbound(), connAckMsg);
    }
}
