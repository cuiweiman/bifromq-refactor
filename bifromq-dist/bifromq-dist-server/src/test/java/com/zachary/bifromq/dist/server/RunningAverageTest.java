package com.zachary.bifromq.dist.server;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RunningAverageTest {
    @Test
    public void test() {
        RunningAverage runningAverage = new RunningAverage(1);
        assertEquals(runningAverage.estimate(), 0);

        runningAverage.log(1);
        assertEquals(runningAverage.estimate(), 1);

        runningAverage.log(2);
        assertEquals(runningAverage.estimate(), 2);

        runningAverage = new RunningAverage(2);
        runningAverage.log(1);
        runningAverage.log(3);
        assertEquals(runningAverage.estimate(), 2);
    }
}
