package com.zachary.bifromq.sessiondict.server;

import com.zachary.bifromq.baserpc.AckStream;
import com.zachary.bifromq.sessiondict.PipelineUtil;
import com.zachary.bifromq.sessiondict.rpc.proto.KillReply;
import com.zachary.bifromq.sessiondict.rpc.proto.KillRequest;
import com.zachary.bifromq.sessiondict.rpc.proto.Ping;
import com.zachary.bifromq.sessiondict.rpc.proto.Quit;
import com.zachary.bifromq.sessiondict.rpc.proto.SessionDictionaryServiceGrpc;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.zachary.bifromq.type.ClientInfo;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.baserpc.UnaryResponse.response;
import static com.zachary.bifromq.metrics.TenantMeter.gauging;
import static com.zachary.bifromq.metrics.TenantMeter.stopGauging;
import static com.zachary.bifromq.metrics.TenantMetric.MqttConnectionGauge;
import static com.zachary.bifromq.type.MQTTClientInfoConstants.MQTT_CLIENT_ADDRESS_KEY;
import static com.zachary.bifromq.type.MQTTClientInfoConstants.MQTT_CLIENT_ID_KEY;
import static com.zachary.bifromq.type.MQTTClientInfoConstants.MQTT_TYPE_VALUE;
import static com.zachary.bifromq.type.MQTTClientInfoConstants.MQTT_USER_ID_KEY;
import static com.github.benmanes.caffeine.cache.RemovalCause.EXPIRED;
import static com.github.benmanes.caffeine.cache.RemovalCause.SIZE;

@Slf4j
public class SessionDictionaryService extends SessionDictionaryServiceGrpc.SessionDictionaryServiceImplBase {

    private final Cache<AckStream<Ping, Quit>, ClientInfo> kickedPipelines = Caffeine.newBuilder()
        .expireAfterWrite(5, TimeUnit.SECONDS)
        .maximumSize(1000_000)
        .removalListener((RemovalListener<AckStream<Ping, Quit>, ClientInfo>) (key, value, cause) -> {
            if (cause == EXPIRED || cause == SIZE) {
                if (key != null) {
                    key.send(Quit.newBuilder().setKiller(value).build());
                    key.onCompleted();
                }
            }
        })
        .build();
    // tenantId -> userId/clientId -> AckPipeline<Ping, Quit>
    private final Map<String, Map<String, Registration>> registry = new ConcurrentHashMap<>();

    @Override
    public StreamObserver<Ping> join(StreamObserver<Quit> responseObserver) {
        return new Registration(responseObserver);
    }

    @Override
    public void kill(KillRequest request, StreamObserver<KillReply> responseObserver) {
        response(tenantId -> {
            Registration reg = registry.getOrDefault(tenantId, Collections.emptyMap())
                .get(toRegKey(request.getUserId(), request.getClientId()));
            if (reg != null) {
                reg.quit(request.getKiller());
            }
            return CompletableFuture.completedFuture(KillReply.newBuilder()
                .setReqId(request.getReqId())
                .setResult(KillReply.Result.OK)
                .build());
        }, responseObserver);
    }

    void close() {
        registry.forEach((tenantId, sessions) ->
            sessions.forEach((sessionId, ackPipeline) -> ackPipeline.onCompleted()));
    }

    private class Registration extends AckStream<Ping, Quit> {
        private final String regKey;
        private final ClientInfo clientInfo;

        protected Registration(StreamObserver<Quit> responseObserver) {
            super(responseObserver);
            clientInfo = PipelineUtil.decode(metadata.get(PipelineUtil.CLIENT_INFO));
            assert MQTT_TYPE_VALUE.equals(clientInfo.getType());
            log.trace("Receive session registering, tenantId={}, userId={}, clientId={}, addr={}",
                tenantId, clientInfo.getMetadataOrDefault(MQTT_USER_ID_KEY, ""),
                clientInfo.getMetadataOrDefault(MQTT_CLIENT_ID_KEY, ""),
                clientInfo.getMetadataOrDefault(MQTT_CLIENT_ADDRESS_KEY, ""));
            regKey =
                toRegKey(clientInfo.getMetadataOrDefault(MQTT_USER_ID_KEY, ""),
                    clientInfo.getMetadataOrDefault(MQTT_CLIENT_ID_KEY, ""));
            registry.compute(tenantId, (t, m) -> {
                if (m == null) {
                    m = new HashMap<>();
                    gauging(tenantId, MqttConnectionGauge,
                        () -> registry.getOrDefault(tenantId, Collections.emptyMap()).size());
                }
                m.compute(regKey, (r, oldPipeline) -> {
                    if (oldPipeline != null) {
                        oldPipeline.quit(clientInfo);
                        kickedPipelines.put(oldPipeline, clientInfo);
                    }
                    return this;
                });
                return m;
            });

            this.ack().doOnComplete(this::leave).subscribe();
        }

        public void quit(ClientInfo killer) {
            long reqId = System.nanoTime();
            if (log.isTraceEnabled()) {
                log.trace("Quit pipeline: reqId={}, tenantId={}, userId={}, clientId={}, addr={}",
                    reqId, tenantId, clientInfo.getMetadataOrDefault(MQTT_USER_ID_KEY, ""),
                    clientInfo.getMetadataOrDefault(MQTT_CLIENT_ID_KEY, ""),
                    clientInfo.getMetadataOrDefault(MQTT_CLIENT_ADDRESS_KEY, ""));
            }
            send(Quit.newBuilder().setKiller(killer).build());
        }

        private void leave() {
            registry.compute(tenantId, (t, m) -> {
                if (m == null) {
                    stopGauging(tenantId, MqttConnectionGauge);
                    return null;
                } else {
                    m.remove(regKey, this);
                    if (m.size() == 0) {
                        stopGauging(tenantId, MqttConnectionGauge);
                        return null;
                    }
                    return m;
                }
            });
            kickedPipelines.invalidate(this);
        }
    }

    private String toRegKey(String userId, String clientId) {
        return userId + "/" + clientId;
    }
}
