package com.zachary.bifromq.basecluster.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface StoreCfg {
    String id();

    boolean isSeed() default false;

    int joinRetryInSec() default 1;

    int joinTimeout() default 30;

    String bindAddr() default "127.0.0.1";

    int bindPort();

    long purgeDelayInSec() default 1;

    long compactDelayInSec() default 1;
}
