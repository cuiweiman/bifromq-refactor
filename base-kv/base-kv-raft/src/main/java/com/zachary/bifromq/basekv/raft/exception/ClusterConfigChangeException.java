package com.zachary.bifromq.basekv.raft.exception;

public class ClusterConfigChangeException extends RuntimeException {
    public static final ClusterConfigChangeException CONCURRENT_CHANGE =
        new ClusterConfigChangeException("Only one on-going change is allowed");
    public static final ClusterConfigChangeException EMPTY_VOTERS =
        new ClusterConfigChangeException("Voters can not be empty");
    public static final ClusterConfigChangeException LEARNERS_OVERLAP =
        new ClusterConfigChangeException("Learners must not overlap voters");
    public static final ClusterConfigChangeException SLOW_LEARNER =
        new ClusterConfigChangeException("Some new added servers are too slow to catch up leader's progress");
    public static final ClusterConfigChangeException LEADER_STEP_DOWN =
        new ClusterConfigChangeException("Leader has stepped down");
    public static final ClusterConfigChangeException NOT_LEADER =
        new ClusterConfigChangeException("Cluster change can only do via leader");
    public static final ClusterConfigChangeException NO_LEADER =
        new ClusterConfigChangeException("No leader elected");

    private ClusterConfigChangeException(String message) {
        super(message);
    }
}
