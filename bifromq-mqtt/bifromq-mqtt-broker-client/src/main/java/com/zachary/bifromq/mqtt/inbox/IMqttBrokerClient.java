package com.zachary.bifromq.mqtt.inbox;

import com.zachary.bifromq.plugin.subbroker.ISubBroker;

public interface IMqttBrokerClient extends ISubBroker {
    static MqttBrokerClientBuilder.InProcMqttBrokerClientBuilder inProcClientBuilder() {
        return new MqttBrokerClientBuilder.InProcMqttBrokerClientBuilder();
    }

    static MqttBrokerClientBuilder.NonSSLMqttBrokerClientBuilder nonSSLClientBuilder() {
        return new MqttBrokerClientBuilder.NonSSLMqttBrokerClientBuilder();
    }

    static MqttBrokerClientBuilder.SSLMqttBrokerClientBuilder sslClientBuilder() {
        return new MqttBrokerClientBuilder.SSLMqttBrokerClientBuilder();
    }

    @Override
    default int id() {
        return 0;
    }
}
