package com.zachary.bifromq.basecrdt.store;

import com.zachary.bifromq.basecrdt.core.api.CRDTEngineOptions;
import com.zachary.bifromq.basecrdt.store.annotation.StoreCfg;
import com.zachary.bifromq.basecrdt.store.annotation.StoreCfgs;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Comparator;
import java.util.concurrent.Callable;

import static org.testng.Assert.fail;

@Slf4j
public abstract class CRDTStoreTestTemplate {
    private Path dbRootDir;
    protected CRDTStoreTestCluster storeMgr;

    @BeforeMethod(alwaysRun = true)
    public void setup() throws IOException {
        dbRootDir = Files.createTempDirectory("");
        storeMgr = new CRDTStoreTestCluster(dbRootDir.toString());
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() {
        if (storeMgr != null) {
            log.info("Shutting down test cluster");
            storeMgr.shutdown();
            try {
                Files.walk(dbRootDir)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                log.error("Failed to delete db root dir", e);
            }
        }
    }

    public void createClusterByAnnotation(Method testMethod) {
        StoreCfgs storeCfgs = testMethod.getAnnotation(StoreCfgs.class);
        StoreCfg storeCfg = testMethod.getAnnotation(StoreCfg.class);
        if (storeCfgs != null) {
            for (StoreCfg cfg : storeCfgs.stores()) {
                storeMgr.newStore(cfg.id(), build(cfg));
            }
        }
        if (storeCfg != null) {
            storeMgr.newStore(storeCfg.id(), build(storeCfg));
        }
    }

    public void awaitUntilTrue(Callable<Boolean> condition, long timeoutInMS) {
        try {
            long waitingTime = 0;
            while (!condition.call()) {
                Thread.sleep(500);
                waitingTime += 500;
                if (waitingTime > timeoutInMS) {
                    fail("timeout");
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void awaitUntilTrue(Callable<Boolean> condition) {
        awaitUntilTrue(condition, 15000);
    }

    private CRDTStoreTestCluster.CRDTStoreMeta build(StoreCfg cfg) {
        return new CRDTStoreTestCluster.CRDTStoreMeta(
            CRDTStoreOptions.builder()
                .maxEventsInDelta(cfg.maxEventsInBatch())
                .engineOptions(new CRDTEngineOptions()
                    .inflationInterval(Duration.ofMillis(cfg.inflationInterval()))
                    .orHistoryExpireTime(Duration.ofMillis(cfg.historyExpireTime())))
                .build(),
            cfg.packetLossPercent(),
            cfg.packetDelayTime(),
            cfg.packetRandom()
        );
    }
}
