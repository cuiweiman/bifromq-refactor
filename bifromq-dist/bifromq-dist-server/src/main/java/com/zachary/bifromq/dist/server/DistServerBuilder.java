package com.zachary.bifromq.dist.server;

import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.baserpc.CertInfo;
import com.zachary.bifromq.baserpc.IRPCServer;
import com.zachary.bifromq.dist.IDistServiceBuilder;
import com.zachary.bifromq.dist.RPCBluePrint;
import com.zachary.bifromq.dist.server.scheduler.IGlobalDistCallRateSchedulerFactory;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import com.google.common.base.Preconditions;
import io.netty.channel.EventLoopGroup;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Executor;

import static com.zachary.bifromq.basehookloader.BaseHookLoader.load;

@Slf4j
public abstract class DistServerBuilder<T extends DistServerBuilder<T>> implements IDistServiceBuilder {
    protected Executor executor;
    protected IBaseKVStoreClient storeClient;
    protected ISettingProvider settingProvider;
    protected IEventCollector eventCollector;
    protected ICRDTService crdtService;
    protected String distCallPreSchedulerFactoryClass;

    @SuppressWarnings("unchecked")
    public T ioExecutor(Executor executor) {
        this.executor = executor;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T storeClient(IBaseKVStoreClient storeClient) {
        this.storeClient = storeClient;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T settingProvider(ISettingProvider settingProvider) {
        this.settingProvider = settingProvider;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T eventCollector(IEventCollector eventCollector) {
        this.eventCollector = eventCollector;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T crdtService(ICRDTService crdtService) {
        this.crdtService = crdtService;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T distCallPreSchedulerFactoryClass(String factoryClass) {
        this.distCallPreSchedulerFactoryClass = factoryClass;
        return (T) this;
    }

    public abstract IDistServer build();

    protected IGlobalDistCallRateSchedulerFactory distCallPreBatchSchedulerFactory() {
        if (distCallPreSchedulerFactoryClass == null) {
            log.info("DistCallPreBatchSchedulerFactory[DEFAULT] loaded");
            return IGlobalDistCallRateSchedulerFactory.DEFAULT;
        } else {
            Map<String, IGlobalDistCallRateSchedulerFactory> factoryMap =
                load(IGlobalDistCallRateSchedulerFactory.class);
            IGlobalDistCallRateSchedulerFactory factory =
                factoryMap.getOrDefault(distCallPreSchedulerFactoryClass, IGlobalDistCallRateSchedulerFactory.DEFAULT);
            log.info("DistCallPreBatchSchedulerFactory[{}] loaded",
                factory != IGlobalDistCallRateSchedulerFactory.DEFAULT ? distCallPreSchedulerFactoryClass : "DEFAULT");
            return factory;
        }
    }

    public static final class InProcDistServerBuilder extends DistServerBuilder<InProcDistServerBuilder> {

        public InProcDistServerBuilder() {
        }

        @Override
        public IDistServer build() {
            return new DistServer(storeClient, settingProvider, eventCollector, crdtService,
                distCallPreBatchSchedulerFactory()) {
                @Override
                protected IRPCServer buildRPCServer(DistService distService) {
                    return IRPCServer.inProcServerBuilder()
                        .serviceUniqueName(SERVICE_NAME)
                        .executor(executor)
                        .bluePrint(RPCBluePrint.INSTANCE)
                        .bindService(distService)
                        .build();
                }
            };
        }
    }

    abstract static class InterProcDistServerBuilder<T extends InterProcDistServerBuilder<T>>
        extends DistServerBuilder<T> {
        protected String id;
        protected String host;
        protected Integer port;
        protected EventLoopGroup bossEventLoopGroup;
        protected EventLoopGroup workerEventLoopGroup;

        @SuppressWarnings("unchecked")
        public T id(@NonNull String id) {
            this.id = id;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T host(@NonNull String host) {
            Preconditions.checkArgument(!"0.0.0.0".equals(host), "Invalid host ip");
            this.host = host;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T port(@NonNull Integer port) {
            this.port = port;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T bossEventLoopGroup(EventLoopGroup bossEventLoopGroup) {
            this.bossEventLoopGroup = bossEventLoopGroup;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T workerEventLoopGroup(EventLoopGroup workerEventLoopGroup) {
            this.workerEventLoopGroup = workerEventLoopGroup;
            return (T) this;
        }
    }

    public static final class NonSSLDistServerBuilder extends InterProcDistServerBuilder<NonSSLDistServerBuilder> {

        @Override
        public IDistServer build() {
            return new DistServer(storeClient, settingProvider, eventCollector, crdtService,
                distCallPreBatchSchedulerFactory()) {
                @Override
                protected IRPCServer buildRPCServer(DistService distService) {
                    return IRPCServer.nonSSLServerBuilder()
                        .serviceUniqueName(SERVICE_NAME)
                        .executor(executor)
                        .bluePrint(RPCBluePrint.INSTANCE)
                        .bindService(distService)
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

    public static final class SSLDistServerBuilder extends InterProcDistServerBuilder<SSLDistServerBuilder> {
        private File serviceIdentityCertFile;
        private File privateKeyFile;
        private File trustCertsFile;

        public SSLDistServerBuilder serviceIdentityCertFile(@NonNull File serviceIdentityCertFile) {
            this.serviceIdentityCertFile = serviceIdentityCertFile;
            CertInfo certInfo = CertInfo.parse(serviceIdentityCertFile);
            Preconditions.checkArgument(certInfo.serverAuth, "Not server auth cert");
            return this;
        }

        public SSLDistServerBuilder privateKeyFile(@NonNull File privateKeyFile) {
            this.privateKeyFile = privateKeyFile;
            return this;
        }

        public SSLDistServerBuilder trustCertsFile(@NonNull File trustCertsFile) {
            this.trustCertsFile = trustCertsFile;
            return this;
        }

        @Override
        public IDistServer build() {
            Preconditions.checkNotNull(serviceIdentityCertFile);
            Preconditions.checkNotNull(privateKeyFile);
            Preconditions.checkNotNull(trustCertsFile);
            return new DistServer(storeClient, settingProvider, eventCollector, crdtService,
                distCallPreBatchSchedulerFactory()) {
                @Override
                protected IRPCServer buildRPCServer(DistService distService) {
                    return IRPCServer.sslServerBuilder()
                        .executor(executor)
                        .bluePrint(RPCBluePrint.INSTANCE)
                        .bindService(distService)
                        .id(id)
                        .serviceUniqueName(SERVICE_NAME)
                        .host(host)
                        .port(port)
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
