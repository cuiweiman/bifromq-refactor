package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.localengine.IKVEngine;
import com.zachary.bifromq.basekv.proto.KVPair;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.KVRangeSnapshot;
import com.zachary.bifromq.basekv.proto.State;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.google.protobuf.ByteString;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.zachary.bifromq.basekv.Constants.FULL_RANGE;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.dataBound;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.dataKey;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.lastAppliedIndexKey;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.rangeKey;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.stateKey;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.verKey;
import static com.zachary.bifromq.basekv.store.util.KVUtil.toByteString;
import static com.zachary.bifromq.basekv.store.util.KVUtil.toByteStringNativeOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KVRangeRestorerTest {
    @Mock
    IKVRangeMetadataAccessor metadata;
    @Mock
    IKVEngine kvEngine;

    KVRangeStateAccessor accessor = new KVRangeStateAccessor();
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
    public void restore() {
        KVRangeId rangeId = KVRangeIdUtil.generate();
        int batchId = 1;
        KVRangeSnapshot snapshot = KVRangeSnapshot.newBuilder()
            .setId(rangeId)
            .setVer(0)
            .setLastAppliedIndex(10)
            .setRange(FULL_RANGE)
            .setState(State.newBuilder().setType(State.StateType.Normal).build())
            .setCheckpointId("CheckPoint")
            .build();
        ByteString key = ByteString.copyFromUtf8("Key");
        ByteString val = ByteString.copyFromUtf8("Val");

        when(kvEngine.startBatch()).thenReturn(batchId);
        when(metadata.dataBound()).thenReturn(dataBound(FULL_RANGE));

        KVRangeRestorer restorer = new KVRangeRestorer(snapshot, metadata, kvEngine, accessor.mutator());
        restorer.add(KVPair.newBuilder().setKey(key).setValue(val).build());
        restorer.close();

        verify(metadata, times(2)).refresh();
        verify(kvEngine).put(batchId, IKVEngine.DEFAULT_NS, verKey(rangeId),
            toByteStringNativeOrder(snapshot.getVer()));
        verify(kvEngine).put(batchId, IKVEngine.DEFAULT_NS, lastAppliedIndexKey(rangeId),
            toByteString(snapshot.getLastAppliedIndex()));
        verify(kvEngine).put(batchId, IKVEngine.DEFAULT_NS, rangeKey(rangeId), snapshot.getRange().toByteString());
        verify(kvEngine).put(batchId, IKVEngine.DEFAULT_NS, stateKey(rangeId), snapshot.getState().toByteString());
        verify(kvEngine).clearSubRange(batchId, metadata.dataBoundId(),
            metadata.dataBound().getStartKey(),
            metadata.dataBound().getEndKey());
        kvEngine.put(batchId, IKVEngine.DEFAULT_NS, dataKey(key), val);

        verify(kvEngine).endBatch(batchId);
    }
}
