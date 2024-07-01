package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.localengine.IKVEngineIterator;
import com.zachary.bifromq.basekv.store.api.IKVIterator;
import com.google.protobuf.ByteString;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class KVRangeIteratorTest {
    @Mock
    private IKVEngineIterator engineIterator;
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
    public void key() {
        ByteString userKey = ByteString.copyFromUtf8("key");
        when(engineIterator.key()).thenReturn(KVRangeKeys.dataKey(userKey));
        IKVIterator itr = new KVRangeIterator(() -> engineIterator);
        assertEquals(itr.key(), userKey);
    }

    @Test
    public void value() {
        IKVIterator itr = new KVRangeIterator(() -> engineIterator);
        itr.value();
        verify(engineIterator).value();
    }

    @Test
    public void isValid() {
        IKVIterator itr = new KVRangeIterator(() -> engineIterator);
        itr.isValid();
        verify(engineIterator).isValid();
    }

    @Test
    public void next() {
        IKVIterator itr = new KVRangeIterator(() -> engineIterator);
        itr.next();
        verify(engineIterator).next();
    }

    @Test
    public void prev() {
        IKVIterator itr = new KVRangeIterator(() -> engineIterator);
        itr.prev();
        verify(engineIterator).prev();
    }

    @Test
    public void seekToFirst() {
        IKVIterator itr = new KVRangeIterator(() -> engineIterator);
        itr.seekToFirst();
        verify(engineIterator).seekToFirst();
    }

    @Test
    public void seekToLast() {
        IKVIterator itr = new KVRangeIterator(() -> engineIterator);
        itr.seekToLast();
        verify(engineIterator).seekToLast();
    }

    @Test
    public void seek() {
        ByteString userKey = ByteString.copyFromUtf8("key");
        IKVIterator itr = new KVRangeIterator(() -> engineIterator);
        itr.seek(userKey);
        ArgumentCaptor<ByteString> captor = ArgumentCaptor.forClass(ByteString.class);
        verify(engineIterator).seek(captor.capture());
        assertEquals(captor.getValue(), KVRangeKeys.dataKey(userKey));
    }

    @Test
    public void seekForPrev() {
        ByteString userKey = ByteString.copyFromUtf8("key");
        IKVIterator itr = new KVRangeIterator(() -> engineIterator);
        itr.seekForPrev(userKey);
        ArgumentCaptor<ByteString> captor = ArgumentCaptor.forClass(ByteString.class);
        verify(engineIterator).seekForPrev(captor.capture());
        assertEquals(captor.getValue(), KVRangeKeys.dataKey(userKey));
    }

}
