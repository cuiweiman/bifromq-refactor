package com.zachary.bifromq.dist.server.benchmark;

import com.zachary.bifromq.basecluster.AgentHostOptions;
import com.zachary.bifromq.basecluster.IAgentHost;
import com.zachary.bifromq.basecrdt.service.CRDTServiceOptions;
import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.basekv.balance.option.KVRangeBalanceControllerOptions;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basekv.localengine.InMemoryKVEngineConfigurator;
import com.zachary.bifromq.basekv.store.option.KVRangeStoreOptions;
import com.zachary.bifromq.dist.client.IDistClient;
import com.zachary.bifromq.dist.server.IDistServer;
import com.zachary.bifromq.dist.worker.IDistWorker;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import com.zachary.bifromq.plugin.settingprovider.Setting;
import com.zachary.bifromq.plugin.subbroker.DeliveryPack;
import com.zachary.bifromq.plugin.subbroker.DeliveryResult;
import com.zachary.bifromq.plugin.subbroker.IDeliverer;
import com.zachary.bifromq.plugin.subbroker.ISubBroker;
import com.zachary.bifromq.plugin.subbroker.ISubBrokerManager;
import com.zachary.bifromq.type.ClientInfo;
import com.zachary.bifromq.type.SubInfo;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@State(Scope.Benchmark)
public class DistServiceState {
    public static final int MqttBroker = 0;
    public static final int InboxService = 1;
    public final ClientInfo clientInfo = ClientInfo.newBuilder()
        .setTenantId("DevOnly")
        .putMetadata("type", "benchmark")
        .build();

    private final IAgentHost agentHost;
    private final ICRDTService crdtService;
    private final IDistClient distClient;
    private final IDistWorker distWorker;
    private final IDistServer distServer;
    private final IBaseKVStoreClient storeClient;
    private final AtomicLong seqNo = new AtomicLong(10000);

    public DistServiceState() {
        AgentHostOptions agentHostOpts = AgentHostOptions.builder()
            .addr("127.0.0.1")
            .baseProbeInterval(Duration.ofSeconds(10))
            .joinRetryInSec(5)
            .joinTimeout(Duration.ofMinutes(5))
            .build();
        agentHost = IAgentHost.newInstance(agentHostOpts);
        agentHost.start();
        CRDTServiceOptions crdtServiceOptions = CRDTServiceOptions.builder().build();
        crdtService = ICRDTService.newInstance(crdtServiceOptions);
        crdtService.start(agentHost);
        distClient = IDistClient.inProcClientBuilder().build();

        KVRangeStoreOptions kvRangeStoreOptions = new KVRangeStoreOptions();
        kvRangeStoreOptions.setDataEngineConfigurator(new InMemoryKVEngineConfigurator());
        kvRangeStoreOptions.setWalEngineConfigurator(new InMemoryKVEngineConfigurator());
        storeClient = IBaseKVStoreClient
            .inProcClientBuilder()
            .clusterId(IDistWorker.CLUSTER_NAME)
            .crdtService(crdtService)
            .build();
        ISettingProvider settingProvider = Setting::current;
        IEventCollector eventCollector = event -> {

        };
        ISubBrokerManager subBrokerMgr = new ISubBrokerManager() {
            public ISubBroker get(int subBrokerId) {
                return new ISubBroker() {
                    @Override
                    public int id() {
                        return 0;
                    }

                    @Override
                    public IDeliverer open(String delivererKey) {
                        return new IDeliverer() {
                            @Override
                            public CompletableFuture<Map<SubInfo, DeliveryResult>> deliver(
                                Iterable<DeliveryPack> packs) {
                                return CompletableFuture.completedFuture(Collections.emptyMap());
                            }

                            @Override
                            public void close() {

                            }
                        };
                    }

                    @Override
                    public CompletableFuture<Boolean> hasInbox(long reqId, String tenantId, String inboxId,
                                                               String delivererKey) {
                        return CompletableFuture.completedFuture(true);
                    }

                    @Override
                    public void close() {

                    }
                };
            }

            @Override
            public void stop() {

            }
        };
        distWorker = IDistWorker
            .inProcBuilder()
            .agentHost(agentHost)
            .crdtService(crdtService)
            .settingProvider(settingProvider)
            .eventCollector(eventCollector)
            .distClient(distClient)
            .storeClient(storeClient)
            .queryExecutor(Executors.newWorkStealingPool())
            .mutationExecutor(Executors.newWorkStealingPool())
            .bgTaskExecutor(Executors.newSingleThreadScheduledExecutor())
            .tickTaskExecutor(Executors.newSingleThreadScheduledExecutor())
            .balanceControllerOptions(new KVRangeBalanceControllerOptions())
            .kvRangeStoreOptions(kvRangeStoreOptions)
            .subBrokerManager(subBrokerMgr)
            .build();
        distServer = IDistServer.inProcBuilder()
            .storeClient(storeClient)
            .settingProvider(settingProvider)
            .eventCollector(eventCollector)
            .crdtService(crdtService)
            .build();
    }

    @Setup(Level.Trial)
    public void setup() {
        distWorker.start(true);
        distServer.start();
        storeClient.join();
        log.info("Setup finished, and start testing");
    }

    @TearDown(Level.Trial)
    public void teardown() {
        log.info("Finish testing, and tearing down");
        storeClient.stop();
        distServer.shutdown();
        distWorker.stop();
        crdtService.stop();
        agentHost.shutdown();
    }

    public void requestClear(String inboxId, String delivererKey, int brokerId, ClientInfo clientInfo) {
        long reqId = seqNo.incrementAndGet();
        distClient.clear(reqId, clientInfo.getTenantId(), inboxId, delivererKey, brokerId).join();
    }
}
