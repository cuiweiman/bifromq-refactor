package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.proto.KVRangeSnapshot;

import java.util.concurrent.CompletionStage;

public interface IKVRangeSnapshotChecker {
    CompletionStage<Void> check(KVRangeSnapshot snapshot);
}
