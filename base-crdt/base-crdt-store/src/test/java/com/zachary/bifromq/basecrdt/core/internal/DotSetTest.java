package com.zachary.bifromq.basecrdt.core.internal;

import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import org.testng.annotations.Test;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static com.zachary.bifromq.basecrdt.core.internal.ProtoUtils.dot;
import static com.zachary.bifromq.basecrdt.core.internal.ProtoUtils.singleDot;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class DotSetTest {
    private final ByteString replicaA = copyFromUtf8("A");
    private final ByteString replicaB = copyFromUtf8("B");

    @Test
    public void testAdd() {
        DotSet dotSet = new DotSet();
        assertTrue(dotSet.isBottom());
        assertFalse(dotSet.iterator().hasNext());

        assertTrue(dotSet.add(singleDot(replicaA, 1)));
        assertFalse(dotSet.add(singleDot(replicaA, 1)));
        assertTrue(dotSet.add(singleDot(replicaB, 1)));
        assertFalse(dotSet.isBottom());

        TestUtil.assertUnorderedSame(Sets.newHashSet(dot(replicaA, 1), dot(replicaB, 1)).iterator(), dotSet.iterator());
    }

    @Test
    public void testRemove() {
        DotSet dotSet = new DotSet();

        dotSet.add(singleDot(replicaA, 1));
        dotSet.add(singleDot(replicaB, 1));

        assertTrue(dotSet.remove(singleDot(replicaA, 1)));
        assertFalse(dotSet.remove(singleDot(replicaA, 1)));
        assertTrue(dotSet.remove(singleDot(replicaB, 1)));
        assertTrue(dotSet.isBottom());
        assertFalse(dotSet.iterator().hasNext());
    }
}
