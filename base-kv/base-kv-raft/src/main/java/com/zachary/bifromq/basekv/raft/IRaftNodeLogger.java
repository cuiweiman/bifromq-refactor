package com.zachary.bifromq.basekv.raft;

public interface IRaftNodeLogger {

    void logTrace(String message, Object... args);

    void logDebug(String message, Object... args);

    void logInfo(String message, Object... args);

    void logWarn(String message, Object... args);

    void logError(String message, Object... args);
}
