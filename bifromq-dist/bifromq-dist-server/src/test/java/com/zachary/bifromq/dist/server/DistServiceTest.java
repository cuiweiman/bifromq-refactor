package com.zachary.bifromq.dist.server;

import com.zachary.bifromq.basecluster.AgentHostOptions;
import com.zachary.bifromq.basecluster.IAgentHost;
import com.zachary.bifromq.basecrdt.service.CRDTServiceOptions;
import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.baseenv.EnvProvider;
import com.zachary.bifromq.basekv.balance.option.KVRangeBalanceControllerOptions;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basekv.localengine.InMemoryKVEngineConfigurator;
import com.zachary.bifromq.basekv.store.option.KVRangeStoreOptions;
import com.zachary.bifromq.baserpc.IRPCClient;
import com.zachary.bifromq.dist.client.IDistClient;
import com.zachary.bifromq.dist.worker.IDistWorker;
import com.zachary.bifromq.plugin.eventcollector.Event;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import com.zachary.bifromq.plugin.settingprovider.Setting;
import com.zachary.bifromq.plugin.subbroker.IDeliverer;
import com.zachary.bifromq.plugin.subbroker.ISubBroker;
import com.zachary.bifromq.plugin.subbroker.ISubBrokerManager;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
public abstract class DistServiceTest {
    private IAgentHost agentHost;
    private ICRDTService clientCrdtService;
    private ICRDTService serverCrdtService;
    private IDistWorker distWorker;
    private IDistServer distServer;
    private IDistClient distClient;
    private IBaseKVStoreClient workerClient;
    private ExecutorService queryExecutor;
    private ExecutorService mutationExecutor;
    private ScheduledExecutorService tickTaskExecutor;
    private ScheduledExecutorService bgTaskExecutor;
    private ISettingProvider settingProvider = Setting::current;

    private IEventCollector eventCollector = new IEventCollector() {
        @Override
        public void report(Event<?> event) {
            log.debug("event {}", event);
        }
    };

    @Mock
    protected ISubBroker inboxBroker;
    @Mock
    protected IDeliverer inboxDeliverer;
    @Mock
    private ISubBrokerManager subBrokerMgr;

    private AutoCloseable closeable;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        when(subBrokerMgr.get(anyInt())).thenReturn(inboxBroker);
        when(inboxBroker.open(anyString())).thenReturn(inboxDeliverer);
        when(inboxBroker.hasInbox(anyInt(), anyString(), anyString(), anyString())).thenReturn(
            CompletableFuture.completedFuture(true));
        queryExecutor = ExecutorServiceMetrics.monitor(Metrics.globalRegistry,
            new ThreadPoolExecutor(2, 2, 0L,
                TimeUnit.MILLISECONDS, new LinkedTransferQueue<>(),
                EnvProvider.INSTANCE.newThreadFactory("query-executor")),
            "query-executor");
        mutationExecutor = ExecutorServiceMetrics.monitor(Metrics.globalRegistry,
            new ThreadPoolExecutor(2, 2, 0L,
                TimeUnit.MILLISECONDS, new LinkedTransferQueue<>(),
                EnvProvider.INSTANCE.newThreadFactory("mutation-executor")),
            "mutation-executor");
        tickTaskExecutor = ExecutorServiceMetrics
            .monitor(Metrics.globalRegistry, new ScheduledThreadPoolExecutor(2,
                EnvProvider.INSTANCE.newThreadFactory("tick-task-executor")), "tick-task-executor");
        bgTaskExecutor = ExecutorServiceMetrics
            .monitor(Metrics.globalRegistry, new ScheduledThreadPoolExecutor(1,
                EnvProvider.INSTANCE.newThreadFactory("bg-task-executor")), "bg-task-executor");
        AgentHostOptions agentHostOpts = AgentHostOptions.builder()
            .addr("127.0.0.1")
            .baseProbeInterval(Duration.ofSeconds(10))
            .joinRetryInSec(5)
            .joinTimeout(Duration.ofMinutes(5))
            .build();
        agentHost = IAgentHost.newInstance(agentHostOpts);
        agentHost.start();
        clientCrdtService = ICRDTService.newInstance(CRDTServiceOptions.builder().build());
        clientCrdtService.start(agentHost);

        serverCrdtService = ICRDTService.newInstance(CRDTServiceOptions.builder().build());
        serverCrdtService.start(agentHost);

        distClient = IDistClient.inProcClientBuilder().build();

        KVRangeStoreOptions kvRangeStoreOptions = new KVRangeStoreOptions();
        kvRangeStoreOptions.setDataEngineConfigurator(new InMemoryKVEngineConfigurator());
        kvRangeStoreOptions.setWalEngineConfigurator(new InMemoryKVEngineConfigurator());

        KVRangeBalanceControllerOptions balanceControllerOptions = new KVRangeBalanceControllerOptions();
        workerClient = IBaseKVStoreClient
            .inProcClientBuilder()
            .clusterId(IDistWorker.CLUSTER_NAME)
            .crdtService(clientCrdtService)
            .build();
        distWorker = IDistWorker
            .inProcBuilder()
            .agentHost(agentHost)
            .crdtService(serverCrdtService)
            .settingProvider(settingProvider)
            .eventCollector(eventCollector)
            .distClient(distClient)
            .storeClient(workerClient)
            .statsInterval(Duration.ofSeconds(5))
            .queryExecutor(queryExecutor)
            .mutationExecutor(mutationExecutor)
            .tickTaskExecutor(tickTaskExecutor)
            .bgTaskExecutor(bgTaskExecutor)
            .kvRangeStoreOptions(kvRangeStoreOptions)
            .balanceControllerOptions(balanceControllerOptions)
            .subBrokerManager(subBrokerMgr)
            .build();
        distServer = IDistServer.inProcBuilder()
            .storeClient(workerClient)
            .settingProvider(settingProvider)
            .eventCollector(eventCollector)
            .crdtService(clientCrdtService)
            .build();

        distWorker.start(true);
        distServer.start();
        workerClient.join();
        distClient.connState().filter(s -> s == IRPCClient.ConnState.READY).blockingFirst();
        log.info("Setup finished, and start testing");
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() throws Exception {
        log.info("Finish testing, and tearing down");
        workerClient.stop();
        distWorker.stop();
        distClient.stop();
        distServer.shutdown();
        clientCrdtService.stop();
        serverCrdtService.stop();
        agentHost.shutdown();
//        queryExecutor.shutdown();
//        mutationExecutor.shutdown();
//        tickTaskExecutor.shutdown();
//        bgTaskExecutor.shutdown();
        closeable.close();
    }

    protected final IDistClient distClient() {
        return distClient;
    }
}
