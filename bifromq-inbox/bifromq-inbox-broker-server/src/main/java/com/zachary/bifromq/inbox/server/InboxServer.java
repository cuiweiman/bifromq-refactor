package com.zachary.bifromq.inbox.server;

import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.baserpc.IRPCServer;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;

@Slf4j
abstract class InboxServer implements IInboxServer {
    private final IRPCServer rpcServer;
    private final InboxService inboxService;

    InboxServer(ISettingProvider settingProvider,
                IBaseKVStoreClient inboxStoreClient,
                ScheduledExecutorService bgTaskExecutor) {
        this.inboxService = new InboxService(settingProvider, inboxStoreClient, bgTaskExecutor);
        this.rpcServer = buildRPCServer(inboxService);
    }

    protected abstract IRPCServer buildRPCServer(InboxService distService);

    @Override
    public void start() {
        log.info("Starting inbox server");
        log.debug("Starting inbox service");
        inboxService.start();
        log.debug("Starting rpc server");
        rpcServer.start();
        log.info("Inbox server started");
    }

    @SneakyThrows
    @Override
    public void shutdown() {
        log.info("Shutting down inbox server");
        log.debug("Shutting down inbox rpc server");
        rpcServer.shutdown();
        log.debug("Stopping inbox service");
        inboxService.stop();
        log.info("Inbox server shutdown");
    }
}
