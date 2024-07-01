package com.zachary.bifromq.basekv.raft.exception;

public class CompactionException extends RuntimeException {
    public static final CompactionException STALE_SNAPSHOT = new CompactionException("Stale Snapshot");

    public CompactionException(String message, Throwable cause) {
        super(message, cause);
    }

    private CompactionException(String message) {
        super(message);
    }
}
