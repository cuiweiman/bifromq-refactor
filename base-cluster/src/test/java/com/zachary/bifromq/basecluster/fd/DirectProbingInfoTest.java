package com.zachary.bifromq.basecluster.fd;

import org.testng.annotations.Test;

import java.util.Optional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class DirectProbingInfoTest {
    @Test
    public void construct() {
        DirectProbingInfo info = new DirectProbingInfo(Optional.of(Fixtures.DIRECT_PROBING_TARGET));
        assertEquals(info.target.get(), Fixtures.DIRECT_PROBING_TARGET);
        assertTrue(info.piggybacked.isEmpty());
    }
}
