package com.zachary.bifromq.basekv.localengine;

import com.google.protobuf.ByteString;

public abstract class AbstractWriteBatch<K extends AbstractKeyRange> {
    public final int batchId;

    protected AbstractWriteBatch(int batchId) {
        this.batchId = batchId;
    }

    public abstract int count();

    public abstract void insert(K range, ByteString key, ByteString value);

    public abstract void put(K range, ByteString key, ByteString value);

    public abstract void delete(K range, ByteString key);

    public abstract void end();

    public abstract void abort();
}
