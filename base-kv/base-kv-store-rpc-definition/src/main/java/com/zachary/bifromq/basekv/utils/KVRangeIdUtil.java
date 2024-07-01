package com.zachary.bifromq.basekv.utils;

import com.zachary.bifromq.basehlc.HLC;
import com.zachary.bifromq.basekv.proto.KVRangeId;


public final class KVRangeIdUtil {
    public static KVRangeId generate() {
        long hlc = HLC.INST.get();
        return KVRangeId.newBuilder()
            .setEpoch(hlc)
            .setId(0)
            .build();
    }

    public static KVRangeId next(KVRangeId from) {
        return KVRangeId.newBuilder()
            .setEpoch(from.getEpoch()) // under same epoch
            .setId(HLC.INST.get())
            .build();
    }

    public static String toString(KVRangeId kvRangeId) {
        return Long.toUnsignedString(kvRangeId.getEpoch()) + "_" + Long.toUnsignedString(kvRangeId.getId());
    }

    public static KVRangeId fromString(String id) {
        String[] parts = id.split("_");
        assert parts.length == 2;
        return KVRangeId.newBuilder()
            .setEpoch(Long.parseUnsignedLong(parts[0]))
            .setId(Long.parseUnsignedLong(parts[1]))
            .build();
    }

    public static String toShortString(KVRangeId kvRangeId) {
        String s = toString(kvRangeId);
        return s.substring(s.length() - 4);
    }
}
