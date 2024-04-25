package com.zachary.bifromq.basecrdt.core.util;

import com.google.common.collect.Maps;
import com.google.protobuf.ByteString;
import org.testng.annotations.Test;

import java.util.NavigableMap;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static org.testng.Assert.assertEquals;

public class LatticeIndexUtilTest {
    private final ByteString replicaA = copyFromUtf8("A");
    private final ByteString replicaB = copyFromUtf8("B");

    @Test
    public void testRemember() {
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            LatticeIndexUtil.remember(a, 1, 2);
            LatticeIndexUtil.remember(a, 3, 3);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            LatticeIndexUtil.remember(b, 1, 3);
            assertEquals(b, a);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            LatticeIndexUtil.remember(a, 3, 3);
            LatticeIndexUtil.remember(a, 1, 2);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            LatticeIndexUtil.remember(b, 1, 3);
            assertEquals(b, a);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            LatticeIndexUtil.remember(a, 1, 3);
            LatticeIndexUtil.remember(a, 3, 5);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            LatticeIndexUtil.remember(b, 1, 5);
            assertEquals(b, a);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            LatticeIndexUtil.remember(a, 1, 3);
            LatticeIndexUtil.remember(a, 4, 5);
            LatticeIndexUtil.remember(a, 6, 8);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            LatticeIndexUtil.remember(b, 1, 8);
            assertEquals(b, a);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            LatticeIndexUtil.remember(a, 1, 3);
            LatticeIndexUtil.remember(a, 6, 8);
            LatticeIndexUtil.remember(a, 4, 5);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            LatticeIndexUtil.remember(b, 1, 8);
            assertEquals(b, a);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            LatticeIndexUtil.remember(a, 6, 8);
            LatticeIndexUtil.remember(a, 1, 3);
            LatticeIndexUtil.remember(a, 4, 5);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            LatticeIndexUtil.remember(b, 1, 8);
            assertEquals(b, a);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            LatticeIndexUtil.remember(a, 1, 2);
            LatticeIndexUtil.remember(a, 1, 5);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            LatticeIndexUtil.remember(b, 1, 5);
            assertEquals(b, a);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            LatticeIndexUtil.remember(a, 1, 55);
            LatticeIndexUtil.remember(a, 10, 10);
            LatticeIndexUtil.remember(a, 56, 95);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            LatticeIndexUtil.remember(b, 1, 95);
            assertEquals(b, a);
        }
        {
            NavigableMap<Long, Long> a = Maps.newTreeMap();
            LatticeIndexUtil.remember(a, 1, 44);
            LatticeIndexUtil.remember(a, 14, 14);
            LatticeIndexUtil.remember(a, 17, 17);
            LatticeIndexUtil.remember(a, 21, 22);
            LatticeIndexUtil.remember(a, 24, 24);
            LatticeIndexUtil.remember(a, 45, 96);
            NavigableMap<Long, Long> b = Maps.newTreeMap();
            LatticeIndexUtil.remember(b, 1, 96);
            assertEquals(b, a);
        }
    }
}
