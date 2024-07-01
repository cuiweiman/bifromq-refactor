package com.zachary.bifromq.mqtt.session;

import java.util.concurrent.CompletableFuture;

public interface IMQTTSession {
    CompletableFuture<Void> disconnect();
}
