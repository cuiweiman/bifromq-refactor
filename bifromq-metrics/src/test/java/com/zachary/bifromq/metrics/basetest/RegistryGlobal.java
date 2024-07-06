package com.zachary.bifromq.metrics.basetest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

/**
 * @description: Global Registry
 * @author: cuiweiman
 * @date: 2024/7/5 18:01
 */
public class RegistryGlobal {

    public static final CompositeMeterRegistry globalRegistry = new CompositeMeterRegistry();

    /**
     * 当使用Metrics.counter(…)之类的方法构建 meters 之后，就可以向 globalRegistry 中 注册 registry 了
     * 这些 meters 会被添加到每个注册好的 registry 中
     *
     * @param registry Registry to add.
     */
    public static void addRegistry(MeterRegistry registry) {
        globalRegistry.add(registry);
    }

    /**
     * 从 全局注册器 中移除 度量注册器. 溢出 度量注册器 不影响 之前添加的 度量
     * Remove a registry from the global composite registry. Removing a registry does not remove any meters
     * that were added to it by previous participation in the global composite.
     *
     * @param registry Registry to remove.
     */
    public static void removeRegistry(MeterRegistry registry) {
        globalRegistry.remove(registry);
    }

    /**
     * Tracks a monotonically increasing value.
     * 快速 向 全局注册器 中 注册一个 计数器
     *
     * @param name The base metric name
     * @param tags Sequence of dimensions for breaking down the name.
     * @return A new or existing counter.
     */
    public static Counter counter(String name, Iterable<Tag> tags) {
        return globalRegistry.counter(name, tags);
    }


}
