package com.zachary.bifromq.basekv.store.util;

import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static com.zachary.bifromq.basekv.store.util.KVUtil.cap;
import static com.zachary.bifromq.basekv.store.util.KVUtil.toByteString;
import static com.zachary.bifromq.basekv.store.util.KVUtil.toInt;
import static com.zachary.bifromq.basekv.store.util.KVUtil.toKVRangeId;
import static com.zachary.bifromq.basekv.store.util.KVUtil.toLong;
import static com.zachary.bifromq.basekv.store.util.KVUtil.toLongNativeOrder;
import static org.testng.Assert.assertEquals;

@Slf4j
public class KVUtilTest {
    @Test
    public void testToBytes() {
        assertEquals(toLong(KVUtil.toByteString(100L)), 100L);
        assertEquals(toInt(KVUtil.toByteString(100)), 100);
        assertEquals(toLongNativeOrder(KVUtil.toByteStringNativeOrder(100L)), 100L);
    }

    @Test
    public void testConcat() {
        assertEquals(KVUtil.concat(ByteString.copyFrom(new byte[] {1, 2}),
                ByteString.copyFrom(new byte[] {3, 4, 5}),
                ByteString.copyFrom(new byte[0]),
                ByteString.copyFrom(new byte[] {6, 7})),
            ByteString.copyFrom(new byte[] {1, 2, 3, 4, 5, 6, 7}));
        assertEquals(KVUtil.concat(ByteString.EMPTY, ByteString.EMPTY), ByteString.EMPTY);
        assertEquals(KVUtil.concat(ByteString.copyFrom(new byte[] {1, 2, 3})),
            ByteString.copyFrom(new byte[] {1, 2, 3}));
    }

    @Test
    public void testUpperBound() {
        KVRangeId bucketId = cap(KVRangeId.newBuilder().build());
        assertEquals(0L, bucketId.getEpoch());
        assertEquals(1L, bucketId.getId());
    }

    @Test
    public void testKVRangeIdCodec() {
        KVRangeId id = KVRangeIdUtil.generate();
        assertEquals(toKVRangeId(toByteString(id)), id);
    }
}
