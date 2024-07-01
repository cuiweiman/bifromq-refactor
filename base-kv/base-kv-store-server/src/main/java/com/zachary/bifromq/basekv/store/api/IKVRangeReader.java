package com.zachary.bifromq.basekv.store.api;

import com.zachary.bifromq.basekv.proto.State;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * Thread-Unsafe reader to access the consistent view of KVRange
 */
@NotThreadSafe
public interface IKVRangeReader {
    long ver();

    State state();

    long lastAppliedIndex();

    IKVReader kvReader();

    /**
     * Refresh to access the latest consistent view
     */
    void refresh();
}
