package com.zachary.bifromq.dist.server;

import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.baserpc.IRPCServer;
import com.zachary.bifromq.dist.server.scheduler.IGlobalDistCallRateSchedulerFactory;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class DistServer implements IDistServer {
    private final IRPCServer rpcServer;
    private final DistService distService;

    DistServer(IBaseKVStoreClient storeClient,
               ISettingProvider settingProvider,
               IEventCollector eventCollector,
               ICRDTService crdtService,
               IGlobalDistCallRateSchedulerFactory distCallPreBatchSchedulerFactory) {
        this.distService = new DistService(storeClient, settingProvider, eventCollector, crdtService,
            distCallPreBatchSchedulerFactory);
        this.rpcServer = buildRPCServer(distService);
    }

    protected abstract IRPCServer buildRPCServer(DistService distService);

    @Override
    public void start() {
        log.info("Starting dist server");
        log.debug("Starting rpc server");
        rpcServer.start();
        log.info("Dist Server started");
    }

    @SneakyThrows
    @Override
    public void shutdown() {
        log.info("Stopping dist server");
        log.debug("Stop dist rpc server");
        rpcServer.shutdown();
        log.debug("Stop dist service");
        distService.stop();
        log.info("Dist server stopped");
    }
}
