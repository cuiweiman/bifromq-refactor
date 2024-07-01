package com.zachary.bifromq.inbox.server.benchmark;

import com.zachary.bifromq.basecluster.AgentHostOptions;
import com.zachary.bifromq.basecluster.IAgentHost;
import com.zachary.bifromq.basecrdt.service.CRDTServiceOptions;
import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basekv.localengine.RocksDBKVEngineConfigurator;
import com.zachary.bifromq.basekv.store.option.KVRangeStoreOptions;
import com.zachary.bifromq.inbox.client.IInboxBrokerClient;
import com.zachary.bifromq.inbox.client.IInboxReaderClient;
import com.zachary.bifromq.inbox.server.IInboxServer;
import com.zachary.bifromq.inbox.store.IInboxStore;
import com.zachary.bifromq.plugin.eventcollector.Event;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import com.zachary.bifromq.plugin.settingprovider.Setting;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Comparator;
import java.util.UUID;

@Slf4j
@State(Scope.Benchmark)
abstract class InboxServiceState {
    private static final String DB_NAME = "testDB";
    private static final String DB_CHECKPOINT_DIR_NAME = "testDB_cp";

    private static final String DB_WAL_NAME = "testWAL";
    private static final String DB_WAL_CHECKPOINT_DIR = "testWAL_cp";

    protected IInboxReaderClient inboxReaderClient;
    protected IInboxBrokerClient inboxBrokerClient;
    private IAgentHost agentHost;
    private ICRDTService clientCrdtService;
    private ICRDTService serverCrdtService;
    private ISettingProvider settingProvider = Setting::current;
    private IBaseKVStoreClient inboxStoreKVStoreClient;

    private IInboxStore inboxStore;

    private IInboxServer inboxServer;

    private Path dbRootDir;

    private IEventCollector eventCollector = new IEventCollector() {
        @Override
        public void report(Event<?> event) {

        }
    };

    public InboxServiceState() {
        try {
            dbRootDir = Files.createTempDirectory("");
        } catch (IOException e) {
        }
        AgentHostOptions agentHostOpts = AgentHostOptions.builder()
            .addr("127.0.0.1")
            .baseProbeInterval(Duration.ofSeconds(10))
            .joinRetryInSec(5)
            .joinTimeout(Duration.ofMinutes(5))
            .build();
        agentHost = IAgentHost.newInstance(agentHostOpts);
        agentHost.start();
        CRDTServiceOptions crdtServiceOptions = CRDTServiceOptions.builder().build();
        clientCrdtService = ICRDTService.newInstance(crdtServiceOptions);
        clientCrdtService.start(agentHost);

        serverCrdtService = ICRDTService.newInstance(crdtServiceOptions);
        serverCrdtService.start(agentHost);

        inboxBrokerClient = IInboxBrokerClient.inProcClientBuilder().build();
        inboxReaderClient = IInboxReaderClient.inProcClientBuilder().build();


        KVRangeStoreOptions kvRangeStoreOptions = new KVRangeStoreOptions();
//        kvRangeStoreOptions.setDataEngineConfigurator(new InMemoryKVEngineConfigurator());
//        kvRangeStoreOptions.setWalEngineConfigurator(new InMemoryKVEngineConfigurator());
        kvRangeStoreOptions.setDataEngineConfigurator(new RocksDBKVEngineConfigurator());
        String uuid = UUID.randomUUID().toString();
        ((RocksDBKVEngineConfigurator) kvRangeStoreOptions.getDataEngineConfigurator())
            .setDbCheckpointRootDir(Paths.get(dbRootDir.toString(), DB_CHECKPOINT_DIR_NAME, uuid)
                .toString())
            .setDbRootDir(Paths.get(dbRootDir.toString(), DB_NAME, uuid).toString());
        kvRangeStoreOptions.setWalEngineConfigurator(new RocksDBKVEngineConfigurator());
        ((RocksDBKVEngineConfigurator) kvRangeStoreOptions
            .getWalEngineConfigurator())
            .setDbCheckpointRootDir(Paths.get(dbRootDir.toString(), DB_WAL_CHECKPOINT_DIR, uuid)
                .toString())
            .setDbRootDir(Paths.get(dbRootDir.toString(), DB_WAL_NAME, uuid).toString());

        inboxStoreKVStoreClient = IBaseKVStoreClient
            .inProcClientBuilder()
            .clusterId(IInboxStore.CLUSTER_NAME)
            .crdtService(clientCrdtService)
            .build();
        inboxStore = IInboxStore.inProcBuilder()
            .agentHost(agentHost)
            .crdtService(serverCrdtService)
            .storeClient(inboxStoreKVStoreClient)
            .eventCollector(eventCollector)
            .kvRangeStoreOptions(kvRangeStoreOptions)
            .build();
        inboxServer = IInboxServer.inProcBuilder()
            .settingProvider(settingProvider)
            .storeClient(inboxStoreKVStoreClient)
            .build();
    }

    @Setup(Level.Trial)
    public void setup() {
        inboxStore.start(true);
        inboxServer.start();
        inboxStoreKVStoreClient.join();
        afterSetup();
        log.info("Setup finished, and start testing");
    }

    protected abstract void afterSetup();

    @TearDown(Level.Trial)
    public void teardown() {
        log.info("Finish testing, and tearing down");
        beforeTeardown();
        inboxBrokerClient.close();
        inboxReaderClient.stop();
        log.debug("Inbox server stopping");
        inboxServer.shutdown();
        log.debug("Inbox store client stopping");
        inboxStoreKVStoreClient.stop();
        log.debug("Inbox store stopping");
        inboxStore.stop();
        log.debug("crdt stopping");
        clientCrdtService.stop();
        serverCrdtService.stop();
        log.debug("agent host stopping");
        agentHost.shutdown();
        try {
            Files.walk(dbRootDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            log.error("Failed to delete db root dir", e);
        }
    }

    protected abstract void beforeTeardown();
}
