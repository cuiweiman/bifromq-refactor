package com.zachary.bifromq.basekv.raft.functest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Cluster {
    String v() default "V1,V2,V3";

    String l() default "";

    String nv() default "";

    String nl() default "";
}
