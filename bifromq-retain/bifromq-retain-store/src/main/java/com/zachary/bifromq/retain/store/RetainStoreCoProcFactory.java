package com.zachary.bifromq.retain.store;

import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProc;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProcFactory;
import com.zachary.bifromq.basekv.store.api.IKVRangeReader;

import java.time.Clock;
import java.util.function.Supplier;

public class RetainStoreCoProcFactory implements IKVRangeCoProcFactory {
    private final Clock clock;

    public RetainStoreCoProcFactory(Clock clock) {
        this.clock = clock;
    }

    @Override
    public IKVRangeCoProc create(KVRangeId id, Supplier<IKVRangeReader> rangeReaderProvider) {
        return new RetainStoreCoProc(id, rangeReaderProvider, clock);
    }
}
