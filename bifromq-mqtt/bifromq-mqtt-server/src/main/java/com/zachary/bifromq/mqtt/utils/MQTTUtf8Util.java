package com.zachary.bifromq.mqtt.utils;

public class MQTTUtf8Util {
    private static final int MAX_STRING_LENGTH = 65535; // MQTT-1.5.3

    public static boolean isWellFormed(String str, boolean sanityCheck) {
        int len = str.length();
        if (len == 0) {
            return true;
        }
        if (len > MAX_STRING_LENGTH) {
            return false;
        }
        char cl = str.charAt(0);
        if (cl == '\u0000') {
            return false;
        }
        if (sanityCheck && isUnacceptableChar(cl)) {
            return false;
        }
        for (int i = 1; i < len; i++) {
            final char cr = str.charAt(i);
            if (cr == '\u0000') {
                return false;
            }
            if (Character.isSurrogatePair(cl, cr)) {
                return false;
            }
            if (sanityCheck && isUnacceptableChar(cr)) {
                return false;
            }
            cl = cr;
        }
        return true;
    }

    private static boolean isUnacceptableChar(char ch) {
        return Character.isISOControl(ch) || !Character.isDefined(ch);
    }
}
