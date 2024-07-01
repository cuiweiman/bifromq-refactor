package com.zachary.bifromq.dist.worker;

import com.zachary.bifromq.basecluster.IAgentHost;
import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.basekv.balance.option.KVRangeBalanceControllerOptions;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basekv.server.IBaseKVStoreServer;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProcFactory;
import com.zachary.bifromq.basekv.store.option.KVRangeStoreOptions;
import com.zachary.bifromq.dist.client.IDistClient;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import com.zachary.bifromq.plugin.subbroker.ISubBrokerManager;
import com.google.common.base.Preconditions;
import io.netty.channel.EventLoopGroup;
import lombok.NonNull;

import java.io.File;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

public abstract class DistWorkerBuilder<T extends DistWorkerBuilder<?>> {
    protected IAgentHost agentHost;
    protected ICRDTService crdtService;
    protected IEventCollector eventCollector;
    protected IDistClient distClient;
    protected IBaseKVStoreClient storeClient;

    protected ISettingProvider settingProvider;
    protected ISubBrokerManager subBrokerManager;
    protected KVRangeStoreOptions kvRangeStoreOptions;
    protected KVRangeBalanceControllerOptions balanceControllerOptions = new KVRangeBalanceControllerOptions();
    protected Duration statsInterval = Duration.ofSeconds(30);
    protected Duration gcInterval = Duration.ofMinutes(5);
    protected Executor ioExecutor;
    protected Executor queryExecutor;
    protected Executor mutationExecutor;
    protected ScheduledExecutorService tickTaskExecutor;
    protected ScheduledExecutorService bgTaskExecutor;

    public T agentHost(@NonNull IAgentHost agentHost) {
        this.agentHost = agentHost;
        return (T) this;
    }

    public T crdtService(@NonNull ICRDTService crdtService) {
        this.crdtService = crdtService;
        return (T) this;
    }

    public T settingProvider(ISettingProvider settingProvider) {
        this.settingProvider = settingProvider;
        return (T) this;
    }

    public T eventCollector(IEventCollector eventCollector) {
        this.eventCollector = eventCollector;
        return (T) this;
    }

    public T distClient(@NonNull IDistClient distClient) {
        this.distClient = distClient;
        return (T) this;
    }

    public T storeClient(@NonNull IBaseKVStoreClient storeClient) {
        this.storeClient = storeClient;
        return (T) this;
    }

    public T subBrokerManager(ISubBrokerManager subBrokerManager) {
        this.subBrokerManager = subBrokerManager;
        return (T) this;
    }

    public T kvRangeStoreOptions(KVRangeStoreOptions kvRangeStoreOptions) {
        this.kvRangeStoreOptions = kvRangeStoreOptions;
        return (T) this;
    }

    public T balanceControllerOptions(KVRangeBalanceControllerOptions balanceControllerOptions) {
        this.balanceControllerOptions = balanceControllerOptions;
        return (T) this;
    }

    public T ioExecutor(Executor rpcExecutor) {
        this.ioExecutor = rpcExecutor;
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

    public abstract IDistWorker build();

    public static final class InProcDistWorker extends DistWorkerBuilder<InProcDistWorker> {
        @Override
        public IDistWorker build() {
            Preconditions.checkNotNull(agentHost);
            Preconditions.checkNotNull(crdtService);
            Preconditions.checkNotNull(distClient);
            Preconditions.checkNotNull(storeClient);
            return new DistWorker(agentHost,
                crdtService,
                settingProvider,
                eventCollector,
                distClient,
                storeClient,
                subBrokerManager,
                statsInterval,
                gcInterval,
                kvRangeStoreOptions,
                balanceControllerOptions,
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

    abstract static class InterProcDistWorkerBuilder<T extends InterProcDistWorkerBuilder<?>>
        extends DistWorkerBuilder<T> {
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

    public static final class NonSSLDistWorkerBuilder extends InterProcDistWorkerBuilder<NonSSLDistWorkerBuilder> {

        @Override
        public IDistWorker build() {
            Preconditions.checkNotNull(agentHost);
            Preconditions.checkNotNull(crdtService);
            Preconditions.checkNotNull(distClient);
            Preconditions.checkNotNull(storeClient);
            Preconditions.checkNotNull(bindAddr);
            return new DistWorker(agentHost,
                crdtService,
                settingProvider,
                eventCollector,
                distClient,
                storeClient,
                subBrokerManager,
                statsInterval,
                gcInterval,
                kvRangeStoreOptions,
                balanceControllerOptions,
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

    public static final class SSLDistWorkerBuilder extends InterProcDistWorkerBuilder<SSLDistWorkerBuilder> {
        private File serviceIdentityCertFile;

        private File privateKeyFile;

        private File trustCertsFile;

        public SSLDistWorkerBuilder serviceIdentityCertFile(File serviceIdentityFile) {
            this.serviceIdentityCertFile = serviceIdentityFile;
            return this;
        }

        public SSLDistWorkerBuilder privateKeyFile(File privateKeyFile) {
            this.privateKeyFile = privateKeyFile;
            return this;
        }

        public SSLDistWorkerBuilder trustCertsFile(File trustCertsFile) {
            this.trustCertsFile = trustCertsFile;
            return this;
        }

        @Override
        public IDistWorker build() {
            Preconditions.checkNotNull(agentHost);
            Preconditions.checkNotNull(crdtService);
            Preconditions.checkNotNull(distClient);
            Preconditions.checkNotNull(storeClient);
            Preconditions.checkNotNull(bindAddr);
            return new DistWorker(agentHost,
                crdtService,
                settingProvider,
                eventCollector,
                distClient,
                storeClient,
                subBrokerManager,
                statsInterval,
                gcInterval,
                kvRangeStoreOptions,
                balanceControllerOptions,
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
