package com.zachary.bifromq.basekv.raft.exception;

public class InternalError extends Error {
    public InternalError(Throwable e) {
        super(e);
    }
}
