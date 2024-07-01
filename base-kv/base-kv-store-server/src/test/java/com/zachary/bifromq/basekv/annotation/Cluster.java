package com.zachary.bifromq.basekv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Cluster {
    int initNodes() default 3;

    int installSnapshotTimeoutTick() default 2000;

    boolean asyncAppend() default true;
}
