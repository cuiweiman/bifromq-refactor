package com.zachary.bifromq.basekv.store.range;

import com.google.common.util.concurrent.MoreExecutors;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class KVRangeQueryLinearizerTest {
    @Mock
    private Supplier<CompletableFuture<Long>> readIndexSupplier;
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
    public void linearize() {
        KVRangeQueryLinearizer linearizer =
            new KVRangeQueryLinearizer(readIndexSupplier, MoreExecutors.directExecutor());
        when(readIndexSupplier.get())
            .thenReturn(CompletableFuture.completedFuture(1L),
                CompletableFuture.completedFuture(1L),
                CompletableFuture.completedFuture(2L),
                CompletableFuture.completedFuture(2L));
        CompletableFuture<Void> t1 = linearizer.linearize().toCompletableFuture();
        CompletableFuture<Void> t2 = linearizer.linearize().toCompletableFuture();
        CompletableFuture<Void> t3 = linearizer.linearize().toCompletableFuture();
        assertFalse(t1.isDone());
        assertFalse(t2.isDone());
        assertFalse(t3.isDone());
        linearizer.afterLogApplied(1);
        assertTrue(t1.isDone());
        assertTrue(t2.isDone());
        assertFalse(t3.isDone());
        linearizer.afterLogApplied(2L);
        assertTrue(t3.isDone());

        assertTrue(linearizer.linearize().toCompletableFuture().isDone());
    }
}
