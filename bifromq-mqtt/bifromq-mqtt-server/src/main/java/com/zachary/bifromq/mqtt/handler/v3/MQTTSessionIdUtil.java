package com.zachary.bifromq.mqtt.handler.v3;

import static com.zachary.bifromq.type.MQTTClientInfoConstants.MQTT_CLIENT_ID_KEY;
import static com.zachary.bifromq.type.MQTTClientInfoConstants.MQTT_USER_ID_KEY;

public class MQTTSessionIdUtil {
    public static String userSessionId(ClientInfo info) {
        return info.getMetadataOrThrow(MQTT_USER_ID_KEY) + "/" + info.getMetadataOrDefault(MQTT_CLIENT_ID_KEY, "");
    }
}
