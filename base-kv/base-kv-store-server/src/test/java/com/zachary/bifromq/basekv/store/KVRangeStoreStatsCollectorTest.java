package com.zachary.bifromq.basekv.store;

import com.zachary.bifromq.basekv.store.option.KVRangeStoreOptions;
import com.google.common.util.concurrent.MoreExecutors;
import io.reactivex.rxjava3.observers.TestObserver;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Map;

import static org.testng.Assert.assertTrue;

public class KVRangeStoreStatsCollectorTest {

    @Test
    public void testScrap() {
        KVRangeStoreOptions options = new KVRangeStoreOptions();
        KVRangeStoreStatsCollector collector = new KVRangeStoreStatsCollector(options, Duration.ofSeconds(1),
            MoreExecutors.directExecutor());
        TestObserver<Map<String, Double>> statsObserver = TestObserver.create();
        collector.collect().subscribe(statsObserver);
        statsObserver.awaitCount(1);
        Map<String, Double> stats = statsObserver.values().get(0);
        assertTrue(stats.containsKey("db.usable"));
        assertTrue(stats.containsKey("db.total"));
        assertTrue(stats.containsKey("wal.usable"));
        assertTrue(stats.containsKey("wal.total"));
    }
}
