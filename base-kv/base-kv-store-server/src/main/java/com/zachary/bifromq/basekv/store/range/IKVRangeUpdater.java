package com.zachary.bifromq.basekv.store.range;

public interface IKVRangeUpdater {
    void abort();

    int size();

    void close();
}
