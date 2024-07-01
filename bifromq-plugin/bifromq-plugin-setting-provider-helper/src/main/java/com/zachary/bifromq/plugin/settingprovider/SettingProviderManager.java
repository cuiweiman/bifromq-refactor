package com.zachary.bifromq.plugin.settingprovider;

import com.google.common.base.Preconditions;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginManager;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
public class SettingProviderManager implements ISettingProvider {
    private final AtomicBoolean stopped = new AtomicBoolean();
    private final ISettingProvider provider;
    private final Timer provideCallTimer;
    private final Counter provideCallErrorCounter;

    public SettingProviderManager(String settingProviderFQN, PluginManager pluginMgr) {
        Map<String, ISettingProvider> availSettingProviders = pluginMgr.getExtensions(ISettingProvider.class).stream()
            .collect(Collectors.toMap(e -> e.getClass().getName(), e -> e));
        if (availSettingProviders.isEmpty()) {
            log.warn("No setting provider plugin available, use DEV ONLY one instead");
            provider = new DevOnlySettingProvider();
        } else {
            if (settingProviderFQN == null) {
                log.warn("Auth provider plugin type are not specified, use DEV ONLY one instead");
                provider = new DevOnlySettingProvider();
            } else {
                Preconditions.checkArgument(availSettingProviders.containsKey(settingProviderFQN),
                    String.format("Setting Provider Plugin '%s' not found", settingProviderFQN));
                log.info("Setting provider plugin type: {}", settingProviderFQN);
                provider = availSettingProviders.get(settingProviderFQN);
            }
        }
        provideCallTimer = Timer.builder("call.exec.timer")
            .tag("method", "SettingProvider/provide")
            .tag("type", provider.getClass().getName())
            .register(Metrics.globalRegistry);
        provideCallErrorCounter = Counter.builder("call.exec.fail.count")
            .tag("method", "SettingProvider/provide")
            .tag("type", provider.getClass().getName())
            .register(Metrics.globalRegistry);
    }

    public <R> R provide(Setting setting, String tenantId) {
        assert !stopped.get();
        R current = setting.current(tenantId);
        try {
            Timer.Sample sample = Timer.start();
            R newVal = provider.provide(setting, tenantId);
            sample.stop(provideCallTimer);
            if (setting.isValid(newVal)) {
                // update the value
                setting.current(tenantId, newVal);
            } else {
                log.warn("Invalid setting value: setting={}, value={}", setting.name(), newVal);
            }
        } catch (Throwable e) {
            log.error("Setting provider throws exception: setting={}", setting.name(), e);
            // keep current value in case provider throws
            setting.current(tenantId, current);
            provideCallErrorCounter.increment();
        }
        return current;
    }

    public ISettingProvider get() {
        return provider;
    }

    @Override
    public void close() {
        if (stopped.compareAndSet(false, true)) {
            log.info("Closing setting provider manager");
            provider.close();
            Metrics.globalRegistry.remove(provideCallTimer);
            Metrics.globalRegistry.remove(provideCallErrorCounter);
            log.info("Setting provider manager closed");
        }
    }
}
