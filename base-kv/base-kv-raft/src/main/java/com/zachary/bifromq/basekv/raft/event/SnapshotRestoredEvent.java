package com.zachary.bifromq.basekv.raft.event;

import com.zachary.bifromq.basekv.raft.proto.Snapshot;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SnapshotRestoredEvent extends RaftEvent {
    public final Snapshot snapshot;

    public SnapshotRestoredEvent(String nodeId, Snapshot snapshot) {
        super(nodeId, RaftEventType.SNAPSHOT_RESTORED);
        this.snapshot = snapshot;
    }
}
