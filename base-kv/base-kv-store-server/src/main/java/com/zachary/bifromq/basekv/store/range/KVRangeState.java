package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.localengine.IKVEngine;
import com.zachary.bifromq.basekv.localengine.IKVEngineIterator;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.KVRangeSnapshot;
import com.zachary.bifromq.basekv.proto.Range;
import com.zachary.bifromq.basekv.store.api.IKVIterator;
import com.zachary.bifromq.basekv.store.api.IKVRangeReader;
import io.reactivex.rxjava3.core.Observable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
class KVRangeState implements IKVRangeState {
    private final KVRangeId rangeId;
    private final IKVEngine kvEngine;
    private final KVRangeMetadataAccessor metadata;
    private final KVRangeStateAccessor accessor;
    //    private final Set<IKVRangeReader> sharedReaders = new LinkedHashSet<>();
    private final ConcurrentLinkedQueue<IKVRangeReader> sharedReaders = new ConcurrentLinkedQueue<>();

    KVRangeState(KVRangeSnapshot snapshot, IKVEngine kvEngine) {
        this(snapshot.getId(), kvEngine);
        reset(snapshot).close();
    }

    KVRangeState(KVRangeId id, IKVEngine kvEngine) {
        this.rangeId = id;
        this.kvEngine = kvEngine;
        this.metadata = new KVRangeMetadataAccessor(rangeId, kvEngine);
        this.accessor = new KVRangeStateAccessor();
    }

    @Override
    public KVRangeSnapshot checkpoint() {
        String checkpointId = kvEngine.checkpoint();
        KVRangeMetadataAccessor metadata = new KVRangeMetadataAccessor(rangeId, kvEngine, checkpointId);
        KVRangeSnapshot.Builder builder = KVRangeSnapshot.newBuilder()
            .setVer(metadata.version())
            .setId(rangeId)
            .setCheckpointId(checkpointId)
            .setLastAppliedIndex(metadata.lastAppliedIndex())
            .setState(metadata.state())
            .setRange(metadata.range());
        return builder.build();
    }

    @Override
    public boolean hasCheckpoint(KVRangeSnapshot checkpoint) {
        assert checkpoint.getId().equals(rangeId);
        return checkpoint.hasCheckpointId() && kvEngine.hasCheckpoint(checkpoint.getCheckpointId());
    }

    @Override
    public IKVIterator open(KVRangeSnapshot checkpoint) {
        assert hasCheckpoint(checkpoint);
        KVRangeMetadataAccessor metadata =
            new KVRangeMetadataAccessor(checkpoint.getId(), kvEngine, checkpoint.getCheckpointId());
        Range dataBound = metadata.dataBound();
        IKVEngineIterator dataIterator = kvEngine.newIterator(checkpoint.getCheckpointId(),
            metadata.dataBoundId(), dataBound.getStartKey(), dataBound.getEndKey());
        return new KVRangeIterator(() -> dataIterator);
    }

    @SneakyThrows
    @Override
    public IKVRangeReader borrow() {
        IKVRangeReader reader = sharedReaders.poll();
        if (reader == null) {
            return new KVRangeReader(rangeId, kvEngine, accessor.refresher());
        }
        reader.refresh();
        return reader;

//        if (!sharedReaders.isEmpty()) {
//            synchronized (this) {
//                Iterator<IKVRangeReader> readerItr = sharedReaders.iterator();
//                IKVRangeReader reader;
//                if (readerItr.hasNext()) {
//                    reader = readerItr.next();
//                    readerItr.remove();
//                    reader.refresh();
//                } else {
//                    reader = new KVRangeReader(rangeId, kvEngine, accessor.refresher());
//                }
//                return reader;
//            }
//        } else {
//            IKVRangeReader reader = new KVRangeReader(rangeId, kvEngine, accessor.refresher());
//            return reader;
//        }
    }

    @Override
    public void returnBorrowed(IKVRangeReader reader) {
        sharedReaders.add(reader);
//        synchronized (this) {
//            sharedReaders.add(reader);
//        }
    }

    @Override
    public IKVRangeReader getReader() {
        return new KVRangeReader(rangeId, kvEngine, accessor.refresher());
    }

    @Override
    public IKVRangeWriter getWriter() {
        return new KVRangeWriter(rangeId, metadata, kvEngine, accessor.mutator());
    }

    @Override
    public Observable<KVRangeMeta> metadata() {
        return metadata.source();
    }

    @Override
    public IKVRangeRestorer reset(KVRangeSnapshot checkpoint) {
        assert rangeId.equals(checkpoint.getId());
        return new KVRangeRestorer(checkpoint, metadata, kvEngine, accessor.mutator());
    }

    @Override
    public void destroy(boolean includeData) {
        metadata.destroy(includeData);
    }
}
