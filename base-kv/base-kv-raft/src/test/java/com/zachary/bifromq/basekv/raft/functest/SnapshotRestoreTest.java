package com.zachary.bifromq.basekv.raft.functest;

import com.zachary.bifromq.basekv.raft.event.SnapshotRestoredEvent;
import com.zachary.bifromq.basekv.raft.functest.annotation.Cluster;
import com.zachary.bifromq.basekv.raft.functest.template.RaftGroupTestListener;
import com.zachary.bifromq.basekv.raft.functest.template.SharedRaftConfigTestTemplate;
import com.zachary.bifromq.basekv.raft.proto.ClusterConfig;
import com.google.protobuf.ByteString;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners(RaftGroupTestListener.class)
public class SnapshotRestoreTest extends SharedRaftConfigTestTemplate {
    @Cluster(v = "V1")
    @Test(groups = "integration")
    public void snapshotRestoredEventAfterSnapshotInstalled() {
        String leader = group.currentLeader().get();
        group.compact("V1", ByteString.EMPTY, 1).join();
        group.propose("V1", ByteString.copyFromUtf8("Value1")).join();
        group.awaitIndexCommitted("V1", 2);

        group.addRaftNode("V2", 0, 0, ClusterConfig.newBuilder().addVoters("V4").build(), raftConfig());
        group.connect("V2");
        Set<String> newVoters = new HashSet<String>() {{
            add("V1");
            add("V2");
        }};
        group.changeClusterConfig(leader, "cId", newVoters, Collections.emptySet()).join();
        for (String peerId : newVoters) {
            assertTrue(group.awaitIndexCommitted(peerId, 3));
        }
        List<SnapshotRestoredEvent> events = group.snapshotRestoredLogs("V2");
        assertEquals(events.size(), 1);
        assertEquals(events.get(0).nodeId, "V2");
        assertTrue(events.get(0).snapshot.getClusterConfig().getVotersList().contains("V1"));
    }
}
