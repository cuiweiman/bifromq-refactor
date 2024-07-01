package com.zachary.bifromq.basekv.raft;

import com.zachary.bifromq.basekv.raft.proto.Snapshot;

public class InMemoryStateStoreTest extends BasicStateStoreTest {
    @Override
    protected IRaftStateStore createStorage(String id, Snapshot initSnapshot) {
        return new InMemoryStateStore(id, initSnapshot);
    }

    @Override
    protected String localId() {
        return "V1";
    }
}
