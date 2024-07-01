package com.zachary.bifromq.mqtt.inbox;

import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.baserpc.IRPCClient;
import com.google.common.base.Preconditions;
import io.netty.channel.EventLoopGroup;
import lombok.NonNull;

import java.io.File;
import java.util.concurrent.Executor;

abstract class MqttBrokerClientBuilder<T extends MqttBrokerClientBuilder>
    implements IMqttBrokerServiceBuilder {
    protected Executor executor;

    public T executor(Executor executor) {
        this.executor = executor;
        return (T) this;
    }

    public IMqttBrokerClient build() {
        return new MqttBrokerClient(buildRPCClient());
    }

    protected abstract IRPCClient buildRPCClient();

    public static final class InProcMqttBrokerClientBuilder
        extends MqttBrokerClientBuilder<InProcMqttBrokerClientBuilder> {
        @Override
        protected IRPCClient buildRPCClient() {
            return IRPCClient.builder()
                .serviceUniqueName(SERVICE_NAME)
                .bluePrint(RPCBluePrint.INSTANCE)
                .executor(executor)
                .inProcChannel()
                .buildChannel()
                .build();
        }
    }

    abstract static class InterProcMqttBrokerClientBuilder<T extends InterProcMqttBrokerClientBuilder>
        extends MqttBrokerClientBuilder<T> {
        protected ICRDTService crdtService;
        protected EventLoopGroup eventLoopGroup;

        public T crdtService(@NonNull ICRDTService crdtService) {
            this.crdtService = crdtService;
            return (T) this;
        }

        public T eventLoopGroup(EventLoopGroup eventLoopGroup) {
            this.eventLoopGroup = eventLoopGroup;
            return (T) this;
        }
    }

    public static final class NonSSLMqttBrokerClientBuilder
        extends InterProcMqttBrokerClientBuilder<NonSSLMqttBrokerClientBuilder> {
        @Override
        protected IRPCClient buildRPCClient() {
            Preconditions.checkNotNull(crdtService);
            return IRPCClient.builder()
                .serviceUniqueName(SERVICE_NAME)
                .bluePrint(RPCBluePrint.INSTANCE)
                .executor(executor)
                .nonSSLChannel()
                .eventLoopGroup(eventLoopGroup)
                .crdtService(crdtService)
                .buildChannel()
                .build();
        }
    }

    public static final class SSLMqttBrokerClientBuilder
        extends InterProcMqttBrokerClientBuilder<SSLMqttBrokerClientBuilder> {
        private File serviceIdentityCertFile;
        private File privateKeyFile;
        private File trustCertsFile;

        public SSLMqttBrokerClientBuilder serviceIdentityCertFile(File serviceIdentityCertFile) {
            this.serviceIdentityCertFile = serviceIdentityCertFile;
            return this;
        }

        public SSLMqttBrokerClientBuilder privateKeyFile(File privateKeyFile) {
            this.privateKeyFile = privateKeyFile;
            return this;
        }

        public SSLMqttBrokerClientBuilder trustCertsFile(File trustCertsFile) {
            this.trustCertsFile = trustCertsFile;
            return this;
        }

        @Override
        protected IRPCClient buildRPCClient() {
            Preconditions.checkNotNull(crdtService);
            Preconditions.checkNotNull(serviceIdentityCertFile);
            Preconditions.checkNotNull(privateKeyFile);
            Preconditions.checkNotNull(trustCertsFile);
            return IRPCClient.builder()
                .serviceUniqueName(SERVICE_NAME)
                .bluePrint(RPCBluePrint.INSTANCE)
                .executor(executor)
                .sslChannel()
                .serviceIdentityCertFile(serviceIdentityCertFile)
                .privateKeyFile(privateKeyFile)
                .trustCertsFile(trustCertsFile)
                .eventLoopGroup(eventLoopGroup)
                .crdtService(crdtService)
                .buildChannel()
                .build();
        }
    }
}
