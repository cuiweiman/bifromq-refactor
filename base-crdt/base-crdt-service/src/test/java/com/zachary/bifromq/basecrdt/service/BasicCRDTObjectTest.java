package com.zachary.bifromq.basecrdt.service;

import com.zachary.bifromq.basecrdt.core.api.operation.CCounterOperation;
import com.zachary.bifromq.basecrdt.core.api.CRDTURI;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.ICCounter;
import com.zachary.bifromq.basecrdt.proto.Replica;
import com.zachary.bifromq.basecrdt.service.annotation.ServiceCfg;
import com.zachary.bifromq.basecrdt.service.annotation.ServiceCfgs;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Slf4j
@Listeners(CRDTServiceTestListener.class)
public class BasicCRDTObjectTest extends CRDTServiceTestTemplate {

    @ServiceCfgs(services =
        {
            @ServiceCfg(id = "s1", bindPort = 11111, isSeed = true),
            @ServiceCfg(id = "s2", bindPort = 22222)
        })
    @Test(groups = "integration")
    public void testConvergence() {
        ICRDTService service1 = testCluster.getService("s1");
        ICRDTService service2 = testCluster.getService("s2");
        String uri = CRDTURI.toURI(CausalCRDTType.cctr, "test");
        service1.host(uri);
        service2.host(uri);
        awaitUntilTrue(() -> service1.aliveReplicas(uri).blockingFirst().size() == 2);
        awaitUntilTrue(() -> service2.aliveReplicas(uri).blockingFirst().size() == 2);

        ICCounter counter1 = (ICCounter) service1.get(uri).get();
        ICCounter counter2 = (ICCounter) service2.get(uri).get();
        counter1.execute(CCounterOperation.add(1)).join();
        counter2.execute(CCounterOperation.add(2)).join();
        awaitUntilTrue(() -> counter1.read() == counter2.read());
        Assert.assertEquals(3, counter1.read());

        service1.stopHosting(uri).join();
        service2.stopHosting(uri).join();
    }

    @ServiceCfgs(services =
        {
            @ServiceCfg(id = "s1", bindPort = 11111, isSeed = true),
            @ServiceCfg(id = "s2", bindPort = 22222)
        })
    @Test(groups = "integration")
    public void testPartition() {
        ICRDTService service1 = testCluster.getService("s1");
        ICRDTService service2 = testCluster.getService("s2");
        String uri = CRDTURI.toURI(CausalCRDTType.cctr, "test");
        service1.host(uri);
        Replica counter2Id = service2.host(uri);
        awaitUntilTrue(() -> service1.aliveReplicas(uri).blockingFirst().size() == 2);
        awaitUntilTrue(() -> service2.aliveReplicas(uri).blockingFirst().size() == 2);

        ICCounter counter1 = (ICCounter) service1.get(uri).get();
        ICCounter counter2 = (ICCounter) service2.get(uri).get();
        counter1.execute(CCounterOperation.add(1)).join();
        counter2.execute(CCounterOperation.add(2)).join();
        awaitUntilTrue(() -> counter1.read() == counter2.read());
        Assert.assertEquals(3, counter1.read());

        service2.stopHosting(uri).join();
        awaitUntilTrue(() -> service1.aliveReplicas(uri).blockingFirst().size() == 1);
        Assert.assertEquals(3, counter1.read());

        counter1.execute(CCounterOperation.zeroOut(counter2Id.getId())).join();
        Assert.assertEquals(1, counter1.read());

        service1.stopHosting(uri).join();
    }
}
