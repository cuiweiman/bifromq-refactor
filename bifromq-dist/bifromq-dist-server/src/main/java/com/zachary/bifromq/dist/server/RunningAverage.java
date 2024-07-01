package com.zachary.bifromq.dist.server;

public class RunningAverage {
    private final int[] window;
    private int total;
    private int count;
    private int estimate;

    public RunningAverage(int windowSize) {
        assert windowSize > 0;
        window = new int[windowSize];
    }

    public synchronized void log(int value) {
        int idx = count++ % window.length;
        int dropped = window[idx];
        window[idx] = value;
        total += value - dropped;
        estimate = total / Math.min(count, window.length);
    }

    public int estimate() {
        return estimate;
    }

}
