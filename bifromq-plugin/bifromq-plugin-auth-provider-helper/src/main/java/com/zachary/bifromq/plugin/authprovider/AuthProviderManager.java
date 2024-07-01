package com.zachary.bifromq.plugin.authprovider;

import com.zachary.bifromq.plugin.authprovider.type.MQTT3AuthData;
import com.zachary.bifromq.plugin.authprovider.type.MQTT3AuthResult;
import com.zachary.bifromq.plugin.authprovider.type.MQTTAction;
import com.zachary.bifromq.plugin.authprovider.type.Reject;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.accessctrl.AccessControlError;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import com.zachary.bifromq.type.ClientInfo;
import com.google.common.base.Preconditions;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginManager;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.zachary.bifromq.plugin.eventcollector.ThreadLocalEventPool.getLocal;
import static com.zachary.bifromq.plugin.settingprovider.Setting.ByPassPermCheckError;

@Slf4j
public class AuthProviderManager implements IAuthProvider {
    private final AtomicBoolean stopped = new AtomicBoolean();
    private final IAuthProvider delegate;
    private final ISettingProvider settingProvider;
    private final IEventCollector eventCollector;
    private MetricManager metricMgr;

    public AuthProviderManager(String authProviderFQN,
                               PluginManager pluginMgr,
                               ISettingProvider settingProvider,
                               IEventCollector eventCollector) {
        this.settingProvider = settingProvider;
        this.eventCollector = eventCollector;
        Map<String, IAuthProvider> availAuthProviders = pluginMgr.getExtensions(IAuthProvider.class)
            .stream().collect(Collectors.toMap(e -> e.getClass().getName(), e -> e));
        if (availAuthProviders.isEmpty()) {
            log.warn("No auth provider plugin available, use DEV ONLY one instead");
            delegate = new DevOnlyAuthProvider();
        } else {
            if (authProviderFQN == null) {
                log.warn("Auth provider plugin type are not specified, use DEV ONLY one instead");
                delegate = new DevOnlyAuthProvider();
            } else {
                Preconditions.checkArgument(availAuthProviders.containsKey(authProviderFQN),
                    String.format("Auth Provider Plugin '%s' not found", authProviderFQN));
                log.info("Auth provider plugin type: {}", authProviderFQN);
                delegate = availAuthProviders.get(authProviderFQN);
            }
        }
        init();
    }

    private void init() {
        metricMgr = new MetricManager(delegate.getClass().getName());
    }

    @Override
    public CompletableFuture<MQTT3AuthResult> auth(MQTT3AuthData authData) {
        assert !stopped.get();
        long start = System.nanoTime();
        try {
            return delegate.auth(authData)
                .handle((v, e) -> {
                    if (e != null) {
                        return MQTT3AuthResult.newBuilder()
                            .setReject(Reject.newBuilder()
                                .setCode(Reject.Code.Error)
                                .setReason(e.getMessage() != null ? e.getMessage() : e.toString())
                                .build())
                            .build();
                    } else {
                        metricMgr.authCallTimer.record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
                        return v;
                    }
                });
        } catch (Throwable e) {
            metricMgr.authCallErrorCounter.increment();
            log.warn("Unexpected error", e);
            Reject.Builder rb = Reject.newBuilder().setCode(Reject.Code.Error);
            if (e.getMessage() != null) {
                rb.setReason(e.getMessage());
            }
            return CompletableFuture.completedFuture(MQTT3AuthResult.newBuilder()
                .setReject(Reject.newBuilder()
                    .setCode(Reject.Code.Error)
                    .setReason(e.getMessage() != null ? e.getMessage() : e.toString())
                    .build())
                .build());
        }
    }

    @Override
    public CompletableFuture<Boolean> check(ClientInfo client, MQTTAction action) {
        assert !stopped.get();
        long start = System.nanoTime();
        try {
            return delegate.check(client, action)
                .exceptionally(e -> {
                    eventCollector.report(getLocal(AccessControlError.class).clientInfo(client).cause(e));
                    return settingProvider.provide(ByPassPermCheckError, client.getTenantId());
                })
                .thenApply(v -> {
                    metricMgr.checkCallTimer.record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
                    return v;
                });
        } catch (Throwable e) {
            metricMgr.checkCallErrorCounter.increment();
            eventCollector.report(getLocal(AccessControlError.class).clientInfo(client).cause(e));
            return CompletableFuture.completedFuture(
                settingProvider.provide(ByPassPermCheckError, client.getTenantId()));
        }
    }

    public void close() {
        if (stopped.compareAndSet(false, true)) {
            log.info("Closing auth provider manager");
            delegate.close();
            metricMgr.close();
            log.info("Auth provider manager stopped");
        }
    }

    private static class MetricManager {
        private final Timer authCallTimer;
        private final Counter authCallErrorCounter;
        private final Timer checkCallTimer;
        private final Counter checkCallErrorCounter;

        MetricManager(String id) {
            authCallTimer = Timer.builder("call.exec.timer")
                .tag("method", "AuthProvider/auth")
                .tag("type", id)
                .register(Metrics.globalRegistry);

            authCallErrorCounter = Counter.builder("call.exec.fail.count")
                .tag("method", "AuthProvider/auth")
                .tag("type", id)
                .register(Metrics.globalRegistry);

            checkCallTimer = Timer.builder("call.exec.timer")
                .tag("method", "AuthProvider/check")
                .tag("type", id)
                .register(Metrics.globalRegistry);

            checkCallErrorCounter = Counter.builder("call.exec.fail.count")
                .tag("method", "AuthProvider/check")
                .tag("type", id)
                .register(Metrics.globalRegistry);
        }

        void close() {
            Metrics.globalRegistry.remove(authCallTimer);
            Metrics.globalRegistry.remove(authCallErrorCounter);

            Metrics.globalRegistry.remove(checkCallTimer);
            Metrics.globalRegistry.remove(checkCallErrorCounter);
        }
    }
}
