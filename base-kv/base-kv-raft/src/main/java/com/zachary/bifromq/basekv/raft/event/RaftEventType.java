package com.zachary.bifromq.basekv.raft.event;

public enum RaftEventType {
    COMMIT,
    ELECTION,
    SNAPSHOT_RESTORED,
    STATUS_CHANGED,
    SYNC_STATE_CHANGED
}
