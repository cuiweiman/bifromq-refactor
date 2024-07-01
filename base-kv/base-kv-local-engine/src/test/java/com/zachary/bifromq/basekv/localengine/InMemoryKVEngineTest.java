package com.zachary.bifromq.basekv.localengine;

import com.zachary.bifromq.baseenv.EnvProvider;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

@Slf4j
public class InMemoryKVEngineTest extends AbstractKVEngineTest {
    private ScheduledExecutorService maintenanceTaskExecutor;

    @BeforeMethod
    public void setup() {
        maintenanceTaskExecutor =
            newSingleThreadScheduledExecutor(EnvProvider.INSTANCE.newThreadFactory("Checkpoint GC"));
        InMemoryKVEngineConfigurator configurator = new InMemoryKVEngineConfigurator().setGcIntervalInSec(60000);
        kvEngine = new InMemoryKVEngine(null, singletonList(NS), this::isUsed, configurator,
            Duration.ofSeconds(-1));
        kvEngine.start(maintenanceTaskExecutor);
    }

    @AfterMethod
    public void teardown() {
        kvEngine.stop();
        MoreExecutors.shutdownAndAwaitTermination(maintenanceTaskExecutor, 5, TimeUnit.SECONDS);
    }
}
