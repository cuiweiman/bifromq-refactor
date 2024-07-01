package com.zachary.bifromq.basekv.store.wal;

import com.zachary.bifromq.basekv.proto.KVRangeSnapshot;
import com.zachary.bifromq.basekv.raft.proto.LogEntry;

import java.util.concurrent.CompletableFuture;

public interface IKVRangeWALSubscriber {
    default void onSubscribe(IKVRangeWALSubscription subscription) {

    }

    CompletableFuture<Void> apply(LogEntry log);

    CompletableFuture<Void> apply(KVRangeSnapshot snapshot);
}
