package com.zachary.bifromq.dist.client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

public abstract class ClearResult {
    public enum Type {
        OK, ERROR
    }

    public abstract Type type();

    public static final ClearResult.OK OK = new OK();

    public static final Error INTERNAL_ERROR =
        new Error(new RuntimeException("Internal Error"));

    public static Error error(Throwable e) {
        return new Error(e);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class OK extends ClearResult {
        @Override
        public Type type() {
            return Type.OK;
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Error extends ClearResult {
        public final Throwable cause;

        @Override
        public Type type() {
            return Type.ERROR;
        }
    }

}
