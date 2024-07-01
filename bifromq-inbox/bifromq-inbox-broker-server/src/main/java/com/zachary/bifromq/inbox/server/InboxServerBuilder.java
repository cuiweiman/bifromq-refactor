package com.zachary.bifromq.inbox.server;

import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.baserpc.CertInfo;
import com.zachary.bifromq.baserpc.IRPCServer;
import com.zachary.bifromq.inbox.IInboxServiceBuilder;
import com.zachary.bifromq.inbox.RPCBluePrint;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import com.google.common.base.Preconditions;
import io.netty.channel.EventLoopGroup;
import lombok.NonNull;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

public abstract class InboxServerBuilder<T extends InboxServerBuilder<T>>
    implements IInboxServiceBuilder {
    protected ISettingProvider settingProvider;
    protected Executor executor;
    protected IBaseKVStoreClient storeClient;
    protected ScheduledExecutorService bgTaskExecutor;

    public T settingProvider(ISettingProvider settingProvider) {
        this.settingProvider = settingProvider;
        return (T) this;
    }

    public T executor(Executor ioExecutor) {
        this.executor = ioExecutor;
        return (T) this;
    }

    public T storeClient(IBaseKVStoreClient storeClient) {
        this.storeClient = storeClient;
        return (T) this;
    }

    public T bgTaskExecutor(ScheduledExecutorService bgTaskExecutor) {
        this.bgTaskExecutor = bgTaskExecutor;
        return (T) this;
    }

    public abstract IInboxServer build();

    public static final class InProcServerBuilder extends InboxServerBuilder<InProcServerBuilder> {

        @Override
        public IInboxServer build() {
            return new InboxServer(settingProvider, storeClient, bgTaskExecutor) {
                @Override
                protected IRPCServer buildRPCServer(InboxService inboxService) {
                    return IRPCServer.inProcServerBuilder()
                        .serviceUniqueName(SERVICE_NAME)
                        .executor(executor)
                        .bluePrint(RPCBluePrint.INSTANCE)
                        .bindService(inboxService)
                        .build();
                }
            };
        }
    }

    abstract static class InterProcInboxServerBuilder<T extends InterProcInboxServerBuilder<T>>
        extends InboxServerBuilder<T> {
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

    public static final class NonSSLServerBuilder extends InterProcInboxServerBuilder<NonSSLServerBuilder> {
        @Override
        public IInboxServer build() {
            return new InboxServer(settingProvider, storeClient, bgTaskExecutor) {
                @Override
                protected IRPCServer buildRPCServer(InboxService inboxService) {
                    return IRPCServer.nonSSLServerBuilder()
                        .serviceUniqueName(SERVICE_NAME)
                        .executor(executor)
                        .bluePrint(RPCBluePrint.INSTANCE)
                        .bindService(inboxService)
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

    public static final class SSLServerBuilder extends InterProcInboxServerBuilder<SSLServerBuilder> {
        private @NonNull File serviceIdentityCertFile;
        private @NonNull File privateKeyFile;
        private @NonNull File trustCertsFile;

        public SSLServerBuilder serviceIdentityCertFile(@NonNull File serviceIdentityCertFile) {
            this.serviceIdentityCertFile = serviceIdentityCertFile;
            CertInfo certInfo = CertInfo.parse(serviceIdentityCertFile);
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
        public IInboxServer build() {
            return new InboxServer(settingProvider, storeClient, bgTaskExecutor) {
                @Override
                protected IRPCServer buildRPCServer(InboxService inboxService) {
                    return IRPCServer.sslServerBuilder()
                        .serviceUniqueName(SERVICE_NAME)
                        .id(id)
                        .host(host)
                        .port(port)
                        .bluePrint(RPCBluePrint.INSTANCE)
                        .bindService(inboxService)
                        .executor(executor)
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
