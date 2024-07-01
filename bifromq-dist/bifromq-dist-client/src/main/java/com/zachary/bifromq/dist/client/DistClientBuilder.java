package com.zachary.bifromq.dist.client;


import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.baserpc.IRPCClient;
import com.zachary.bifromq.dist.IDistServiceBuilder;
import com.zachary.bifromq.dist.RPCBluePrint;
import com.google.common.base.Preconditions;
import io.netty.channel.EventLoopGroup;
import lombok.NonNull;

import java.io.File;
import java.util.concurrent.Executor;

public abstract class DistClientBuilder<T extends DistClientBuilder> implements IDistServiceBuilder {
    protected Executor executor;

    public T executor(Executor executor) {
        this.executor = executor;
        return (T) this;
    }

    public IDistClient build() {
        return new DistClient(buildRPCClient());
    }

    protected abstract IRPCClient buildRPCClient();

    public static final class InProcDistClientBuilder extends DistClientBuilder<InProcDistClientBuilder> {
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

    abstract static class InterProcDistClientBuilder<T extends InterProcDistClientBuilder>
        extends DistClientBuilder<T> {
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

    public static final class NonSSLDistClientBuilder extends InterProcDistClientBuilder<NonSSLDistClientBuilder> {
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

    public static final class SSLDistClientBuilder extends InterProcDistClientBuilder<SSLDistClientBuilder> {
        private File serviceIdentityCertFile;
        private File privateKeyFile;
        private File trustCertsFile;

        public SSLDistClientBuilder serviceIdentityCertFile(File serviceIdentityCertFile) {
            this.serviceIdentityCertFile = serviceIdentityCertFile;
            return this;
        }

        public SSLDistClientBuilder privateKeyFile(File privateKeyFile) {
            this.privateKeyFile = privateKeyFile;
            return this;
        }

        public SSLDistClientBuilder trustCertsFile(File trustCertsFile) {
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
