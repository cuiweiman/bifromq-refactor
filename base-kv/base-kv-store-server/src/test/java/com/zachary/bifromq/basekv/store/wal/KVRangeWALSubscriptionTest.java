package com.zachary.bifromq.basekv.store.wal;

import com.zachary.bifromq.basekv.proto.KVRangeSnapshot;
import com.zachary.bifromq.basekv.raft.proto.LogEntry;
import com.zachary.bifromq.basekv.store.exception.KVRangeException;
import com.google.common.collect.Iterators;
import com.google.common.util.concurrent.MoreExecutors;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Slf4j
public class KVRangeWALSubscriptionTest {
    private long maxSize = 1024;
    @Mock
    private IKVRangeWAL wal;
    private PublishSubject<IKVRangeWAL.SnapshotInstallTask> snapshotSource = PublishSubject.create();
    private BehaviorSubject<Long> commitIndexSource = BehaviorSubject.create();
    @Mock
    private IKVRangeWALSubscriber subscriber;

    private ExecutorService executor;
    private AutoCloseable closeable;
    @BeforeMethod
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        executor = Executors.newSingleThreadScheduledExecutor();
        when(wal.snapshotInstallTask()).thenReturn(snapshotSource);
    }

    @AfterMethod
    public void teardown() throws Exception {
        MoreExecutors.shutdownAndAwaitTermination(executor, Duration.ofSeconds(5));
        closeable.close();
    }

    @SneakyThrows
    @Test
    public void retrieveFailAndRetry() {
        when(wal.retrieveCommitted(0, maxSize))
            .thenReturn(
                CompletableFuture.failedFuture(new IllegalArgumentException()),
                CompletableFuture.completedFuture(Iterators.forArray(LogEntry.newBuilder()
                    .setTerm(0)
                    .setIndex(0)
                    .build())));
        CountDownLatch latch = new CountDownLatch(1);
        when(subscriber.apply(any(LogEntry.class))).thenAnswer(
            (Answer<CompletableFuture<Void>>) invocationOnMock -> {
                latch.countDown();
                return CompletableFuture.completedFuture(null);
            });

        KVRangeWALSubscription walSub =
            new KVRangeWALSubscription(maxSize, wal, commitIndexSource, 0, subscriber, executor);
        commitIndexSource.onNext(0L);
        latch.await();
        verify(wal, times(2)).retrieveCommitted(0, maxSize);
    }

    @SneakyThrows
    @Test
    public void stopRetryWhenStop() {
        CountDownLatch latch = new CountDownLatch(2);

        when(wal.retrieveCommitted(0, maxSize))
            .thenAnswer((Answer<CompletableFuture<Iterator<LogEntry>>>) invocationOnMock -> {
                latch.countDown();
                return CompletableFuture.failedFuture(
                    new IllegalArgumentException());
            });

        KVRangeWALSubscription walSub =
            new KVRangeWALSubscription(maxSize, wal, commitIndexSource, 0, subscriber, executor);
        commitIndexSource.onNext(0L);
        latch.await();
        walSub.stop();
        verify(wal, atLeast(2)).retrieveCommitted(0, maxSize);
    }

    @SneakyThrows
    @Test
    public void stopRetryWhenSnapshot() {
        AtomicInteger retryCount = new AtomicInteger();

        when(wal.retrieveCommitted(0, maxSize))
            .thenAnswer((Answer<CompletableFuture<Iterator<LogEntry>>>) invocationOnMock -> {
                retryCount.incrementAndGet();
                return CompletableFuture.failedFuture(
                    new IllegalArgumentException("For Testing"));
            });
        CountDownLatch latch = new CountDownLatch(1);
        when(subscriber.apply(any(KVRangeSnapshot.class))).thenAnswer(
            (Answer<CompletableFuture<Void>>) invocationOnMock -> {
                latch.countDown();
                return CompletableFuture.completedFuture(null);
            });

        KVRangeWALSubscription walSub =
            new KVRangeWALSubscription(maxSize, wal, commitIndexSource, 0, subscriber, executor);
        commitIndexSource.onNext(0L);
        await().until(() -> retryCount.get() > 2);
        snapshotSource.onNext(new IKVRangeWAL.SnapshotInstallTask(KVRangeSnapshot.getDefaultInstance().toByteString()));
        latch.await();
        int c = retryCount.get();
        Thread.sleep(100);
        assertEquals(retryCount.get(), c);
        verify(wal, atLeast(2)).retrieveCommitted(0, maxSize);
    }

    @SneakyThrows
    @Test
    public void reapplyLog() {
        when(wal.retrieveCommitted(0, maxSize)).thenReturn(CompletableFuture.completedFuture(
            Iterators.forArray(
                LogEntry.newBuilder().setTerm(0).setIndex(0).build(),
                LogEntry.newBuilder().setTerm(0).setIndex(1).build()))
        );
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger applyCount = new AtomicInteger();
        when(subscriber.apply(any(LogEntry.class)))
            .thenAnswer((Answer<CompletableFuture<Void>>) invocationOnMock -> {
                log.info("invoke");
                if (applyCount.getAndIncrement() == 0) {
                    return CompletableFuture.failedFuture(new KVRangeException.TryLater("try again"));
                }
                latch.countDown();
                return CompletableFuture.completedFuture(null);
            });
        KVRangeWALSubscription walSub =
            new KVRangeWALSubscription(maxSize, wal, commitIndexSource, 0, subscriber, executor);
        commitIndexSource.onNext(0L);
        latch.await();
        log.info("{}", applyCount.get());
        assertTrue(1 < applyCount.get());
        ArgumentCaptor<LogEntry> logEntryCap = ArgumentCaptor.forClass(LogEntry.class);
        await().until(() -> {
            verify(subscriber, atLeast(2)).apply(logEntryCap.capture());
            return logEntryCap.getAllValues().get(0).getIndex() == 0 &&
                logEntryCap.getAllValues().get(logEntryCap.getAllValues().size() - 1).getIndex() == 1;
        });
    }

    @SneakyThrows
    @Test
    public void cancelApplyLogWhenSnapshot() {
        when(wal.retrieveCommitted(0, maxSize)).thenReturn(CompletableFuture.completedFuture(
            Iterators.forArray(
                LogEntry.newBuilder().setTerm(0).setIndex(0).build(),
                LogEntry.newBuilder().setTerm(0).setIndex(1).build()))
        );
        CountDownLatch latch = new CountDownLatch(1);
        CompletableFuture<Void> applyLogFuture = new CompletableFuture<>();
        when(subscriber.apply(any(LogEntry.class)))
            .thenAnswer((Answer<CompletableFuture<Void>>) invocationOnMock -> {
                latch.countDown();
                return applyLogFuture;
            });
        KVRangeWALSubscription walSub =
            new KVRangeWALSubscription(maxSize, wal, commitIndexSource, 0, subscriber, executor);
        commitIndexSource.onNext(0L);
        latch.await();
        snapshotSource.onNext(new IKVRangeWAL.SnapshotInstallTask(KVRangeSnapshot.getDefaultInstance().toByteString()));
        await().until(() -> applyLogFuture.isCancelled());
    }

    @SneakyThrows
    @Test
    public void cancelReapplyWhenSnapshot() {
        when(wal.retrieveCommitted(0, maxSize)).thenReturn(CompletableFuture.completedFuture(
            Iterators.forArray(
                LogEntry.newBuilder().setTerm(0).setIndex(0).build(),
                LogEntry.newBuilder().setTerm(0).setIndex(1).build()))
        );
        AtomicInteger retryCount = new AtomicInteger();
        when(subscriber.apply(any(LogEntry.class)))
            .thenAnswer((Answer<CompletableFuture<Void>>) invocationOnMock -> {
                retryCount.incrementAndGet();
                return CompletableFuture.failedFuture(new KVRangeException.TryLater("Try again"));
            });
        CountDownLatch latch = new CountDownLatch(1);
        when(subscriber.apply(any(KVRangeSnapshot.class)))
            .thenAnswer((Answer<CompletableFuture<Void>>) invocationOnMock -> {
                latch.countDown();
                return CompletableFuture.completedFuture(null);
            });
        KVRangeWALSubscription walSub =
            new KVRangeWALSubscription(maxSize, wal, commitIndexSource, 0, subscriber, executor);
        commitIndexSource.onNext(0L);
        await().until(() -> retryCount.get() > 2);
        snapshotSource.onNext(new IKVRangeWAL.SnapshotInstallTask(KVRangeSnapshot.getDefaultInstance().toByteString()));
        latch.await();
        int c = retryCount.get();
        Thread.sleep(100);
        assertEquals(retryCount.get(), c);
    }

    @SneakyThrows
    @Test
    public void cancelApplySnapshot() {
        CountDownLatch latch = new CountDownLatch(1);
        CompletableFuture<Void> applySnapshotFuture = new CompletableFuture<>();
        when(subscriber.apply(any(KVRangeSnapshot.class)))
            .thenAnswer((Answer<CompletableFuture<Void>>) invocationOnMock -> {
                latch.countDown();
                return applySnapshotFuture;
            });
        KVRangeWALSubscription walSub =
            new KVRangeWALSubscription(maxSize, wal, commitIndexSource, 0, subscriber, executor);
        snapshotSource.onNext(new IKVRangeWAL.SnapshotInstallTask(KVRangeSnapshot.getDefaultInstance().toByteString()));
        latch.await();
        snapshotSource.onNext(new IKVRangeWAL.SnapshotInstallTask(KVRangeSnapshot.getDefaultInstance().toByteString()));
        await().until(() -> applySnapshotFuture.isCancelled());
    }

    @SneakyThrows
    @Test
    public void cancelApplySnapshotWhenStop() {
        CountDownLatch latch = new CountDownLatch(1);
        CompletableFuture<Void> applySnapshotFuture = new CompletableFuture<>();
        when(subscriber.apply(any(KVRangeSnapshot.class)))
            .thenAnswer((Answer<CompletableFuture<Void>>) invocationOnMock -> {
                latch.countDown();
                return applySnapshotFuture;
            });
        KVRangeWALSubscription walSub =
            new KVRangeWALSubscription(maxSize, wal, commitIndexSource, 0, subscriber, executor);
        snapshotSource.onNext(new IKVRangeWAL.SnapshotInstallTask(KVRangeSnapshot.getDefaultInstance().toByteString()));
        latch.await();
        walSub.stop();
        await().until(() -> applySnapshotFuture.isCancelled());
    }

    @SneakyThrows
    @Test
    public void applyLogsAndSnapshot() {
        LogEntry entry1 = LogEntry.newBuilder().setTerm(0).setIndex(0).build();
        LogEntry entry2 = LogEntry.newBuilder().setTerm(0).setIndex(1).build();
        when(wal.retrieveCommitted(0, maxSize))
            .thenReturn(CompletableFuture.completedFuture(Iterators.forArray(entry1, entry2)));
        CountDownLatch latch = new CountDownLatch(1);
        CompletableFuture<Void> applyLogFuture1 = new CompletableFuture<>();
        when(subscriber.apply(entry1))
            .thenAnswer((Answer<CompletableFuture<Void>>) invocationOnMock -> {
                latch.countDown();
                return applyLogFuture1;
            });
        KVRangeWALSubscription walSub =
            new KVRangeWALSubscription(maxSize, wal, commitIndexSource, 0, subscriber, executor);
        commitIndexSource.onNext(0L);
        latch.await();
        snapshotSource.onNext(new IKVRangeWAL.SnapshotInstallTask(KVRangeSnapshot.getDefaultInstance().toByteString()));
        await().until(() -> applyLogFuture1.isCancelled());
        verify(subscriber, times(1)).apply(any(LogEntry.class));
        verify(subscriber, times(1)).apply(KVRangeSnapshot.getDefaultInstance());
    }
}
