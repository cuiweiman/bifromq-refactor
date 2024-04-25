package com.zachary.bifromq.basecrdt.core.internal;

import com.zachary.bifromq.basecrdt.core.api.CRDTEngineOptions;
import com.zachary.bifromq.basecrdt.proto.Replica;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.zachary.bifromq.basecrdt.core.api.CRDTURI.toURI;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.aworset;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.mvreg;

public class InMemCRDTEngineTest {
    private InMemCRDTEngine engine;

    @BeforeMethod
    public void setup() {
        engine = new InMemCRDTEngine(CRDTEngineOptions.builder().build());
        engine.start();
    }

    @AfterMethod
    public void teardown() {
        engine.stop();
    }

    @Test
    public void testHostWithReplicaIdSpecified() {
        Replica mvRegReplica = engine.host(toURI(mvreg, "mvreg"));
        Replica aworsetReplica = engine.host(toURI(aworset, "aworset"), mvRegReplica.getId());

        Assert.assertEquals(aworsetReplica.getId(), mvRegReplica.getId());
        Assert.assertNotEquals(engine.get(toURI(aworset, "aworset")).get(), engine.get(toURI(mvreg, "mvreg")).get());
    }
}
