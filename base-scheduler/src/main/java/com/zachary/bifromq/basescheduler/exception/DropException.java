package com.zachary.bifromq.basescheduler.exception;

public class DropException extends RuntimeException {
    public static final DropException EXCEED_LIMIT = new DropException("Request rate exceed limit");
    public static final DropException BATCH_NOT_AVAILABLE = new DropException("Batch not available");
    public static final DropException ABORT = new DropException("Abort on close");

    private DropException(String message) {
        super(message);
    }
}
