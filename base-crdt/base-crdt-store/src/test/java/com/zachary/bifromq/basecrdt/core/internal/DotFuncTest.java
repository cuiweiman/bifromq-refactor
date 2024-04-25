package com.zachary.bifromq.basecrdt.core.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import org.testng.annotations.Test;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static com.zachary.bifromq.basecrdt.core.internal.ProtoUtils.dot;
import static com.zachary.bifromq.basecrdt.core.internal.ProtoUtils.singleValue;
import static com.zachary.bifromq.basecrdt.core.internal.TestUtil.assertUnorderedSame;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class DotFuncTest {
    private final ByteString replicaA = copyFromUtf8("A");
    private final ByteString replicaB = copyFromUtf8("B");

    private final ByteString val1 = copyFromUtf8("val1");
    private final ByteString val2 = copyFromUtf8("val2");


    @Test
    public void testAdd() {
        DotFunc dotFunc = new DotFunc();
        assertTrue(dotFunc.isBottom());
        assertFalse(dotFunc.iterator().hasNext());
        assertFalse(dotFunc.values().iterator().hasNext());

        assertTrue(dotFunc.add(singleValue(replicaA, 1, val1)));
        assertTrue(dotFunc.add(singleValue(replicaB, 1, val2)));
        assertFalse(dotFunc.isBottom());
        assertEquals(Sets.newHashSet(dotFunc), Sets.newHashSet(dot(replicaA, 1), dot(replicaB, 1)));
        assertUnorderedSame(Lists.newArrayList(val1, val2).iterator(), dotFunc.values().iterator());
    }

    @Test
    public void testAddSameDotWithDifferentVal() {
        DotFunc dotFunc = new DotFunc();

        dotFunc.add(singleValue(replicaA, 1, val1));
        assertFalse(dotFunc.add(singleValue(replicaA, 1, val2)));
        assertEquals(Sets.newHashSet(dotFunc), Sets.newHashSet(dot(replicaA, 1)));
        assertUnorderedSame(Lists.<ByteString>newArrayList(val1).iterator(), dotFunc.values().iterator());
    }

    @Test
    public void testAddDifferentDotWithSameVal() {
        DotFunc dotFunc = new DotFunc();

        dotFunc.add(singleValue(replicaA, 1, val1));
        assertFalse(dotFunc.add(singleValue(replicaA, 1, val1)));
        assertEquals(Sets.newHashSet(dotFunc), Sets.newHashSet(dot(replicaA, 1)));
        assertUnorderedSame(Lists.newArrayList(val1, val1).iterator(), dotFunc.values().iterator());
    }

    @Test
    public void testRemove() {
        DotFunc dotFunc = new DotFunc();

        dotFunc.add(singleValue(replicaA, 1, val1));

        // value is not match
        assertFalse(dotFunc.remove(singleValue(replicaA, 1, val2)));
        assertFalse(dotFunc.isBottom());

        assertTrue(dotFunc.remove(singleValue(replicaA, 1, val1)));
        assertTrue(dotFunc.isBottom());
    }
}
