package com.zachary.bifromq.mqtt;

import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.baserpc.CertInfo;
import com.zachary.bifromq.dist.client.IDistClient;
import com.zachary.bifromq.inbox.client.IInboxReaderClient;
import com.zachary.bifromq.mqtt.service.ILocalSessionBrokerServer;
import com.zachary.bifromq.plugin.authprovider.IAuthProvider;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import com.zachary.bifromq.retain.client.IRetainServiceClient;
import com.zachary.bifromq.sessiondict.client.ISessionDictionaryClient;
import com.google.common.base.Preconditions;
import io.netty.channel.EventLoopGroup;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.Executor;

@Slf4j
public abstract class MQTTBrokerBuilder<T extends MQTTBrokerBuilder<?>> {
    protected String host;
    protected MQTTBrokerOptions options = new MQTTBrokerOptions();
    protected EventLoopGroup bossGroup;
    protected EventLoopGroup workerGroup;
    protected Executor ioExecutor;
    protected Executor bgTaskExecutor;

    protected IAuthProvider authProvider;
    protected IEventCollector eventCollector;
    protected ISettingProvider settingProvider;

    protected IDistClient distClient;
    protected IInboxReaderClient inboxClient;
    protected IRetainServiceClient retainClient;
    protected ISessionDictionaryClient sessionDictClient;

    protected Optional<ConnListenerBuilder.TCPConnListenerBuilder> tcpListenerBuilder = Optional.empty();
    protected Optional<ConnListenerBuilder.TLSConnListenerBuilder> tlsListenerBuilder = Optional.empty();
    protected Optional<ConnListenerBuilder.WSConnListenerBuilder> wsListenerBuilder = Optional.empty();
    protected Optional<ConnListenerBuilder.WSSConnListenerBuilder> wssListenerBuilder = Optional.empty();

    public T host(String host) {
        this.host = host;
        return (T) this;
    }

    public T ioExecutor(Executor executor) {
        this.ioExecutor = executor;
        return (T) this;
    }

    public T bgTaskExecutor(Executor executor) {
        this.bgTaskExecutor = executor;
        return (T) this;
    }

    public T bossGroup(EventLoopGroup bossGroup) {
        this.bossGroup = bossGroup;
        return (T) this;
    }

    public T workerGroup(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
        return (T) this;
    }

    public T connectTimeoutSeconds(int connectTimeoutSeconds) {
        this.options.connectTimeoutSeconds(connectTimeoutSeconds);
        return (T) this;
    }

    public T connectRateLimit(int connectPerSecond) {
        this.options.connectRateLimit(connectPerSecond);
        return (T) this;
    }

    public T disconnectRate(int disconnectRate) {
        this.options.disconnectRate(disconnectRate);
        return (T) this;
    }

    public T maxResendTimes(int maxResendTimes) {
        this.options.maxResendTimes(maxResendTimes);
        return (T) this;
    }

    public T defaultKeepAliveSeconds(int defaultKeepAliveSeconds) {
        this.options.defaultKeepAliveSeconds(defaultKeepAliveSeconds);
        return (T) this;
    }

    public T writeLimit(long writeLimit) {
        this.options.writeLimit(writeLimit);
        return (T) this;
    }

    public T readLimit(long readLimit) {
        this.options.readLimit(readLimit);
        return (T) this;
    }

    public T maxBytesInMessage(int maxBytesInMessage) {
        this.options.maxBytesInMessage(maxBytesInMessage);
        return (T) this;
    }

    public T qos2ConfirmWindowSeconds(int qos2ConfirmWindowSec) {
        this.options.qos2ConfirmWindowSeconds(qos2ConfirmWindowSec);
        return (T) this;
    }

    public T authProvider(IAuthProvider auth) {
        this.authProvider = auth;
        return (T) this;
    }

    public T eventCollector(IEventCollector eventCollector) {
        this.eventCollector = eventCollector;
        return (T) this;
    }

    public T settingProvider(ISettingProvider settingProvider) {
        this.settingProvider = settingProvider;
        return (T) this;
    }

    public T distClient(IDistClient distClient) {
        this.distClient = distClient;
        return (T) this;
    }

    public T inboxReader(IInboxReaderClient inboxReader) {
        this.inboxClient = inboxReader;
        return (T) this;
    }

    public T sessionDictClient(ISessionDictionaryClient sessionDictClient) {
        this.sessionDictClient = sessionDictClient;
        return (T) this;
    }

    public T retainClient(IRetainServiceClient retainClient) {
        this.retainClient = retainClient;
        return (T) this;
    }

    public ConnListenerBuilder.TCPConnListenerBuilder buildTcpConnListener() {
        if (!tcpListenerBuilder.isPresent()) {
            tcpListenerBuilder = Optional.of(new ConnListenerBuilder.TCPConnListenerBuilder(this));
        }
        return tcpListenerBuilder.get();
    }

    public ConnListenerBuilder.TLSConnListenerBuilder buildTLSConnListener() {
        if (!tlsListenerBuilder.isPresent()) {
            tlsListenerBuilder = Optional.of(new ConnListenerBuilder.TLSConnListenerBuilder(this));
        }
        return tlsListenerBuilder.get();
    }

    public ConnListenerBuilder.WSConnListenerBuilder buildWSConnListener() {
        if (!wsListenerBuilder.isPresent()) {
            wsListenerBuilder = Optional.of(new ConnListenerBuilder.WSConnListenerBuilder(this));
        }
        return wsListenerBuilder.get();
    }

    public ConnListenerBuilder.WSSConnListenerBuilder buildWSSConnListener() {
        if (!wssListenerBuilder.isPresent()) {
            wssListenerBuilder = Optional.of(new ConnListenerBuilder.WSSConnListenerBuilder(this));
        }
        return wssListenerBuilder.get();
    }

    public abstract IMQTTBroker build();

    public static final class InProcBrokerBuilder extends MQTTBrokerBuilder<InProcBrokerBuilder> {

        @Override
        public IMQTTBroker build() {
            return new MQTTBroker(host,
                    bossGroup,
                    workerGroup,
                    options,
                    authProvider,
                    eventCollector,
                    settingProvider,
                    distClient,
                    inboxClient,
                    retainClient,
                    sessionDictClient,
                    tcpListenerBuilder.orElse(null),
                    tlsListenerBuilder.orElse(null),
                    wsListenerBuilder.orElse(null),
                    wssListenerBuilder.orElse(null)
            ) {
                @Override
                protected ILocalSessionBrokerServer buildLocalSessionBroker() {
                    return ILocalSessionBrokerServer.inProcBrokerBuilder()
                            .executor(ioExecutor)
                            .build();
                }
            };
        }
    }

    abstract static class InterProcBrokerBuilder<T extends InterProcBrokerBuilder<?>> extends MQTTBrokerBuilder<T> {
        protected String serverId;
        protected String rpcBindAddr;
        protected Integer port;
        protected ICRDTService crdtService;
        protected EventLoopGroup rpcWorkerGroup;

        /**
         * The id of the server instance
         *
         * @param serverId
         * @return
         */
        public T serverId(String serverId) {
            this.serverId = serverId;
            return (T) this;
        }

        public T rpcBindAddr(String rpcBindAddr) {
            this.rpcBindAddr = rpcBindAddr;
            return (T) this;
        }

        public T port(@NonNull Integer port) {
            this.port = port;
            return (T) this;
        }

        public T crdtService(ICRDTService crdtService) {
            this.crdtService = crdtService;
            return (T) this;
        }

        public T rpcWorkerGroup(EventLoopGroup workerGroup) {
            this.rpcWorkerGroup = workerGroup;
            return (T) this;
        }
    }

    public static final class NonSSLBrokerBuilder extends InterProcBrokerBuilder<NonSSLBrokerBuilder> {
        @Override
        public IMQTTBroker build() {
            return new MQTTBroker(host,
                    bossGroup,
                    workerGroup,
                    options,
                    authProvider,
                    eventCollector,
                    settingProvider,
                    distClient,
                    inboxClient,
                    retainClient,
                    sessionDictClient,
                    tcpListenerBuilder.orElse(null),
                    tlsListenerBuilder.orElse(null),
                    wsListenerBuilder.orElse(null),
                    wssListenerBuilder.orElse(null)
            ) {
                @Override
                protected ILocalSessionBrokerServer buildLocalSessionBroker() {
                    return ILocalSessionBrokerServer.nonSSLBrokerBuilder()
                            .serverId(serverId)
                            .host(rpcBindAddr)
                            .port(port)
                            .executor(ioExecutor)
                            .bossEventLoopGroup(bossGroup)
                            .workerEventLoopGroup(rpcWorkerGroup)
                            .crdtService(crdtService)
                            .build();
                }
            };
        }
    }

    public static final class SSLBrokerBuilder extends InterProcBrokerBuilder<SSLBrokerBuilder> {
        private @NonNull File serviceIdentityCertFile;
        private @NonNull File privateKeyFile;
        private @NonNull File trustCertsFile;

        public SSLBrokerBuilder serviceIdentityCertFile(@NonNull File serviceIdentityCertFile) {
            this.serviceIdentityCertFile = serviceIdentityCertFile;
            CertInfo certInfo = CertInfo.parse(serviceIdentityCertFile);
            Preconditions.checkArgument(certInfo.serverAuth, "Not server auth cert");
            return this;
        }

        public SSLBrokerBuilder privateKeyFile(@NonNull File privateKeyFile) {
            this.privateKeyFile = privateKeyFile;
            return this;
        }

        public SSLBrokerBuilder trustCertsFile(@NonNull File trustCertsFile) {
            this.trustCertsFile = trustCertsFile;
            return this;
        }

        @Override
        public IMQTTBroker build() {
            return new MQTTBroker(host,
                    bossGroup,
                    workerGroup,
                    options,
                    authProvider,
                    eventCollector,
                    settingProvider,
                    distClient,
                    inboxClient,
                    retainClient,
                    sessionDictClient,
                    tcpListenerBuilder.orElse(null),
                    tlsListenerBuilder.orElse(null),
                    wsListenerBuilder.orElse(null),
                    wssListenerBuilder.orElse(null)
            ) {
                @Override
                protected ILocalSessionBrokerServer buildLocalSessionBroker() {
                    return ILocalSessionBrokerServer.sslBrokerBuilder()
                            .serverId(serverId)
                            .host(rpcBindAddr)
                            .port(port)
                            .executor(ioExecutor)
                            .bossEventLoopGroup(bossGroup)
                            .workerEventLoopGroup(rpcWorkerGroup)
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
