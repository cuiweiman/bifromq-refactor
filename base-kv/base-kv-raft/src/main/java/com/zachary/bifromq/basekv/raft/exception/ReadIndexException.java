package com.zachary.bifromq.basekv.raft.exception;

public class ReadIndexException extends RuntimeException {
    public static final ReadIndexException COMMIT_INDEX_NOT_CONFIRMED =
        new ReadIndexException("Leader has not confirmed the commit index of its term");

    public static final ReadIndexException LEADER_STEP_DOWN =
        new ReadIndexException("Leader has been stepped down");

    public static final ReadIndexException NO_LEADER = new ReadIndexException("No leader elected");

    public static final ReadIndexException FORWARD_TIMEOUT =
        new ReadIndexException("Doesn't receive read index from leader within timeout");

    private ReadIndexException(String message) {
        super(message);
    }
}
