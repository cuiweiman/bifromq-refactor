package com.zachary.bifromq.basekv.store.wal;

import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.raft.IRaftStateStore;
import com.zachary.bifromq.basekv.raft.proto.Snapshot;

import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

public interface IKVRangeWALStoreEngine {
    String id();

    void start(ScheduledExecutorService bgTaskExecutor);

    void stop();

    IRaftStateStore newRaftStateStorage(KVRangeId kvRangeId, Snapshot initSnapshot);

    Set<KVRangeId> allKVRangeIds();

    boolean has(KVRangeId kvRangeId);

    IRaftStateStore get(KVRangeId kvRangeId);

    long storageSize(KVRangeId kvRangeId);

    void destroy(KVRangeId kvRangeId);
}
