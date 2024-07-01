package com.zachary.bifromq.basekv.localengine;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class InMemoryKVEngineConfigurator implements KVEngineConfigurator<InMemoryKVEngineConfigurator> {
    private long gcIntervalInSec = 300; // ms

    public static InMemoryKVEngineConfiguratorBuilder builder() {
        return new InMemoryKVEngineConfigurator().toBuilder();
    }

    public InMemoryKVEngineConfiguratorBuilder toBuilder() {
        return new InMemoryKVEngineConfiguratorBuilder().gcInterval(this.gcIntervalInSec);
    }

    public static class InMemoryKVEngineConfiguratorBuilder implements
        KVEngineConfiguratorBuilder<InMemoryKVEngineConfigurator> {
        private long gcInterval;

        InMemoryKVEngineConfiguratorBuilder() {
        }

        public InMemoryKVEngineConfiguratorBuilder gcInterval(long gcInterval) {
            this.gcInterval = gcInterval;
            return this;
        }

        public InMemoryKVEngineConfigurator build() {
            return new InMemoryKVEngineConfigurator(gcInterval);
        }

        public String toString() {
            return "InMemoryKVEngineConfigurator.InMemoryKVEngineConfiguratorBuilder(gcInterval="
                + this.gcInterval + ")";
        }
    }
}
