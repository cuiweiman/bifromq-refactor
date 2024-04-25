package com.zachary.bifromq.basecrdt.core.internal;

import com.google.common.collect.Maps;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.NavigableMap;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static com.zachary.bifromq.basecrdt.core.internal.EventHistoryUtil.diff;
import static com.zachary.bifromq.basecrdt.core.util.LatticeIndexUtil.remember;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Slf4j
public class EventHistoryUtilTest {
    private final ByteString replicaA = copyFromUtf8("A");
    private final ByteString replicaB = copyFromUtf8("B");

    @Test
    public void testCommon() {
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            assertEquals(EventHistoryUtil.common(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(0L, 1L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            assertEquals(EventHistoryUtil.common(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(0L, 1L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(2L, 3L);
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            assertEquals(EventHistoryUtil.common(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(0L, 1L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(0L, 1L);
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            c.put(0L, 1L);
            assertEquals(EventHistoryUtil.common(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(0L, 10L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(0L, 1L);
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            c.put(0L, 1L);
            assertEquals(EventHistoryUtil.common(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(0L, 10L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(1L, 1L);
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            c.put(1L, 1L);
            assertEquals(EventHistoryUtil.common(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(0L, 5L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(0L, 10L);
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            c.put(0L, 5L);
            assertEquals(EventHistoryUtil.common(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(0L, 5L);
            a.put(7L, 10L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(4L, 8L);
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            c.put(4L, 5L);
            c.put(7L, 8L);
            assertEquals(EventHistoryUtil.common(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(0L, 5L);
            a.put(7L, 10L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(4L, 11L);
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            c.put(4L, 5L);
            c.put(7L, 10L);
            assertEquals(EventHistoryUtil.common(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(4L, 11L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(0L, 5L);
            b.put(7L, 10L);
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            c.put(4L, 5L);
            c.put(7L, 10L);
            assertEquals(EventHistoryUtil.common(a, b), c);
        }
    }

    @Test
    public void testForget() {
        Map<ByteString, NavigableMap<Long, Long>> historyMap = Maps.newLinkedHashMap();
        remember(historyMap, replicaA, 1);
        remember(historyMap, replicaA, 2);
        remember(historyMap, replicaA, 3);
        remember(historyMap, replicaA, 5);
        remember(historyMap, replicaA, 7);
        remember(historyMap, replicaA, 8);
        remember(historyMap, replicaB, 1);

        EventHistoryUtil.forget(historyMap, replicaB, 0);
        assertTrue(historyMap.containsKey(replicaB));

        EventHistoryUtil.forget(historyMap, replicaB, 1);
        assertFalse(historyMap.containsKey(replicaB));

        EventHistoryUtil.forget(historyMap, replicaA, 1);
        assertFalse(historyMap.get(replicaA).containsKey(1L));
    }

    @Test
    public void testDiff() {
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            assertEquals(diff(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(0L, 1L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            assertEquals(diff(a, b), a);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(0L, 1L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(0L, 1L);
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            assertEquals(diff(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(0L, 1L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(0L, 2L);
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            assertEquals(diff(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(0L, 1L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(2L, 3L);
            assertEquals(diff(a, b), a);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(0L, 1L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(1L, 3L);
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            c.put(0L, 0L);
            assertEquals(diff(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(1L, 3L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(0L, 1L);
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            c.put(2L, 3L);
            assertEquals(diff(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(1L, 3L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(2L, 4L);
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            c.put(1L, 1L);
            assertEquals(diff(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(2L, 5L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(1L, 3L);
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            c.put(4L, 5L);
            assertEquals(diff(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(0L, 5L);
            a.put(7L, 10L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(4L, 11L);
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            c.put(0L, 3L);
            assertEquals(diff(a, b), c);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            a.put(4L, 11L);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            b.put(0L, 5L);
            b.put(7L, 10L);
            NavigableMap<Long, Long> c = Maps.newTreeMap();
            c.put(6L, 6L);
            c.put(11L, 11L);
            assertEquals(diff(a, b), c);
        }
    }
}
