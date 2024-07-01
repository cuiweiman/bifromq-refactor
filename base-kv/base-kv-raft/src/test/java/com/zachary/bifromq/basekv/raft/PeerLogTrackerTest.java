package com.zachary.bifromq.basekv.raft;

import com.zachary.bifromq.basekv.raft.event.SyncStateChangedEvent;
import com.zachary.bifromq.basekv.raft.proto.ClusterConfig;
import com.zachary.bifromq.basekv.raft.proto.RaftNodeSyncState;
import com.zachary.bifromq.basekv.raft.proto.Snapshot;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class PeerLogTrackerTest {
    PeerLogTracker logTracker;
    RaftConfig config;
    IRaftStateStore stateStorage;
    @Mock
    PeerLogReplicatorStateProbing stateProbing;
    @Mock
    IRaftNode.IRaftEventListener statusListener;
    @Mock
    IRaftNodeLogger logger;
    private AutoCloseable closeable;
    @BeforeMethod
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        config = new RaftConfig();
        stateStorage = new InMemoryStateStore("V1", Snapshot.newBuilder()
            .setIndex(0)
            .setTerm(0)
            .setClusterConfig(ClusterConfig.newBuilder().addVoters("V1").addVoters("V2").addVoters("V3").build())
            .build());
        logTracker = new PeerLogTracker(stateStorage.local(), config, stateStorage, statusListener, logger);
    }

    @AfterMethod
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void testCurrentStates() {
        logTracker.startTracking(new HashSet<>() {{
            add("V1");
        }}, true);

        ArgumentCaptor<SyncStateChangedEvent> captured = ArgumentCaptor.forClass(SyncStateChangedEvent.class);
        verify(statusListener).onEvent(captured.capture());
        assertEquals(captured.getValue().states, new HashMap<String, RaftNodeSyncState>() {{
            put("V1", RaftNodeSyncState.Probing);
        }});

        logTracker.startTracking(new HashSet<>() {{
            add("V2");
        }}, true);

        captured = ArgumentCaptor.forClass(SyncStateChangedEvent.class);
        verify(statusListener, times(2)).onEvent(captured.capture());

        assertEquals(captured.getValue().states, new HashMap<String, RaftNodeSyncState>() {{
            put("V1", RaftNodeSyncState.Probing);
            put("V2", RaftNodeSyncState.Probing);
        }});
    }

    @Test
    public void testStartTrackAndStopTrack() {
        assertFalse(logTracker.isTracking("Abc"));
        logTracker.startTracking(new HashSet<String>() {{
            add("Abc");
        }}, true);
        assertTrue(logTracker.isTracking("Abc"));
        assertEquals(logTracker.status("Abc"), RaftNodeSyncState.Probing);
    }

    @Test
    public void testStartTrackWithoutNotify() {
        assertFalse(logTracker.isTracking("Abc"));
        logTracker.startTracking(new HashSet<>() {{
            add("Abc");
        }}, false);

        verify(statusListener, times(0)).onEvent(any());
    }

    @Test
    public void testStopTrackPredicateAndNotify() {
        logTracker.startTracking(new HashSet<>() {{
            add("A1");
            add("A2");
            add("B1");
        }}, true);

        ArgumentCaptor<SyncStateChangedEvent> captured = ArgumentCaptor.forClass(SyncStateChangedEvent.class);
        verify(statusListener).onEvent(captured.capture());
        assertEquals(captured.getValue().states, new HashMap<String, RaftNodeSyncState>() {{
            put("A1", RaftNodeSyncState.Probing);
            put("A2", RaftNodeSyncState.Probing);
            put("B1", RaftNodeSyncState.Probing);
        }});

        logTracker.stopTracking(key -> key.startsWith("A"), true);

        captured = ArgumentCaptor.forClass(SyncStateChangedEvent.class);
        verify(statusListener, times(2)).onEvent(captured.capture());
        assertEquals(captured.getValue().states, new HashMap<String, RaftNodeSyncState>() {{
            put("B1", RaftNodeSyncState.Probing);
        }});

        assertFalse(logTracker.isTracking("A1"));
        assertFalse(logTracker.isTracking("A2"));
        assertTrue(logTracker.isTracking("B1"));
    }

    @Test
    public void testStopTrackPredicateWithoutNotify() {
        logTracker.startTracking(new HashSet<>() {{
            add("A1");
            add("A2");
            add("B1");
        }}, true);

        ArgumentCaptor<SyncStateChangedEvent> captured = ArgumentCaptor.forClass(SyncStateChangedEvent.class);
        verify(statusListener).onEvent(captured.capture());
        assertEquals(captured.getValue().states, new HashMap<String, RaftNodeSyncState>() {{
            put("A1", RaftNodeSyncState.Probing);
            put("A2", RaftNodeSyncState.Probing);
            put("B1", RaftNodeSyncState.Probing);
        }});
        logTracker.stopTracking(key -> key.startsWith("A"), false);
        verify(statusListener, times(1)).onEvent(any());

        assertFalse(logTracker.isTracking("A1"));
        assertFalse(logTracker.isTracking("A2"));
        assertTrue(logTracker.isTracking("B1"));
    }

    // TODO: cover replication status notification situations
}
