package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.localengine.IKVEngine;
import com.zachary.bifromq.basekv.localengine.IKVEngineIterator;
import com.zachary.bifromq.basekv.proto.Range;
import com.zachary.bifromq.basekv.store.api.IKVIterator;
import com.zachary.bifromq.basekv.store.api.IKVReader;
import com.google.protobuf.ByteString;

import java.util.Optional;
import java.util.function.Supplier;

import static com.zachary.bifromq.basekv.utils.KeyRangeUtil.contains;
import static com.zachary.bifromq.basekv.utils.KeyRangeUtil.inRange;

class KVReader implements IKVReader {
    private final IKVRangeMetadataAccessor metadata;
    private final IKVEngine engine;
    private final Supplier<IKVEngineIterator[]> dataIterator;

    KVReader(IKVRangeMetadataAccessor metadata,
             IKVEngine engine,
             Supplier<IKVEngineIterator[]> dataIterator) {
        this.metadata = metadata;
        this.engine = engine;
        this.dataIterator = dataIterator;
    }

    @Override
    public Range range() {
        return metadata.range();
    }

    @Override
    public long size(Range range) {
        assert contains(range, range());
        Range bound = KVRangeKeys.dataBound(range);
        return engine.size(IKVEngine.DEFAULT_NS, bound.getStartKey(), bound.getEndKey());
    }

    @Override
    public boolean exist(ByteString key) {
        assert inRange(key, range());
        ByteString dataKey = KVRangeKeys.dataKey(key);
        IKVEngineIterator itr = dataIterator.get()[0];
        itr.seek(dataKey);
        return itr.isValid() && itr.key().equals(dataKey);
    }

    @Override
    public Optional<ByteString> get(ByteString key) {
        assert inRange(key, range());
        ByteString dataKey = KVRangeKeys.dataKey(key);
        IKVEngineIterator itr = dataIterator.get()[0];
        itr.seek(dataKey);
        if (!itr.isValid() || !itr.key().equals(dataKey)) {
            return Optional.empty();
        } else {
            return Optional.of(itr.value());
        }
    }

    @Override
    public IKVIterator iterator() {
        return new KVRangeIterator(() -> dataIterator.get()[1]);
    }
}
