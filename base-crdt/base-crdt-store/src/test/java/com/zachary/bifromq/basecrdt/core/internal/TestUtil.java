package com.zachary.bifromq.basecrdt.core.internal;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class TestUtil {
    public static <E> void assertSame(Iterator<E> a, Iterator<E> b) {
        while (a.hasNext() && b.hasNext()) {
            assertEquals(b.next(), a.next());
        }
        assertEquals(b.hasNext(), a.hasNext());
    }

    public static <E> void assertUnorderedSame(Iterator<E> a, Iterator<E> b) {
        Set<E> aSet = Sets.newHashSet();
        Set<E> bSet = Sets.newHashSet();
        a.forEachRemaining(aSet::add);
        b.forEachRemaining(bSet::add);
        assertEquals(bSet, aSet);
    }

    public static Map<ByteString, NavigableMap<Long, Long>> toLatticeEvents(ByteString replicaId, long... boundaries) {
        Map<ByteString, NavigableMap<Long, Long>> histories = Maps.newHashMap();
        NavigableMap<Long, Long> ranges = histories.computeIfAbsent(replicaId, k -> Maps.newTreeMap());
        for (int i = 0; i < boundaries.length; i = i + 2) {
            ranges.put(boundaries[i], boundaries[i + 1]);
        }
        return histories;
    }
}
