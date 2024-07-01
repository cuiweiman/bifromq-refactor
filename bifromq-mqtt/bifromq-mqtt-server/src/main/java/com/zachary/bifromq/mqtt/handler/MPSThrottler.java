package com.zachary.bifromq.mqtt.handler;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.concurrent.TimeUnit;

/**
 * simple mps throttler with 'hard' maximum mps limit, mps could be reset
 */
@NotThreadSafe
public class MPSThrottler {
    private int maxMPS;
    private long lastSec = 0;
    private long count = 0;

    public MPSThrottler(int maxMPS) {
        reset(maxMPS);
    }

    public int rateLimit() {
        return maxMPS;
    }

    public boolean pass() {
        long currentSec = upTimeInSec();
        if (lastSec == currentSec) {
            // same second bucket
            return ++count < maxMPS;
        } else {
            // new second bucket
            count = 0;
            lastSec = currentSec;
            return true;
        }
    }

    public void reset(int maxMPS) {
        assert maxMPS > 0;
        this.maxMPS = maxMPS;
    }

    private long upTimeInSec() {
        return TimeUnit.NANOSECONDS.toSeconds(System.nanoTime());
    }
}
