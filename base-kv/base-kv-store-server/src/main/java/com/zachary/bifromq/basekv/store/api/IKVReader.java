package com.zachary.bifromq.basekv.store.api;

import com.zachary.bifromq.basekv.proto.Range;
import com.google.protobuf.ByteString;

import java.util.Optional;

public interface IKVReader {
    Range range();

    long size(Range range);

    boolean exist(ByteString key);

    Optional<ByteString> get(ByteString key);

    IKVIterator iterator();
}
