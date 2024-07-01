package com.zachary.bifromq.basekv.raft.functest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Config {
    boolean asyncAppend() default true;

    boolean disableForwardProposal() default false;

    int electionTimeoutTick() default 10;

    int heartbeatTimeoutTick() default 1;

    int installSnapshotTimeoutTick() default 20;

    int maxInflightAppends() default 10;

    int maxSizePerAppend() default 1024;

    int maxUnappliedEntries() default 10;

    boolean preVote() default true;

    int readOnlyBatch() default 10;

    boolean readOnlyLeaderLeaseMode() default true;
}
