package com.zachary.bifromq.basekv.raft;

import com.zachary.bifromq.basekv.raft.proto.RaftNodeSyncState;
import com.zachary.bifromq.basekv.raft.proto.Snapshot;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class PeerLogReplicatorStateSnapshotSyncingTest {
    PeerLogReplicatorStateSnapshotSyncing stateSnapshotSyncing;
    RaftConfig config = new RaftConfig().setInstallSnapshotTimeoutTick(3);
    Snapshot snapshot = Snapshot.newBuilder()
        .setIndex(10L)
        .build();
    @Mock
    IRaftStateStore stateStorage;
    @Mock
    IRaftNodeLogger logger;
    private AutoCloseable closeable;
    @BeforeMethod
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        when(stateStorage.latestSnapshot()).thenReturn(snapshot);
        stateSnapshotSyncing = new PeerLogReplicatorStateSnapshotSyncing("V1", config, stateStorage, logger);
    }

    @AfterMethod
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void testInitialize() {
        assertFalse(stateSnapshotSyncing.pauseReplicating());
        assertEquals(stateSnapshotSyncing.matchIndex(), 10);
        assertEquals(stateSnapshotSyncing.nextIndex(), 11);
        assertEquals(stateSnapshotSyncing.catchupRate(), 0);
    }

    @Test
    public void testCurrentState() {
        assertEquals(stateSnapshotSyncing.state(), RaftNodeSyncState.SnapshotSyncing);
    }

    @Test
    public void testPauseBehavior() {
        stateSnapshotSyncing.replicateTo(10);
        assertTrue(stateSnapshotSyncing.pauseReplicating());
    }

    @Test
    public void testPauseResumedByInstallTimeout() {
        when(stateStorage.lastIndex()).thenReturn(15L);

        stateSnapshotSyncing.replicateTo(10);
        assertTrue(stateSnapshotSyncing.pauseReplicating());
        assertEquals(stateSnapshotSyncing.tick(), stateSnapshotSyncing);
        assertFalse(stateSnapshotSyncing.needHeartbeat());
        assertEquals(stateSnapshotSyncing.tick(), stateSnapshotSyncing);
        assertTrue(stateSnapshotSyncing.needHeartbeat());
        stateSnapshotSyncing.replicateTo(10);
        PeerLogReplicatorState nextState = stateSnapshotSyncing.tick();
        assertFalse(stateSnapshotSyncing.needHeartbeat());
        assertEquals(nextState.state(), RaftNodeSyncState.Probing);
        assertEquals(nextState.matchIndex(), 10);
        assertEquals(nextState.nextIndex(), 16);
    }

    @Test
    public void testConfirmTransitToReplicating() {
        stateSnapshotSyncing.replicateTo(10);
        assertTrue(stateSnapshotSyncing.pauseReplicating());
        PeerLogReplicatorState nextState = stateSnapshotSyncing.confirmMatch(10);
        assertEquals(nextState.state(), RaftNodeSyncState.Replicating);
        assertEquals(nextState.matchIndex(), 10);
        assertEquals(nextState.nextIndex(), 11);
    }

    @Test
    public void testConfirmTransitToSnapshotSyncing() {
        when(stateStorage.latestSnapshot()).thenReturn(Snapshot.newBuilder().setIndex(11L).build());

        stateSnapshotSyncing.replicateTo(10);
        assertTrue(stateSnapshotSyncing.pauseReplicating());
        PeerLogReplicatorState nextState = stateSnapshotSyncing.confirmMatch(10);
        assertEquals(nextState.state(), RaftNodeSyncState.SnapshotSyncing);
        assertEquals(nextState.matchIndex(), 11);
    }

    @Test
    public void testBackoffTransitToSnapshotSyncing() {
        stateSnapshotSyncing.replicateTo(10);
        assertTrue(stateSnapshotSyncing.pauseReplicating());
        PeerLogReplicatorState nextState = stateSnapshotSyncing.backoff(10, 10);
        assertEquals(nextState.state(), RaftNodeSyncState.SnapshotSyncing);
        assertEquals(nextState.matchIndex(), 10);
        assertEquals(nextState.nextIndex(), 11);
    }

    @Test
    public void testReplicatedToTransitToSnapshotSyncingAgain() {
        Snapshot snapshot = Snapshot.newBuilder()
            .setIndex(20L)
            .build();
        when(stateStorage.latestSnapshot()).thenReturn(snapshot);
        PeerLogReplicatorState nextState = stateSnapshotSyncing.replicateTo(20);
        assertEquals(nextState.matchIndex(), 20);
        assertEquals(nextState.nextIndex(), 21);
    }
}
