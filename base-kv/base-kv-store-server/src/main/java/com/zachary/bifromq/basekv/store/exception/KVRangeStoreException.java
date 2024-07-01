package com.zachary.bifromq.basekv.store.exception;

public class KVRangeStoreException extends RuntimeException {
    public static final KVRangeStoreException RANGE_NOT_FOUND = new KVRangeStoreException("Range not found");

    public KVRangeStoreException(String message) {
        super(message);
    }

    public KVRangeStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
