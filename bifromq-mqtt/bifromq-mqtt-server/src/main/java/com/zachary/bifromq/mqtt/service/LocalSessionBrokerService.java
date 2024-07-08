package com.zachary.bifromq.mqtt.service;

import static com.zachary.bifromq.baserpc.UnaryResponse.response;

import com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxReply;
import com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxRequest;
import com.zachary.bifromq.mqtt.inbox.rpc.proto.OnlineInboxBrokerGrpc;
import com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteReply;
import com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteRequest;
import com.zachary.bifromq.mqtt.session.IMQTTSession;
import com.zachary.bifromq.mqtt.session.v3.IMQTT3TransientSession;
import com.google.common.util.concurrent.RateLimiter;
import io.grpc.stub.StreamObserver;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final class LocalSessionBrokerService extends OnlineInboxBrokerGrpc.OnlineInboxBrokerImplBase {
    private final ConcurrentMap<String, IMQTTSession> sessionMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, IMQTT3TransientSession> transientSessionMap = new ConcurrentHashMap<>();

    private final Gauge connCountGauge;

    public LocalSessionBrokerService() {
        connCountGauge = Gauge.builder("mqtt.server.connection.gauge", sessionMap::size)
                .register(Metrics.globalRegistry);
    }

    @Override
    public StreamObserver<WriteRequest> write(StreamObserver<WriteReply> responseObserver) {
        return new LocalSessionWritePipeline(transientSessionMap, responseObserver);
    }

    @Override
    public void hasInbox(HasInboxRequest request, StreamObserver<HasInboxReply> responseObserver) {
        response(tenantId -> CompletableFuture.completedFuture(HasInboxReply
                .newBuilder()
                .setReqId(request.getReqId())
                .setResult(sessionMap.containsKey(request.getInboxId()))
                .build()), responseObserver);
    }

    void reg(String sessionId, IMQTTSession session) {
        sessionMap.putIfAbsent(sessionId, session);
        if (session instanceof IMQTT3TransientSession) {
            transientSessionMap.putIfAbsent(sessionId, (IMQTT3TransientSession) session);
        }
    }

    boolean unreg(String sessionId, IMQTTSession session) {
        transientSessionMap.remove(sessionId);
        return sessionMap.remove(sessionId, session);
    }

    public CompletableFuture<Void> disconnectAll(int disconnectRate) {
        RateLimiter limiter = RateLimiter.create(Math.max(1, disconnectRate));
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (String sessionId : sessionMap.keySet()) {
            limiter.acquire();
            futures.add(disconnect(sessionId));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    public void close() {
        Metrics.globalRegistry.remove(connCountGauge);
    }

    private CompletableFuture<Void> disconnect(String sessionId) {
        IMQTTSession session = sessionMap.remove(sessionId);
        transientSessionMap.remove(sessionId);
        if (session != null) {
            return session.disconnect();
        }
        return CompletableFuture.completedFuture(null);
    }
}
