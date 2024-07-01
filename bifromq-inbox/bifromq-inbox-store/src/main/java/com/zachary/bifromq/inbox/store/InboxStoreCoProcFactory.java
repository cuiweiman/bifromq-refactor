package com.zachary.bifromq.inbox.store;

import com.zachary.bifromq.basekv.localengine.RangeUtil;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProc;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProcFactory;
import com.zachary.bifromq.basekv.store.api.IKVRangeReader;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.google.protobuf.ByteString;

import java.time.Clock;
import java.time.Duration;
import java.util.function.Supplier;

import static com.zachary.bifromq.inbox.util.KeyUtil.parseScopedInboxId;

public class InboxStoreCoProcFactory implements IKVRangeCoProcFactory {
    private final IEventCollector eventCollector;
    private final Clock clock;
    private final Duration purgeDelay;

    public InboxStoreCoProcFactory(IEventCollector eventCollector, Clock clock, Duration purgeDelay) {
        this.eventCollector = eventCollector;
        this.clock = clock;
        this.purgeDelay = purgeDelay;
    }

    public ByteString findSplitKey(ByteString key) {
        return RangeUtil.upperBound(parseScopedInboxId(key));
    }

    @Override
    public IKVRangeCoProc create(KVRangeId id, Supplier<IKVRangeReader> rangeReaderProvider) {
        return new InboxStoreCoProc(id, rangeReaderProvider, eventCollector, clock, purgeDelay);
    }
}
