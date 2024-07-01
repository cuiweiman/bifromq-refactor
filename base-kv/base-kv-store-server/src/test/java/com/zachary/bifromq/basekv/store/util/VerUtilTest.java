package com.zachary.bifromq.basekv.store.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class VerUtilTest {
    @Test
    public void bumpTest() {
        assertEquals(VerUtil.bump(0, false), 2);
        assertEquals(VerUtil.bump(1, false), 2);

        assertEquals(VerUtil.bump(0, true), 1);
        assertEquals(VerUtil.bump(1, true), 3);
    }
}
