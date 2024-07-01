package com.zachary.bifromq.mqtt.service;

import java.util.concurrent.CompletableFuture;

public interface ILocalSessionBrokerServer extends ILocalSessionRegistry {
    static LocalSessionBrokerServerBuilder.InProcBrokerBuilder inProcBrokerBuilder() {
        return new LocalSessionBrokerServerBuilder.InProcBrokerBuilder();
    }

    static LocalSessionBrokerServerBuilder.NonSSLBrokerBuilder nonSSLBrokerBuilder() {
        return new LocalSessionBrokerServerBuilder.NonSSLBrokerBuilder();
    }

    static LocalSessionBrokerServerBuilder.SSLBrokerBuilder sslBrokerBuilder() {
        return new LocalSessionBrokerServerBuilder.SSLBrokerBuilder();
    }

    String id();

    void start();

    CompletableFuture<Void> disconnectAll(int disconnectRate);

    void shutdown();
}
