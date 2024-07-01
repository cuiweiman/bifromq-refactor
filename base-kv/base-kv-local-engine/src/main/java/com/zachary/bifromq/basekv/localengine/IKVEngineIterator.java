package com.zachary.bifromq.basekv.localengine;

import com.google.protobuf.ByteString;

public interface IKVEngineIterator extends AutoCloseable {
    ByteString key();

    ByteString value();

    boolean isValid();

    void next();

    void prev();

    void seekToFirst();

    void seekToLast();

    void seek(ByteString target);

    void seekForPrev(ByteString target);

    void refresh();

    void close();
}
