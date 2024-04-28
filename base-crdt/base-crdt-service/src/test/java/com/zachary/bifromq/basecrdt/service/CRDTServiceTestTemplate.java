package com.zachary.bifromq.basecrdt.service;

import com.zachary.bifromq.basecluster.AgentHostOptions;
import com.zachary.bifromq.basecrdt.service.annotation.ServiceCfg;
import com.zachary.bifromq.basecrdt.service.annotation.ServiceCfgs;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import static org.testng.Assert.fail;

@Slf4j
public class CRDTServiceTestTemplate {
    protected CRDTServiceTestCluster testCluster;

    public void createClusterByAnnotation(Method testMethod) {
        ServiceCfgs serviceCfgs = testMethod.getAnnotation(ServiceCfgs.class);
        ServiceCfg serviceCfg = testMethod.getAnnotation(ServiceCfg.class);
        String seedStoreId = null;
        if (testCluster != null) {
            if (serviceCfgs != null) {
                for (ServiceCfg cfg : serviceCfgs.services()) {
                    testCluster.newService(cfg.id(), buildHostOptions(cfg), buildCrdtServiceOptions(cfg));
                    if (cfg.isSeed()) {
                        seedStoreId = cfg.id();
                    }
                }
            }
            if (serviceCfg != null) {
                testCluster.newService(serviceCfg.id(),
                        buildHostOptions(serviceCfg),
                        buildCrdtServiceOptions(serviceCfg));
            }
            if (seedStoreId != null && serviceCfgs != null) {
                for (ServiceCfg cfg : serviceCfgs.services()) {
                    if (!cfg.id().equals(seedStoreId)) {
                        try {
                            testCluster.join(cfg.id(), seedStoreId);
                        } catch (Exception e) {
                            log.error("Join failed", e);
                        }
                    }
                }
            }
        }
    }

    @BeforeMethod(groups = "integration")
    public void setup() {
        testCluster = new CRDTServiceTestCluster();
    }

    @AfterMethod(groups = "integration")
    public void teardown() {
        if (testCluster != null) {
            log.info("Shutting down test cluster");
            testCluster.shutdown();
        }
    }

    public void awaitUntilTrue(Callable<Boolean> condition) {
        awaitUntilTrue(condition, 5000);
    }

    public void awaitUntilTrue(Callable<Boolean> condition, long timeoutInMS) {
        try {
            long waitingTime = 0;
            while (!condition.call()) {
                Thread.sleep(100);
                waitingTime += 100;
                if (waitingTime > timeoutInMS) {
                    fail();
                }
            }
        } catch (Exception e) {
            fail();
        }
    }

    private CRDTServiceOptions buildCrdtServiceOptions(ServiceCfg cfg) {
        return new CRDTServiceOptions();
    }

    private AgentHostOptions buildHostOptions(ServiceCfg cfg) {
        // expose more options
        return new AgentHostOptions()
                .addr(cfg.bindAddr())
                .port(cfg.bindPort());
    }
}
