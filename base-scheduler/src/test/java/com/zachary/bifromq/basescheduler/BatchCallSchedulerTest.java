package com.zachary.bifromq.basescheduler;

import com.zachary.bifromq.basescheduler.exception.DropException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class BatchCallSchedulerTest {
    private ExecutorService executor;

    @BeforeMethod
    public void setup() {
        executor = Executors.newFixedThreadPool(2);
    }

    @AfterMethod
    public void teardown() {
        executor.shutdown();
    }

    @SneakyThrows
    @Test
    public void batchCall() {
        TestBatchCallScheduler scheduler = new TestBatchCallScheduler(4, Duration.ofMillis(10));
        AtomicInteger count = new AtomicInteger(1000);
        CountDownLatch latch = new CountDownLatch(count.get());
        executor.submit(() -> {
            int i;
            while ((i = count.decrementAndGet()) >= 0) {
                scheduler.schedule(i).whenComplete((v, e) -> latch.countDown());
            }
        });
        latch.await();
        scheduler.close();
    }

    @Test
    public void backPressure() {
        TestBatchCallScheduler scheduler = new TestBatchCallScheduler(1, Duration.ofMillis(1));
        AtomicBoolean stop = new AtomicBoolean();
        List<CompletableFuture<Integer>> respFutures = new ArrayList<>();
        int i = 0;
        while (!stop.get()) {
            int j = i++;
            CompletableFuture<Integer> respFuture = scheduler.schedule(j);
            respFutures.add(respFuture);
            respFuture.whenComplete((v, e) -> {
                if (e != null) {
                    stop.set(true);
                }
            });
        }
        try {
            CompletableFuture.allOf(respFutures.toArray(CompletableFuture[]::new)).join();
        } catch (Throwable e) {
            Assert.assertEquals(DropException.EXCEED_LIMIT, e.getCause());
        }
    }
}
