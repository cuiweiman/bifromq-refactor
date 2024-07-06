package com.zachary.bifromq.metrics.basetest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Micrometer 是一个监控数据收集的库，它支持将数据收集并发布到不同的监控系统，
 * 比如 Prometheus、Graphite、InfluxDB 等。
 *
 * @description:
 * @author: cuiweiman
 * @date: 2024/7/5 15:22
 */
@Slf4j
public class BaseMeterTest {

    @Test
    public void testCounter() {
        CompositeMeterRegistry composite = new CompositeMeterRegistry();

        Counter compositeCounter = composite.counter("counter");
        // 此处increment语句处于等待状态，直到 CompositeMeterRegistry 注册了一个registry。
        // 即时执行了 Counter.increment() 方法, counter 计数器也为 0
        compositeCounter.increment();

        SimpleMeterRegistry simple = new SimpleMeterRegistry();
        // counter计数器注册到simple registry
        composite.add(simple);

        // simple registry counter 与CompositeMeterRegistry中的其他registries的counter一起递增
        compositeCounter.increment();
        compositeCounter.increment();

        Assert.assertEquals(compositeCounter.count(), 2.0d);

    }


}
