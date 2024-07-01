package com.zachary.bifromq.basekv.localengine.benchmark;


import com.zachary.bifromq.basekv.localengine.IKVEngine;
import com.zachary.bifromq.basekv.localengine.IKVEngineIterator;
import com.google.protobuf.ByteString;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import static com.zachary.bifromq.basekv.localengine.TestUtil.toByteString;

@State(Scope.Benchmark)
public class ContinuousKeySingleDeleteAndSeekState extends BenchmarkState {
    int keyCount = 1000000;
    ByteString key = ByteString.copyFromUtf8("key");
    IKVEngineIterator itr;
    int rangeId;

    @Override
    protected void afterSetup() {
        int rangeId = kvEngine.registerKeyRange(IKVEngine.DEFAULT_NS, null, null);
        int batchId = kvEngine.startBatch();
        for (int i = 0; i < keyCount; i++) {
            kvEngine.put(batchId, rangeId, key.concat(toByteString(i)), ByteString.EMPTY);
            kvEngine.delete(batchId, rangeId, key.concat(toByteString(i)));
        }
        kvEngine.put(batchId, rangeId, key.concat(toByteString(keyCount)), ByteString.EMPTY);
        kvEngine.endBatch(batchId);
        itr = kvEngine.newIterator(rangeId);
    }

    @Override
    protected void beforeTeardown() {

    }
}
