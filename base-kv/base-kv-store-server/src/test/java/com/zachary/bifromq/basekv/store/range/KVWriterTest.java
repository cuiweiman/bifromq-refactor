package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.localengine.IKVEngine;
import com.zachary.bifromq.basekv.proto.Range;
import com.google.protobuf.ByteString;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.zachary.bifromq.basekv.Constants.FULL_RANGE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KVWriterTest {
    @Mock
    private IKVEngine engine;
    @Mock
    private IKVRangeMetadataAccessor metadata;
    private AutoCloseable closeable;
    @BeforeMethod
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterMethod
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void write() {
        int batchId = 1;
        int dataBoundId = 2;
        when(metadata.dataBoundId()).thenReturn(dataBoundId);
        when(metadata.range()).thenReturn(FULL_RANGE);
        KVWriter writer = new KVWriter(batchId, metadata, engine);

        // delete
        ByteString delKey = ByteString.copyFromUtf8("delKey");
        writer.delete(delKey);
        verify(engine).delete(batchId, dataBoundId, KVRangeKeys.dataKey(delKey));

        // insert
        ByteString insKey = ByteString.copyFromUtf8("insertKey");
        ByteString insValue = ByteString.copyFromUtf8("insertValue");
        writer.insert(insKey, insValue);
        verify(engine).insert(batchId, dataBoundId, KVRangeKeys.dataKey(insKey), insValue);

        // put
        ByteString putKey = ByteString.copyFromUtf8("putKey");
        ByteString putValue = ByteString.copyFromUtf8("putValue");
        writer.put(putKey, putValue);
        verify(engine).put(batchId, dataBoundId, KVRangeKeys.dataKey(putKey), putValue);

        // delete range
        Range delRange = Range.newBuilder()
            .setStartKey(ByteString.copyFromUtf8("a"))
            .setStartKey(ByteString.copyFromUtf8("z"))
            .build();
        Range bound = KVRangeKeys.dataBound(delRange);
        writer.deleteRange(delRange);
        verify(engine).clearSubRange(batchId, dataBoundId, bound.getStartKey(), bound.getEndKey());
    }
}
