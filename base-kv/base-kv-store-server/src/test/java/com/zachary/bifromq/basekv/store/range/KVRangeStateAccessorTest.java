package com.zachary.bifromq.basekv.store.range;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class KVRangeStateAccessorTest {

    @Mock
    private Runnable refresh;

    @Mock
    private Runnable mutate;
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
    public void refreshAsNeeded() {
        KVRangeStateAccessor accessor = new KVRangeStateAccessor();
        KVRangeStateAccessor.KVRangeReaderRefresher refresher1 = accessor.refresher();
        KVRangeStateAccessor.KVRangeReaderRefresher refresher2 = accessor.refresher();
        KVRangeStateAccessor.KVRangeWriterMutator mutator = accessor.mutator();

        refresher1.run(refresh);
        refresher2.run(refresh);
        verify(refresh, never()).run();

        mutator.run(mutate);
        verify(mutate, times(1)).run();

        refresher1.run(refresh);
        refresher2.run(refresh);
        verify(refresh, times(2)).run();

        refresher1.run(refresh);
        refresher2.run(refresh);
        verify(refresh, times(2)).run();
    }
}
