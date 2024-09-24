package com.zachary.bifromq.basekv.localengine;

import com.zachary.bifromq.baseenv.EnvProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.BlockBasedTableConfig;
import org.rocksdb.BloomFilter;
import org.rocksdb.ColumnFamilyOptions;
import org.rocksdb.ColumnFamilyOptionsInterface;
import org.rocksdb.CompactionStyle;
import org.rocksdb.CompressionType;
import org.rocksdb.DBOptions;
import org.rocksdb.DBOptionsInterface;
import org.rocksdb.DataBlockIndexType;
import org.rocksdb.Env;
import org.rocksdb.IndexType;
import org.rocksdb.LRUCache;
import org.rocksdb.MutableColumnFamilyOptionsInterface;
import org.rocksdb.MutableDBOptionsInterface;
import org.rocksdb.RateLimiter;
import org.rocksdb.Statistics;
import org.rocksdb.util.SizeUnit;

import java.lang.ref.Cleaner;

import static java.lang.Math.max;

@Slf4j
@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class RocksDBKVEngineConfigurator implements KVEngineConfigurator<RocksDBKVEngineConfigurator> {
    private static final Cleaner CLEANER = Cleaner.create();

    public static RocksDBKVEngineConfiguratorBuilder builder() {
        return new RocksDBKVEngineConfigurator().toBuilder();
    }

    public interface Configurator {
        default <T extends AutoCloseable> T gcable(T object) {
            CLEANER.register(object, new CloseableState(object));
            return object;
        }
    }

    public interface DBOptionsConfigurator extends Configurator {
        void config(DBOptionsInterface<DBOptions> targetOption);

        void config(MutableDBOptionsInterface<DBOptions> targetOption);
    }

    public interface CFOptionsConfigurator extends Configurator {
        void config(String name, ColumnFamilyOptionsInterface<ColumnFamilyOptions> targetOption);

        void config(String name, MutableColumnFamilyOptionsInterface<ColumnFamilyOptions> targetOption);
    }

    public class BaseDBOptionConfigurator implements DBOptionsConfigurator {
        @Override
        public void config(DBOptionsInterface<DBOptions> targetOption) {
            targetOption.setEnv(Env.getDefault())
                    .setAtomicFlush(true)
                    .setCreateIfMissing(true)
                    .setCreateMissingColumnFamilies(true)
                    .setRecycleLogFileNum(10)
                    .setAvoidUnnecessaryBlockingIO(true)
                    .setStatistics(gcable(new Statistics()))
                    .setRateLimiter(gcable(new RateLimiter(512 * SizeUnit.MB,
                            RateLimiter.DEFAULT_REFILL_PERIOD_MICROS,
                            RateLimiter.DEFAULT_FAIRNESS,
                            RateLimiter.DEFAULT_MODE, true)));
        }

        @Override
        public void config(MutableDBOptionsInterface<DBOptions> targetOption) {
            targetOption
                    .setMaxOpenFiles(256)
                    .setMaxBackgroundJobs(max(EnvProvider.INSTANCE.availableProcessors() / 4, 2));
        }
    }

    public class BaseCFOptionConfigurator implements CFOptionsConfigurator {
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
                                    // To speed up point-lookup
                                    // https://rocksdb.org/blog/2018/08/23/data-block-hash-index.html
                                    .setDataBlockIndexType(DataBlockIndexType.kDataBlockBinaryAndHash)
                                    .setDataBlockHashTableUtilRatio(0.75)
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
            targetOption
                    // Flushing options:
                    // write_buffer_size sets the size of a single mem_table. Once mem_table exceeds
                    // this size, it is marked immutable and a new one is created.
                    .setWriteBufferSize(128 * SizeUnit.MB)
                    // Level Style Compaction:
                    // level0_file_num_compaction_trigger -- Once level 0 reaches this number of
                    // files, L0->L1 compaction is triggered. We can therefore estimate level 0
                    // size in stable state as
                    // write_buffer_size * min_write_buffer_number_to_merge * level0_file_num_compaction_trigger.
                    .setLevel0FileNumCompactionTrigger(4)
                    // Level Style Compaction:
                    // max_bytes_for_level_base and max_bytes_for_level_multiplier
                    //  -- max_bytes_for_level_base is total size of level 1. As mentioned, we
                    // recommend that this be around the size of level 0. Each subsequent level
                    // is max_bytes_for_level_multiplier larger than previous one. The default
                    // is 10 and we do not recommend changing that.
                    .setMaxBytesForLevelBase(128 * SizeUnit.MB)
                    .setCompressionType(CompressionType.LZ4_COMPRESSION)

                    // Below methods are defined in AdvancedMutableColumnFamilyOptionsInterface

                    // Level Style Compaction:
                    // target_file_size_base and target_file_size_multiplier
                    //  -- Files in level 1 will have target_file_size_base bytes. Each next
                    // level's file size will be target_file_size_multiplier bigger than previous
                    // one. However, by default target_file_size_multiplier is 1, so files in all
                    // L1..LMax levels are equal. Increasing target_file_size_base will reduce total
                    // number of database files, which is generally a good thing. We recommend setting
                    // target_file_size_base to be max_bytes_for_level_base / 10, so that there are
                    // 10 files in level 1.
                    .setTargetFileSizeBase(64 * SizeUnit.MB)
                    // If prefix_extractor is set and memtable_prefix_bloom_size_ratio is not 0,
                    // create prefix bloom for memtable with the size of
                    // write_buffer_size * memtable_prefix_bloom_size_ratio.
                    // If it is larger than 0.25, it is santinized to 0.25.
                    .setMemtablePrefixBloomSizeRatio(0.125)
                    // Soft limit on number of level-0 files. We start slowing down writes at this
                    // point. A value 0 means that no writing slow down will be triggered by number
                    // of files in level-0.
                    .setLevel0SlowdownWritesTrigger(80)
                    // Maximum number of level-0 files.  We stop writes at this point.
                    .setLevel0StopWritesTrigger(100)
                    // Flushing options:
                    // max_write_buffer_number sets the maximum number of mem_tables, both active
                    // and immutable.  If the active mem_table fills up and the total number of
                    // mem_tables is larger than max_write_buffer_number we stall further writes.
                    // This may happen if the flush process is slower than the write rate.
                    .setMaxWriteBufferNumber(4)
                    .setMinWriteBufferNumberToMerge(3);
        }
    }

    private final DBOptionsConfigurator dbOptionsConfigurator;

    private final CFOptionsConfigurator cfOptionsConfigurator;

    private String dbRootDir;

    private String dbCheckpointRootDir;
    private boolean disableWAL = false;

    private int compactMinTombstoneKeys = 50000;

    private double compactTombstonePercent = 0.3;
    private long gcIntervalInSec = 300; // ms

    public RocksDBKVEngineConfigurator() {
        this.dbOptionsConfigurator = new BaseDBOptionConfigurator();
        this.cfOptionsConfigurator = new BaseCFOptionConfigurator();
    }

    public RocksDBKVEngineConfigurator(DBOptionsConfigurator dbOptionsConfigurator,
                                       CFOptionsConfigurator cfOptionsConfigurator) {
        this.dbOptionsConfigurator = dbOptionsConfigurator;
        this.cfOptionsConfigurator = cfOptionsConfigurator;
    }

    DBOptions config() {
        DBOptions targetOption = new DBOptions();
        dbOptionsConfigurator.config((DBOptionsInterface) targetOption);
        dbOptionsConfigurator.config((MutableDBOptionsInterface) targetOption);
        return targetOption;
    }

    ColumnFamilyOptions config(String name) {
        ColumnFamilyOptions targetOption = new ColumnFamilyOptions();
        cfOptionsConfigurator.config(name, (ColumnFamilyOptionsInterface) targetOption);
        cfOptionsConfigurator.config(name, (MutableColumnFamilyOptionsInterface) targetOption);
        return targetOption;
    }

    public RocksDBKVEngineConfiguratorBuilder toBuilder() {
        return new RocksDBKVEngineConfiguratorBuilder()
                .dbOptionsConfigurator(this.dbOptionsConfigurator)
                .columnFamilyOptionsConfigurator(this.cfOptionsConfigurator)
                .dbRootDir(this.dbRootDir)
                .dbCheckpointRootDir(this.dbCheckpointRootDir)
                .disableWAL(this.disableWAL)
                .gcInterval(this.gcIntervalInSec);
    }

    @ToString
    public static class RocksDBKVEngineConfiguratorBuilder
            implements KVEngineConfiguratorBuilder<RocksDBKVEngineConfigurator> {
        private DBOptionsConfigurator dbOptionsConfigurator;
        private CFOptionsConfigurator cfOptionsConfigurator;
        private String dbRootDir;
        private String dbCheckpointRootDir;
        private boolean disableWAL;
        private int compactMinTombstoneKeys;
        private double compactTombstonePercent;
        private long gcInterval;

        RocksDBKVEngineConfiguratorBuilder() {
        }

        public RocksDBKVEngineConfiguratorBuilder dbOptionsConfigurator(DBOptionsConfigurator dbOptionsConfigurator) {
            this.dbOptionsConfigurator = dbOptionsConfigurator;
            return this;
        }

        public RocksDBKVEngineConfiguratorBuilder
        columnFamilyOptionsConfigurator(CFOptionsConfigurator cfOptionsConfigurator) {
            this.cfOptionsConfigurator = cfOptionsConfigurator;
            return this;
        }

        public RocksDBKVEngineConfiguratorBuilder dbRootDir(String dbRootDir) {
            this.dbRootDir = dbRootDir;
            return this;
        }

        public RocksDBKVEngineConfiguratorBuilder dbCheckpointRootDir(String dbCheckpointRootDir) {
            this.dbCheckpointRootDir = dbCheckpointRootDir;
            return this;
        }

        public RocksDBKVEngineConfiguratorBuilder disableWAL(boolean disableWAL) {
            this.disableWAL = disableWAL;
            return this;
        }

        public RocksDBKVEngineConfiguratorBuilder compactMinTombstoneKeys(int compactMinTombstoneKeys) {
            this.compactMinTombstoneKeys = compactMinTombstoneKeys;
            return this;
        }

        public RocksDBKVEngineConfiguratorBuilder compactTombstonePercent(double compactTombstonePercent) {
            this.compactTombstonePercent = compactTombstonePercent;
            return this;
        }

        public RocksDBKVEngineConfiguratorBuilder gcInterval(long gcInterval) {
            this.gcInterval = gcInterval;
            return this;
        }

        public RocksDBKVEngineConfigurator build() {
            return new RocksDBKVEngineConfigurator(dbOptionsConfigurator, cfOptionsConfigurator, dbRootDir,
                    dbCheckpointRootDir, disableWAL, compactMinTombstoneKeys, compactTombstonePercent, gcInterval);
        }
    }

    private static class CloseableState implements Runnable {
        private final AutoCloseable state;

        private CloseableState(AutoCloseable state) {
            this.state = state;
        }

        @Override
        public void run() {
            try {
                this.state.close();
            } catch (Exception e) {
                log.error("Failed to close object", e);
            }
        }
    }
}