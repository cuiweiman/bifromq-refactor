package com.zachary.bifromq.mqtt.handler.v3;

import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttSubAckMessage;
import io.netty.handler.codec.mqtt.MqttSubAckPayload;
import io.netty.handler.codec.mqtt.MqttUnsubAckMessage;

import java.util.List;

public final class MQTT3MessageUtils {
    static MqttSubAckMessage toMqttSubAckMessage(int packetId, List<Integer> ackedQoSs) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.SUBACK,
            false,
            MqttQoS.AT_MOST_ONCE,
            false,
            0);
        MqttMessageIdVariableHeader mqttMessageIdVariableHeader = MqttMessageIdVariableHeader.from(packetId);
        MqttSubAckPayload mqttSubAckPayload = new MqttSubAckPayload(ackedQoSs);
        return new MqttSubAckMessage(mqttFixedHeader,
            mqttMessageIdVariableHeader,
            mqttSubAckPayload);
    }

    static MqttPublishMessage toDuplicated(MqttPublishMessage mqttMessage) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH,
            true,
            MqttQoS.AT_LEAST_ONCE,
            mqttMessage.fixedHeader().isRetain(),
            0);
        MqttPublishVariableHeader mqttVariableHeader = mqttMessage.variableHeader();
        return new MqttPublishMessage(mqttFixedHeader, mqttVariableHeader, mqttMessage.payload());
    }

    static MqttUnsubAckMessage toMqttUnsubAckMessage(int messageId) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.UNSUBACK,
            false,
            MqttQoS.AT_MOST_ONCE,
            false,
            0);
        MqttMessageIdVariableHeader mqttMessageIdVariableHeader = MqttMessageIdVariableHeader.from(messageId);
        return new MqttUnsubAckMessage(mqttFixedHeader, mqttMessageIdVariableHeader);
    }
}
