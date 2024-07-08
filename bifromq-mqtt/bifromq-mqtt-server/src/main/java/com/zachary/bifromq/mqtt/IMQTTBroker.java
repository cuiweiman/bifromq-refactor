package com.zachary.bifromq.mqtt;

public interface IMQTTBroker {

    static MQTTBrokerBuilder.InProcBrokerBuilder inProcBrokerBuilder() {
        return new MQTTBrokerBuilder.InProcBrokerBuilder();
    }

    static MQTTBrokerBuilder.NonSSLBrokerBuilder nonSSLBrokerBuilder() {
        return new MQTTBrokerBuilder.NonSSLBrokerBuilder();
    }

    static MQTTBrokerBuilder.SSLBrokerBuilder sslBrokerBuilder() {
        return new MQTTBrokerBuilder.SSLBrokerBuilder();
    }

    void start();

    void shutdown();
}
