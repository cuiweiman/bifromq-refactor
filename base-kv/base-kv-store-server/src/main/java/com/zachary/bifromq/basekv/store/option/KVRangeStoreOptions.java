package com.zachary.bifromq.basekv.store.option;

import com.zachary.bifromq.basekv.localengine.KVEngineConfigurator;
import com.zachary.bifromq.basekv.localengine.RocksDBKVEngineConfigurator;
import com.zachary.bifromq.basekv.store.util.ProcessUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.nio.file.Paths;

@Accessors(chain = true)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class KVRangeStoreOptions {
    private String overrideIdentity;
    private KVRangeOptions kvRangeOptions = new KVRangeOptions();
    private int walFlushBufferSize = 1024;
    private int statsCollectIntervalSec = 5;

    private KVEngineConfigurator dataEngineConfigurator = new RocksDBKVEngineConfigurator()
        .setDisableWAL(true) // data engine no need extra wal
        .setDbRootDir(Paths.get(System.getProperty("java.io.tmpdir"), "com/zachary/bifromq/basekv",
            ProcessUtil.processId(), "data").toString())
        .setDbCheckpointRootDir(Paths.get(System.getProperty("java.io.tmpdir"), "basekvcp",
            ProcessUtil.processId(), "data").toString());

    private KVEngineConfigurator walEngineConfigurator = new RocksDBKVEngineConfigurator()
        .setDbRootDir(Paths.get(System.getProperty("java.io.tmpdir"), "com/zachary/bifromq/basekv",
            ProcessUtil.processId(), "wal").toString())
        .setDbCheckpointRootDir(Paths.get(System.getProperty("java.io.tmpdir"), "basekvcp",
            ProcessUtil.processId(), "wal").toString());
}
