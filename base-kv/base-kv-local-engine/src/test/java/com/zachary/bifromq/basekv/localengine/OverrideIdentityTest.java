package com.zachary.bifromq.basekv.localengine;

import com.zachary.bifromq.baseenv.EnvProvider;
import com.google.common.util.concurrent.MoreExecutors;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static org.testng.Assert.assertEquals;

public class OverrideIdentityTest {
    private String DB_NAME = "testDB";
    private String DB_CHECKPOINT_DIR = "testDB_cp";
    private String cf = "TestCF";
    private IKVEngine engine;
    private AtomicReference<String> cp = new AtomicReference<>();
    private ScheduledExecutorService bgTaskExecutor;
    public Path dbRootDir;

    @BeforeMethod
    public void setup() throws IOException {
        dbRootDir = Files.createTempDirectory("");
        bgTaskExecutor =
            newSingleThreadScheduledExecutor(EnvProvider.INSTANCE.newThreadFactory("Checkpoint GC"));
    }

    @AfterMethod
    public void teardown() {
        MoreExecutors.shutdownAndAwaitTermination(bgTaskExecutor, 5, TimeUnit.SECONDS);
        try {
            Files.walk(dbRootDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOverrideIdentity() {
        String overrideIdentity = UUID.randomUUID().toString();
        KVEngineConfigurator configurator = new RocksDBKVEngineConfigurator()
            .setDbRootDir(Paths.get(dbRootDir.toString(), DB_NAME).toString())
            .setDbCheckpointRootDir(Paths.get(dbRootDir.toString(), DB_CHECKPOINT_DIR).toString());
        engine = KVEngineFactory.create(overrideIdentity, List.of(IKVEngine.DEFAULT_NS, cf),
            this::isUsed, configurator);
        engine.start(bgTaskExecutor);
        assertEquals(engine.id(), overrideIdentity);
        engine.stop();
        // restart without overrideIdentity specified
        configurator = new RocksDBKVEngineConfigurator()
            .setDbRootDir(Paths.get(dbRootDir.toString(), DB_NAME).toString())
            .setDbCheckpointRootDir(Paths.get(dbRootDir.toString(), DB_CHECKPOINT_DIR).toString());

        engine = KVEngineFactory.create(null, List.of(IKVEngine.DEFAULT_NS, cf),
            this::isUsed, configurator);
        engine.start(bgTaskExecutor);

        assertEquals(engine.id(), overrideIdentity);
        engine.stop();
        // restart with different overrideIdentity specified
        String another = UUID.randomUUID().toString();
        configurator = new RocksDBKVEngineConfigurator()
            .setDbRootDir(Paths.get(dbRootDir.toString(), DB_NAME).toString())
            .setDbCheckpointRootDir(Paths.get(dbRootDir.toString(), DB_CHECKPOINT_DIR).toString());

        engine = KVEngineFactory.create(another, List.of(IKVEngine.DEFAULT_NS, cf),
            this::isUsed, configurator);
        engine.start(bgTaskExecutor);

        assertEquals(engine.id(), overrideIdentity);
        engine.stop();
    }

    @Test
    public void testCanOnlyOverrideWhenInit() {
        KVEngineConfigurator configurator = new RocksDBKVEngineConfigurator()
            .setDbRootDir(Paths.get(dbRootDir.toString(), DB_NAME).toString())
            .setDbCheckpointRootDir(Paths.get(dbRootDir.toString(), DB_CHECKPOINT_DIR).toString());

        engine = KVEngineFactory.create(null, List.of(IKVEngine.DEFAULT_NS, cf),
            this::isUsed, configurator);
        engine.start(bgTaskExecutor);
        String identity = engine.id();
        engine.stop();
        // restart with overrideIdentity specified
        String overrideIdentity = UUID.randomUUID().toString();
        configurator = new RocksDBKVEngineConfigurator()
            .setDbRootDir(Paths.get(dbRootDir.toString(), DB_NAME).toString())
            .setDbCheckpointRootDir(Paths.get(dbRootDir.toString(), DB_CHECKPOINT_DIR).toString());

        engine = KVEngineFactory.create(overrideIdentity, List.of(IKVEngine.DEFAULT_NS, cf),
            this::isUsed, configurator);
        engine.start(bgTaskExecutor);

        assertEquals(engine.id(), identity);
        engine.stop();
    }

    private boolean isUsed(String checkpointId) {
        return checkpointId.equals(cp.get());
    }
}
