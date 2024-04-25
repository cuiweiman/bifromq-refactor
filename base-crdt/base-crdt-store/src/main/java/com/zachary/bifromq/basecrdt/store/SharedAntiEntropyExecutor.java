
package com.zachary.bifromq.basecrdt.store;


import com.zachary.bifromq.baseenv.EnvProvider;
import com.zachary.bifromq.baseenv.IEnvProvider;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

class SharedAntiEntropyExecutor {
    private static ScheduledExecutorService instance;

    static synchronized ScheduledExecutorService getInstance() {
        if (instance == null) {
            IEnvProvider envProvider = EnvProvider.INSTANCE;
            instance = new ScheduledThreadPoolExecutor(
                    Math.max(2, envProvider.availableProcessors() / 20),
                    envProvider.newThreadFactory("shared-crdt-store", true));
        }
        return instance;
    }
}
