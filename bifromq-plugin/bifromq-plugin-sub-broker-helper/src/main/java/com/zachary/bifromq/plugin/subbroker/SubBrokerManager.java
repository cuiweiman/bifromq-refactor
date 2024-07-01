package com.zachary.bifromq.plugin.subbroker;

import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class SubBrokerManager implements ISubBrokerManager {
    private final AtomicBoolean stopped = new AtomicBoolean();
    private final Map<Integer, ISubBroker> receivers = new HashMap<>();

    public SubBrokerManager(PluginManager pluginMgr, ISubBroker... builtInReceivers) {
        for (ISubBroker receiver : builtInReceivers) {
            log.info("Register built-in receiver[{}] with id[{}]", receiver.getClass().getSimpleName(), receiver.id());
            receivers.put(receiver.id(), new MonitoredSubBroker(receiver));
        }
        List<ISubBroker> customReceivers = pluginMgr.getExtensions(ISubBroker.class);
        for (ISubBroker customReceiver : customReceivers) {
            if (receivers.containsKey(customReceiver.id())) {
                log.warn("Id[{}] is reserved for receiver[{}], skip registering custom receiver[{}]",
                    customReceiver.id(),
                    receivers.get(customReceiver.id()).getClass().getName(),
                    customReceiver.getClass().getName());
            } else {
                log.info("Register custom receiver[{}] with id[{}]",
                    customReceiver.getClass().getSimpleName(), customReceiver.id());
                receivers.put(customReceiver.id(), new MonitoredSubBroker(customReceiver));
            }
        }
    }

    @Override
    public ISubBroker get(int subBrokerId) {
        return receivers.getOrDefault(subBrokerId, NoInboxSubBroker.INSTANCE);
    }

    @Override
    public void stop() {
        if (stopped.compareAndSet(false, true)) {
            log.info("Stopping SubBrokerManager");
            receivers.values().forEach(ISubBroker::close);
            log.info("SubBrokerManager stopped");
        }
    }
}
