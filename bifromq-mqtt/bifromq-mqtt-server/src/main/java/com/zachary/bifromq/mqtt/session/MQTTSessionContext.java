package com.zachary.bifromq.mqtt.session;

import com.zachary.bifromq.baserpc.utils.FutureTracker;
import com.zachary.bifromq.dist.client.IDistClient;
import com.zachary.bifromq.inbox.client.IInboxReaderClient;
import com.zachary.bifromq.mqtt.service.ILocalSessionBrokerServer;
import com.zachary.bifromq.plugin.authprovider.IAuthProvider;
import com.zachary.bifromq.plugin.authprovider.type.MQTT3AuthData;
import com.zachary.bifromq.plugin.authprovider.type.MQTT3AuthResult;
import com.zachary.bifromq.plugin.authprovider.type.MQTTAction;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import com.zachary.bifromq.retain.client.IRetainServiceClient;
import com.zachary.bifromq.sessiondict.client.ISessionDictionaryClient;
import com.zachary.bifromq.type.ClientInfo;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.google.common.base.Ticker;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import io.netty.channel.ChannelHandlerContext;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import com.zachary.bifromq.mqtt.service.ILocalSessionRegistry;

import java.time.Duration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
public final class MQTTSessionContext {
    private final IAuthProvider authProvider;
    public final ILocalSessionRegistry localSessionRegistry;
    public final IEventCollector eventCollector;
    public final ISettingProvider settingProvider;
    public final IDistClient distClient;
    public final IInboxReaderClient inboxClient;
    public final IRetainServiceClient retainClient;
    public final ISessionDictionaryClient sessionDictClient;
    public final String serverId;

    public final int maxResendTimes;
    public final int resendDelayMillis;
    public final int defaultKeepAliveTimeSeconds;
    // track under confirming id count per tenantId
    private final InUseQoS2MessageIds unreleasedQoS2MessageIds;
    // cache for client dist pipeline
    private final LoadingCache<ClientInfo, IRetainServiceClient.IClientPipeline> clientRetainPipelines;
    private final FutureTracker bgTaskTracker;
    private final Ticker ticker;
    private final Gauge retainPplnNumGauge;

    @Builder
    MQTTSessionContext(ILocalSessionBrokerServer brokerServer,
                       IAuthProvider authProvider,
                       IDistClient distClient,
                       IInboxReaderClient inboxClient,
                       IRetainServiceClient retainClient,
                       ISessionDictionaryClient sessionDictClient,
                       int maxResendTimes,
                       int resendDelayMillis,
                       int defaultKeepAliveTimeSeconds,
                       int qos2ConfirmWindowSeconds,
                       IEventCollector eventCollector,
                       ISettingProvider settingProvider,
                       Ticker ticker) {
        this.localSessionRegistry = brokerServer;
        this.serverId = brokerServer.id();
        this.authProvider = authProvider;
        this.eventCollector = eventCollector;
        this.settingProvider = settingProvider;
        this.distClient = distClient;
        this.inboxClient = inboxClient;
        this.retainClient = retainClient;
        this.sessionDictClient = sessionDictClient;
        this.unreleasedQoS2MessageIds = new InUseQoS2MessageIds(Duration.ofSeconds(qos2ConfirmWindowSeconds));
        this.maxResendTimes = maxResendTimes;
        this.resendDelayMillis = resendDelayMillis;
        this.defaultKeepAliveTimeSeconds = defaultKeepAliveTimeSeconds;
        this.clientRetainPipelines = Caffeine.newBuilder()
                .scheduler(Scheduler.systemScheduler())
                .expireAfterAccess(Duration.ofSeconds(30))
                .removalListener((RemovalListener<ClientInfo, IRetainServiceClient.IClientPipeline>)
                        (key, value, cause) -> {
                            if (value != null) {
                                log.trace("Close client retain pipeline: clientInfo={}, cause={}", key, cause);
                                value.close();
                            }
                        })
                .build(retainClient::open);
        retainPplnNumGauge = Gauge.builder("mqtt.server.ppln.retain.gauge", clientRetainPipelines::estimatedSize)
                .register(Metrics.globalRegistry);
        this.bgTaskTracker = new FutureTracker();
        this.ticker = ticker == null ? Ticker.systemTicker() : ticker;
    }

    public long nanoTime() {
        return ticker.read();
    }

    public IAuthProvider authProvider(ChannelHandlerContext ctx) {
        // a wrapper to ensure async fifo semantic for check call
        return new IAuthProvider() {
            private final LinkedHashMap<CompletableFuture<Boolean>, CompletableFuture<Boolean>> checkTaskQueue =
                    new LinkedHashMap<>();

            @Override
            public CompletableFuture<MQTT3AuthResult> auth(MQTT3AuthData authData) {
                return authProvider.auth(authData);
            }

            @Override
            public CompletableFuture<Boolean> check(ClientInfo client, MQTTAction action) {
                CompletableFuture<Boolean> task = authProvider.check(client, action);
                if (task.isDone()) {
                    return task;
                } else {
                    // queue it for fifo semantic
                    CompletableFuture<Boolean> onDone = new CompletableFuture<>();
                    // in case authProvider returns same future object;
                    task = task.thenApply(v -> v);
                    checkTaskQueue.put(task, onDone);
                    task.whenCompleteAsync((_v, _e) -> {
                        Iterator<CompletableFuture<Boolean>> itr = checkTaskQueue.keySet().iterator();
                        while (itr.hasNext()) {
                            CompletableFuture<Boolean> k = itr.next();
                            if (k.isDone()) {
                                CompletableFuture<Boolean> r = checkTaskQueue.get(k);
                                try {
                                    r.complete(k.join());
                                } catch (Throwable e) {
                                    r.completeExceptionally(e);
                                }
                                itr.remove();
                            } else {
                                break;
                            }
                        }
                    }, ctx.channel().eventLoop());
                    return onDone;
                }
            }
        };
    }

    public void addForConfirming(String tenantId, String channelId, int qos2MessageId) {
        unreleasedQoS2MessageIds.use(tenantId, channelId, qos2MessageId);
    }

    public boolean isConfirming(String tenantId, String channelId, int qos2MessageId) {
        return unreleasedQoS2MessageIds.inUse(tenantId, channelId, qos2MessageId);
    }

    public void confirm(String tenantId, String channelId, int qos2MessageId) {
        unreleasedQoS2MessageIds.release(tenantId, channelId, qos2MessageId);
    }

    public IRetainServiceClient.IClientPipeline getClientRetainPipeline(ClientInfo clientInfo) {
        return clientRetainPipelines.get(clientInfo);
    }

    public void closeClientRetainPipeline(ClientInfo clientInfo) {
        clientRetainPipelines.invalidate(clientInfo);
    }

    public void addBgTask(Supplier<CompletableFuture<Void>> taskSupplier) {
        bgTaskTracker.track(taskSupplier.get());
    }

    public void awaitBgTaskDone() {
        bgTaskTracker.whenComplete((v, e) -> log.debug("All bg tasks done")).join();
    }
}
