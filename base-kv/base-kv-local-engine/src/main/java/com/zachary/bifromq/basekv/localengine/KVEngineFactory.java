package com.zachary.bifromq.basekv.localengine;

import java.util.List;
import java.util.function.Predicate;

public class KVEngineFactory {
    public static IKVEngine create(String overrideIdentity,
                                   List<String> namespaces,
                                   Predicate<String> checkpointInUse,
                                   KVEngineConfigurator configurator) {
        if (configurator instanceof InMemoryKVEngineConfigurator) {
            return new InMemoryKVEngine(overrideIdentity, namespaces, checkpointInUse,
                (InMemoryKVEngineConfigurator) configurator);
        }
        if (configurator instanceof RocksDBKVEngineConfigurator) {
            return new RocksDBKVEngine(overrideIdentity, namespaces, checkpointInUse,
                (RocksDBKVEngineConfigurator) configurator);
        }
        throw new UnsupportedOperationException();
    }
}
