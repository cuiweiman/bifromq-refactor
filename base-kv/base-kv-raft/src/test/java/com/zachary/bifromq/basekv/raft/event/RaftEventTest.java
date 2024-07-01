package com.zachary.bifromq.basekv.raft.event;

import com.zachary.bifromq.basekv.raft.proto.RaftNodeStatus;
import com.zachary.bifromq.basekv.raft.proto.Snapshot;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.testng.Assert.assertEquals;

public class RaftEventTest {
    @Test
    public void typeMatch() {
        CommitEvent commitEvent = new CommitEvent("S1", 1);
        assertEquals(commitEvent.type, RaftEventType.COMMIT);

        ElectionEvent electionEvent = new ElectionEvent("S1", "S2", 0);
        assertEquals(electionEvent.type, RaftEventType.ELECTION);

        SnapshotRestoredEvent snapshotRestoredEvent = new SnapshotRestoredEvent("S1", Snapshot.getDefaultInstance());
        assertEquals(snapshotRestoredEvent.type, RaftEventType.SNAPSHOT_RESTORED);

        StatusChangedEvent statusChangedEvent = new StatusChangedEvent("S1", RaftNodeStatus.Leader);
        assertEquals(statusChangedEvent.type, RaftEventType.STATUS_CHANGED);

        SyncStateChangedEvent syncStateChangedEvent = new SyncStateChangedEvent("S1", Collections.emptyMap());
        assertEquals(syncStateChangedEvent.type, RaftEventType.SYNC_STATE_CHANGED);
    }

    @Test
    public void equals() {
        assertEquals(new CommitEvent("S1", 1), new CommitEvent("S1", 1));
        assertEquals(new ElectionEvent("S1", "S2", 0), new ElectionEvent("S1", "S2", 0));
        assertEquals(new SnapshotRestoredEvent("S1", Snapshot.getDefaultInstance()),
            new SnapshotRestoredEvent("S1", Snapshot.getDefaultInstance()));
        assertEquals(new StatusChangedEvent("S1", RaftNodeStatus.Leader),
            new StatusChangedEvent("S1", RaftNodeStatus.Leader));
        assertEquals(new SyncStateChangedEvent("S1", Collections.emptyMap()),
            new SyncStateChangedEvent("S1", Collections.emptyMap()));
    }
}
