package com.zachary.bifromq.mqtt.service;

import com.zachary.bifromq.baserpc.IRPCServer;
import com.zachary.bifromq.mqtt.session.IMQTTSession;
import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.CompletableFuture;

abstract class LocalSessionBrokerServer implements ILocalSessionBrokerServer {

    private final LocalSessionBrokerService service;
    private final IRPCServer server;

    public LocalSessionBrokerServer() {
        service = new LocalSessionBrokerService();
        server = buildRPCServer(service);
    }

    protected abstract IRPCServer buildRPCServer(LocalSessionBrokerService service);

    @Override
    public String id() {
        return server.id();
    }

    @Override
    public CompletableFuture<Void> disconnectAll(int disconnectRate) {
        return service.disconnectAll(disconnectRate);
    }

    @Override
    public void start() {
        server.start();
    }

    @SneakyThrows
    @Override
    public void shutdown() {
        server.shutdown();
        service.close();
    }

    @Override
    public void add(String sessionId, IMQTTSession session) {
        service.reg(sessionId, session);
    }

    @Override
    public boolean remove(String sessionId, IMQTTSession session) {
        return service.unreg(sessionId, session);
    }

    @Override
    public List<IMQTTSession> removeAll() {
        return null;
    }
}
