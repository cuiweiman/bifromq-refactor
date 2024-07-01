package com.zachary.bifromq.basekv.store.wal;

import com.zachary.bifromq.basekv.TestUtil;
import com.zachary.bifromq.basekv.localengine.InMemoryKVEngineConfigurator;
import com.zachary.bifromq.basekv.localengine.KVEngineConfigurator;
import com.zachary.bifromq.basekv.localengine.RocksDBKVEngineConfigurator;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.raft.IRaftStateStore;
import com.zachary.bifromq.basekv.raft.proto.ClusterConfig;
import com.zachary.bifromq.basekv.raft.proto.Snapshot;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.basekv.TestUtil.isDevEnv;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

@Slf4j
public class KVRangeWALStoreEngineTest {
    private static final String DB_NAME = "testDB";
    private static final String DB_CHECKPOINT_DIR = "testDB_cp";
    private String dbPath;


    private KVEngineConfigurator engineConfigurator;

    public Path dbRootDir;

    private ScheduledExecutorService bgMgmtTaskExecutor;

    @BeforeMethod
    public void setup() throws IOException {
        bgMgmtTaskExecutor = Executors.newSingleThreadScheduledExecutor();
        if (isDevEnv()) {
            engineConfigurator = InMemoryKVEngineConfigurator.builder().build();
        } else {
            dbRootDir = Files.createTempDirectory("");
            dbPath = Paths.get(dbRootDir.toString(), DB_NAME).toString();
            engineConfigurator = new RocksDBKVEngineConfigurator()
                .setDbCheckpointRootDir(Paths.get(dbRootDir.toString(), DB_CHECKPOINT_DIR).toString())
                .setDbRootDir(dbPath);
        }
    }

    @AfterMethod
    public void teardown() {
        MoreExecutors.shutdownAndAwaitTermination(bgMgmtTaskExecutor, 5, TimeUnit.SECONDS);
        if (dbRootDir != null) {
            TestUtil.deleteDir(dbRootDir.toString());
            dbRootDir.toFile().delete();
        }
    }

    @Test
    public void testStartAndStop() {
        try {
            KVRangeWALStorageEngine stateStorageEngine =
                new KVRangeWALStorageEngine(null, engineConfigurator);
            stateStorageEngine.start(bgMgmtTaskExecutor);
            if (engineConfigurator instanceof RocksDBKVEngineConfigurator) {
                assertTrue((new File(dbPath)).isDirectory());
            }
            assertTrue(stateStorageEngine.allKVRangeIds().isEmpty());
            stateStorageEngine.stop();
        } catch (Exception e) {
            log.error("Failed to init", e);
            fail();
        }
    }

    @Test
    public void testNewRaftStateStorage() {
        try {
            KVRangeId testId = KVRangeIdUtil.generate();
            KVRangeWALStorageEngine stateStorageEngine =
                new KVRangeWALStorageEngine(null, engineConfigurator);
            stateStorageEngine.start(bgMgmtTaskExecutor);
            Snapshot snapshot = Snapshot.newBuilder()
                .setIndex(0)
                .setTerm(0)
                .setClusterConfig(ClusterConfig.newBuilder()
                    .addVoters(stateStorageEngine.id())
                    .build())
                .build();
            IRaftStateStore stateStorage = stateStorageEngine.newRaftStateStorage(testId, snapshot);
            assertEquals(stateStorage.local(), stateStorageEngine.id());
            assertEquals(stateStorage.lastIndex(), 0);
            assertEquals(stateStorage.firstIndex(), 1);
            assertFalse(stateStorage.currentVoting().isPresent());
            assertEquals(stateStorage.latestSnapshot(), snapshot);
            assertEquals(stateStorage.latestClusterConfig(), snapshot.getClusterConfig());

            assertTrue(stateStorageEngine.has(testId));
            assertEquals(stateStorageEngine.get(testId), stateStorage);
            assertEquals(stateStorageEngine.allKVRangeIds().size(), 1);
            assertTrue(stateStorageEngine.allKVRangeIds().contains(testId));
            stateStorageEngine.stop();
        } catch (Exception e) {
            log.error("Failed to init", e);
            fail();
        }
    }

    @Test
    public void testLoadExistingRaftStateStorage() {
        if (engineConfigurator instanceof InMemoryKVEngineConfigurator) {
            return;
        }

        KVRangeId testId1 = KVRangeIdUtil.generate();
        KVRangeId testId2 = KVRangeIdUtil.next(testId1);
        KVRangeWALStorageEngine stateStorageEngine =
            new KVRangeWALStorageEngine(null, engineConfigurator);
        stateStorageEngine.start(bgMgmtTaskExecutor);
        Snapshot snapshot = Snapshot.newBuilder()
            .setIndex(0)
            .setTerm(0)
            .setClusterConfig(ClusterConfig.newBuilder()
                .addVoters(stateStorageEngine.id())
                .build())
            .build();
        stateStorageEngine.newRaftStateStorage(testId1, snapshot);
        stateStorageEngine.newRaftStateStorage(testId2, snapshot);
        assertEquals(stateStorageEngine.allKVRangeIds().size(), 2);
        stateStorageEngine.stop();

        stateStorageEngine = new KVRangeWALStorageEngine(null, engineConfigurator);
        stateStorageEngine.start(bgMgmtTaskExecutor);
        assertEquals(stateStorageEngine.allKVRangeIds().size(), 2);
        IRaftStateStore stateStorage = stateStorageEngine.get(testId1);
        assertEquals(stateStorage.local(), stateStorageEngine.id());
        assertEquals(stateStorage.lastIndex(), 0);
        assertEquals(stateStorage.firstIndex(), 1);
        assertFalse(stateStorage.currentVoting().isPresent());
        assertEquals(stateStorage.latestSnapshot(), snapshot);
        assertEquals(stateStorage.latestClusterConfig(), snapshot.getClusterConfig());
    }

    @Test
    public void testDestroyRaftStateStorage() {
        if (engineConfigurator instanceof InMemoryKVEngineConfigurator) {
            return;
        }

        KVRangeId testId1 = KVRangeIdUtil.generate();
        KVRangeId testId2 = KVRangeIdUtil.next(testId1);
        KVRangeWALStorageEngine stateStorageEngine =
            new KVRangeWALStorageEngine(null, engineConfigurator);
        stateStorageEngine.start(bgMgmtTaskExecutor);
        Snapshot snapshot = Snapshot.newBuilder()
            .setIndex(0)
            .setTerm(0)
            .setClusterConfig(ClusterConfig.newBuilder()
                .addVoters(stateStorageEngine.id())
                .build())
            .build();
        stateStorageEngine.newRaftStateStorage(testId1, snapshot);
        stateStorageEngine.newRaftStateStorage(testId2, snapshot);
        stateStorageEngine.destroy(testId1);
        assertEquals(stateStorageEngine.allKVRangeIds().size(), 1);
        assertFalse(stateStorageEngine.has(testId1));
        assertTrue(stateStorageEngine.has(testId2));
        stateStorageEngine.stop();

        stateStorageEngine = new KVRangeWALStorageEngine(null, engineConfigurator);
        stateStorageEngine.start(bgMgmtTaskExecutor);
        assertEquals(stateStorageEngine.allKVRangeIds().size(), 1);
        assertTrue(stateStorageEngine.has(testId2));
    }
}
