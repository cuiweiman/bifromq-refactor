package com.zachary.bifromq.basekv.store;

import com.zachary.bifromq.basekv.localengine.RocksDBKVEngineConfigurator;
import com.zachary.bifromq.basekv.store.option.KVRangeStoreOptions;
import com.zachary.bifromq.basekv.store.stats.StatsCollector;

import java.io.File;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Executor;

class KVRangeStoreStatsCollector extends StatsCollector {
    private final KVRangeStoreOptions opt;

    KVRangeStoreStatsCollector(KVRangeStoreOptions opt, Duration interval, Executor executor) {
        super(interval, executor);
        this.opt = opt;
        tick();
    }

    @Override
    protected void scrap(Map<String, Double> stats) {
        if (opt.getDataEngineConfigurator() instanceof RocksDBKVEngineConfigurator) {
            RocksDBKVEngineConfigurator conf = (RocksDBKVEngineConfigurator) opt.getWalEngineConfigurator();
            File dbRootDir = new File(conf.getDbRootDir());
            stats.put("db.usable", (double) dbRootDir.getUsableSpace());
            stats.put("db.total", (double) dbRootDir.getTotalSpace());
        }
        if (opt.getWalEngineConfigurator() instanceof RocksDBKVEngineConfigurator) {
            RocksDBKVEngineConfigurator conf = (RocksDBKVEngineConfigurator) opt.getWalEngineConfigurator();
            File dbRootDir = new File(conf.getDbRootDir());
            stats.put("wal.usable", (double) dbRootDir.getUsableSpace());
            stats.put("wal.total", (double) dbRootDir.getTotalSpace());
        }
    }
}
