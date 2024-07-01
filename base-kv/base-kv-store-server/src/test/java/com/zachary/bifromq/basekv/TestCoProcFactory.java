package com.zachary.bifromq.basekv;

import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProc;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProcFactory;
import com.zachary.bifromq.basekv.store.api.IKVRangeReader;

import java.util.function.Supplier;

public class TestCoProcFactory implements IKVRangeCoProcFactory {
    @Override
    public IKVRangeCoProc create(KVRangeId id, Supplier<IKVRangeReader> readerProvider) {
        return new TestCoProc(id, readerProvider);
    }
}
