package com.zachary.bifromq.basekv.exception;

public class BaseKVException extends RuntimeException {

    public static final BaseKVException SERVER_NOT_FOUND = new BaseKVException("Server not found");

    public BaseKVException(String message) {
        super(message);
    }

    public BaseKVException(String message, Throwable cause) {
        super(message, cause);
    }
}
