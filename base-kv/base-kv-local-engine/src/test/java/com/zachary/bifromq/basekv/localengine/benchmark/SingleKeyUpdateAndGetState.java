package com.zachary.bifromq.basekv.localengine.benchmark;


import com.google.protobuf.ByteString;
import com.zachary.bifromq.basekv.localengine.IKVEngine;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import static com.zachary.bifromq.basekv.localengine.TestUtil.toByteStringNativeOrder;

@Slf4j
@State(Scope.Group)
public class SingleKeyUpdateAndGetState extends BenchmarkState {
    ByteString key = ByteString.copyFromUtf8("key");
    int rangeId;

    @Override
    protected void afterSetup() {
        rangeId = kvEngine.registerKeyRange(IKVEngine.DEFAULT_NS, null, null);
        kvEngine.insert(rangeId, key, toByteStringNativeOrder(-1024));
    }

    @Override
    protected void beforeTeardown() {

    }
}
