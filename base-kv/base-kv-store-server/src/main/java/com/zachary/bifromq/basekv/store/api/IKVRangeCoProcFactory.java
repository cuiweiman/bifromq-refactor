package com.zachary.bifromq.basekv.store.api;

import com.zachary.bifromq.basekv.proto.KVRangeId;

import java.util.function.Supplier;

public interface IKVRangeCoProcFactory {
    IKVRangeCoProc create(KVRangeId id, Supplier<IKVRangeReader> readerProvider);
}
