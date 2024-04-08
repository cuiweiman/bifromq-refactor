package com.zachary.bifromq.baseenv;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;

/**
 * 因为 {@link EnvProvider} 的访问级别是 同一个包下
 *
 * @description: {@link IEnvProvider} 测试实现类
 * @author: cuiweiman
 * @date: 2024/4/3 11:06
 */
@Slf4j
public class TestEnvProvider implements IEnvProvider {

    @Override
    public int availableProcessors() {
        return 1;
    }


    @Override
    public ThreadFactory newThreadFactory(String name, boolean daemon, int priority) {
        return r -> {
            Thread t = new Thread(r);
            t.setName(name);
            t.setDaemon(daemon);
            t.setPriority(priority);
            return t;
        };
    }
}
