package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.localengine.IKVEngine;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KVRangeWriterTest {
    @Mock
    private IKVEngine engine;
    @Mock
    private IKVRangeMetadataAccessor metadataAccessor;
    private KVRangeStateAccessor accessor = new KVRangeStateAccessor();
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
    public void update() {
        KVRangeId rangeId = KVRangeIdUtil.generate();
        when(metadataAccessor.dataBoundId()).thenReturn(1);
        when(metadataAccessor.version()).thenReturn(0L);
        when(engine.startBatch()).thenReturn(1);
        KVRangeWriter rangeWriter = new KVRangeWriter(rangeId, metadataAccessor, engine, accessor.mutator());

        rangeWriter.resetVer(2);
        rangeWriter.close();
        verify(engine).endBatch(1);
        verify(metadataAccessor, times(2)).refresh();
    }
}
