package com.zachary.bifromq.sessiondict;

import static com.zachary.bifromq.type.MQTTClientInfoConstants.MQTT_CLIENT_ID_KEY;
import static com.zachary.bifromq.type.MQTTClientInfoConstants.MQTT_USER_ID_KEY;

public class WCHKeyUtil {
    public static String toWCHKey(ClientInfo clientInfo) {
        return toWCHKey(clientInfo.getMetadataOrDefault(MQTT_USER_ID_KEY, ""),
            clientInfo.getMetadataOrDefault(MQTT_CLIENT_ID_KEY, ""));
    }

    public static String toWCHKey(String userId, String clientId) {
        return userId + clientId;
    }
}
