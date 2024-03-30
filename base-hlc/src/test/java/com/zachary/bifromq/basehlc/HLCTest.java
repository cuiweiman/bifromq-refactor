package com.zachary.bifromq.basehlc;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;


public class HLCTest {

    @Test
    public void testGet() {
        long now = System.currentTimeMillis();
        long t1 = HLC.INST.get();
        Assert.assertTrue(t1 > 0 && HLC.INST.getPhysical(t1) >= now);
        for (int i = 0; i < 1000; i++) {
            long t = HLC.INST.get();
            Assert.assertTrue(t > t1);
            t1 = t;
        }
    }

    @Test
    public void testUpdate() {
        long now = System.currentTimeMillis();
        long t1 = HLC.INST.update(HLC.INST.get());
        assertTrue(t1 > 0 && HLC.INST.getPhysical(t1) >= now);
        for (int i = 0; i < 1000; i++) {
            long t = HLC.INST.update(t1);
            assertTrue(t > t1);
            t1 = t;
        }
    }


}