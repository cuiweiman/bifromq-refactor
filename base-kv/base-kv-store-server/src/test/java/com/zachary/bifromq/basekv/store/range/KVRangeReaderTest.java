package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.localengine.IKVEngine;
import com.zachary.bifromq.basekv.localengine.IKVEngineIterator;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.Range;
import com.zachary.bifromq.basekv.proto.State;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.google.protobuf.ByteString;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static com.zachary.bifromq.basekv.Constants.EMPTY_RANGE;
import static com.zachary.bifromq.basekv.Constants.FULL_RANGE;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.lastAppliedIndexKey;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.rangeKey;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.stateKey;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.verKey;
import static com.zachary.bifromq.basekv.store.util.KVUtil.toByteString;
import static com.zachary.bifromq.basekv.store.util.KVUtil.toByteStringNativeOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class KVRangeReaderTest {
    @Mock
    private IKVEngine engine;
    @Mock
    private IKVEngineIterator engineIterator;
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
    public void init() {
        KVRangeId id = KVRangeIdUtil.generate();
        when(engine.get(anyString(), any(ByteString.class))).thenReturn(Optional.empty());
        when(engine.registerKeyRange(anyString(), any(ByteString.class), any(ByteString.class)))
            .thenReturn(1);
        KVRangeReader rangeReader = new KVRangeReader(id, engine, accessor.refresher());
        assertEquals(rangeReader.ver(), -1L);
        assertEquals(rangeReader.lastAppliedIndex(), -1L);
        assertEquals(rangeReader.state().getType(), State.StateType.Normal);
        assertEquals(rangeReader.kvReader().range(), EMPTY_RANGE);
    }

    @Test
    public void refresh() {
        KVRangeId id = KVRangeIdUtil.generate();
        when(engine.newIterator(1)).thenReturn(engineIterator);
        when(engine.get(anyString(), any(ByteString.class))).thenReturn(Optional.empty());
        when(engine.registerKeyRange(anyString(), any(ByteString.class), any(ByteString.class)))
            .thenReturn(1);

        KVRangeReader rangeReader = new KVRangeReader(id, engine, accessor.refresher());

        int keyRangeId = 2;
        long ver = 10;
        Range range = FULL_RANGE;
        Range dataBound = KVRangeKeys.dataBound(range);
        long lastAppliedIndex = 10;
        State state = State.newBuilder().setType(State.StateType.Normal).build();

        when(engine.registerKeyRange(IKVEngine.DEFAULT_NS, dataBound.getStartKey(), dataBound.getEndKey()))
            .thenReturn(keyRangeId);
        when(engine.get(IKVEngine.DEFAULT_NS, verKey(id))).thenReturn(Optional.of(toByteStringNativeOrder(ver)));
        when(engine.get(IKVEngine.DEFAULT_NS, rangeKey(id))).thenReturn(Optional.of(range.toByteString()));
        when(engine.get(IKVEngine.DEFAULT_NS, lastAppliedIndexKey(id)))
            .thenReturn(Optional.of(toByteString(lastAppliedIndex)));
        when(engine.get(IKVEngine.DEFAULT_NS, stateKey(id))).thenReturn(Optional.of(state.toByteString()));

        accessor.mutator().run(() -> {
        });

        rangeReader.refresh();

        assertEquals(rangeReader.ver(), 10);
        assertEquals(rangeReader.lastAppliedIndex(), 10);
        assertEquals(rangeReader.state().getType(), State.StateType.Normal);
        assertEquals(rangeReader.kvReader().range(), FULL_RANGE);
        verify(engine, times(2)).newIterator(2);
        verify(engineIterator, times(2)).close();
    }
}
