package com.zachary.bifromq.basekv.raft.exception;

public class LeaderTransferException extends RuntimeException {
    public static final LeaderTransferException LEADER_NOT_READY =
        new LeaderTransferException("Leader has not been ready " +
            "due to commit index of its term has not been confirmed");

    public static final LeaderTransferException TRANSFERRING_IN_PROGRESS =
        new LeaderTransferException("There is transferring in progress");

    public static final LeaderTransferException SELF_TRANSFER = new LeaderTransferException("Cannot transfer to self");

    public static final LeaderTransferException STEP_DOWN_BY_OTHER =
        new LeaderTransferException("Step down by another candidate");

    public static final LeaderTransferException NOT_FOUND_OR_QUALIFIED =
        new LeaderTransferException("Transferee not found or not qualified");

    public static final LeaderTransferException TRANSFER_TIMEOUT =
        new LeaderTransferException("Cannot finish transfer within one election timeout");

    public static final LeaderTransferException NOT_LEADER = new LeaderTransferException("Only leader can do transfer");

    public static final LeaderTransferException NO_LEADER = new LeaderTransferException("No leader elected");

    private LeaderTransferException(String message) {
        super(message);
    }
}
