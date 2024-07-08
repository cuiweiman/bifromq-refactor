package com.zachary.bifromq.mqtt.utils;

import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class MQTTUtf8UtilTest {
    @Test
    public void clientId() {
        for (int i = 0; i < 100; i++) {
            String s = UUID.randomUUID().toString();
            assertTrue(MQTTUtf8Util.isWellFormed(s, false));
        }
    }

    @Test
    public void emptyClientId() {
        assertTrue(MQTTUtf8Util.isWellFormed("", false));
        assertTrue(MQTTUtf8Util.isWellFormed(" ", false)); // whitespace is acceptable according to MQTT spec
    }

    @Test
    public void mustNotChars() {
        assertFalse(MQTTUtf8Util.isWellFormed("hello\u0000world", false)); // null character U+0000
        assertFalse(MQTTUtf8Util.isWellFormed("hello\uD83D\uDE0Aworld", false)); // surrogate pairs
    }

    @Test
    public void shouldNotChars() {
        for (int i = '\u0001'; i <= '\u001F'; i++) {
            assertFalse(MQTTUtf8Util.isWellFormed("hello" + (char) i, true)); // control character
        }
        for (int i = '\u007F'; i <= '\u009F'; i++) {
            assertFalse(MQTTUtf8Util.isWellFormed("hello" + (char) i, true)); // control character
        }
        assertFalse(MQTTUtf8Util.isWellFormed("hello\uFFFF", true)); // non character
    }

    @Test
    public void noSanityCheck() {
        for (int i = '\u0001'; i <= '\u001F'; i++) {
            assertTrue(MQTTUtf8Util.isWellFormed("hello" + (char) i, false)); // control character
        }
        for (int i = '\u007F'; i <= '\u009F'; i++) {
            assertTrue(MQTTUtf8Util.isWellFormed("hello" + (char) i, false)); // control character
        }
        assertTrue(MQTTUtf8Util.isWellFormed("hello\uFFFF", false)); // non character
    }
}
