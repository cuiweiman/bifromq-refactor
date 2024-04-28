package com.zachary.bifromq.basecluster.fd;

import java.time.Duration;

public final class FailureDetectorMath {

    public static Duration scale(Duration d, int score) {
        if (score <= 0) {
            return d;
        }
        return Duration.ofMillis(d.toMillis() * (score + 1));
    }

}
