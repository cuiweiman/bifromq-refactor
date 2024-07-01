package com.zachary.bifromq.basekv.localengine.benchmark;

import com.zachary.bifromq.baseenv.EnvProvider;
import com.zachary.bifromq.basekv.localengine.IKVEngine;
import com.zachary.bifromq.basekv.localengine.KVEngineFactory;
import com.zachary.bifromq.basekv.localengine.RocksDBKVEngineConfigurator;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.rocksdb.BlockBasedTableConfig;
import org.rocksdb.BloomFilter;
import org.rocksdb.ColumnFamilyOptions;
import org.rocksdb.ColumnFamilyOptionsInterface;
import org.rocksdb.CompactionStyle;
import org.rocksdb.DBOptions;
import org.rocksdb.DBOptionsInterface;
import org.rocksdb.Env;
import org.rocksdb.IndexType;
import org.rocksdb.LRUCache;
import org.rocksdb.MutableColumnFamilyOptionsInterface;
import org.rocksdb.MutableDBOptionsInterface;
import org.rocksdb.Statistics;
import org.rocksdb.util.SizeUnit;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.basekv.localengine.IKVEngine.DEFAULT_NS;
import static java.lang.Math.max;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

@Slf4j
public abstract class BenchmarkTemplate {
    private Path dbRootDir;
    protected IKVEngine kvEngine;
    private ScheduledExecutorService bgTaskExecutor;

    @Setup
    public void setup() {
        try {
            dbRootDir = Files.createTempDirectory("");
        } catch (IOException e) {
            log.error("Failed to create temp dir", e);
        }
        String DB_NAME = "testDB";
        String DB_CHECKPOINT_DIR = "testDB_cp";
        String uuid = UUID.randomUUID().toString();
        bgTaskExecutor =
            newSingleThreadScheduledExecutor(EnvProvider.INSTANCE.newThreadFactory("Checkpoint GC"));
        RocksDBKVEngineConfigurator configurator = new RocksDBKVEngineConfigurator(new RocksDBKVEngineConfigurator
            .DBOptionsConfigurator() {
            @Override
            public void config(DBOptionsInterface<DBOptions> targetOption) {
                targetOption.setEnv(Env.getDefault())
                    .setCreateIfMissing(true)
                    .setCreateMissingColumnFamilies(true)
                    .setManualWalFlush(true)
                    .setRecycleLogFileNum(10)
                    .setAvoidUnnecessaryBlockingIO(true)
                    .setStatistics(gcable(new Statistics()));
            }

            @Override
            public void config(MutableDBOptionsInterface<DBOptions> targetOption) {
                targetOption
                    .setMaxOpenFiles(20)
                    .setStatsDumpPeriodSec(5)
                    .setMaxBackgroundJobs(max(EnvProvider.INSTANCE.availableProcessors(), 2));
            }
        }, new RocksDBKVEngineConfigurator.CFOptionsConfigurator() {
            @Override
            public void config(String name, ColumnFamilyOptionsInterface<ColumnFamilyOptions> targetOption) {
                targetOption.setMergeOperatorName("uint64add")
                    .setTableFormatConfig(
                        new BlockBasedTableConfig() //
                            // Begin to use partitioned index filters
                            // https://github.com/facebook/rocksdb/wiki/Partitioned-Index-Filters#how-to-use-it
                            .setIndexType(IndexType.kTwoLevelIndexSearch) //
                            .setFilterPolicy(
                                gcable(new BloomFilter(16, false)))
                            .setPartitionFilters(true) //
                            .setMetadataBlockSize(8 * SizeUnit.KB) //
                            .setCacheIndexAndFilterBlocks(true) //
                            .setPinTopLevelIndexAndFilter(true)
                            .setCacheIndexAndFilterBlocksWithHighPriority(true) //
                            .setPinL0FilterAndIndexBlocksInCache(true) //
                            // End of partitioned index filters settings.
                            .setBlockSize(4 * SizeUnit.KB)//
                            .setBlockCache(
                                gcable(new LRUCache(512 * SizeUnit.MB, 8))))
                    .optimizeLevelStyleCompaction()
                    .setCompactionStyle(CompactionStyle.LEVEL) //
                    // Flushing options:
                    // min_write_buffer_number_to_merge is the minimum number of mem_tables to be
                    // merged before flushing to storage. For example, if this option is set to 2,
                    // immutable mem_tables are only flushed when there are two of them - a single
                    // immutable mem_table will never be flushed.  If multiple mem_tables are merged
                    // together, less data may be written to storage since two updates are merged to
                    // a single key. However, every Get() must traverse all immutable mem_tables
                    // linearly to check if the key is there. Setting this option too high may hurt
                    // read performance.
                    .setMinWriteBufferNumberToMerge(2)
                    // https://github.com/facebook/rocksdb/pull/5744
                    .setForceConsistencyChecks(true);

            }

            @Override
            public void config(String name, MutableColumnFamilyOptionsInterface<ColumnFamilyOptions> targetOption) {

            }
        }).setDbCheckpointRootDir(Paths.get(dbRootDir.toString(), uuid, DB_CHECKPOINT_DIR).toString())
            .setDbRootDir(Paths.get(dbRootDir.toString(), uuid, DB_NAME).toString());
        kvEngine = KVEngineFactory.create(null, List.of(DEFAULT_NS), cpId -> true, configurator);
        kvEngine.start(bgTaskExecutor);
        doSetup();
    }

    @TearDown
    public void tearDown() {
        kvEngine.stop();
        MoreExecutors.shutdownAndAwaitTermination(bgTaskExecutor, 5, TimeUnit.SECONDS);
        try {
            Files.walk(dbRootDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
            Files.delete(dbRootDir);
        } catch (IOException e) {
            log.error("Failed to delete db root dir", e);
        }
    }

    protected void doSetup() {
    }


    @Test
    @Ignore
    public void test() {
        Options opt = new OptionsBuilder()
            .include(this.getClass().getSimpleName())
            .warmupIterations(5)
            .measurementIterations(10)
            .forks(1)
            .build();
        try {
            new Runner(opt).run();
        } catch (RunnerException e) {
            throw new RuntimeException(e);
        }
    }
}
