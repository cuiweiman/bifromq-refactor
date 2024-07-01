package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.localengine.IKVEngine;
import com.zachary.bifromq.basekv.proto.Range;
import com.zachary.bifromq.basekv.store.api.IKVWriter;
import com.zachary.bifromq.basekv.utils.KeyRangeUtil;
import com.google.protobuf.ByteString;

public class KVWriter implements IKVWriter {
    private final int batchId;
    private final IKVEngine kvEngine;
    private final IKVRangeMetadataAccessor metadata;
    private final int keyRangeId;

    public KVWriter(int batchId, IKVRangeMetadataAccessor metadata, IKVEngine kvEngine) {
        this.batchId = batchId;
        this.kvEngine = kvEngine;
        this.keyRangeId = metadata.dataBoundId();
        this.metadata = metadata;
    }


    @Override
    public void delete(ByteString key) {
        assert KeyRangeUtil.inRange(key, metadata.range());
        kvEngine.delete(batchId, keyRangeId, KVRangeKeys.dataKey(key));
    }

    @Override
    public void deleteRange(Range range) {
        assert KeyRangeUtil.contains(range, metadata.range());
        Range bound = KVRangeKeys.dataBound(range);
        kvEngine.clearSubRange(batchId, keyRangeId, bound.getStartKey(), bound.getEndKey());
    }

    @Override
    public void insert(ByteString key, ByteString value) {
        assert KeyRangeUtil.inRange(key, metadata.range());
        kvEngine.insert(batchId, keyRangeId, KVRangeKeys.dataKey(key), value);
    }

    @Override
    public void put(ByteString key, ByteString value) {
        assert KeyRangeUtil.inRange(key, metadata.range());
        kvEngine.put(batchId, keyRangeId, KVRangeKeys.dataKey(key), value);
    }
}
