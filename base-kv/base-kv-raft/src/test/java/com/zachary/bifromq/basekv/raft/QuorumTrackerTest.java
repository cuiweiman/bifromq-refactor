package com.zachary.bifromq.basekv.raft;

import com.zachary.bifromq.basekv.raft.proto.ClusterConfig;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class QuorumTrackerTest {
    @Mock
    IRaftNodeLogger logger;
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
    public void testNonJointQuorumWithOddVoters() {
        ClusterConfig clusterConfig = ClusterConfig.newBuilder()
            .addVoters("V1")
            .addVoters("V2")
            .addVoters("V3")
            .build();
        QuorumTracker quorumTracker = new QuorumTracker(clusterConfig, logger);

        assertEquals(quorumTracker.tally().result, QuorumTracker.VoteResult.Pending);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Pending, 0, 0, 3);

        quorumTracker.reset();
        quorumTracker.poll("V1", true);
        assertEquals(quorumTracker.tally().result, QuorumTracker.VoteResult.Pending);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Pending, 1, 0, 2);

        quorumTracker.reset();
        quorumTracker.poll("V1", true);
        quorumTracker.poll("V2", true);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Won, 2, 0, 1);

        quorumTracker.reset();
        quorumTracker.poll("V1", true);
        quorumTracker.poll("V2", true);
        quorumTracker.poll("V3", true);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Won, 3, 0, 0);

        quorumTracker.reset();
        quorumTracker.poll("V1", true);
        quorumTracker.poll("V2", false);
        quorumTracker.poll("V3", true);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Won, 2, 1, 0);

        quorumTracker.reset();
        quorumTracker.poll("V1", false);
        quorumTracker.poll("V2", false);
        quorumTracker.poll("V3", true);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Lost, 1, 2, 0);

        quorumTracker.reset();
        quorumTracker.poll("V1", false);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Pending, 0, 1, 2);

        quorumTracker.reset();
        quorumTracker.poll("V1", false);
        quorumTracker.poll("V2", false);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Lost, 0, 2, 1);

        quorumTracker.reset();
        quorumTracker.poll("V1", false);
        quorumTracker.poll("V2", false);
        quorumTracker.poll("V3", false);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Lost, 0, 3, 0);
    }

    @Test
    public void testNonJointQuorumWithEvenVoters() {
        ClusterConfig clusterConfig = ClusterConfig.newBuilder()
            .addVoters("V1")
            .addVoters("V2")
            .build();
        QuorumTracker quorumTracker = new QuorumTracker(clusterConfig, logger);

        assertEquals(quorumTracker.tally().result, QuorumTracker.VoteResult.Pending);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Pending, 0, 0, 2);

        quorumTracker.reset();
        quorumTracker.poll("V1", true);
        assertEquals(quorumTracker.tally().result, QuorumTracker.VoteResult.Pending);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Pending, 1, 0, 1);

        quorumTracker.reset();
        quorumTracker.poll("V1", true);
        quorumTracker.poll("V2", true);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Won, 2, 0, 0);

        quorumTracker.reset();
        quorumTracker.poll("V1", true);
        quorumTracker.poll("V2", false);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Lost, 1, 1, 0);

        quorumTracker.reset();
        quorumTracker.poll("V1", false);
        quorumTracker.poll("V2", false);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Lost, 0, 2, 0);

        quorumTracker.reset();
        quorumTracker.poll("V1", false);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Lost, 0, 1, 1);
    }

    @Test
    public void testInvalidVote() {
        ClusterConfig clusterConfig = ClusterConfig.newBuilder()
            .addVoters("V1")
            .addVoters("V2")
            .build();
        QuorumTracker quorumTracker = new QuorumTracker(clusterConfig, logger);
        quorumTracker.poll("Fake", true);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Pending, 0, 0, 2);
    }

    @Test
    public void testEmptyQuorum() {
        ClusterConfig clusterConfig = ClusterConfig.newBuilder().build();
        QuorumTracker quorumTracker = new QuorumTracker(clusterConfig, logger);
        assertEquals(quorumTracker.tally().result, QuorumTracker.VoteResult.Won);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Won, 0, 0, 0);
        verifyVoteGroupResult(quorumTracker.tally().groupTwoResult, QuorumTracker.VoteResult.Won, 0, 0, 0);
    }

    @Test
    public void testJointQuorum() {
        ClusterConfig clusterConfig = ClusterConfig.newBuilder()
            .addVoters("V1")
            .addVoters("V2")
            .addVoters("V3")
            .addNextVoters("N1")
            .addNextVoters("N2")
            .addNextVoters("N3")
            .build();
        QuorumTracker quorumTracker = new QuorumTracker(clusterConfig, logger);
        quorumTracker.poll("V1", true); // pending
        quorumTracker.poll("N1", true); // pending
        assertEquals(quorumTracker.tally().result, QuorumTracker.VoteResult.Pending);

        quorumTracker.reset();
        quorumTracker.poll("V1", true);
        quorumTracker.poll("V2", true); // won
        quorumTracker.poll("N1", true); // pending
        assertEquals(quorumTracker.tally().result, QuorumTracker.VoteResult.Pending);

        quorumTracker.reset();
        quorumTracker.poll("V1", true);
        quorumTracker.poll("V2", true); // won
        quorumTracker.poll("N1", true);
        quorumTracker.poll("N2", true); // won
        assertEquals(quorumTracker.tally().result, QuorumTracker.VoteResult.Won);

        quorumTracker.reset();
        quorumTracker.poll("V1", true);
        quorumTracker.poll("V2", true); // won
        quorumTracker.poll("N1", false);
        quorumTracker.poll("N2", false); // lost
        assertEquals(quorumTracker.tally().result, QuorumTracker.VoteResult.Lost);

        quorumTracker.reset();
        quorumTracker.poll("V1", false);
        quorumTracker.poll("V2", false); // lost
        quorumTracker.poll("N1", false);
        quorumTracker.poll("N2", false); // lost
        assertEquals(quorumTracker.tally().result, QuorumTracker.VoteResult.Lost);
    }

    @Test
    public void testRefresh() {
        ClusterConfig clusterConfig = ClusterConfig.newBuilder()
            .addVoters("V1")
            .addVoters("V2")
            .addVoters("V3")
            .addNextVoters("N1")
            .addNextVoters("N2")
            .addNextVoters("N3")
            .build();
        QuorumTracker quorumTracker = new QuorumTracker(clusterConfig, logger);
        quorumTracker.poll("V1", true); // pending
        quorumTracker.poll("N1", true); // pending
        assertEquals(quorumTracker.tally().result, QuorumTracker.VoteResult.Pending);

        ClusterConfig clusterConfig1 = ClusterConfig.newBuilder()
            .addVoters("V1")
            .build();
        quorumTracker.refresh(clusterConfig1);
        assertEquals(quorumTracker.tally().result, QuorumTracker.VoteResult.Won);
        verifyVoteGroupResult(quorumTracker.tally().groupOneResult, QuorumTracker.VoteResult.Won, 1, 0, 0);
    }

    private void verifyVoteGroupResult(QuorumTracker.VoteGroupResult voteGroupResult,
                                       QuorumTracker.VoteResult result,
                                       int yes,
                                       int no,
                                       int missing) {

        assertEquals(voteGroupResult.result, result);
        assertEquals(voteGroupResult.yes, yes);
        assertEquals(voteGroupResult.no, no);
        assertEquals(voteGroupResult.miss, missing);
    }
}
