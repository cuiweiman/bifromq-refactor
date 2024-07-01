package com.zachary.bifromq.baserpc;

import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.baseenv.EnvProvider;
import com.zachary.bifromq.baserpc.interceptor.TenantAwareClientInterceptor;
import com.zachary.bifromq.baserpc.loadbalancer.IUpdateListener;
import com.zachary.bifromq.baserpc.loadbalancer.TrafficDirectiveLoadBalancerProvider;
import com.zachary.bifromq.baserpc.nameresolver.TrafficGovernorNameResolverProvider;
import com.zachary.bifromq.baserpc.trafficgovernor.IRPCServiceTrafficDirector;
import com.zachary.bifromq.baserpc.utils.BehaviorSubject;
import com.zachary.bifromq.baserpc.utils.NettyUtil;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.MoreExecutors;
import io.grpc.Channel;
import io.grpc.ConnectivityState;
import io.grpc.LoadBalancerProvider;
import io.grpc.LoadBalancerRegistry;
import io.grpc.ManagedChannel;
import io.grpc.MethodDescriptor;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.io.File;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;

public class RPCClientBuilder {
    private String serviceUniqueName;
    private BluePrint bluePrint;
    private ChannelBuilder channelBuilder = new InProcChannelBuilder(this);
    private Executor executor;

    RPCClientBuilder() {
    }

    public RPCClientBuilder serviceUniqueName(@NonNull String serviceUniqueName) {
        this.serviceUniqueName = serviceUniqueName;
        return this;
    }

    public RPCClientBuilder bluePrint(@NonNull BluePrint bluePrint) {
        this.bluePrint = bluePrint;
        return this;
    }

    public RPCClientBuilder executor(Executor executor) {
        this.executor = executor;
        return this;
    }

    public SSLChannelBuilder sslChannel() {
        if (!(channelBuilder instanceof SSLChannelBuilder)) {
            channelBuilder = new SSLChannelBuilder(this);
        }
        return (SSLChannelBuilder) channelBuilder;
    }

    public NonSSLChannelBuilder nonSSLChannel() {
        if (!(channelBuilder instanceof NonSSLChannelBuilder)) {
            channelBuilder = new NonSSLChannelBuilder(this);
        }
        return (NonSSLChannelBuilder) channelBuilder;
    }

    public InProcChannelBuilder inProcChannel() {
        if (!(channelBuilder instanceof InProcChannelBuilder)) {
            channelBuilder = new InProcChannelBuilder(this);
        }
        return (InProcChannelBuilder) channelBuilder;
    }

    public RPCClient build() {
        RPCClient.ChannelHolder channelHolder = new RPCClient.ChannelHolder() {
            private final boolean inProc;
            private final ManagedChannel channel;
            private final BehaviorSubject<IUpdateListener.IServerSelector> serverSelectorSubject =
                BehaviorSubject.create();
            private final BehaviorSubject<IRPCClient.ConnState> connStateSubject = BehaviorSubject.create();
            private final Observable<Set<String>> serverListSubject;
            private LoadBalancerProvider lbProvider;
            private final Executor rpcExecutor;
            private final boolean needShutdownExecutor;

            {
                this.needShutdownExecutor = executor == null;
                if (needShutdownExecutor) {
                    int threadNum = Math.max(EnvProvider.INSTANCE.availableProcessors(), 1);
                    rpcExecutor = ExecutorServiceMetrics
                        .monitor(Metrics.globalRegistry, new ThreadPoolExecutor(threadNum, threadNum,
                                0L, TimeUnit.MILLISECONDS,
                                new LinkedTransferQueue<>(),
                                EnvProvider.INSTANCE.newThreadFactory(serviceUniqueName + "_client-executor", true)),
                            serviceUniqueName + "_client-executor");
                } else {
                    rpcExecutor = executor;
                }

                if (channelBuilder instanceof InProcChannelBuilder) {
                    inProc = true;
                    serverListSubject = BehaviorSubject.createDefault(singleton(serviceUniqueName));
                    serverSelectorSubject.onNext(new IUpdateListener.IServerSelector() {
                        @Override
                        public boolean direct(String tenantId, String serverId,
                                              MethodDescriptor<?, ?> methodDescriptor) {
                            return true;
                        }

                        @Override
                        public Optional<String> hashing(String tenantId, String key,
                                                        MethodDescriptor<?, ?> methodDescriptor) {
                            return Optional.of(serviceUniqueName);
                        }

                        @Override
                        public Optional<String> roundRobin(String tenantId, MethodDescriptor<?, ?> methodDescriptor) {
                            return Optional.of(serviceUniqueName);
                        }

                        @Override
                        public Optional<String> random(String tenantId, MethodDescriptor<?, ?> methodDescriptor) {
                            return Optional.of(serviceUniqueName);
                        }
                    });
                    channel = InProcessChannelBuilder.forName(serviceUniqueName)
                        .intercept(new TenantAwareClientInterceptor(serviceUniqueName))
                        .executor(rpcExecutor)
                        .build();
                } else {
                    inProc = false;
                    InterProcChannelBuilder<?> ncBuilder = (InterProcChannelBuilder<?>) channelBuilder;
                    lbProvider = new TrafficDirectiveLoadBalancerProvider(bluePrint, serverSelectorSubject::onNext);
                    IRPCServiceTrafficDirector trafficDirector = IRPCServiceTrafficDirector
                        .newInstance(serviceUniqueName, ncBuilder.crdtService);
                    serverListSubject = trafficDirector.serverList()
                        .map(sl -> sl.stream().map(s -> s.id).collect(Collectors.toSet()));
                    LoadBalancerRegistry
                        .getDefaultRegistry()
                        .register(lbProvider);
                    NettyChannelBuilder channelBuilder = NettyChannelBuilder
                        .forTarget(serviceUniqueName)
                        .keepAliveTime(ncBuilder.keepAliveInSec <= 0 ?
                            600 : ncBuilder.keepAliveInSec, TimeUnit.SECONDS)
                        .keepAliveWithoutCalls(true)
                        .idleTimeout(ncBuilder.idleTimeoutInSec <= 0 ?
                            (365 * 24 * 3600) : ncBuilder.idleTimeoutInSec, TimeUnit.SECONDS)
                        .nameResolverFactory(TrafficGovernorNameResolverProvider
                            .builder()
                            .serviceUniqueName(serviceUniqueName)
                            .trafficDirector(trafficDirector)
                            .build())
                        .defaultLoadBalancingPolicy(lbProvider.getPolicyName())
                        .executor(rpcExecutor);
                    if (ncBuilder instanceof SSLChannelBuilder) {
                        channelBuilder
                            .negotiationType(NegotiationType.TLS)
                            .intercept(new TenantAwareClientInterceptor())
                            .sslContext(((SSLChannelBuilder) ncBuilder).sslContext())
                            .overrideAuthority(serviceUniqueName);
                    } else {
                        channelBuilder
                            .negotiationType(NegotiationType.PLAINTEXT)
                            .intercept(new TenantAwareClientInterceptor());
                    }
                    if (ncBuilder.eventLoopGroup != null) {
                        channelBuilder.eventLoopGroup(ncBuilder.eventLoopGroup)
                            .channelType(NettyUtil.determineSocketChannelClass(ncBuilder.eventLoopGroup));
                    }
                    channel = channelBuilder.build();
                }
                ConnStateListener connStateListener = (server, connState) ->
                    connStateSubject.onNext(IRPCClient.ConnState.values()[connState.ordinal()]);
                startStateListener(connStateListener);
            }

            @Override
            public boolean inProc() {
                return inProc;
            }

            @Override
            public Executor rpcExecutor() {
                return rpcExecutor;
            }

            @Override
            public Channel channel() {
                return channel;
            }

            @Override
            public Observable<IRPCClient.ConnState> connState() {
                return connStateSubject;
            }

            @Override
            public Observable<Set<String>> serverList() {
                return serverListSubject;
            }

            @Override
            public Observable<IUpdateListener.IServerSelector> serverSelectorObservable() {
                return serverSelectorSubject;
            }

            @Override
            public boolean shutdown(long timeout, TimeUnit unit) {
                if (channel.isShutdown()) {
                    return true;
                }
                long nsLeft = TimeUnit.NANOSECONDS.convert(timeout, unit);
                long timeoutNS = nsLeft;
                if (lbProvider != null) {
                    LoadBalancerRegistry.getDefaultRegistry().deregister(lbProvider);
                }
                boolean result;
                try {
                    long start = System.nanoTime();
                    channel.shutdownNow();
                    result = channel.awaitTermination(timeout / 2, unit);
                    nsLeft -= System.nanoTime() - start;
                } catch (InterruptedException e) {
                    result = channel.isTerminated();
                }
                if (needShutdownExecutor) {
                    ExecutorService executorService = (ExecutorService) rpcExecutor;
                    result &= MoreExecutors.shutdownAndAwaitTermination(executorService,
                        Math.max(timeoutNS / 2, nsLeft), TimeUnit.NANOSECONDS);
                }
                serverSelectorSubject.onComplete();
                connStateSubject.onComplete();
                return result;
            }

            private void startStateListener(ConnStateListener connStateListener) {
                ConnectivityState currentState = this.channel.getState(true);
                connStateListener.onChange(serviceUniqueName, currentState);
                if (currentState != ConnectivityState.SHUTDOWN) {
                    this.channel.notifyWhenStateChanged(currentState, () -> startStateListener(connStateListener));
                }
            }
        };
        return new RPCClient(serviceUniqueName, bluePrint, channelHolder);
    }

    private abstract static class ChannelBuilder {
        protected final RPCClientBuilder parentBuilder;

        ChannelBuilder(RPCClientBuilder builder) {
            parentBuilder = builder;
        }

        public RPCClientBuilder buildChannel() {
            return parentBuilder;
        }
    }

    public static class InProcChannelBuilder extends ChannelBuilder {
        InProcChannelBuilder(RPCClientBuilder builder) {
            super(builder);
        }
    }

    public abstract static class InterProcChannelBuilder<T extends InterProcChannelBuilder<T>>
        extends ChannelBuilder {
        private ICRDTService crdtService;
        private EventLoopGroup eventLoopGroup;
        private long keepAliveInSec;
        private long idleTimeoutInSec;

        InterProcChannelBuilder(RPCClientBuilder builder) {
            super(builder);
        }

        public T crdtService(@NonNull ICRDTService crdtService) {
            this.crdtService = crdtService;
            return (T) this;
        }

        public T eventLoopGroup(EventLoopGroup eventLoopGroup) {
            this.eventLoopGroup = eventLoopGroup;
            return (T) this;
        }

        public T keepAliveInSec(long keepAliveInSec) {
            this.keepAliveInSec = keepAliveInSec;
            return (T) this;
        }

        public T idleTimeoutInSec(long idleTimeoutInSec) {
            this.idleTimeoutInSec = idleTimeoutInSec;
            return (T) this;
        }

    }

    public static class NonSSLChannelBuilder extends InterProcChannelBuilder<NonSSLChannelBuilder> {
        NonSSLChannelBuilder(RPCClientBuilder builder) {
            super(builder);
        }
    }

    public static class SSLChannelBuilder extends InterProcChannelBuilder<SSLChannelBuilder> {
        private File serviceIdentityCertFile;
        private File privateKeyFile;
        private File trustCertsFile;

        SSLChannelBuilder(RPCClientBuilder builder) {
            super(builder);
        }

        public SSLChannelBuilder serviceIdentityCertFile(@NonNull File serviceIdentityCertFile) {
            this.serviceIdentityCertFile = serviceIdentityCertFile;
            CertInfo certInfo = CertInfo.parse(serviceIdentityCertFile);
            if (!certInfo.clientAuth) {
                throw new IllegalArgumentException("Not client auth cert");
            }
            return this;
        }

        public SSLChannelBuilder privateKeyFile(@NonNull File privateKeyFile) {
            this.privateKeyFile = privateKeyFile;
            return this;
        }

        public SSLChannelBuilder trustCertsFile(@NonNull File trustCertsFile) {
            this.trustCertsFile = trustCertsFile;
            return this;
        }

        SslContext sslContext() {
            Preconditions.checkNotNull(serviceIdentityCertFile);
            Preconditions.checkNotNull(privateKeyFile);
            Preconditions.checkNotNull(trustCertsFile);
            try {
                SslContextBuilder builder = GrpcSslContexts.forClient();
                builder.trustManager(trustCertsFile);
                builder.keyManager(serviceIdentityCertFile, privateKeyFile);
                return builder.build();
            } catch (Exception e) {
                throw new RuntimeException("Unable initialize SSLContext", e);
            }
        }
    }
}

