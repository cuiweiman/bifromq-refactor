package com.zachary.bifromq.basekv.store.wal;

import com.zachary.bifromq.baseenv.EnvProvider;
import com.zachary.bifromq.basekv.TestUtil;
import com.zachary.bifromq.basekv.localengine.InMemoryKVEngineConfigurator;
import com.zachary.bifromq.basekv.localengine.KVEngineConfigurator;
import com.zachary.bifromq.basekv.localengine.RocksDBKVEngineConfigurator;
import com.zachary.bifromq.basekv.raft.BasicStateStoreTest;
import com.zachary.bifromq.basekv.raft.IRaftStateStore;
import com.zachary.bifromq.basekv.raft.proto.Snapshot;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.google.common.util.concurrent.MoreExecutors;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.basekv.TestUtil.isDevEnv;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public class KVRangeWALStoreTest extends BasicStateStoreTest {
    private static final String DB_NAME = "testDB";
    private static final String DB_CHECKPOINT_DIR = "testDB_cp";
    private KVRangeWALStorageEngine stateStorageEngine;
    private ScheduledExecutorService bgMgmtTaskExecutor;

    public Path dbRootDir;

    @BeforeMethod
    public void setup() throws IOException {
        bgMgmtTaskExecutor =
            newSingleThreadScheduledExecutor(EnvProvider.INSTANCE.newThreadFactory("bg-task-executor"));
        KVEngineConfigurator<?> walConfigurator;
        if (isDevEnv()) {
            walConfigurator = new InMemoryKVEngineConfigurator();
        } else {
            dbRootDir = Files.createTempDirectory("");
            walConfigurator = new RocksDBKVEngineConfigurator()
                .setDbCheckpointRootDir(Paths.get(dbRootDir.toString(), DB_CHECKPOINT_DIR).toString())
                .setDbRootDir(Paths.get(dbRootDir.toString(), DB_NAME).toString());
        }
        stateStorageEngine = new KVRangeWALStorageEngine(null, walConfigurator);
        stateStorageEngine.start(bgMgmtTaskExecutor);
    }

    @AfterMethod
    public void teardown() {
        MoreExecutors.shutdownAndAwaitTermination(bgMgmtTaskExecutor, 5, TimeUnit.SECONDS);
        stateStorageEngine.stop();
        if (dbRootDir != null) {
            TestUtil.deleteDir(dbRootDir.toString());
            dbRootDir.toFile().delete();
        }
    }

    @Override
    protected String localId() {
        return stateStorageEngine.id();
    }

    @Override
    protected IRaftStateStore createStorage(String id, Snapshot snapshot) {
        return stateStorageEngine.newRaftStateStorage(KVRangeIdUtil.generate(), snapshot);
    }
}
