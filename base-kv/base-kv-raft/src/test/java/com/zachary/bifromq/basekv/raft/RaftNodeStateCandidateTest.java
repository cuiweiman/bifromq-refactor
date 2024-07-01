package com.zachary.bifromq.basekv.raft;

import com.zachary.bifromq.basekv.raft.proto.AppendEntries;
import com.zachary.bifromq.basekv.raft.proto.ClusterConfig;
import com.zachary.bifromq.basekv.raft.proto.InstallSnapshot;
import com.zachary.bifromq.basekv.raft.proto.LogEntry;
import com.zachary.bifromq.basekv.raft.proto.RaftMessage;
import com.zachary.bifromq.basekv.raft.proto.RaftNodeStatus;
import com.zachary.bifromq.basekv.raft.proto.RequestPreVote;
import com.zachary.bifromq.basekv.raft.proto.RequestPreVoteReply;
import com.zachary.bifromq.basekv.raft.proto.RequestVote;
import com.zachary.bifromq.basekv.raft.proto.RequestVoteReply;
import com.zachary.bifromq.basekv.raft.proto.Snapshot;
import com.google.protobuf.ByteString;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class RaftNodeStateCandidateTest extends RaftNodeStateTest {
    private final Logger log = LoggerFactory.getLogger("RaftNodeStateCandidateTest");
    private AutoCloseable closeable;
    @BeforeMethod
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterMethod
    public void releaseMocks() throws Exception {
        closeable.close();
    }
    @Test
    public void testStartUp() {
        IRaftStateStore stateStorage = new InMemoryStateStore("testLocal", Snapshot.newBuilder()
            .setClusterConfig(clusterConfig).build());
        RaftConfig raftConfig = new RaftConfig()
            .setPreVote(true)
            .setDisableForwardProposal(false)
            .setElectionTimeoutTick(5)
            .setHeartbeatTimeoutTick(3)
            .setInstallSnapshotTimeoutTick(5)
            .setMaxInflightAppends(3);
        // preVote && !leaderTransfer
        new RaftNodeStateCandidate(1, 0, raftConfig, stateStorage, log,
            new LinkedHashMap<>(),
            messages -> assertEquals(messages, new HashMap<String, List<RaftMessage>>() {{
                put("v1", Collections.singletonList(RaftMessage.newBuilder()
                    .setTerm(2)
                    .setRequestPreVote(RequestPreVote.newBuilder()
                        .setCandidateId(local)
                        .setLastLogTerm(0)
                        .setLastLogIndex(0)
                        .build())
                    .build()));
                put("v2", Collections.singletonList(RaftMessage.newBuilder()
                    .setTerm(2)
                    .setRequestPreVote(RequestPreVote.newBuilder()
                        .setCandidateId(local)
                        .setLastLogTerm(0)
                        .setLastLogIndex(0)
                        .build())
                    .build()));
            }}), eventListener, snapshotInstaller, onSnapshotInstalled);


        stateStorage = new InMemoryStateStore("testLocal", Snapshot.newBuilder()
            .setClusterConfig(clusterConfig).build());
        raftConfig = new RaftConfig()
            .setPreVote(false)
            .setDisableForwardProposal(false)
            .setElectionTimeoutTick(5)
            .setHeartbeatTimeoutTick(3)
            .setInstallSnapshotTimeoutTick(5)
            .setMaxInflightAppends(3);
        // !preVote && !leaderTransfer
        RaftNodeStateCandidate raftNodeStateCandidate = new RaftNodeStateCandidate(1, 0, raftConfig,
            stateStorage, log, new LinkedHashMap<>(),
            messages -> assertEquals(messages, new HashMap<String, List<RaftMessage>>() {{
                put("v1", Collections.singletonList(RaftMessage.newBuilder()
                    .setTerm(2)
                    .setRequestVote(RequestVote.newBuilder()
                        .setCandidateId(local)
                        .setLastLogTerm(0)
                        .setLastLogIndex(0)
                        .setLeaderTransfer(false)
                        .build())
                    .build()));
                put("v2", Collections.singletonList(RaftMessage.newBuilder()
                    .setTerm(2)
                    .setRequestVote(RequestVote.newBuilder()
                        .setCandidateId(local)
                        .setLastLogTerm(0)
                        .setLastLogIndex(0)
                        .setLeaderTransfer(false)
                        .build())
                    .build()));
            }}), eventListener, snapshotInstaller, onSnapshotInstalled);
        raftNodeStateCandidate.campaign(false, false);
        assertEquals(raftNodeStateCandidate.currentTerm(), 2);

        stateStorage = new InMemoryStateStore("testLocal", Snapshot.newBuilder()
            .setClusterConfig(clusterConfig).build());
        // !preVote && !leaderTransfer
        raftNodeStateCandidate = new RaftNodeStateCandidate(1, 0, raftConfig, stateStorage, log,
            new LinkedHashMap<>(),
            messages -> assertEquals(messages, new HashMap<String, List<RaftMessage>>() {{
                put("v1", Collections.singletonList(RaftMessage.newBuilder()
                    .setTerm(2)
                    .setRequestVote(RequestVote.newBuilder()
                        .setCandidateId(local)
                        .setLastLogTerm(0)
                        .setLastLogIndex(0)
                        .setLeaderTransfer(true)
                        .build())
                    .build()));
                put("v2", Collections.singletonList(RaftMessage.newBuilder()
                    .setTerm(2)
                    .setRequestVote(RequestVote.newBuilder()
                        .setCandidateId(local)
                        .setLastLogTerm(0)
                        .setLastLogIndex(0)
                        .setLeaderTransfer(true)
                        .build())
                    .build()));
            }}), eventListener, snapshotInstaller, onSnapshotInstalled);
        raftNodeStateCandidate.campaign(raftConfig.isPreVote(), true);
        assertEquals(raftNodeStateCandidate.currentTerm(), 2);
    }

    @Test
    public void testElectionElapsed() {
        IRaftStateStore stateStorage = new InMemoryStateStore("testLocal", Snapshot.newBuilder()
            .setClusterConfig(clusterConfig).build());
        RaftConfig raftConfig = new RaftConfig()
            .setPreVote(true)
            .setDisableForwardProposal(false)
            .setElectionTimeoutTick(5)
            .setHeartbeatTimeoutTick(3)
            .setInstallSnapshotTimeoutTick(5)
            .setMaxInflightAppends(3);

        AtomicInteger onMessageReadyIndex = new AtomicInteger();
        RaftNodeStateCandidate raftNodeStateCandidate = new RaftNodeStateCandidate(1, 0, raftConfig,
            stateStorage, log, new LinkedHashMap<>(),
            messages -> {
                if (onMessageReadyIndex.get() == 0) {
                    onMessageReadyIndex.incrementAndGet();
                    assertEquals(messages, new HashMap<String, List<RaftMessage>>() {{
                        put("v1", Collections.singletonList(RaftMessage.newBuilder()
                            .setTerm(2)
                            .setRequestPreVote(RequestPreVote.newBuilder()
                                .setCandidateId(local)
                                .setLastLogTerm(0)
                                .setLastLogIndex(0)
                                .build())
                            .build()));
                        put("v2", Collections.singletonList(RaftMessage.newBuilder()
                            .setTerm(2)
                            .setRequestPreVote(RequestPreVote.newBuilder()
                                .setCandidateId(local)
                                .setLastLogTerm(0)
                                .setLastLogIndex(0)
                                .build())
                            .build()));
                    }});
                } else if (onMessageReadyIndex.get() == 1) {
                    onMessageReadyIndex.incrementAndGet();
                    assertEquals(messages, new HashMap<String, List<RaftMessage>>() {{
                        put("v1", Collections.singletonList(RaftMessage.newBuilder()
                            .setTerm(2)
                            .setRequestPreVote(RequestPreVote.newBuilder()
                                .setCandidateId(local)
                                .setLastLogTerm(0)
                                .setLastLogIndex(0)
                                .build())
                            .build()));
                        put("v2", Collections.singletonList(RaftMessage.newBuilder()
                            .setTerm(2)
                            .setRequestPreVote(RequestPreVote.newBuilder()
                                .setCandidateId(local)
                                .setLastLogTerm(0)
                                .setLastLogIndex(0)
                                .build())
                            .build()));
                    }});
                } else if (onMessageReadyIndex.get() == 2) {
                    // !promotable
                    fail();
                }
            }, eventListener, snapshotInstaller, onSnapshotInstalled);

        raftNodeStateCandidate.tick();
        raftNodeStateCandidate.tick();
        raftNodeStateCandidate.tick();
        raftNodeStateCandidate.tick();
        raftNodeStateCandidate.tick();

        // change cluster config to not promotable
        stateStorage.append(Collections.singletonList(LogEntry.newBuilder()
            .setConfig(ClusterConfig.newBuilder().addAllVoters(Arrays.asList("v2", "v3")).build())
            .setTerm(1)
            .setIndex(stateStorage.lastIndex() + 1)
            .build()), false);
        raftNodeStateCandidate.tick();
        raftNodeStateCandidate.tick();
        raftNodeStateCandidate.tick();
        raftNodeStateCandidate.tick();
        raftNodeStateCandidate.tick();
    }

    @Test
    public void testPropose() {
        IRaftStateStore stateStorage = new InMemoryStateStore("testLocal", Snapshot.newBuilder()
            .setClusterConfig(clusterConfig).build());

        RaftNodeStateCandidate raftNodeStateCandidate = new RaftNodeStateCandidate(1, 0, defaultRaftConfig,
            stateStorage, log, new LinkedHashMap<>(), msgSender, eventListener, snapshotInstaller, onSnapshotInstalled);

        CompletableFuture<Void> onDone = new CompletableFuture<>();
        raftNodeStateCandidate.propose(ByteString.copyFromUtf8("command"), onDone);
        assertTrue(onDone.isCompletedExceptionally());
    }

    @Test
    public void testReadIndex() {
        IRaftStateStore stateStorage = new InMemoryStateStore("testLocal", Snapshot.newBuilder()
            .setClusterConfig(clusterConfig).build());

        RaftNodeStateCandidate raftNodeStateCandidate = new RaftNodeStateCandidate(1, 0, defaultRaftConfig,
            stateStorage, log, new LinkedHashMap<>(), msgSender, eventListener, snapshotInstaller, onSnapshotInstalled);

        CompletableFuture<Long> onDone = new CompletableFuture<>();
        raftNodeStateCandidate.readIndex(onDone);
        assertTrue(onDone.isCompletedExceptionally());
    }

    @Test
    public void testTransferLeadership() {
        IRaftStateStore stateStorage = new InMemoryStateStore("testLocal", Snapshot.newBuilder()
            .setClusterConfig(clusterConfig).build());

        RaftNodeStateCandidate raftNodeStateCandidate = new RaftNodeStateCandidate(1, 0, defaultRaftConfig,
            stateStorage, log, new LinkedHashMap<>(), msgSender, eventListener, snapshotInstaller, onSnapshotInstalled);

        CompletableFuture<Void> onDone = new CompletableFuture<>();
        raftNodeStateCandidate.transferLeadership("v1", onDone);
        assertTrue(onDone.isCompletedExceptionally());
    }

    @Test
    public void testChangeClusterConfig() {
        IRaftStateStore stateStorage = new InMemoryStateStore("testLocal", Snapshot.newBuilder()
            .setClusterConfig(clusterConfig).build());

        RaftNodeStateCandidate raftNodeStateCandidate = new RaftNodeStateCandidate(1, 0, defaultRaftConfig,
            stateStorage, log, new LinkedHashMap<>(), msgSender, eventListener, snapshotInstaller,
            onSnapshotInstalled);

        CompletableFuture<Void> onDone = new CompletableFuture<>();
        raftNodeStateCandidate.changeClusterConfig("cId",
            Collections.singleton("v3"),
            Collections.singleton("l4"),
            onDone);
        assertTrue(onDone.isCompletedExceptionally());
    }


    @Test
    public void testReceiveHigherTermMessage() {
        AtomicInteger onMessageReadyIndex = new AtomicInteger();
        IRaftStateStore stateStorage = new InMemoryStateStore("testLocal", Snapshot.newBuilder()
            .setClusterConfig(clusterConfig).build());

        RaftNodeStateCandidate raftNodeStateCandidate = new RaftNodeStateCandidate(1, 0, defaultRaftConfig,
            stateStorage, log, new LinkedHashMap<>(),
            messages -> {
                if (onMessageReadyIndex.get() == 1) {
                    onMessageReadyIndex.incrementAndGet();
                    onMessageReadyIndex.incrementAndGet();
                    assertEquals(messages, new HashMap<String, List<RaftMessage>>() {{
                        put("v1", Collections.singletonList(RaftMessage.newBuilder()
                            .setTerm(2)
                            .setRequestPreVoteReply(RequestPreVoteReply.newBuilder()
                                .setVoteCouldGranted(true)
                                .build())
                            .build()));
                    }});
                }
            }, eventListener, snapshotInstaller, onSnapshotInstalled);

        RaftMessage preVote = RaftMessage.newBuilder()
            .setTerm(2)
            .setRequestPreVote(RequestPreVote.newBuilder()
                .setLastLogTerm(1)
                .setLastLogIndex(1)
                .build())
            .build();
        RaftNodeState raftNodeState = raftNodeStateCandidate.receive("v1", preVote);
        assertSame(raftNodeState.getState(), RaftNodeStatus.Candidate);

        raftNodeStateCandidate = new RaftNodeStateCandidate(1, 0, defaultRaftConfig,
            stateStorage, log, new LinkedHashMap<>(),
            messages -> {
                if (onMessageReadyIndex.get() == 1) {
                    onMessageReadyIndex.incrementAndGet();
                    assertEquals(messages, new HashMap<String, List<RaftMessage>>() {{
                        put("v1", Collections.singletonList(RaftMessage.newBuilder()
                            .setTerm(2)
                            .setRequestPreVoteReply(RequestPreVoteReply.newBuilder()
                                .setVoteCouldGranted(true)
                                .build())
                            .build()));
                    }});
                }
            }, eventListener, snapshotInstaller, onSnapshotInstalled);

        RaftMessage appendEntries = RaftMessage.newBuilder()
            .setTerm(2)
            .setAppendEntries(AppendEntries.newBuilder()
                .setLeaderId("v1")
                .build())
            .build();
        raftNodeState = raftNodeStateCandidate.receive("v1", appendEntries);
        assertSame(raftNodeState.getState(), RaftNodeStatus.Follower);


    }

    @Test
    public void testReceivePreVoteReplyWon() {
        AtomicInteger onMessageReadyIndex = new AtomicInteger();
        IRaftStateStore stateStorage = new InMemoryStateStore("testLocal", Snapshot.newBuilder()
            .setClusterConfig(clusterConfig).build());

        RaftNodeStateCandidate raftNodeStateCandidate = new RaftNodeStateCandidate(1, 0, defaultRaftConfig,
            stateStorage, log, new LinkedHashMap<>(),
            messages -> {
                if (onMessageReadyIndex.get() == 0) {
                    onMessageReadyIndex.incrementAndGet();
                    // preVoteRequests
                } else if (onMessageReadyIndex.get() == 1) {
                    onMessageReadyIndex.incrementAndGet();
                    assertEquals(messages, new HashMap<String, List<RaftMessage>>() {
                        {
                            put("v1", Collections.singletonList(RaftMessage.newBuilder()
                                .setTerm(2)
                                .setRequestVote(RequestVote.newBuilder()
                                    .setCandidateId(local)
                                    .setLastLogTerm(0)
                                    .setLastLogIndex(0)
                                    .setLeaderTransfer(false)
                                    .build())
                                .build()));
                            put("v2", Collections.singletonList(RaftMessage.newBuilder()
                                .setTerm(2)
                                .setRequestVote(RequestVote.newBuilder()
                                    .setCandidateId(local)
                                    .setLastLogTerm(0)
                                    .setLastLogIndex(0)
                                    .setLeaderTransfer(false)
                                    .build())
                                .build()));
                        }
                    });
                }
            }, eventListener, snapshotInstaller, onSnapshotInstalled);
        RaftMessage preVoteReplyGranted = RaftMessage.newBuilder()
            .setTerm(2)
            .setRequestPreVoteReply(RequestPreVoteReply.newBuilder()
                .setVoteCouldGranted(true)
                .build())
            .build();
        raftNodeStateCandidate.receive("v1", preVoteReplyGranted);
    }

    @Test
    public void testReceivePreVoteReplyLost() {
        IRaftStateStore stateStorage = new InMemoryStateStore("testLocal", Snapshot.newBuilder()
            .setClusterConfig(clusterConfig).build());

        RaftNodeStateCandidate raftNodeStateCandidate = new RaftNodeStateCandidate(1, 0, defaultRaftConfig,
            stateStorage, log, new LinkedHashMap<>(), msgSender, eventListener, snapshotInstaller, onSnapshotInstalled);

        RaftMessage preVoteReplyRejected = RaftMessage.newBuilder()
            .setTerm(2)
            .setRequestPreVoteReply(RequestPreVoteReply.newBuilder()
                .setVoteCouldGranted(false)
                .build())
            .build();
        raftNodeStateCandidate.receive("v1", preVoteReplyRejected);
        RaftNodeState raftNodeState = raftNodeStateCandidate.receive("v2", preVoteReplyRejected);

        // keep in candidate state when pre-vote lost
        assertSame(raftNodeState.getState(), RaftNodeStatus.Candidate);
        assertEquals(raftNodeState.currentTerm(), 1);
        assertNull(raftNodeState.currentLeader());
    }

    @Test
    public void testReceiveMatchedTermAppendEntriesAndInstallSnapshot() {
        IRaftStateStore stateStorage = new InMemoryStateStore("testLocal", Snapshot.newBuilder()
            .setClusterConfig(clusterConfig).build());

        RaftNodeStateCandidate raftNodeStateCandidate =
            new RaftNodeStateCandidate(1, 0, defaultRaftConfig, stateStorage,
                log, new LinkedHashMap<>(), msgSender, eventListener, snapshotInstaller, onSnapshotInstalled);

        RaftMessage appendEntries = RaftMessage.newBuilder()
            .setTerm(1)
            .setAppendEntries(AppendEntries.newBuilder()
                .setLeaderId("v1")
                .setPrevLogTerm(1)
                .setPrevLogIndex(1)
                .setCommitIndex(0)
                .setReadIndex(-1)
                .build())
            .build();
        RaftNodeState raftNodeState = raftNodeStateCandidate.receive("v1", appendEntries);
        assertSame(raftNodeState.getState(), RaftNodeStatus.Follower);
        assertEquals(raftNodeState.currentLeader(), "v1");

        stateStorage = new InMemoryStateStore("testLocal", Snapshot.newBuilder()
            .setClusterConfig(clusterConfig).build());

        raftNodeStateCandidate = new RaftNodeStateCandidate(1, 0, defaultRaftConfig, stateStorage,
            log, new LinkedHashMap<>(), msgSender, eventListener, snapshotInstaller, onSnapshotInstalled);

        when(snapshotInstaller.install(any(ByteString.class))).thenReturn(new CompletableFuture<>());
        RaftMessage installSnapshot = RaftMessage.newBuilder()
            .setTerm(1)
            .setInstallSnapshot(InstallSnapshot.newBuilder()
                .setLeaderId("v1")
                .build())
            .build();
        raftNodeState = raftNodeStateCandidate.receive("v1", installSnapshot);
        assertSame(raftNodeState.getState(), RaftNodeStatus.Follower);
        assertEquals(raftNodeState.currentLeader(), "v1");
    }

    @Test
    public void testReceiveVote() {
        AtomicInteger onMessageReadyIndex = new AtomicInteger();
        IRaftStateStore stateStorage = new InMemoryStateStore("testLocal", Snapshot.newBuilder()
            .setClusterConfig(clusterConfig).build());

        RaftNodeStateCandidate raftNodeStateCandidate = new RaftNodeStateCandidate(1, 0, defaultRaftConfig,
            stateStorage, log, new LinkedHashMap<>(),
            messages -> {
                if (onMessageReadyIndex.get() == 0) {
                    onMessageReadyIndex.incrementAndGet();
                    // preVoteRequests
                } else if (onMessageReadyIndex.get() == 1) {
                    onMessageReadyIndex.incrementAndGet();
                    assertEquals(messages, new HashMap<String, List<RaftMessage>>() {{
                        put("v1", Collections.singletonList(RaftMessage.newBuilder()
                            .setTerm(1)
                            .setRequestVoteReply(RequestVoteReply.newBuilder()
                                .setVoteGranted(false)
                                .build())
                            .build()));
                    }});
                }
            }, eventListener, snapshotInstaller, onSnapshotInstalled);

        RaftMessage vote = RaftMessage.newBuilder()
            .setTerm(1)
            .setRequestVote(RequestVote.newBuilder()
                .build())
            .build();
        RaftNodeState raftNodeState = raftNodeStateCandidate.receive("v1", vote);
        assertSame(raftNodeState.getState(), RaftNodeStatus.Candidate);
    }

    @Test
    public void testReceiveVoteReplyWon() {
        RaftConfig raftConfig = new RaftConfig()
            .setPreVote(false)
            .setDisableForwardProposal(false)
            .setElectionTimeoutTick(5)
            .setHeartbeatTimeoutTick(3)
            .setInstallSnapshotTimeoutTick(5)
            .setMaxInflightAppends(3);
        IRaftStateStore stateStorage = new InMemoryStateStore("testLocal", Snapshot.newBuilder()
            .setClusterConfig(clusterConfig).build());

        RaftNodeStateCandidate raftNodeStateCandidate = new RaftNodeStateCandidate(1, 0, raftConfig, stateStorage,
            log, new LinkedHashMap<>(), msgSender, eventListener, snapshotInstaller, onSnapshotInstalled);
        raftNodeStateCandidate.campaign(false, false);

        RaftMessage voteReply = RaftMessage.newBuilder()
            .setTerm(2)
            .setRequestVoteReply(RequestVoteReply.newBuilder()
                .setVoteGranted(true)
                .build())
            .build();
        RaftNodeState raftNodeState = raftNodeStateCandidate.receive("v1", voteReply);
        assertSame(raftNodeState.getState(), RaftNodeStatus.Leader);
    }

    @Test
    public void testReceiveVoteReplyLost() {
        RaftConfig raftConfig = new RaftConfig()
            .setPreVote(false)
            .setDisableForwardProposal(false)
            .setElectionTimeoutTick(5)
            .setHeartbeatTimeoutTick(3)
            .setInstallSnapshotTimeoutTick(5)
            .setMaxInflightAppends(3);
        IRaftStateStore stateStorage = new InMemoryStateStore("testLocal", Snapshot.newBuilder()
            .setClusterConfig(clusterConfig).build());

        RaftNodeStateCandidate raftNodeStateCandidate = new RaftNodeStateCandidate(1, 0,
            raftConfig, stateStorage, log, new LinkedHashMap<>(), msgSender, eventListener, snapshotInstaller,
            onSnapshotInstalled);
        raftNodeStateCandidate.campaign(false, false);

        RaftMessage voteReplyNoGranted = RaftMessage.newBuilder()
            .setTerm(2)
            .setRequestVoteReply(RequestVoteReply.newBuilder()
                .setVoteGranted(false)
                .build())
            .build();
        RaftNodeState raftNodeState = raftNodeStateCandidate.receive("v1", voteReplyNoGranted);
        assertSame(raftNodeState.getState(), RaftNodeStatus.Candidate);
        raftNodeState = raftNodeStateCandidate.receive("v2", voteReplyNoGranted);
        assertSame(raftNodeState.getState(), RaftNodeStatus.Follower);
    }
}