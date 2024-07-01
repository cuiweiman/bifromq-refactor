package com.zachary.bifromq.basekv.raft.functest;

import com.zachary.bifromq.basekv.raft.RaftConfig;
import com.zachary.bifromq.basekv.raft.functest.template.RaftGroupTestListener;
import com.zachary.bifromq.basekv.raft.functest.template.RaftGroupTestTemplate;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.basekv.raft.functest.RaftNodeGroup.DefaultRaftConfig;
import static org.awaitility.Awaitility.await;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

@Slf4j
@Listeners(RaftGroupTestListener.class)
public class RaftConfigMigrationTest extends RaftGroupTestTemplate {
    @Test(groups = "integration")
    public void testLeaderElectionWithPreVoteMigration() {
        RaftConfig raftConfigWithPreVote = DefaultRaftConfig.toBuilder().preVote(true).build();
        RaftConfig raftConfigWithoutPreVote = DefaultRaftConfig.toBuilder().preVote(false).build();
        Map<String, RaftConfig> raftConfigs = new HashMap<String, RaftConfig>() {{
            put("V1", raftConfigWithPreVote);
            put("V2", raftConfigWithPreVote);
            put("V3", raftConfigWithoutPreVote);
        }};
        RaftNodeGroup group = new RaftNodeGroup(clusterConfig(), raftConfigs);
        // enough ticks for leader election
        group.run(10, TimeUnit.MILLISECONDS);
        await().until(() -> group.currentLeader().isPresent());
        String leader = group.currentLeader().get();
        assertTrue(group.awaitIndexCommitted(leader, 1));
        log.info("Leader {} elected", leader);

        String blockedFollower = group.currentFollowers().get(0);
        log.info("Follower {} isolating", blockedFollower);
        group.isolate(blockedFollower);
        group.propose(leader, ByteString.copyFromUtf8("appCommand1"));
        group.propose(leader, ByteString.copyFromUtf8("appCommand2"));
        assertTrue(group.awaitIndexCommitted(leader, 3));
        assertTrue(group.awaitIndexCommitted(group.currentFollowers().get(1), 3));

        // isolate leader, V3 and another follower will campaign, but V3 will not elected since that it has less logs
        group.recoverNetwork();
        log.info("Unblock {} and isolating Leader {}", blockedFollower, leader);
        group.isolate(leader);
        await().until(() -> group.currentLeader().isPresent() && !leader.equals(group.currentLeader().get()));
        String newLeader = group.currentLeader().get();
        log.info("New leader {} elected", newLeader);
        assertNotEquals(newLeader, leader);
        assertNotEquals(newLeader, blockedFollower);
    }
}
