package com.zachary.bifromq.sessiondict.server;

import com.zachary.bifromq.baserpc.IRPCServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class SessionDictServer implements ISessionDictionaryServer {
    private final String serviceUniqueName;
    private final IRPCServer rpcServer;
    private final SessionDictionaryService service = new SessionDictionaryService();

    SessionDictServer(String serviceUniqueName) {
        this.serviceUniqueName = serviceUniqueName;
        this.rpcServer = buildRPCServer(service);
    }

    protected abstract IRPCServer buildRPCServer(SessionDictionaryService distService);

    @Override
    public void start() {
        log.info("Starting session dict server");
        log.debug("Starting rpc server");
        rpcServer.start();
        log.info("Session dict Server started");
    }

    @SneakyThrows
    @Override
    public void shutdown() {
        log.info("Shutting down session dict server");
        log.debug("Shutting down rpc server");
        rpcServer.shutdown();
        log.debug("Closing session dict service");
        service.close();
        log.info("Session dict server shutdown");
    }
}