package com.zachary.bifromq.retain.server;

import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.baserpc.CertInfo;
import com.zachary.bifromq.baserpc.IRPCServer;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import com.zachary.bifromq.retain.IRetainServiceBuilder;
import com.zachary.bifromq.retain.RPCBluePrint;
import com.google.common.base.Preconditions;
import io.netty.channel.EventLoopGroup;
import lombok.NonNull;

import java.io.File;
import java.util.concurrent.Executor;

public abstract class RetainServerBuilder<T extends RetainServerBuilder> implements IRetainServiceBuilder {
    protected ISettingProvider settingProvider;
    protected Executor executor;
    protected IBaseKVStoreClient storeClient;

    public T settingProvider(ISettingProvider settingProvider) {
        this.settingProvider = settingProvider;
        return (T) this;
    }

    public T ioExecutor(Executor executor) {
        this.executor = executor;
        return (T) this;
    }

    public T storeClient(IBaseKVStoreClient storeClient) {
        this.storeClient = storeClient;
        return (T) this;
    }

    public abstract IRetainServer build();

    public static final class InProcServerBuilder extends RetainServerBuilder<InProcServerBuilder> {

        @Override
        public IRetainServer build() {
            return new RetainServer(SERVICE_NAME, settingProvider, storeClient) {
                @Override
                protected IRPCServer buildRPCServer(RetainService retainService) {
                    return IRPCServer.inProcServerBuilder()
                        .serviceUniqueName(SERVICE_NAME)
                        .executor(executor)
                        .bluePrint(RPCBluePrint.INSTANCE)
                        .bindService(retainService)
                        .build();
                }
            };
        }
    }

    abstract static class InterProcBuilder<T extends InterProcBuilder> extends RetainServerBuilder<T> {
        protected String id;
        protected String host;
        protected Integer port;
        protected ICRDTService crdtService;
        protected EventLoopGroup bossEventLoopGroup;
        protected EventLoopGroup workerEventLoopGroup;

        public T id(@NonNull String id) {
            this.id = id;
            return (T) this;
        }

        public T host(@NonNull String host) {
            Preconditions.checkArgument(!"0.0.0.0".equals(host), "Invalid host ip");
            this.host = host;
            return (T) this;
        }

        public T port(@NonNull Integer port) {
            this.port = port;
            return (T) this;
        }

        public T crdtService(@NonNull ICRDTService crdtService) {
            this.crdtService = crdtService;
            return (T) this;
        }

        public T bossEventLoopGroup(EventLoopGroup bossEventLoopGroup) {
            this.bossEventLoopGroup = bossEventLoopGroup;
            return (T) this;
        }

        public T workerEventLoopGroup(EventLoopGroup workerEventLoopGroup) {
            this.workerEventLoopGroup = workerEventLoopGroup;
            return (T) this;
        }
    }

    public static final class NonSSLServerBuilder extends InterProcBuilder<NonSSLServerBuilder> {
        @Override
        public IRetainServer build() {
            return new RetainServer(SERVICE_NAME, settingProvider, storeClient) {
                @Override
                protected IRPCServer buildRPCServer(RetainService retainService) {
                    return IRPCServer.nonSSLServerBuilder()
                        .serviceUniqueName(SERVICE_NAME)
                        .executor(executor)
                        .bluePrint(RPCBluePrint.INSTANCE)
                        .bindService(retainService)
                        .id(id)
                        .host(host)
                        .port(port)
                        .bossEventLoopGroup(bossEventLoopGroup)
                        .workerEventLoopGroup(workerEventLoopGroup)
                        .crdtService(crdtService)
                        .build();
                }
            };
        }
    }

    public static final class SSLServerBuilder extends InterProcBuilder<SSLServerBuilder> {
        private @NonNull File serviceIdentityCertFile;
        private @NonNull File privateKeyFile;
        private @NonNull File trustCertsFile;
        private CertInfo certInfo;

        public SSLServerBuilder serviceIdentityCertFile(@NonNull File serviceIdentityCertFile) {
            this.serviceIdentityCertFile = serviceIdentityCertFile;
            certInfo = CertInfo.parse(serviceIdentityCertFile);
            Preconditions.checkArgument(certInfo.serverAuth, "Not server auth cert");
            return this;
        }

        public SSLServerBuilder privateKeyFile(@NonNull File privateKeyFile) {
            this.privateKeyFile = privateKeyFile;
            return this;
        }

        public SSLServerBuilder trustCertsFile(@NonNull File trustCertsFile) {
            this.trustCertsFile = trustCertsFile;
            return this;
        }

        @Override
        public IRetainServer build() {
            return new RetainServer(SERVICE_NAME, settingProvider, storeClient) {
                @Override
                protected IRPCServer buildRPCServer(RetainService retainService) {
                    return IRPCServer.sslServerBuilder()
                        .executor(executor)
                        .bluePrint(RPCBluePrint.INSTANCE)
                        .bindService(retainService)
                        .id(id)
                        .host(host)
                        .port(port)
                        .serviceUniqueName(SERVICE_NAME)
                        .bossEventLoopGroup(bossEventLoopGroup)
                        .workerEventLoopGroup(workerEventLoopGroup)
                        .crdtService(crdtService)
                        .serviceIdentityCertFile(serviceIdentityCertFile)
                        .privateKeyFile(privateKeyFile)
                        .trustCertsFile(trustCertsFile)
                        .build();
                }
            };
        }
    }
}
