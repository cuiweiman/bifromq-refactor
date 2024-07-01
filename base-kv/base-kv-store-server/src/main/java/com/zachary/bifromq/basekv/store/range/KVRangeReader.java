package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.localengine.IKVEngine;
import com.zachary.bifromq.basekv.localengine.IKVEngineIterator;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.Range;
import com.zachary.bifromq.basekv.proto.State;
import com.zachary.bifromq.basekv.store.api.IKVRangeReader;
import com.zachary.bifromq.basekv.store.api.IKVReader;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class KVRangeReader implements IKVRangeReader {
    private final IKVEngine kvEngine;
    private final KVRangeMetadataAccessor metadata;
    private final AtomicReference<IKVEngineIterator[]> engineIterator = new AtomicReference<>();
    private final KVRangeStateAccessor.KVRangeReaderRefresher refresher;

    public KVRangeReader(KVRangeId rangeId, IKVEngine engine, KVRangeStateAccessor.KVRangeReaderRefresher refresher) {
        this.kvEngine = engine;
        this.refresher = refresher;
        refresher.lock();
        try {
            this.metadata = new KVRangeMetadataAccessor(rangeId, engine);
            engineIterator.set(new IKVEngineIterator[] {
                engine.newIterator(metadata.dataBoundId()),
                engine.newIterator(metadata.dataBoundId())
            });
        } finally {
            refresher.unlock();
        }
    }

    @Override
    public long ver() {
        return metadata.version();
    }

    @Override
    public State state() {
        return metadata.state();
    }

    @Override
    public long lastAppliedIndex() {
        return metadata.lastAppliedIndex();
    }

    @Override
    public IKVReader kvReader() {
        return new KVReader(metadata, kvEngine, engineIterator::get);
    }

    @Override
    public void refresh() {
        Range orig = metadata.dataBound();
        refresher.run(() -> {
            metadata.refresh();
            Range now = metadata.dataBound();
            if (!orig.equals(now)) {
                for (IKVEngineIterator itr : engineIterator.getAndSet(
                    new IKVEngineIterator[] {
                        kvEngine.newIterator(metadata.dataBoundId()),
                        kvEngine.newIterator(metadata.dataBoundId())
                    })) {
                    itr.close();
                }
            } else {
                for (IKVEngineIterator itr : engineIterator.get()) {
                    itr.refresh();
                }
            }
        });
    }
}
