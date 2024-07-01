package com.zachary.bifromq.basekv.store.api;

import com.google.protobuf.ByteString;

public interface IKVIterator {
    ByteString key();

    ByteString value();

    boolean isValid();

    void next();

    void prev();

    void seekToFirst();

    void seekToLast();

    void seek(ByteString key);

    void seekForPrev(ByteString key);
}
