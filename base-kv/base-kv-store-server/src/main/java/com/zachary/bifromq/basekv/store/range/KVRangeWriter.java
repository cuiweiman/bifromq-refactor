package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.localengine.IKVEngine;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.Range;
import com.zachary.bifromq.basekv.proto.State;
import com.zachary.bifromq.basekv.store.api.IKVWriter;
import com.zachary.bifromq.basekv.store.util.KVUtil;
import com.zachary.bifromq.basekv.store.util.VerUtil;

public class KVRangeWriter implements IKVRangeWriter {
    private final KVRangeId rangeId;
    private final IKVEngine kvEngine;
    private final IKVRangeMetadataAccessor metadata;
    private final IKVWriter writer;
    private final int batchId;
    private final KVRangeStateAccessor.KVRangeWriterMutator mutator;
    private long updatedVersion;
    private State updatedState;
    private volatile boolean done;

    public KVRangeWriter(KVRangeId rangeId, IKVRangeMetadataAccessor metadata, IKVEngine kvEngine,
                         KVRangeStateAccessor.KVRangeWriterMutator mutator) {
        this.rangeId = rangeId;
        this.kvEngine = kvEngine;
        metadata.refresh();
        this.metadata = metadata;
        this.batchId = kvEngine.startBatch();
        this.mutator = mutator;
        this.writer = new KVWriter(batchId, metadata, kvEngine);
        this.updatedVersion = metadata.version();
        this.updatedState = metadata.state();
    }

    @Override
    public void abort() {
        assert !done;
        kvEngine.abortBatch(batchId);
        done = true;
    }

    @Override
    public int size() {
        assert !done;
        return kvEngine.batchSize(batchId);
    }

    @Override
    public void close() {
        assert !done;
        if (updatedVersion > metadata.version() || !updatedState.equals(metadata.state())) {
            mutator.run(() -> {
                kvEngine.endBatch(batchId);
                metadata.refresh();
            });
        } else {
            mutator.run(() -> kvEngine.endBatch(batchId));
        }
        done = true;
    }

    @Override
    public IKVRangeWriter bumpVer(boolean toOdd) {
        assert !done;
        resetVer(VerUtil.bump(metadata.version(), toOdd));
        return this;
    }

    @Override
    public IKVRangeWriter resetVer(long ver) {
        assert !done;
        kvEngine.put(batchId, IKVEngine.DEFAULT_NS, KVRangeKeys.verKey(rangeId), KVUtil.toByteStringNativeOrder(ver));
        updatedVersion = ver;
        return this;
    }

    @Override
    public IKVRangeWriter setLastAppliedIndex(long lastAppliedIndex) {
        assert !done;
        kvEngine.put(batchId, IKVEngine.DEFAULT_NS, KVRangeKeys.lastAppliedIndexKey(rangeId),
            KVUtil.toByteString(lastAppliedIndex));
        return this;
    }

    @Override
    public IKVRangeWriter setRange(Range range) {
        assert !done;
        kvEngine.put(batchId, IKVEngine.DEFAULT_NS, KVRangeKeys.rangeKey(rangeId), range.toByteString());
        return this;
    }

    @Override
    public IKVRangeWriter setState(State state) {
        assert !done;
        kvEngine.put(batchId, IKVEngine.DEFAULT_NS, KVRangeKeys.stateKey(rangeId), state.toByteString());
        updatedState = state;
        return this;
    }

    @Override
    public IKVWriter kvWriter() {
        assert !done;
        return writer;
    }
}
