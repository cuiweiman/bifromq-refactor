package com.zachary.bifromq.basecluster;

import com.zachary.bifromq.basecluster.annotation.StoreCfg;
import com.zachary.bifromq.basecluster.annotation.StoreCfgs;
import com.zachary.bifromq.basecrdt.core.api.CRDTEngineOptions;
import com.zachary.bifromq.basecrdt.store.CRDTStoreOptions;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.time.Duration;

@Slf4j
public abstract class AgentTestTemplate {
    protected AgentTestCluster storeMgr;

    AgentTestTemplate() {
    }

    public void createClusterByAnnotation(Method testMethod) {
        StoreCfgs storeCfgs = testMethod.getAnnotation(StoreCfgs.class);
        StoreCfg storeCfg = testMethod.getAnnotation(StoreCfg.class);
        String seedStoreId = null;
        if (storeMgr != null) {
            if (storeCfgs != null) {
                for (StoreCfg cfg : storeCfgs.stores()) {
                    storeMgr.newHost(cfg.id(), build(cfg));
                    if (cfg.isSeed()) {
                        seedStoreId = cfg.id();
                    }
                }
            }
            if (storeCfg != null) {
                storeMgr.newHost(storeCfg.id(), build(storeCfg));
            }
            if (seedStoreId != null && storeCfgs != null) {
                for (StoreCfg cfg : storeCfgs.stores()) {
                    if (!cfg.id().equals(seedStoreId)) {
                        storeMgr.join(cfg.id(), seedStoreId);
                    }
                }
            }
        }
    }

    @BeforeMethod(groups = "integration")
    public void setup() {
        storeMgr = new AgentTestCluster();
    }

    @AfterMethod(groups = "integration")
    public void teardown() {
        if (storeMgr != null) {
            log.info("Shutting down test cluster");
            storeMgr.shutdown();
        }
    }

    private AgentHostOptions build(StoreCfg cfg) {
        return AgentHostOptions.builder()
                .addr(cfg.bindAddr())
                .port(cfg.bindPort())
                .udpPacketLimit(1400)
                .maxChannelsPerHost(1)
                .joinRetryInSec(cfg.joinRetryInSec())
                .joinTimeout(Duration.ofSeconds(cfg.joinTimeout()))
                .crdtStoreOptions(CRDTStoreOptions.builder()
                        .engineOptions(CRDTEngineOptions.builder()
                                .orHistoryExpireTime(Duration.ofSeconds(cfg.compactDelayInSec()))
                                .build())
                        .maxEventsInDelta(100)
                        .build()
                ).build();
    }
}
