package com.zachary.bifromq.mqtt.utils;

import com.zachary.bifromq.type.Message;

public class MQTTMessageSizer {
    public static int sizePublishMsg(String topic, Message msg) {
        return sizePublishMsg(topic, msg.getPayload().size());
    }

    public static int sizePublishMsg(String topic, int payloadSize) {
        // MQTTPublishMessage: FixedHeaderSize = 2, VariableHeaderSize = 4 + topicBytes
        return topic.length() + payloadSize + 6;
    }
}
