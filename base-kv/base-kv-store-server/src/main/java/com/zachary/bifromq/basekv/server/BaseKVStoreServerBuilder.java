package com.zachary.bifromq.basekv.server;

import com.zachary.bifromq.basecluster.IAgentHost;
import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.basekv.RPCBluePrint;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProcFactory;
import com.zachary.bifromq.basekv.store.option.KVRangeStoreOptions;
import com.zachary.bifromq.baserpc.IRPCServer;
import io.netty.channel.EventLoopGroup;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

import static com.google.common.base.Preconditions.checkNotNull;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
public abstract class BaseKVStoreServerBuilder<T extends BaseKVStoreServerBuilder> {
    protected KVRangeStoreOptions storeOptions = new KVRangeStoreOptions();
    protected IAgentHost agentHost;
    protected ICRDTService crdtService;
    protected String clusterId;
    protected IKVRangeCoProcFactory coProcFactory;
    protected Executor ioExecutor;
    protected Executor queryExecutor;
    protected Executor mutationExecutor;
    protected ScheduledExecutorService tickTaskExecutor;
    protected ScheduledExecutorService bgTaskExecutor;

    public T clusterId(@NonNull String clusterId) {
        this.clusterId = clusterId;
        return (T) this;
    }

    public KVRangeStoreOptions storeOptions() {
        return storeOptions;
    }

    public T storeOptions(KVRangeStoreOptions options) {
        if (options != null) {
            this.storeOptions = options.toBuilder().build();
        }
        return (T) this;
    }

    public T agentHost(@NonNull IAgentHost agentHost) {
        this.agentHost = agentHost;
        return (T) this;
    }

    public T crdtService(@NonNull ICRDTService crdtService) {
        this.crdtService = crdtService;
        return (T) this;
    }

    public T coProcFactory(@NonNull IKVRangeCoProcFactory coProcFactory) {
        this.coProcFactory = coProcFactory;
        return (T) this;
    }

    public T ioExecutor(Executor ioExecutor) {
        this.ioExecutor = ioExecutor;
        return (T) this;
    }

    public T queryExecutor(Executor queryExecutor) {
        this.queryExecutor = queryExecutor;
        return (T) this;
    }

    public T mutationExecutor(Executor mutationExecutor) {
        this.mutationExecutor = mutationExecutor;
        return (T) this;
    }

    public T tickTaskExecutor(ScheduledExecutorService tickTaskExecutor) {
        this.tickTaskExecutor = tickTaskExecutor;
        return (T) this;
    }

    public T bgTaskExecutor(ScheduledExecutorService bgTaskExecutor) {
        this.bgTaskExecutor = bgTaskExecutor;
        return (T) this;
    }

    public abstract IBaseKVStoreServer build();

    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static final class InProcBaseKVStoreServerBuilder
        extends BaseKVStoreServerBuilder<InProcBaseKVStoreServerBuilder> {
        @Override
        public IBaseKVStoreServer build() {
            checkNotNull(clusterId);
            checkNotNull(agentHost);
            checkNotNull(crdtService);
            checkNotNull(coProcFactory);
            return new BaseKVStoreServer(clusterId,
                agentHost,
                crdtService,
                coProcFactory,
                // In inproc mode, rpcChannelId = serviceUniqueName = storeId = clusterId
                storeOptions.setOverrideIdentity(clusterId),
                queryExecutor,
                mutationExecutor,
                tickTaskExecutor,
                bgTaskExecutor) {
                @Override
                protected IRPCServer buildServer(BaseKVStoreService service) {
                    return IRPCServer.inProcServerBuilder()
                        .serviceUniqueName(clusterId)
                        .executor(ioExecutor)
                        .bluePrint(RPCBluePrint.INSTANCE)
                        .bindService(service)
                        .build();
                }
            };
        }
    }

    public abstract static class InterProcBaseKVStoreServerBuilder<T extends InterProcBaseKVStoreServerBuilder>
        extends BaseKVStoreServerBuilder<T> {

        protected String bindAddr;

        protected int bindPort;

        protected EventLoopGroup bossEventLoopGroup;

        protected EventLoopGroup workerEventLoopGroup;

        public T bindAddr(String bindAddr) {
            this.bindAddr = bindAddr;
            return (T) this;
        }

        public T bindPort(int bindPort) {
            this.bindPort = bindPort;
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

    public static final class NonSSLBaseKVStoreServerBuilder
        extends InterProcBaseKVStoreServerBuilder<NonSSLBaseKVStoreServerBuilder> {

        @Override
        public IBaseKVStoreServer build() {
            checkNotNull(clusterId);
            checkNotNull(agentHost);
            checkNotNull(crdtService);
            checkNotNull(coProcFactory);
            return new BaseKVStoreServer(clusterId,
                agentHost,
                crdtService,
                coProcFactory,
                storeOptions,
                queryExecutor,
                mutationExecutor,
                tickTaskExecutor,
                bgTaskExecutor) {
                @Override
                protected IRPCServer buildServer(BaseKVStoreService service) {
                    return IRPCServer.nonSSLServerBuilder()
                        .id(service.id())
                        .serviceUniqueName(clusterId)
                        .host(bindAddr)
                        .port(bindPort)
                        .bossEventLoopGroup(bossEventLoopGroup)
                        .workerEventLoopGroup(workerEventLoopGroup)
                        .crdtService(crdtService)
                        .executor(ioExecutor)
                        .bluePrint(RPCBluePrint.INSTANCE)
                        .bindService(service)
                        .build();
                }
            };
        }
    }

    public static final class SSLBaseKVStoreServerBuilder
        extends InterProcBaseKVStoreServerBuilder<SSLBaseKVStoreServerBuilder> {
        private File serviceIdentityFile;

        private File privateKeyFile;

        private File trustCertsFile;

        public SSLBaseKVStoreServerBuilder serviceIdentityFile(@NonNull File serviceIdentityFile) {
            this.serviceIdentityFile = serviceIdentityFile;
            return this;
        }

        public SSLBaseKVStoreServerBuilder privateKeyFile(@NonNull File privateKeyFile) {
            this.privateKeyFile = privateKeyFile;
            return this;
        }

        public SSLBaseKVStoreServerBuilder trustCertsFile(@NonNull File trustCertsFile) {
            this.trustCertsFile = trustCertsFile;
            return this;
        }

        @Override
        public IBaseKVStoreServer build() {
            checkNotNull(clusterId);
            checkNotNull(agentHost);
            checkNotNull(crdtService);
            checkNotNull(coProcFactory);
            return new BaseKVStoreServer(clusterId,
                agentHost,
                crdtService,
                coProcFactory,
                storeOptions,
                queryExecutor,
                mutationExecutor,
                tickTaskExecutor,
                bgTaskExecutor) {
                @Override
                protected IRPCServer buildServer(BaseKVStoreService service) {
                    return IRPCServer.sslServerBuilder()
                        .id(service.id())
                        .host(bindAddr)
                        .port(bindPort)
                        .serviceUniqueName(clusterId)
                        .serviceIdentityCertFile(serviceIdentityFile)
                        .privateKeyFile(privateKeyFile)
                        .trustCertsFile(trustCertsFile)
                        .crdtService(crdtService)
                        .bossEventLoopGroup(bossEventLoopGroup)
                        .workerEventLoopGroup(workerEventLoopGroup)
                        .executor(ioExecutor)
                        .bluePrint(RPCBluePrint.INSTANCE)
                        .bindService(service)
                        .build();
                }
            };
        }
    }
}
