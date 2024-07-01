package com.zachary.bifromq.basekv.store.option;

import com.zachary.bifromq.basekv.raft.RaftConfig;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class KVRangeOptions {
    private int snapshotSyncBytesPerSec = 1024 * 1024;
    private int compactWALThresholdBytes = 128 * 1024 * 1024; // 128MB
    private long tickUnitInMS = 100;
    private int maxWALFatchBatchSize = 64 * 1024; // 64KB
    private int snapshotSyncIdleTimeoutSec = 30;
    private int statsCollectIntervalSec = 5;
    private RaftConfig walRaftConfig = new RaftConfig()
        .setPreVote(true)
        .setInstallSnapshotTimeoutTick(300)
        .setMaxSizePerAppend(2 * 1024 * 1024); // 2MB;
}
