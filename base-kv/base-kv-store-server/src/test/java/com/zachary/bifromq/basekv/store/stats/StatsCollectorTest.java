package com.zachary.bifromq.basekv.store.stats;

import com.google.common.util.concurrent.AtomicDouble;
import com.google.common.util.concurrent.MoreExecutors;
import io.reactivex.rxjava3.observers.TestObserver;
import lombok.SneakyThrows;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doAnswer;
import static org.testng.Assert.assertEquals;

public class StatsCollectorTest {
    @Test
    public void initAndTick() {
        StatsCollector collector = Mockito.mock(StatsCollector.class, Mockito.withSettings()
            .useConstructor(Duration.ofSeconds(1), MoreExecutors.directExecutor())
            .defaultAnswer(Mockito.CALLS_REAL_METHODS));

        doAnswer(invocation -> {
            Map<String, Double> map = invocation.getArgument(0);
            map.put("stat1", 0.0);
            return null;
        }).when(collector).scrap(anyMap());
        TestObserver<Map<String, Double>> statsObserver = TestObserver.create();
        collector.collect().subscribe(statsObserver);
        collector.tick();
        statsObserver.awaitCount(1);
        assertEquals(0.0, 0.0d, statsObserver.values().get(0).get("stat1").doubleValue());
    }

    @SneakyThrows
    @Test
    public void tickInterval() {
        StatsCollector collector = Mockito.mock(StatsCollector.class, Mockito.withSettings()
            .useConstructor(Duration.ofMillis(500), MoreExecutors.directExecutor())
            .defaultAnswer(Mockito.CALLS_REAL_METHODS));
        AtomicDouble statValue = new AtomicDouble();
        doAnswer(invocation -> {
            Map<String, Double> map = invocation.getArgument(0);
            map.put("stat1", statValue.getAndAdd(1.0));
            return null;
        }).when(collector).scrap(anyMap());
        TestObserver<Map<String, Double>> statsObserver = TestObserver.create();
        collector.collect().subscribe(statsObserver);
        collector.tick();
        collector.tick();
        Thread.sleep(550);
        collector.tick();
        collector.stop().toCompletableFuture().join();
        statsObserver.await();
        assertEquals(statsObserver.values().size(), 2);
    }

    @SneakyThrows
    @Test
    public void distinctUntilChange() {
        StatsCollector collector = Mockito.mock(StatsCollector.class, Mockito.withSettings()
            .useConstructor(Duration.ofMillis(10), MoreExecutors.directExecutor())
            .defaultAnswer(Mockito.CALLS_REAL_METHODS));
        doAnswer(invocation -> {
            Map<String, Double> map = invocation.getArgument(0);
            map.put("stat1", 0.0);
            return null;
        }).when(collector).scrap(anyMap());
        TestObserver<Map<String, Double>> statsObserver = TestObserver.create();
        collector.collect().subscribe(statsObserver);
        collector.tick();
        Thread.sleep(10);
        collector.tick();
        Thread.sleep(10);
        collector.tick();
        collector.stop().toCompletableFuture().join();
        statsObserver.await();
        assertEquals(statsObserver.values().size(), 1);
    }
}
