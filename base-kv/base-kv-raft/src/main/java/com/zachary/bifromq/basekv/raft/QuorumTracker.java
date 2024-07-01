package com.zachary.bifromq.basekv.raft;


import com.zachary.bifromq.basekv.raft.proto.ClusterConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


class QuorumTracker {
    enum VoteResult {
        Won, Lost, Pending
    }

    enum TallyResult {
        Yes, No, Miss
    }

    static class VoteGroupResult {
        final VoteResult result;
        final int yes;
        final int no;
        final int miss;

        private VoteGroupResult(int voters, int yes, int no, int miss) {
            this.yes = yes;
            this.no = no;
            this.miss = miss;
            if (voters == 0) {
                this.result = VoteResult.Won;
            } else {
                int quorum = voters / 2 + 1;
                if (yes >= quorum) {
                    this.result = VoteResult.Won;
                } else if (yes + miss >= quorum) {
                    this.result = VoteResult.Pending;
                } else {
                    this.result = VoteResult.Lost;
                }
            }
        }

        @Override
        public String toString() {
            return "VoteGroupResult{" +
                "result=" + result +
                ", yes=" + yes +
                ", no=" + no +
                ", miss=" + miss +
                '}';
        }
    }

    static class JointVoteResult {
        final VoteResult result;
        final VoteGroupResult groupOneResult;
        final VoteGroupResult groupTwoResult;

        JointVoteResult(VoteGroupResult groupOneResult, VoteGroupResult groupTwoResult) {
            this.groupOneResult = groupOneResult;
            this.groupTwoResult = groupTwoResult;
            if (groupOneResult.result == groupTwoResult.result) {
                this.result = groupOneResult.result;
            } else if (groupOneResult.result == VoteResult.Lost || groupTwoResult.result == VoteResult.Lost) {
                this.result = VoteResult.Lost;
            } else {
                this.result = VoteResult.Pending;
            }
        }

        @Override
        public String toString() {
            return "JointVoteResult{" +
                "result=" + result +
                ", groupOneResult=" + groupOneResult +
                ", groupTwoResult=" + groupTwoResult +
                '}';
        }
    }

    private final Set<String> voterGroupOne = new HashSet<>();
    private final Set<String> voterGroupTwo = new HashSet<>(); // non empty in joint config
    private final Map<String, Boolean> votes = new HashMap<>();
    private final IRaftNodeLogger logger;

    QuorumTracker(ClusterConfig clusterConfig, IRaftNodeLogger logger) {
        voterGroupOne.addAll(clusterConfig.getVotersList());
        voterGroupTwo.addAll(clusterConfig.getNextVotersList());
        this.logger = logger;
    }

    void refresh(ClusterConfig clusterConfig) {
        logger.logDebug("Quorum tracker reset to config[v:{},nv:{}]",
            clusterConfig.getVotersList(), clusterConfig.getNextVotersList());
        voterGroupOne.clear();
        voterGroupOne.addAll(clusterConfig.getVotersList());
        voterGroupTwo.clear();
        voterGroupTwo.addAll(clusterConfig.getNextVotersList());
        // only refresh the voter group, don't reset the polls already received and let caller decide.
        // In our case caller wants to leave existing polls intact.
    }

    void reset() {
        votes.clear();
    }

    void poll(String voter, boolean vote) {
        votes.put(voter, vote);
    }

    TallyResult tally(String voter) {
        if (votes.containsKey(voter)) {
            if (votes.get(voter)) {
                return TallyResult.Yes;
            } else {
                return TallyResult.No;
            }
        } else {
            return TallyResult.Miss;
        }
    }

    JointVoteResult tally() {
        VoteGroupResult groupOneResult = tally(voterGroupOne);
        VoteGroupResult groupTwoResult = tally(voterGroupTwo);
        return new JointVoteResult(groupOneResult, groupTwoResult);
    }

    VoteGroupResult tally(Set<String> voters) {
        int miss = 0;
        int yes = 0;
        int no = 0;
        for (String voter : voters) {
            switch (tally(voter)) {
                case Yes:
                    yes++;
                    break;
                case No:
                    no++;
                    break;
                case Miss:
                    miss++;
                    break;
            }
        }
        return new VoteGroupResult(voters.size(), yes, no, miss);
    }
}
