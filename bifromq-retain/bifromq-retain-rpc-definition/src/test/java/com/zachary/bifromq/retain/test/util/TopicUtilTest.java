package com.zachary.bifromq.retain.test.util;

import org.testng.annotations.Test;

import static com.zachary.bifromq.retain.utils.TopicUtil.match;
import static com.zachary.bifromq.retain.utils.TopicUtil.parse;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class TopicUtilTest {
    @Test
    public void matches() {
        assertTrue(matches("/", "/"));
        assertTrue(matches("/", "+/+"));
        assertTrue(matches("/", "+/#"));
        assertTrue(matches("/", "#"));
        assertTrue(matches("//", "//"));
        assertTrue(matches("//", "#"));
        assertTrue(matches("//", "+/#"));
        assertTrue(matches("a", "a"));
        assertTrue(matches("a", "#"));
        assertTrue(matches("a", "+"));
        assertTrue(matches("a/", "+/#"));
        assertTrue(matches("a/", "a/"));
        assertTrue(matches("/a", "/a"));
        assertTrue(matches("/a", "/+"));
        assertTrue(matches("/a/b/c", "/+/#"));
        assertTrue(matches("/a/b/c", "/#"));
        assertTrue(matches("/a/b/c", "#"));

        assertFalse(matches("a", "/a"));
        assertFalse(matches("a", "+/"));
        assertFalse(matches("a/", "/a"));
    }

    private boolean matches(String topic, String topicFilter) {
        return match(parse(topic, false), parse(topicFilter, false));
    }
}
