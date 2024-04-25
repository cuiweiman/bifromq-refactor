package com.zachary.bifromq.basecrdt.core.internal;


import com.zachary.bifromq.baseenv.EnvProvider;
import com.zachary.bifromq.baseenv.IEnvProvider;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/20 17:58
 */
class SharedInflationExecutor {
    private static ScheduledExecutorService instance;

    static synchronized ScheduledExecutorService getInstance() {
        if (instance == null) {
            IEnvProvider envProvider = EnvProvider.INSTANCE;
            instance = new ScheduledThreadPoolExecutor(Math.max(2, envProvider.availableProcessors() / 20),
                envProvider.newThreadFactory("shared-crdt-inflater", true));
        }
        return instance;
    }
}
