package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.proto.State;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProc;
import com.zachary.bifromq.basekv.store.api.IKVRangeReader;
import com.zachary.bifromq.basekv.store.api.IKVReader;
import com.zachary.bifromq.basekv.store.exception.KVRangeException;
import com.google.protobuf.ByteString;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class KVRangeQueryRunnerTest {
    @Mock
    private IKVRangeState accessor;
    @Mock
    private IKVRangeReader rangeReader;
    @Mock
    private IKVReader kvReader;
    @Mock
    private IKVRangeQueryLinearizer linearizer;
    @Mock
    private IKVRangeCoProc coProc;
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
    public void badVersionQuery() {
        KVRangeQueryRunner runner = new KVRangeQueryRunner(accessor, coProc, directExecutor(), linearizer);
        when(accessor.borrow()).thenReturn(rangeReader);
        when(rangeReader.ver()).thenReturn(1L);

        CompletableFuture<ByteString> queryFuture = runner.queryCoProc(0, ByteString.copyFromUtf8("key"), false);
        verify(accessor).returnBorrowed(rangeReader);
        try {
            queryFuture.join();
            fail();
        } catch (Throwable e) {
            assertTrue(e.getCause() instanceof KVRangeException.BadVersion);
        }
    }

    @Test
    public void internalErrorByMergedState() {
        internalErrorByWrongState(State.StateType.Merged);
    }

    @Test
    public void internalErrorByRemovedState() {
        internalErrorByWrongState(State.StateType.Removed);
    }

    @Test
    public void internalErrorByPurgedState() {
        internalErrorByWrongState(State.StateType.Purged);
    }

    private void internalErrorByWrongState(State.StateType stateType) {
        KVRangeQueryRunner runner = new KVRangeQueryRunner(accessor, coProc, directExecutor(), linearizer);
        when(accessor.borrow()).thenReturn(rangeReader);
        when(rangeReader.state()).thenReturn(State.newBuilder().setType(stateType).build());

        CompletableFuture<ByteString> queryFuture = runner.queryCoProc(0, ByteString.copyFromUtf8("key"), false);
        verify(accessor).returnBorrowed(rangeReader);
        try {
            queryFuture.join();
            fail();
        } catch (Throwable e) {
            assertTrue(e.getCause() instanceof KVRangeException.TryLater);
        }
    }

    @Test
    public void get() {
        KVRangeQueryRunner runner = new KVRangeQueryRunner(accessor, coProc, directExecutor(), linearizer);
        when(accessor.borrow()).thenReturn(rangeReader);
        when(rangeReader.kvReader()).thenReturn(kvReader);
        when(rangeReader.ver()).thenReturn(0L);
        when(rangeReader.state()).thenReturn(State.newBuilder().setType(State.StateType.Normal).build());
        when(kvReader.get(any(ByteString.class))).thenReturn(Optional.empty());
        CompletionStage<Optional<ByteString>> queryFuture = runner.get(0, ByteString.copyFromUtf8("key"), false);
        verify(accessor).returnBorrowed(rangeReader);
        try {
            Optional<ByteString> result = queryFuture.toCompletableFuture().join();
            assertFalse(result.isPresent());
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    public void exist() {
        KVRangeQueryRunner runner = new KVRangeQueryRunner(accessor, coProc, directExecutor(), linearizer);
        when(accessor.borrow()).thenReturn(rangeReader);
        when(rangeReader.kvReader()).thenReturn(kvReader);
        when(rangeReader.ver()).thenReturn(0L);
        when(rangeReader.state()).thenReturn(State.newBuilder().setType(State.StateType.Normal).build());
        when(kvReader.exist(any(ByteString.class))).thenReturn(false);
        CompletionStage<Boolean> queryFuture = runner.exist(0, ByteString.copyFromUtf8("key"), false);
        verify(accessor).returnBorrowed(rangeReader);
        try {
            assertFalse(queryFuture.toCompletableFuture().join());
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    public void roCoProc() {
        KVRangeQueryRunner runner = new KVRangeQueryRunner(accessor, coProc, directExecutor(), linearizer);
        ByteString key = ByteString.copyFromUtf8("key");
        ByteString value = ByteString.copyFromUtf8("value");
        when(accessor.borrow()).thenReturn(rangeReader);
        when(rangeReader.kvReader()).thenReturn(kvReader);
        when(rangeReader.ver()).thenReturn(0L);
        when(rangeReader.state()).thenReturn(State.newBuilder().setType(State.StateType.Normal).build());
        when(coProc.query(any(ByteString.class), any(IKVReader.class)))
            .thenReturn(CompletableFuture.completedFuture(value));
        CompletableFuture<ByteString> queryFuture = runner.queryCoProc(0, key, false);
        verify(accessor).returnBorrowed(rangeReader);
        ArgumentCaptor<ByteString> inputCap = ArgumentCaptor.forClass(ByteString.class);
        ArgumentCaptor<IKVReader> kvReaderCap = ArgumentCaptor.forClass(IKVReader.class);
        verify(coProc).query(inputCap.capture(), kvReaderCap.capture());
        assertEquals(inputCap.getValue(), key);
        assertEquals(kvReaderCap.getValue(), kvReader);
        try {
            assertEquals(queryFuture.join(), value);
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    public void linearizedRoCoProc() {
        KVRangeQueryRunner runner = new KVRangeQueryRunner(accessor, coProc, directExecutor(), linearizer);
        ByteString key = ByteString.copyFromUtf8("key");
        ByteString value = ByteString.copyFromUtf8("value");
        when(accessor.borrow()).thenReturn(rangeReader);
        when(rangeReader.kvReader()).thenReturn(kvReader);
        when(rangeReader.ver()).thenReturn(0L);
        when(rangeReader.state()).thenReturn(State.newBuilder().setType(State.StateType.Normal).build());
        when(coProc.query(any(ByteString.class), any(IKVReader.class)))
            .thenReturn(CompletableFuture.completedFuture(value));
        when(linearizer.linearize()).thenReturn(CompletableFuture.completedFuture(null));
        CompletableFuture<ByteString> queryFuture = runner.queryCoProc(0, key, true);
        verify(accessor).returnBorrowed(rangeReader);
        ArgumentCaptor<ByteString> inputCap = ArgumentCaptor.forClass(ByteString.class);
        ArgumentCaptor<IKVReader> kvReaderCap = ArgumentCaptor.forClass(IKVReader.class);
        verify(coProc).query(inputCap.capture(), kvReaderCap.capture());
        assertEquals(inputCap.getValue(), key);
        assertEquals(kvReaderCap.getValue(), kvReader);
        try {
            assertEquals(queryFuture.join(), value);
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    public void close() {
        KVRangeQueryRunner runner = new KVRangeQueryRunner(accessor, coProc, directExecutor(), linearizer);
        ByteString key = ByteString.copyFromUtf8("key");
        when(accessor.borrow()).thenReturn(rangeReader);
        when(rangeReader.kvReader()).thenReturn(kvReader);
        when(rangeReader.ver()).thenReturn(0L);
        when(rangeReader.state()).thenReturn(State.newBuilder().setType(State.StateType.Normal).build());

        when(linearizer.linearize()).thenReturn(new CompletableFuture());
        when(coProc.query(any(ByteString.class), any(IKVReader.class))).thenReturn(new CompletableFuture<>());

        CompletableFuture<ByteString> queryFuture = runner.queryCoProc(0, key, false);
        CompletableFuture<ByteString> linearizedQueryFuture = runner.queryCoProc(0, key, true);

        runner.close();

        assertTrue(queryFuture.isCancelled());
        assertTrue(linearizedQueryFuture.isCancelled());
        assertTrue(runner.queryCoProc(0, key, false).isCancelled());
    }
}
