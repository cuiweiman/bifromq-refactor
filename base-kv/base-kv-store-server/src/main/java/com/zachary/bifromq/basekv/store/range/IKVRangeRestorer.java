package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.proto.KVPair;

public interface IKVRangeRestorer extends IKVRangeUpdater {
    void add(KVPair kvPair);
}
