package com.zachary.bifromq.basekv.raft.functest;

import com.zachary.bifromq.basekv.raft.functest.annotation.Cluster;
import com.zachary.bifromq.basekv.raft.functest.annotation.Config;
import com.zachary.bifromq.basekv.raft.functest.template.RaftGroupTestListener;
import com.zachary.bifromq.basekv.raft.functest.template.SharedRaftConfigTestTemplate;
import com.zachary.bifromq.basekv.raft.proto.RaftNodeSyncState;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;

import static org.awaitility.Awaitility.await;
import static org.testng.Assert.assertTrue;

@Slf4j
@Listeners(RaftGroupTestListener.class)
public class AbnormalTest extends SharedRaftConfigTestTemplate {

    @Cluster
    @Config(maxInflightAppends = 1024)
    @Test(groups = "integration")
    public void testIsolateFollowerAndRepStatusChangeEvent() {
        String leader = group.currentLeader().get();
        assertTrue(group.awaitIndexCommitted(leader, 1));
        String isolatedFollower = group.currentFollowers().get(0);
        await().until(
            () -> RaftNodeSyncState.Replicating.equals(group.latestReplicationStatus(isolatedFollower)));
        log.info("Isolate {}", isolatedFollower);
        group.isolate(isolatedFollower);
        await().until(() -> RaftNodeSyncState.Probing.equals(group.latestReplicationStatus(isolatedFollower)));
    }

    @Cluster
    @Config(maxInflightAppends = 1024)
    @Test(groups = "integration")
    public void testIsolateOneFollowerAndRecover() {
        String leader = group.currentLeader().get();
        assertTrue(group.awaitIndexCommitted(leader, 1));
        String isolatedFollower = group.currentFollowers().get(0);
        log.info("Isolate {}", isolatedFollower);
        group.isolate(isolatedFollower);
        await().atMost(Duration.ofSeconds(5)).until(() ->
            Arrays.asList(RaftNodeSyncState.Probing, RaftNodeSyncState.Replicating,
                    RaftNodeSyncState.Probing)
                .equals(group.syncStateLogs(isolatedFollower)));
        log.info("Restore {} from isolation", isolatedFollower);
        group.recoverNetwork();
        await().atMost(Duration.ofSeconds(5)).until(() ->
            Arrays.asList(RaftNodeSyncState.Probing, RaftNodeSyncState.Replicating,
                    RaftNodeSyncState.Probing, RaftNodeSyncState.Replicating)
                .equals(group.syncStateLogs(isolatedFollower)));
    }

    @Cluster
    @Config(maxInflightAppends = 1024)
    @Test(groups = "integration")
    public void testIsolateTwoFollowersAndRecover() {
        String leader = group.currentLeader().get();
        assertTrue(group.awaitIndexCommitted(leader, 1));
        String isolatedFollowerOne = group.currentFollowers().get(0);
        String isolatedFollowerTwo = group.currentFollowers().get(1);
        log.info("Isolate {}", isolatedFollowerOne);
        group.isolate(isolatedFollowerOne);
        await().atMost(Duration.ofSeconds(5)).until(
            () -> Arrays.asList(RaftNodeSyncState.Probing, RaftNodeSyncState.Replicating,
                    RaftNodeSyncState.Probing)
                .equals(group.syncStateLogs(isolatedFollowerOne)));

        log.info("Isolate {}", isolatedFollowerTwo);
        group.isolate(isolatedFollowerTwo);
        // split-brain now
        await().until(() -> !group.currentLeader().isPresent());

        log.info("Integrate {} into cluster", isolatedFollowerTwo);
        group.integrate(isolatedFollowerTwo);
        // split-brain resolved
        await().until(() -> group.currentLeader().isPresent());
        // the commit index must be confirmed again in new term when leader re-elected
        assertTrue(group.awaitIndexCommitted(group.currentLeader().get(), 2));

        log.info("Integrate {} into cluster", isolatedFollowerOne);
        group.integrate(isolatedFollowerOne);
        assertTrue(group.awaitIndexCommitted(isolatedFollowerOne, 2));

        await().until(() -> group.currentFollowers().size() == 2);
        await().until(() -> RaftNodeSyncState.Replicating ==
            group.latestReplicationStatus(group.currentFollowers().get(0)));
        await().until(() -> RaftNodeSyncState.Replicating ==
            group.latestReplicationStatus(group.currentFollowers().get(1)));
        await().until(
            () -> RaftNodeSyncState.Replicating == group.latestReplicationStatus(group.currentLeader().get()));
    }
}
