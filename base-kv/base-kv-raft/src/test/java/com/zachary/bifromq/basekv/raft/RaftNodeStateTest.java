package com.zachary.bifromq.basekv.raft;

import com.zachary.bifromq.basekv.raft.proto.ClusterConfig;
import com.google.protobuf.ByteString;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;

public abstract class RaftNodeStateTest {
    @Mock
    protected IRaftNode.IRaftMessageSender msgSender;
    @Mock
    protected IRaftNode.ISnapshotInstaller snapshotInstaller;
    @Mock
    protected IRaftNode.IRaftEventListener eventListener;
    @Mock
    protected RaftNodeState.OnSnapshotInstalled onSnapshotInstalled;

    @Mock
    protected IRaftStateStore raftStateStorage;

    protected String local = "testLocal";
    protected String testCandidateId = "testCandidateId";
    protected ByteString command = ByteString.copyFromUtf8("command");

    protected RaftConfig defaultRaftConfig = new RaftConfig()
        .setPreVote(true)
        .setDisableForwardProposal(false)
        .setElectionTimeoutTick(5)
        .setHeartbeatTimeoutTick(3)
        .setInstallSnapshotTimeoutTick(5)
        .setMaxUncommittedProposals(4)
        .setMaxInflightAppends(3)
        .setAsyncAppend(false);
    protected ClusterConfig clusterConfig = ClusterConfig.newBuilder()
        .addAllVoters(Arrays.asList(local, "v1", "v2"))
        .addAllLearners(Collections.singleton("l1"))
        .build();
}