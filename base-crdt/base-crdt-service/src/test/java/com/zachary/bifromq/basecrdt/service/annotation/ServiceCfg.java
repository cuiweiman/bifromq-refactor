package com.zachary.bifromq.basecrdt.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface ServiceCfg {
    String id();

    boolean isSeed() default false;

    String bindAddr() default "127.0.0.1";

    int bindPort();
}
