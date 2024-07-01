package com.zachary.bifromq.inbox.server.scheduler;

import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basescheduler.BatchCallScheduler;
import com.google.common.base.Preconditions;
import com.google.protobuf.ByteString;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.Optional;

public abstract class InboxQueryScheduler<Req, Resp>
    extends BatchCallScheduler<Req, Resp, InboxQueryScheduler.BatchKey> {
    private final IBaseKVStoreClient kvStoreClient;
    private final int queuesPerRange;

    public InboxQueryScheduler(int queuesPerRange, IBaseKVStoreClient kvStoreClient, String name) {
        super(name);
        Preconditions.checkArgument(queuesPerRange > 0, "Queues per range must be positive");
        this.kvStoreClient = kvStoreClient;
        this.queuesPerRange = queuesPerRange;
    }

    @Override
    protected final Optional<BatchKey> find(Req request) {
        Optional<KVRangeSetting> range = kvStoreClient.findByKey(rangeKey(request));
        return range.map(kvRangeSetting -> new BatchKey(kvRangeSetting, selectQueue(queuesPerRange, request)));
    }

    protected abstract int selectQueue(int maxQueueIdx, Req request);

    protected abstract ByteString rangeKey(Req request);

    @AllArgsConstructor
    @EqualsAndHashCode
    static class BatchKey {
        final KVRangeSetting rangeSetting;
        final int queueId;
    }
}
