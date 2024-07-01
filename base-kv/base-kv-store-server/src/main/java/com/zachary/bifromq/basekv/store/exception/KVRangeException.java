package com.zachary.bifromq.basekv.store.exception;

public class KVRangeException extends RuntimeException {
    public KVRangeException(String message) {
        super(message);
    }

    public KVRangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class TryLater extends KVRangeException {

        public TryLater(String message) {
            super(message);
        }

        public TryLater(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class BadVersion extends KVRangeException {

        public BadVersion(String message) {
            super(message);
        }

        public BadVersion(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class BadRequest extends KVRangeException {

        public BadRequest(String message) {
            super(message);
        }

        public BadRequest(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InternalException extends KVRangeException {

        public InternalException(String message) {
            super(message);
        }

        public InternalException(String message, Throwable cause) {
            super(message, cause);
        }
    }


}
