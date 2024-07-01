package com.zachary.bifromq.inbox.server.scheduler;

import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basescheduler.BatchCallScheduler;
import com.google.protobuf.ByteString;

import java.util.Optional;

public abstract class InboxUpdateScheduler<Req, Resp> extends BatchCallScheduler<Req, Resp, KVRangeSetting> {
    private final IBaseKVStoreClient kvStoreClient;

    public InboxUpdateScheduler(IBaseKVStoreClient kvStoreClient, String name) {
        super(name);
        this.kvStoreClient = kvStoreClient;
    }

    @Override
    protected final Optional<KVRangeSetting> find(Req request) {
        return kvStoreClient.findByKey(rangeKey(request));
    }

    protected abstract ByteString rangeKey(Req request);
}
