package com.zachary.bifromq.metrics;

import io.micrometer.core.instrument.Metrics;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;


@Slf4j
public class TenantMeterTest {

    @Test
    public void testGet() throws InterruptedException {
        String tenantId = "testing_traffic";
        TenantMeter meter = TenantMeter.get(tenantId);
        meter.recordCount(TenantMetric.MqttConnectCount);
        Assert.assertTrue(Metrics.globalRegistry.getMeters().stream()
                .anyMatch(m -> tenantId.equals(m.getId().getTag(TenantMeter.TAG_TENANT_ID))));

        meter = null;
        System.gc();

        TenantMeter.cleanUp();
        System.gc();

        Thread.sleep(100);
        // 异步校验工具，Awaitility#await() 用于配置等待的条件，until() 直到指定的条件为true，或等待时间结束为止
        // 等待直到 util(()->{}) 中的 执行结果 返回true.
        Awaitility.await().until(() -> {
            Thread.sleep(1000);
            return Metrics.globalRegistry.getMeters().stream()
                    .noneMatch(m -> tenantId.equals(m.getId().getTag(TenantMeter.TAG_TENANT_ID)));
        });
    }
}