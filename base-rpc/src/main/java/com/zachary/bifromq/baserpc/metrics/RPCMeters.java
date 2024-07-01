package com.zachary.bifromq.baserpc.metrics;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.benmanes.caffeine.cache.Scheduler;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description: gRPC meters
 * @author: cuiweiman
 * @date: 2024/4/28 17:53
 */
public class RPCMeters {

    @Builder
    @EqualsAndHashCode
    public static class MeterKey {
        final String service;
        final String method;
        final String tenantId;
    }

    private static final LoadingCache<MeterKey, LoadingCache<RPCMetric, Meter>> TRAFFIC_METERS = Caffeine.newBuilder().scheduler(Scheduler.systemScheduler())
            .expireAfterAccess(30, TimeUnit.SECONDS)
            .removalListener(new RemovalListener<MeterKey, LoadingCache<RPCMetric, Meter>>() {
                /**
                 * 元素移除监听
                 *
                 * @param key the key represented by this entry, or {@code null} if collected
                 * @param value the value represented by this entry, or {@code null} if collected
                 * @param cause the reason for which the entry was removed
                 */
                @Override
                public void onRemoval(@Nullable MeterKey key, @Nullable LoadingCache<RPCMetric, Meter> value, RemovalCause cause) {
                    if (Objects.nonNull(value)) {
                        value.invalidateAll();
                    }
                }
            })
            .build(new CacheLoader<MeterKey, LoadingCache<RPCMetric, Meter>>() {
                // value 自动生成器，查询缓存的 key 不存在时，将触发此方法 生成获取 value
                @Override
                public @Nullable LoadingCache<RPCMetric, Meter> load(MeterKey key) throws Exception {
                    return Caffeine.newBuilder().expireAfterAccess(30, TimeUnit.SECONDS)
                            .removalListener(new RemovalListener<RPCMetric, Meter>() {
                                // 元素移除 监听器
                                @Override
                                public void onRemoval(@Nullable RPCMetric key, @Nullable Meter value, RemovalCause cause) {
                                    if (Objects.nonNull(value)) {
                                        Metrics.globalRegistry.remove(value);
                                    }
                                }
                            })
                            .build(new CacheLoader<RPCMetric, Meter>() {
                                // value 自动生成器，查询缓存的 key 不存在时，将触发此方法 生成获取 value
                                @Override
                                public @Nullable Meter load(RPCMetric metric) throws Exception {
                                    Tags tags = Tags.of(MetricTag.SERVICE, key.service)
                                            .and(MetricTag.METHOD, key.method)
                                            .and(MetricTag.TENANT_ID, key.tenantId);
                                    switch (metric.meterType) {
                                        case TIMER -> {
                                            return Timer.builder(metric.metricName).tags(tags).register(Metrics.globalRegistry);
                                        }
                                        case COUNTER -> {
                                            return Metrics.counter(metric.metricName, tags);
                                        }
                                        case DISTRIBUTION_SUMMARY -> {
                                            return DistributionSummary.builder(metric.metricName).tags(tags).register(Metrics.globalRegistry);
                                        }
                                        default ->
                                                throw new IllegalStateException("Unsupported Meter Type: " + metric.meterType);
                                    }
                                }
                            });
                }
            });

    public static void recordCount(MeterKey key, RPCMetric metric) {
        assert metric.meterType == Meter.Type.COUNTER;
        ((Counter) TRAFFIC_METERS.get(key).get(metric)).increment();
    }

    public static void recordCount(MeterKey key, RPCMetric metric, double inc) {
        assert metric.meterType == Meter.Type.COUNTER;
        ((Counter) TRAFFIC_METERS.get(key).get(metric)).increment(inc);
    }

    public static Timer timer(MeterKey key, RPCMetric metric) {
        assert metric.meterType == Meter.Type.TIMER;
        return (Timer) TRAFFIC_METERS.get(key).get(metric);
    }

    public static void recordSummary(MeterKey key, RPCMetric metric, int depth) {
        assert metric.meterType == Meter.Type.DISTRIBUTION_SUMMARY;
        ((DistributionSummary) TRAFFIC_METERS.get(key).get(metric)).record(depth);
    }

}
