package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.localengine.IKVEngine;
import com.zachary.bifromq.basekv.proto.KVPair;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.KVRangeSnapshot;
import com.zachary.bifromq.basekv.store.util.KVUtil;

import static com.zachary.bifromq.basekv.localengine.IKVEngine.DEFAULT_NS;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.dataKey;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.lastAppliedIndexKey;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.rangeKey;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.stateKey;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.verKey;
import static com.zachary.bifromq.basekv.store.util.KVUtil.toByteString;

public class KVRangeRestorer implements IKVRangeRestorer {
    private final IKVEngine kvEngine;
    private final IKVRangeMetadataAccessor metadata;
    private final KVRangeStateAccessor.KVRangeWriterMutator mutator;
    private final int batchId;

    public KVRangeRestorer(KVRangeSnapshot checkpoint,
                           IKVRangeMetadataAccessor metadata,
                           IKVEngine kvEngine,
                           KVRangeStateAccessor.KVRangeWriterMutator mutator) {
        this.kvEngine = kvEngine;
        this.metadata = metadata;
        this.mutator = mutator;
        metadata.refresh();
        this.batchId = kvEngine.startBatch();
        KVRangeId rangeId = checkpoint.getId();
        kvEngine.put(batchId, DEFAULT_NS, verKey(rangeId),
            KVUtil.toByteStringNativeOrder(checkpoint.getVer()));
        kvEngine.put(batchId, DEFAULT_NS, lastAppliedIndexKey(rangeId), toByteString(checkpoint.getLastAppliedIndex()));
        kvEngine.put(batchId, DEFAULT_NS, rangeKey(rangeId), checkpoint.getRange().toByteString());
        kvEngine.put(batchId, DEFAULT_NS, stateKey(rangeId), checkpoint.getState().toByteString());
        kvEngine.clearSubRange(batchId, metadata.dataBoundId(),
            metadata.dataBound().getStartKey(),
            metadata.dataBound().getEndKey());
    }

    @Override
    public void add(KVPair kvPair) {
        kvEngine.put(batchId, DEFAULT_NS, dataKey(kvPair.getKey()), kvPair.getValue());
    }

    @Override
    public void abort() {
        kvEngine.abortBatch(batchId);
    }

    @Override
    public int size() {
        return kvEngine.batchSize(batchId);
    }

    @Override
    public void close() {
        mutator.run(() -> {
            kvEngine.endBatch(batchId);
            metadata.refresh();
        });
    }
}
