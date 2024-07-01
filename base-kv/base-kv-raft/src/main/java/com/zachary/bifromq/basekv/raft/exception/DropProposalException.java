package com.zachary.bifromq.basekv.raft.exception;

public class DropProposalException extends RuntimeException {
    public static final DropProposalException TRANSFERRING_LEADER =
        new DropProposalException("Proposal dropped due to on-going transferring leader");

    public static final DropProposalException THROTTLED_BY_THRESHOLD =
        new DropProposalException("Proposal dropped due to too many uncommitted logs");

    public static final DropProposalException NO_LEADER = new DropProposalException("No leader elected");

    public static final DropProposalException LEADER_FORWARD_DISABLED
        = new DropProposalException("Proposal forward feature is disabled");

    public static final DropProposalException FORWARD_TIMEOUT =
        new DropProposalException("Doesn't receive propose reply from leader within timeout");

    public static final DropProposalException OVERRIDDEN
        = new DropProposalException("Proposal dropped due to overridden by proposal from newer leader");

    private DropProposalException(String message) {
        super(message);
    }
}
