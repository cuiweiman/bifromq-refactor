package com.zachary.bifromq.baseenv;

import java.util.concurrent.ThreadFactory;

/**
 * This is a service provider interface(SPI) for customizing the thread running environment.
 * 这是一个用于定制线程运行环境的服务提供者接口（SPI）
 *
 * @description: 环境配置, 处理器数量 与 ThreadFactory 提供者
 * @author: cuiweiman
 * @date: 2024/4/3 10:52Ï
 */
public interface IEnvProvider {

    /**
     * Get current available processors number
     * 获取当前可用的 处理器(CPU) 数量
     *
     * @return the available processors number
     */
    int availableProcessors();

    /**
     * Create a thread factory with given parameters
     * 根据参数 创建 ThreadFactory
     *
     * @param name     线程名称 the name of the created thread
     * @param daemon   是否守护线程 if created thread is daemon thread
     * @param priority 线程优先级 the priority of created thread
     * @return the created thread {@link ThreadFactory}
     */
    ThreadFactory newThreadFactory(String name, boolean daemon, int priority);

    /**
     * Create a thread factory which creating thread with normal priority
     * 使用默认优先级, 创建 ThreadFactory
     * 接口默认实现类
     *
     * @param name   线程名称 the name of the created thread
     * @param daemon 是否守护线程 if created thread is daemon thread
     * @return the created thread  {@link ThreadFactory}
     */
    default ThreadFactory newThreadFactory(String name, boolean daemon) {
        return newThreadFactory(name, daemon, Thread.NORM_PRIORITY);
    }

    /**
     * Create a thread factory which creating non-daemon thread with normal priority
     * 使用默认优先级, 创建 非守护线程 的 ThreadFactory
     *
     * @param name 线程名称 the name of the created thread
     * @return the created thread {@link ThreadFactory}
     */
    default ThreadFactory newThreadFactory(String name) {
        return newThreadFactory(name, false);
    }


}
