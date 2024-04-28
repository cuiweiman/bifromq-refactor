package com.zachary.bifromq.basecluster.fd;

import org.testng.annotations.Test;

import java.time.Duration;

import static com.zachary.bifromq.basecluster.fd.FailureDetectorMath.scale;
import static org.testng.Assert.assertEquals;

public class FailureDetectorMathTest {
    @Test
    public void scaleDuration() {
        assertEquals(scale(Duration.ofMillis(100), -1), Duration.ofMillis(100));
        assertEquals(scale(Duration.ofMillis(100), 0), Duration.ofMillis(100));
        assertEquals(scale(Duration.ofMillis(100), 1), Duration.ofMillis(200));
    }
}
