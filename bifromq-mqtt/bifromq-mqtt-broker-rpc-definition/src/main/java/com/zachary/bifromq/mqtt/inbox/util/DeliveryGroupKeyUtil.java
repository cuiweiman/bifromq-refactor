package com.zachary.bifromq.mqtt.inbox.util;

import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.MQTT_DELIVERERS_PER_SERVER;

public class DeliveryGroupKeyUtil {

    private static final int INBOX_GROUPS = MQTT_DELIVERERS_PER_SERVER.get();

    public static String toDelivererKey(String inboxId, String serverId) {
        return serverId + ":" + groupIdx(inboxId);
    }

    public static String parseServerId(String delivererKey) {
        return delivererKey.substring(0, delivererKey.lastIndexOf(":"));
    }

    private static int groupIdx(String inboxId) {
        int idx = inboxId.hashCode() % INBOX_GROUPS;
        if (idx < 0) {
            idx = (idx + INBOX_GROUPS) % INBOX_GROUPS;
        }
        return idx;
    }
}
