package com.zachary.bifromq.basecrdt.store.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface StoreCfg {
    String id();

    int maxEventsInBatch() default 1024;

    long historyExpireTime() default 5_000;

    long inflationInterval() default 100;

    double packetLossPercent() default 0.0D;

    long packetDelayTime() default 0;

    boolean packetRandom() default false;
}
