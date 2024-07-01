package com.zachary.bifromq.basekv.raft.exception;

public class RecoveryException extends RuntimeException {
    public static final RecoveryException NOT_LOST_QUORUM = new RecoveryException("Not lost quorum");

    public static final RecoveryException ABORT = new RecoveryException("Aborted");

    public static final RecoveryException NOT_VOTER = new RecoveryException("Not voter");

    public static final RecoveryException NOT_QUALIFY = new RecoveryException("Not qualify");

    public static final RecoveryException RECOVERY_IN_PROGRESS = new RecoveryException("There is recovery in progress");

    private RecoveryException(String message) {
        super(message);
    }
}
