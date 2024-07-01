package com.zachary.bifromq.basekv.raft.functest.template;

import com.zachary.bifromq.basekv.raft.RaftConfig;
import com.zachary.bifromq.basekv.raft.functest.RaftNodeGroup;
import com.zachary.bifromq.basekv.raft.functest.annotation.Config;
import com.zachary.bifromq.basekv.raft.functest.annotation.Ticker;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.basekv.raft.functest.RaftNodeGroup.RaftNodeTickMagnitude;
import static org.awaitility.Awaitility.await;
import static org.testng.Assert.assertTrue;

@Slf4j
public abstract class SharedRaftConfigTestTemplate extends RaftGroupTestTemplate {
    private final RaftConfig defaultRaftConfig = RaftNodeGroup.DefaultRaftConfig;
    protected RaftNodeGroup group;
    private RaftConfig raftConfigInUse;
    private boolean disableTick = false;
    private int tickInMS = 10;

    @Override
    protected void startingTest(Method testMethod) {
        Config config = testMethod.getAnnotation(Config.class);
        Ticker ticker = testMethod.getAnnotation(Ticker.class);
        raftConfigInUse = config == null ? defaultRaftConfig : build(config);
        if (ticker != null) {
            tickInMS = ticker.unitInMS();
            disableTick = ticker.disable();
        } else {
            tickInMS = 10;
            disableTick = false;
        }
        setup();
    }

    public final void setup() {
        log.info("Setup a test raft group: v={}, l={}, nv={}, nl={}",
            clusterConfig().getVotersList(),
            clusterConfig().getLearnersList(),
            clusterConfig().getNextVotersList(),
            clusterConfig().getNextLearnersList());
        group = new RaftNodeGroup(clusterConfig(), raftConfigInUse);
        if (!disableTick) {
            group.run(tickInMS, TimeUnit.MILLISECONDS);
            await().forever().until(() -> group.currentLeader().isPresent());
            String leader = group.currentLeader().get();
            assertTrue(group.awaitIndexCommitted(leader, 1));
            log.info("Leader {} elected", leader);
        }
    }

    @AfterMethod(alwaysRun = true)
    public final void teardown() {
        log.info("Stop the test raft group");
        group.shutdown();
        raftConfigInUse = null;
    }

    public final RaftConfig raftConfig() {
        return raftConfigInUse;
    }

    public final int ticks(int electionRound) {
        return electionRound * raftConfig().getElectionTimeoutTick() * RaftNodeTickMagnitude;
    }

    private RaftConfig build(Config c) {
        return new RaftConfig()
            .setAsyncAppend(c.asyncAppend())
            .setDisableForwardProposal(c.disableForwardProposal())
            .setElectionTimeoutTick(c.electionTimeoutTick())
            .setHeartbeatTimeoutTick(c.heartbeatTimeoutTick())
            .setInstallSnapshotTimeoutTick(c.installSnapshotTimeoutTick())
            .setMaxInflightAppends(c.maxInflightAppends())
            .setMaxSizePerAppend(c.maxSizePerAppend())
            .setPreVote(c.preVote())
            .setReadOnlyBatch(c.readOnlyBatch())
            .setReadOnlyLeaderLeaseMode(c.readOnlyLeaderLeaseMode());
    }
}
