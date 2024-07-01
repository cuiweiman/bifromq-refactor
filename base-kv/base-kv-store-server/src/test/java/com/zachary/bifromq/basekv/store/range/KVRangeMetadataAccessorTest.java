package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.localengine.IKVEngine;
import com.zachary.bifromq.basekv.localengine.IKVEngineIterator;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.Range;
import com.zachary.bifromq.basekv.proto.State;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.core.Maybe;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static com.zachary.bifromq.basekv.Constants.EMPTY_RANGE;
import static com.zachary.bifromq.basekv.Constants.FULL_RANGE;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.dataKey;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.lastAppliedIndexKey;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.rangeKey;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.stateKey;
import static com.zachary.bifromq.basekv.store.range.KVRangeKeys.verKey;
import static com.zachary.bifromq.basekv.store.util.KVUtil.toByteString;
import static com.zachary.bifromq.basekv.store.util.KVUtil.toByteStringNativeOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

public class KVRangeMetadataAccessorTest {
    @Mock
    IKVEngine engine;

    @Mock
    IKVEngineIterator itr;
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
    public void initWithNoData() {
        KVRangeId id = KVRangeIdUtil.generate();
        when(engine.get(anyString(), any(ByteString.class))).thenReturn(Optional.empty());
        try {
            KVRangeMetadataAccessor metadata = new KVRangeMetadataAccessor(id, engine);
            assertEquals(metadata.version(), -1L);
            assertEquals(metadata.lastAppliedIndex(), -1);
            assertEquals(metadata.range(), EMPTY_RANGE);
            Maybe<IKVRangeState.KVRangeMeta> metaMayBe = metadata.source().firstElement();
            metadata.destroy(true);
            assertNull(metaMayBe.blockingGet());
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    public void initWithCompleteData() {
        KVRangeId id = KVRangeIdUtil.generate();
        int keyRangeId = 1;
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

        KVRangeMetadataAccessor metadata = new KVRangeMetadataAccessor(id, engine);
        assertEquals(metadata.version(), ver);
        assertEquals(metadata.range(), range);
        assertEquals(metadata.lastAppliedIndex(), lastAppliedIndex);
        assertEquals(metadata.state(), state);
        assertEquals(metadata.dataBoundId(), keyRangeId);

        IKVRangeState.KVRangeMeta meta = metadata.source().blockingFirst();
        assertEquals(meta.ver, ver);
        assertEquals(meta.range, range);
        assertEquals(meta.state, state);
    }

    @Test
    public void lastAppliedIndex() {
        KVRangeId id = KVRangeIdUtil.generate();
        int keyRangeId = 1;
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

        KVRangeMetadataAccessor metadata = new KVRangeMetadataAccessor(id, engine);
        assertEquals(metadata.lastAppliedIndex(), lastAppliedIndex);

        lastAppliedIndex = 11;
        when(engine.get(IKVEngine.DEFAULT_NS, lastAppliedIndexKey(id)))
            .thenReturn(Optional.of(toByteString(lastAppliedIndex)));
        assertEquals(metadata.lastAppliedIndex(), lastAppliedIndex);
    }

    @Test
    public void loadRangeWhenVersionIsZero() {
        KVRangeId id = KVRangeIdUtil.generate();
        int keyRangeId = 1;
        long ver = 0;
        Range range = EMPTY_RANGE;
        Range dataBound = KVRangeKeys.dataBound(range);
        long lastAppliedIndex = 0;
        State state = State.newBuilder().setType(State.StateType.Normal).build();

        when(engine.registerKeyRange(IKVEngine.DEFAULT_NS, dataBound.getStartKey(), dataBound.getEndKey()))
            .thenReturn(keyRangeId);
        when(engine.get(IKVEngine.DEFAULT_NS, verKey(id))).thenReturn(Optional.of(toByteStringNativeOrder(ver)));
        when(engine.get(IKVEngine.DEFAULT_NS, rangeKey(id))).thenReturn(Optional.of(range.toByteString()));
        when(engine.get(IKVEngine.DEFAULT_NS, lastAppliedIndexKey(id)))
            .thenReturn(Optional.of(toByteString(lastAppliedIndex)));
        when(engine.get(IKVEngine.DEFAULT_NS, stateKey(id))).thenReturn(Optional.of(state.toByteString()));

        KVRangeMetadataAccessor metadata = new KVRangeMetadataAccessor(id, engine);
        IKVRangeState.KVRangeMeta meta = metadata.source().blockingFirst();
        // version upgrade to 2;
        long newVer = 2;
        int newKeyRangeId = 2;
        Range newRange = Range.newBuilder()
            .setStartKey(ByteString.copyFromUtf8("a"))
            .setEndKey(ByteString.copyFromUtf8("b"))
            .build();
        Range newBound = KVRangeKeys.dataBound(newRange);

        when(engine.registerKeyRange(IKVEngine.DEFAULT_NS, newBound.getStartKey(), newBound.getEndKey()))
            .thenReturn(newKeyRangeId);
        when(engine.get(IKVEngine.DEFAULT_NS, verKey(id))).thenReturn(Optional.of(toByteStringNativeOrder(newVer)));
        when(engine.get(IKVEngine.DEFAULT_NS, rangeKey(id))).thenReturn(Optional.of(newRange.toByteString()));

        metadata.refresh();
        IKVRangeState.KVRangeMeta newMeta = metadata.source().blockingFirst();
        assertEquals(metadata.version(), newVer);
        assertEquals(metadata.range(), newRange);
        assertEquals(metadata.lastAppliedIndex(), lastAppliedIndex);
        assertEquals(metadata.dataBoundId(), newKeyRangeId);
        assertNotEquals(newMeta, meta);
    }

    @Test
    public void refreshWithNoRangeChange() {
        KVRangeId id = KVRangeIdUtil.generate();
        int keyRangeId = 1;
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

        KVRangeMetadataAccessor metadata = new KVRangeMetadataAccessor(id, engine);
        IKVRangeState.KVRangeMeta meta = metadata.source().blockingFirst();
        // version upgrade to 12;
        long newVer = 12;
        State newState = State.newBuilder().setType(State.StateType.ConfigChanging).build();

        when(engine.get(IKVEngine.DEFAULT_NS, verKey(id))).thenReturn(Optional.of(toByteStringNativeOrder(newVer)));
        when(engine.get(IKVEngine.DEFAULT_NS, stateKey(id))).thenReturn(Optional.of(newState.toByteString()));

        metadata.refresh();
        IKVRangeState.KVRangeMeta newMeta = metadata.source().blockingFirst();
        assertEquals(metadata.version(), newVer);
        assertEquals(metadata.range(), range);
        assertEquals(metadata.lastAppliedIndex(), lastAppliedIndex);
        assertEquals(metadata.state(), newState);
        assertEquals(metadata.dataBoundId(), keyRangeId);

        assertNotEquals(newMeta, meta);
    }

    @Test
    public void refreshWithRangeChanged() {
        KVRangeId id = KVRangeIdUtil.generate();
        int keyRangeId = 1;
        long ver = 10;
        Range range = FULL_RANGE;
        Range dataBound = KVRangeKeys.dataBound(range);
        State state = State.newBuilder().setType(State.StateType.Normal).build();

        when(engine.registerKeyRange(IKVEngine.DEFAULT_NS, dataBound.getStartKey(), dataBound.getEndKey()))
            .thenReturn(keyRangeId);
        when(engine.get(IKVEngine.DEFAULT_NS, verKey(id))).thenReturn(Optional.of(toByteStringNativeOrder(ver)));
        when(engine.get(IKVEngine.DEFAULT_NS, rangeKey(id))).thenReturn(Optional.of(range.toByteString()));
        when(engine.get(IKVEngine.DEFAULT_NS, stateKey(id))).thenReturn(Optional.of(state.toByteString()));

        KVRangeMetadataAccessor metadata = new KVRangeMetadataAccessor(id, engine);
        assertEquals(metadata.dataBound(), KVRangeKeys.dataBound(FULL_RANGE));
        IKVRangeState.KVRangeMeta meta = metadata.source().blockingFirst();

        // version upgrade to 11;
        long newVer = 11;
        int newKeyRangeId = 2;
        Range newRange = Range.newBuilder()
            .setStartKey(ByteString.copyFromUtf8("a"))
            .setEndKey(ByteString.copyFromUtf8("b"))
            .build();
        Range newBound = KVRangeKeys.dataBound(newRange);

        when(engine.registerKeyRange(IKVEngine.DEFAULT_NS, newBound.getStartKey(), newBound.getEndKey()))
            .thenReturn(newKeyRangeId);
        when(engine.get(IKVEngine.DEFAULT_NS, verKey(id))).thenReturn(Optional.of(toByteStringNativeOrder(newVer)));
        when(engine.get(IKVEngine.DEFAULT_NS, rangeKey(id))).thenReturn(Optional.of(newRange.toByteString()));

        metadata.refresh();
        IKVRangeState.KVRangeMeta newMeta = metadata.source().blockingFirst();

        verify(engine).unregisterKeyRange(keyRangeId);
        assertEquals(metadata.version(), newVer);
        assertEquals(metadata.range(), newRange);
        assertEquals(newBound, KVRangeKeys.dataBound(newRange));
        assertEquals(metadata.dataBoundId(), newKeyRangeId);
        assertNotEquals(newMeta, meta);
    }

    @Test
    public void destroyWithDataKept() {
        KVRangeId id = KVRangeIdUtil.generate();
        int keyRangeId = 1;
        long ver = 10;
        Range range = FULL_RANGE;
        Range dataBound = KVRangeKeys.dataBound(range);
        State state = State.newBuilder().setType(State.StateType.Normal).build();

        when(engine.registerKeyRange(IKVEngine.DEFAULT_NS, dataBound.getStartKey(), dataBound.getEndKey()))
            .thenReturn(keyRangeId);
        when(engine.get(IKVEngine.DEFAULT_NS, verKey(id))).thenReturn(Optional.of(toByteStringNativeOrder(ver)));
        when(engine.get(IKVEngine.DEFAULT_NS, rangeKey(id))).thenReturn(Optional.of(range.toByteString()));
        when(engine.get(IKVEngine.DEFAULT_NS, stateKey(id))).thenReturn(Optional.of(state.toByteString()));

        KVRangeMetadataAccessor metadata = new KVRangeMetadataAccessor(id, engine);
        int batchId = 1;
        when(engine.startBatch()).thenReturn(1);
        metadata.destroy(false);

        verify(engine).delete(batchId, IKVEngine.DEFAULT_NS, verKey(id));
        verify(engine).delete(batchId, IKVEngine.DEFAULT_NS, rangeKey(id));
        verify(engine).delete(batchId, IKVEngine.DEFAULT_NS, lastAppliedIndexKey(id));
        verify(engine).delete(batchId, IKVEngine.DEFAULT_NS, stateKey(id));
        verify(engine).endBatch(batchId);
        verify(engine).unregisterKeyRange(keyRangeId);
    }

    @Test
    public void destroyWithEmptyRange() {
        KVRangeId id = KVRangeIdUtil.generate();
        int keyRangeId = 1;
        long ver = 10;
        Range range = EMPTY_RANGE;
        Range dataBound = KVRangeKeys.dataBound(range);
        State state = State.newBuilder().setType(State.StateType.Normal).build();

        when(engine.registerKeyRange(IKVEngine.DEFAULT_NS, dataBound.getStartKey(), dataBound.getEndKey()))
            .thenReturn(keyRangeId);
        when(engine.get(IKVEngine.DEFAULT_NS, verKey(id))).thenReturn(Optional.of(toByteStringNativeOrder(ver)));
        when(engine.get(IKVEngine.DEFAULT_NS, rangeKey(id))).thenReturn(Optional.of(range.toByteString()));
        when(engine.get(IKVEngine.DEFAULT_NS, stateKey(id))).thenReturn(Optional.of(state.toByteString()));

        KVRangeMetadataAccessor metadata = new KVRangeMetadataAccessor(id, engine);

        int batchId = 1;
        when(engine.startBatch()).thenReturn(1);
        metadata.destroy(true);

        verify(engine).delete(batchId, IKVEngine.DEFAULT_NS, verKey(id));
        verify(engine).delete(batchId, IKVEngine.DEFAULT_NS, rangeKey(id));
        verify(engine).delete(batchId, IKVEngine.DEFAULT_NS, lastAppliedIndexKey(id));
        verify(engine).delete(batchId, IKVEngine.DEFAULT_NS, stateKey(id));
        verify(engine).endBatch(batchId);
        verify(engine).unregisterKeyRange(keyRangeId);
    }

    @Test
    public void destroyWithData() {
        KVRangeId id = KVRangeIdUtil.generate();
        int keyRangeId = 1;
        long ver = 10;
        Range range = FULL_RANGE;
        Range dataBound = KVRangeKeys.dataBound(range);
        State state = State.newBuilder().setType(State.StateType.Normal).build();

        when(engine.registerKeyRange(IKVEngine.DEFAULT_NS, dataBound.getStartKey(), dataBound.getEndKey()))
            .thenReturn(keyRangeId);
        when(engine.get(IKVEngine.DEFAULT_NS, verKey(id))).thenReturn(Optional.of(toByteStringNativeOrder(ver)));
        when(engine.get(IKVEngine.DEFAULT_NS, rangeKey(id))).thenReturn(Optional.of(range.toByteString()));
        when(engine.get(IKVEngine.DEFAULT_NS, stateKey(id))).thenReturn(Optional.of(state.toByteString()));

        KVRangeMetadataAccessor metadata = new KVRangeMetadataAccessor(id, engine);

        int batchId = 1;
        ByteString dataKey = dataKey(ByteString.copyFromUtf8("a"));
        when(engine.startBatch()).thenReturn(1);
        when(engine.newIterator(keyRangeId)).thenReturn(itr);
        when(itr.isValid()).thenReturn(true).thenReturn(false);
        when(itr.key()).thenReturn(dataKey);
        metadata.destroy(true);

        verify(engine).delete(batchId, IKVEngine.DEFAULT_NS, verKey(id));
        verify(engine).delete(batchId, IKVEngine.DEFAULT_NS, rangeKey(id));
        verify(engine).delete(batchId, IKVEngine.DEFAULT_NS, lastAppliedIndexKey(id));
        verify(engine).delete(batchId, IKVEngine.DEFAULT_NS, stateKey(id));
        verify(engine).endBatch(batchId);
        verify(engine).unregisterKeyRange(keyRangeId);
        verify(itr).seekToFirst();
        verify(itr).next();
        verify(engine).delete(batchId, keyRangeId, dataKey);
    }
}

