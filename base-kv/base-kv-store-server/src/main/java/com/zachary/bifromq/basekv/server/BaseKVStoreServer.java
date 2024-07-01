package com.zachary.bifromq.basekv.server;

import com.zachary.bifromq.basecluster.IAgentHost;
import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProcFactory;
import com.zachary.bifromq.basekv.store.option.KVRangeStoreOptions;
import com.zachary.bifromq.baserpc.IRPCServer;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
abstract class BaseKVStoreServer implements IBaseKVStoreServer {
    private final AtomicReference<State> state = new AtomicReference<>(State.INIT);

    BaseKVStoreServer(String clusterId,
                      IAgentHost agentHost,
                      ICRDTService crdtService,
                      IKVRangeCoProcFactory coProcFactory,
                      KVRangeStoreOptions storeOptions,
                      Executor queryExecutor,
                      Executor mutationExecutor,
                      ScheduledExecutorService tickTaskExecutor,
                      ScheduledExecutorService bgMgmtTaskExecutor) {
        kvService = new BaseKVStoreService(clusterId, coProcFactory, storeOptions, agentHost, crdtService,
            queryExecutor, mutationExecutor, tickTaskExecutor, bgMgmtTaskExecutor);
    }

    private final BaseKVStoreService kvService;
    private IRPCServer server;

    public String id() {
        Preconditions.checkState(state.get() == State.STARTED);
        return kvService.id();
    }

    @Override
    public void start(boolean bootstrap) {
        if (state.compareAndSet(State.INIT, State.STARTING)) {
            try {
                log.debug("Starting KVService: bootstrap={}", bootstrap);
                kvService.start(bootstrap);
                log.debug("Building KVStore server");
                server = buildServer(kvService);
                log.debug("Starting KVStore server");
                server.start();
                state.set(State.STARTED);
            } catch (Throwable e) {
                state.set(State.FATALFAILURE);
                throw e;
            }
        }
    }

    @Override
    public void stop() {
        if (state.compareAndSet(State.STARTED, State.STOPPING)) {
            try {
                log.debug("BaseKV server shutting down");
                server.shutdown();
                log.debug("BaseKV service stopping");
                kvService.stop();
            } catch (Throwable e) {
                log.error("Error occurred during server shutdown", e);
            } finally {
                state.set(State.STOPPED);
            }
        }
    }

    protected abstract IRPCServer buildServer(BaseKVStoreService service);

    private enum State {
        INIT, STARTING, STARTED, FATALFAILURE, STOPPING, STOPPED
    }
}

