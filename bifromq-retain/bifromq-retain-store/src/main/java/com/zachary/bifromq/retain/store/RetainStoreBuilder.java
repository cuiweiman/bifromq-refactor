package com.zachary.bifromq.retain.store;

import com.zachary.bifromq.basecluster.IAgentHost;
import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.basehlc.HLC;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basekv.server.IBaseKVStoreServer;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProcFactory;
import com.zachary.bifromq.basekv.store.option.KVRangeStoreOptions;
import com.google.common.base.Preconditions;
import io.netty.channel.EventLoopGroup;
import lombok.NonNull;

import java.io.File;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

public abstract class RetainStoreBuilder<T extends RetainStoreBuilder<?>> {
    protected IAgentHost agentHost;
    protected ICRDTService crdtService;
    protected IBaseKVStoreClient storeClient;
    protected KVRangeStoreOptions kvRangeStoreOptions;
    protected Executor ioExecutor;
    protected Executor queryExecutor;
    protected Executor mutationExecutor;
    protected ScheduledExecutorService tickTaskExecutor;
    protected ScheduledExecutorService bgTaskExecutor;
    protected Duration statsInterval = Duration.ofSeconds(30);
    protected Duration gcInterval = Duration.ofMinutes(60);
    protected Clock clock = new Clock() {
        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return Instant.ofEpochMilli(HLC.INST.getPhysical());
        }
    };

    public T agentHost(@NonNull IAgentHost agentHost) {
        this.agentHost = agentHost;
        return (T) this;
    }

    public T crdtService(@NonNull ICRDTService crdtService) {
        this.crdtService = crdtService;
        return (T) this;
    }

    public T storeClient(@NonNull IBaseKVStoreClient storeClient) {
        this.storeClient = storeClient;
        return (T) this;
    }

    public T kvRangeStoreOptions(KVRangeStoreOptions kvRangeStoreOptions) {
        this.kvRangeStoreOptions = kvRangeStoreOptions;
        return (T) this;
    }

    public T ioExecutor(Executor executor) {
        this.ioExecutor = executor;
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

    public T statsInterval(Duration statsInterval) {
        this.statsInterval = statsInterval;
        return (T) this;
    }

    public T gcInterval(Duration gcInterval) {
        this.gcInterval = gcInterval;
        return (T) this;
    }

    public T clock(Clock clock) {
        this.clock = clock;
        return (T) this;
    }

    public abstract IRetainStore build();

    public static final class InProcInboxStore extends RetainStoreBuilder<InProcInboxStore> {
        @Override
        public IRetainStore build() {
            Preconditions.checkNotNull(agentHost);
            Preconditions.checkNotNull(crdtService);
            Preconditions.checkNotNull(storeClient);
            return new RetainStore(agentHost,
                crdtService,
                storeClient,
                statsInterval,
                gcInterval,
                clock,
                kvRangeStoreOptions,
                ioExecutor,
                queryExecutor,
                mutationExecutor,
                tickTaskExecutor,
                bgTaskExecutor) {
                @Override
                protected IBaseKVStoreServer buildKVStoreServer(String clusterId,
                                                                IAgentHost agentHost,
                                                                ICRDTService crdtService,
                                                                IKVRangeCoProcFactory coProcFactory,
                                                                KVRangeStoreOptions kvRangeStoreOptions,
                                                                Executor ioExecutor,
                                                                Executor queryExecutor,
                                                                Executor mutationExecutor,
                                                                ScheduledExecutorService tickTaskExecutor,
                                                                ScheduledExecutorService bgTaskExecutor) {
                    return IBaseKVStoreServer.inProcServerBuilder()
                        .clusterId(clusterId)
                        .agentHost(agentHost)
                        .crdtService(crdtService)
                        .coProcFactory(coProcFactory)
                        .ioExecutor(ioExecutor)
                        .queryExecutor(queryExecutor)
                        .mutationExecutor(mutationExecutor)
                        .tickTaskExecutor(tickTaskExecutor)
                        .bgTaskExecutor(bgTaskExecutor)
                        .storeOptions(kvRangeStoreOptions)
                        .build();
                }
            };
        }
    }

    abstract static class InterProcRetainStoreBuilder<T extends InterProcRetainStoreBuilder<?>>
        extends RetainStoreBuilder<T> {
        protected String bindAddr;
        protected int bindPort;
        protected EventLoopGroup bossEventLoopGroup;
        protected EventLoopGroup workerEventLoopGroup;

        public T bindAddr(@NonNull String bindAddr) {
            this.bindAddr = bindAddr;
            return (T) this;
        }

        public T bindPort(int bindPort) {
            Preconditions.checkArgument(bindPort >= 0);
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

    public static final class NonSSLRetainStoreBuilder extends InterProcRetainStoreBuilder<NonSSLRetainStoreBuilder> {

        @Override
        public IRetainStore build() {
            Preconditions.checkNotNull(agentHost);
            Preconditions.checkNotNull(crdtService);
            Preconditions.checkNotNull(storeClient);
            Preconditions.checkNotNull(bindAddr);
            return new RetainStore(agentHost,
                crdtService,
                storeClient,
                statsInterval,
                gcInterval,
                clock,
                kvRangeStoreOptions,
                ioExecutor,
                queryExecutor,
                mutationExecutor,
                tickTaskExecutor,
                bgTaskExecutor) {
                @Override
                protected IBaseKVStoreServer buildKVStoreServer(String clusterId,
                                                                IAgentHost agentHost,
                                                                ICRDTService crdtService,
                                                                IKVRangeCoProcFactory coProcFactory,
                                                                KVRangeStoreOptions kvRangeStoreOptions,
                                                                Executor ioExecutor,
                                                                Executor queryExecutor,
                                                                Executor mutationExecutor,
                                                                ScheduledExecutorService tickTaskExecutor,
                                                                ScheduledExecutorService bgTaskExecutor) {
                    return IBaseKVStoreServer
                        .nonSSLServerBuilder()
                        .clusterId(clusterId)
                        .agentHost(agentHost)
                        .crdtService(crdtService)
                        .coProcFactory(coProcFactory)
                        .ioExecutor(ioExecutor)
                        .queryExecutor(queryExecutor)
                        .mutationExecutor(mutationExecutor)
                        .tickTaskExecutor(tickTaskExecutor)
                        .bgTaskExecutor(bgTaskExecutor)
                        .storeOptions(kvRangeStoreOptions)
                        .bindAddr(bindAddr)
                        .bindPort(bindPort)
                        .bossEventLoopGroup(bossEventLoopGroup)
                        .workerEventLoopGroup(workerEventLoopGroup)
                        .build();
                }
            };
        }
    }

    public static final class SSLRetainStoreBuilder extends InterProcRetainStoreBuilder<SSLRetainStoreBuilder> {
        private File serviceIdentityCertFile;

        private File privateKeyFile;

        private File trustCertsFile;

        public SSLRetainStoreBuilder serviceIdentityCertFile(File serviceIdentityFile) {
            this.serviceIdentityCertFile = serviceIdentityFile;
            return this;
        }

        public SSLRetainStoreBuilder privateKeyFile(File privateKeyFile) {
            this.privateKeyFile = privateKeyFile;
            return this;
        }

        public SSLRetainStoreBuilder trustCertsFile(File trustCertsFile) {
            this.trustCertsFile = trustCertsFile;
            return this;
        }

        @Override
        public IRetainStore build() {
            Preconditions.checkNotNull(agentHost);
            Preconditions.checkNotNull(crdtService);
            Preconditions.checkNotNull(storeClient);
            Preconditions.checkNotNull(bindAddr);
            return new RetainStore(agentHost,
                crdtService,
                storeClient,
                statsInterval,
                gcInterval,
                clock,
                kvRangeStoreOptions,
                ioExecutor,
                queryExecutor,
                mutationExecutor,
                tickTaskExecutor,
                bgTaskExecutor) {
                @Override
                protected IBaseKVStoreServer buildKVStoreServer(String clusterId,
                                                                IAgentHost agentHost,
                                                                ICRDTService crdtService,
                                                                IKVRangeCoProcFactory coProcFactory,
                                                                KVRangeStoreOptions kvRangeStoreOptions,
                                                                Executor ioExecutor,
                                                                Executor queryExecutor,
                                                                Executor mutationExecutor,
                                                                ScheduledExecutorService tickTaskExecutor,
                                                                ScheduledExecutorService bgTaskExecutor) {
                    return IBaseKVStoreServer
                        .sslServerBuilder()
                        .clusterId(clusterId)
                        .agentHost(agentHost)
                        .crdtService(crdtService)
                        .coProcFactory(coProcFactory)
                        .ioExecutor(ioExecutor)
                        .queryExecutor(queryExecutor)
                        .mutationExecutor(mutationExecutor)
                        .tickTaskExecutor(tickTaskExecutor)
                        .bgTaskExecutor(bgTaskExecutor)
                        .storeOptions(kvRangeStoreOptions)
                        .bindAddr(bindAddr)
                        .bindPort(bindPort)
                        .bossEventLoopGroup(bossEventLoopGroup)
                        .workerEventLoopGroup(workerEventLoopGroup)
                        .serviceIdentityFile(serviceIdentityCertFile)
                        .privateKeyFile(privateKeyFile)
                        .trustCertsFile(trustCertsFile)
                        .build();
                }
            };
        }
    }
}
