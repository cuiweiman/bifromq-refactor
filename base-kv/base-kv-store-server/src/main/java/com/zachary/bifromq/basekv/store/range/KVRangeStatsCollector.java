package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.store.api.IKVRangeReader;
import com.zachary.bifromq.basekv.store.api.IKVReader;
import com.zachary.bifromq.basekv.store.stats.StatsCollector;
import com.zachary.bifromq.basekv.store.wal.IKVRangeWAL;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Executor;

final class KVRangeStatsCollector extends StatsCollector {
    private final IKVRangeReader reader;

    private final IKVRangeWAL wal;

    public KVRangeStatsCollector(IKVRangeState rangeState,
                                 IKVRangeWAL wal,
                                 Duration interval,
                                 Executor executor) {
        super(interval, executor);
        this.reader = rangeState.getReader();
        this.wal = wal;
        tick();
    }

    protected void scrap(Map<String, Double> stats) {
        reader.refresh();
        IKVReader kvReader = reader.kvReader();
        stats.put("dataSize", (double) kvReader.size(kvReader.range()));
        stats.put("walSize", (double) wal.logDataSize());
    }
}
