package com.zachary.bifromq.retain.server;

import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.baserpc.IRPCServer;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class RetainServer implements IRetainServer {
    private final String serviceUniqueName;
    private final IRPCServer rpcServer;
    private final RetainService retainService;

    RetainServer(String serviceUniqueName, ISettingProvider settingProvider,
                 IBaseKVStoreClient storeClient) {
        this.serviceUniqueName = serviceUniqueName;
        this.retainService = new RetainService(settingProvider, storeClient);
        this.rpcServer = buildRPCServer(retainService);
    }

    protected abstract IRPCServer buildRPCServer(RetainService distService);

    @Override
    public void start() {
        log.info("Starting retain server");
        log.debug("Starting rpc server");
        rpcServer.start();
        log.info("Retain server started");
    }

    @SneakyThrows
    @Override
    public void shutdown() {
        log.info("Shutting down retain server");
        log.debug("Shutting down rpc server");
        rpcServer.shutdown();
        log.info("Retain server stopped");
    }
}
