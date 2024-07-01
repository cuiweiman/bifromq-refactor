package com.zachary.bifromq.dist.worker;

import com.zachary.bifromq.basekv.proto.LoadHint;
import com.google.protobuf.ByteString;

public interface ILoadEstimator {
    void track(ByteString key, int loadUnit);

    LoadHint estimate();
}
