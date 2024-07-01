package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.localengine.IKVEngine;
import com.zachary.bifromq.basekv.localengine.IKVEngineIterator;
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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class KVReaderTest {
    @Mock
    private IKVRangeMetadataAccessor metadata;
    @Mock
    private IKVEngine engine;
    @Mock
    private IKVEngineIterator engineIteratorForPointQuery;

    @Mock
    private IKVEngineIterator engineIteratorForIteration;
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
    public void read() {
        KVReader reader = new KVReader(metadata, engine,
            () -> new IKVEngineIterator[] {engineIteratorForPointQuery, engineIteratorForIteration});

        // range
        when(metadata.range()).thenReturn(FULL_RANGE);
        reader.range();

        // size
        Range range = Range.newBuilder()
            .setStartKey(ByteString.copyFromUtf8("a"))
            .setEndKey(ByteString.copyFromUtf8("z"))
            .build();
        Range bound = KVRangeKeys.dataBound(range);
        reader.size(range);
        verify(engine).size(IKVEngine.DEFAULT_NS, bound.getStartKey(), bound.getEndKey());

        // exist
        when(engineIteratorForPointQuery.isValid()).thenReturn(false);
        ByteString existKey1 = ByteString.copyFromUtf8("existKey1");
        assertFalse(reader.exist(existKey1));
        verify(engineIteratorForPointQuery).seek(KVRangeKeys.dataKey(existKey1));

        when(engineIteratorForPointQuery.isValid()).thenReturn(true);
        when(engineIteratorForPointQuery.key()).thenReturn(ByteString.copyFromUtf8("existKey3"));
        ByteString existKey2 = ByteString.copyFromUtf8("existKey2");
        assertFalse(reader.exist(existKey2));
        verify(engineIteratorForPointQuery).seek(KVRangeKeys.dataKey(existKey2));

        when(engineIteratorForPointQuery.isValid()).thenReturn(true);
        when(engineIteratorForPointQuery.key()).thenReturn(KVRangeKeys.dataKey(ByteString.copyFromUtf8("existKey3")));
        ByteString existKey3 = ByteString.copyFromUtf8("existKey3");
        assertTrue(reader.exist(existKey3));
        verify(engineIteratorForPointQuery).seek(KVRangeKeys.dataKey(existKey3));

        // get
        when(engineIteratorForPointQuery.isValid()).thenReturn(false);
        ByteString getKey1 = ByteString.copyFromUtf8("getKey1");
        assertFalse(reader.get(getKey1).isPresent());
        verify(engineIteratorForPointQuery).seek(KVRangeKeys.dataKey(getKey1));

        when(engineIteratorForPointQuery.isValid()).thenReturn(true);
        when(engineIteratorForPointQuery.key()).thenReturn(ByteString.copyFromUtf8("getKey3"));
        ByteString getKey2 = ByteString.copyFromUtf8("getKey2");
        assertFalse(reader.get(getKey2).isPresent());
        verify(engineIteratorForPointQuery).seek(KVRangeKeys.dataKey(getKey2));

        ByteString getKey3 = ByteString.copyFromUtf8("getKey3");
        ByteString getValue3 = ByteString.copyFromUtf8("getValue3");
        when(engineIteratorForPointQuery.isValid()).thenReturn(true);
        when(engineIteratorForPointQuery.key()).thenReturn(KVRangeKeys.dataKey(getKey3));
        when(engineIteratorForPointQuery.value()).thenReturn(getValue3);
        assertEquals(reader.get(getKey3).get(), getValue3);
        verify(engineIteratorForPointQuery).seek(KVRangeKeys.dataKey(getKey3));
    }
}
