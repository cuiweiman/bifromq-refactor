package com.zachary.bifromq.inbox.store.benchmark;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@Slf4j
@State(Scope.Benchmark)
public class HasInboxState extends InboxStoreState {
    @Override
    void afterSetup() {

    }

    @Override
    void beforeTeardown() {

    }
}
