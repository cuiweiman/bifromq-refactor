package com.zachary.bifromq.basekv;


import com.zachary.bifromq.basekv.proto.Range;
import com.google.protobuf.ByteString;

public class Constants {
    public static final ByteString MIN_KEY = ByteString.EMPTY;

    public static final Range EMPTY_RANGE = Range.newBuilder().setEndKey(MIN_KEY).build();
    public static final Range FULL_RANGE = Range.getDefaultInstance();
}
