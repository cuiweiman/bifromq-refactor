package com.zachary.bifromq.plugin.eventcollector;


import com.zachary.bifromq.basehlc.HLC;
import com.google.common.annotations.VisibleForTesting;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class EventCollectorManager implements IEventCollector {
    private static final EventPool ZERO_OUT_HOLDERS = new EventPool();
    private final AtomicBoolean stopped = new AtomicBoolean();
    private final Map<String, IEventCollector> eventCollectors = new HashMap<>();
    private final Map<String, Timer> eventCollectorTimers = new HashMap<>();
    private final Counter callInvokeCounter;

    public EventCollectorManager(PluginManager pluginMgr) {

        for (IEventCollector eventCollector : pluginMgr.getExtensions(IEventCollector.class)) {
            log.info("Event collector loaded: {}", eventCollector.getClass().getName());
            eventCollectors.put(eventCollector.getClass().getName(), eventCollector);
            eventCollectorTimers.put(eventCollector.getClass().getName(), Timer.builder("call.exec.timer")
                .tag("method", "EventCollector/report")
                .tag("type", eventCollector.getClass().getSimpleName())
                .register(Metrics.globalRegistry));
        }
        callInvokeCounter = Counter.builder("event.collector.report.invoke.count")
            .register(Metrics.globalRegistry);
    }

    @Override
    public void report(Event<?> event) {
        callInvokeCounter.increment();
        event.hlc(HLC.INST.get());
        for (Map.Entry<String, IEventCollector> entry : eventCollectors.entrySet()) {
            Timer.Sample sample = Timer.start();
            try {
                entry.getValue().report(event);
            } catch (Throwable e) {
                log.warn("Failed to report event to collector: {}", entry.getKey());
            } finally {
                sample.stop(eventCollectorTimers.get(entry.getKey()));
            }
        }
        // clear out the event
        event.clone(ZERO_OUT_HOLDERS.get(event.type()));
    }


    @Override
    public void close() {
        if (stopped.compareAndSet(false, true)) {
            log.debug("Closing event collector manager");
            eventCollectors.values().forEach(IEventCollector::close);
            eventCollectorTimers.values().forEach(Metrics.globalRegistry::remove);
            Metrics.globalRegistry.remove(callInvokeCounter);
            log.debug("Event collector manager closed");
        }
    }

    @VisibleForTesting
    IEventCollector get(String collectorName) {
        return eventCollectors.get(collectorName);
    }
}
