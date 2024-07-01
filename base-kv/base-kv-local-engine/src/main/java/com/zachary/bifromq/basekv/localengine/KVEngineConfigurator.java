package com.zachary.bifromq.basekv.localengine;

public interface KVEngineConfigurator<T extends KVEngineConfigurator<?>> {
    interface KVEngineConfiguratorBuilder<T extends KVEngineConfigurator<?>> {
        T build();
    }

    KVEngineConfiguratorBuilder<T> toBuilder();
}
