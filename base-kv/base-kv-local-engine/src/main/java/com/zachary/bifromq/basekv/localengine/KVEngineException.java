package com.zachary.bifromq.basekv.localengine;

public class KVEngineException extends RuntimeException {

    public KVEngineException(String message) {
        super(message);
    }

    public KVEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
