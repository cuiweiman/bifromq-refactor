package com.zachary.bifromq.baserpc.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/6/11 17:45
 */
public class Backoff {
    private final int multiplier;
    private final long base;
    private final long max;
    private int n = 0;

    public Backoff(int multiplier, long base, long max) {
        assert multiplier > 0;
        assert base > 0;
        this.multiplier = multiplier;
        this.base = base;
        this.max = max;
    }

    public synchronized long backoff() {
        double c = Math.pow(multiplier, n++);
        if (Double.isInfinite(c) || Double.isNaN(c)) {
            return max;
        }
        long b = (long) (base * c) + ThreadLocalRandom.current().nextLong(0, base);
        if (b <= 0) {
            return max;
        }
        return Math.min(b, max);
    }

    public synchronized void reset() {
        n = 0;
    }
}
