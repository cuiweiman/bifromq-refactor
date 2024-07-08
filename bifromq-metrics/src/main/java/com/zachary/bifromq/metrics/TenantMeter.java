package com.zachary.bifromq.metrics;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

import java.lang.ref.Cleaner;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @description: Micrometer 度量 服务监控注册类 + 服务度量资源 的 回收逻辑
 * @author: cuiweiman
 * @date: 2024/3/29 14:38
 * @see com.zachary.bifromq.metrics.basetest.CleanerTest2 可以参考 资源回收 业务的 实现
 */
@Slf4j
public class TenantMeter {

    /**
     * 创建一个 回收对象 Cleaner，用于 管理 资源 的 清理
     * <p>
     * 1. 创建 {@link State} 类，实现 Runnable 接口，并在其 run 方法中实现资源释放的逻辑。这个类的作用是 定义需要进行清理的任务。
     * 2. 在 {@link TenantMeter} 类中创建一个静态的 Cleaner 对象。Cleaner 类是Java 9中新引入的一个类，用于管理资源的清理。
     * 3. 在 {@link TenantMeter} 类的构造方法中，创建一个 Cleanable 对象并将其与 {@link State} 对象进行关联。
     * Cleanable 对象可以通过 {@link Cleaner#register(Object, Runnable)} 来创建，第一个参数是需要进行清理的对象，第二个参数是 {@link State} 对象。
     * 4. 定义 {@link TenantMeter#destroy} 方法，手动调用 {@link Cleaner.Cleanable#clean()} 来触发资源释放的逻辑。
     * <p>
     * {@link Cleaner#create()} 在JDK1.9以上版本可使用.
     * <p>
     * 在进行 GC 时，如果某些对象在回收前需要做一些处理，可以通过覆写 {@link Object#finalize()} 方法来实现这种回收前的处理。
     * 但是 方法中若 出现 线程死锁操作，可能造成 gc 失败，并发生严重的线程阻塞问题。
     * - 解决办法:
     * 在 JDK1.9 之后，启动了一个专属的 垃圾回收线程 ---- {@link Cleaner} 类。
     * 通过 Cleaner 启动线程进行 gc 前的处理逻辑，或者 通过 {@link Cleaner.Cleanable#clean()} 手动 释放内存资源(释放前会触发 线程进行会收前处理)。
     */
    private static final Cleaner CLEANER = Cleaner.create();

    /**
     * State 线程任务 的作用:
     * 1. 内置缓存，存储并注册 各类 度量
     * 2. 作为监听对象, 结合 Cleaner 类, 当 State 的引用变为 虚引用(不可到达)时, 调用执行 State#run 方法中的清理任务
     */
    static class State implements Runnable {

        final Map<TenantMetric, Meter> meters = new HashMap<>();

        /**
         * 遍历 {@link TenantMetric} 中的 所有枚举，将 度量
         * {@link Tags} 维度 引入维度的概念便于对某一指标数据进行更细粒度的拆分研究。
         * 每一项指标都有一个唯一标识的 名字 和 维度。"维度"和"标签"是一个意思，Micrometer 中有一个Tag接口，仅仅因为它更简短。
         * 一般来说，应该尽可能地使用名称作为轴心。
         * （PS：指标的名字很好理解，维度怎么理解呢？
         * 如果把 name 想象成 横坐标 的话，那么 dimension 就是纵坐标。Tag 是一个 key/value对 ，代表 指标 的一个 维度值）
         */
        State(Tags tags) {
            for (TenantMetric metric : TenantMetric.values()) {
                switch (metric.meterType) {
                    // Metrics.counter(Meter命名，Tag1的命名，Tag2的命名...)，
                    case COUNTER -> meters.put(metric, Metrics.counter(metric.metricName, tags));
                    case TIMER -> meters.put(metric, Metrics.timer(metric.metricName, tags));
                    case DISTRIBUTION_SUMMARY -> meters.put(metric, Metrics.summary(metric.metricName, tags));
                    case GAUGE -> {
                        // ignore gauge
                    }
                    default -> throw new UnsupportedOperationException("Unsupported traffic meter type");
                }
            }
        }

        /**
         * 对象被 GC 时，从 Micrometer 的全局注册表中，移除各个 度量器
         * 并 清空 Map 缓存
         */
        @Override
        public void run() {
            meters.values().forEach(Metrics.globalRegistry::remove);
            meters.clear();
        }
    }

    public static final String TAG_TENANT_ID = "tenantId";
    /**
     *
     */
    private static final ConcurrentMap<String, Map<TenantMetric, Gauge>> TENANT_GAUGES = new ConcurrentHashMap<>();
    /**
     * 创建对 value 对象弱引用的缓存，
     * <p>
     * 强引用 - 堆内存不足时，GC 无法回收到空间，将内存溢出。
     * 软引用 - 对象 只被 软引用时，若堆内存充足则不会被 GC 回收；不足时将会被 GC回收。
     * 弱引用 ({@link Caffeine#weakValues()} {@link WeakReference}) - 生命周期 更短，一旦发生 GC，不论堆内存是否充足，都将回收。
     * 虚引用 - 虚引用并不会决定对象的生命周期。如果一个对象仅持有虚引用，那么它就和没有任何引用一样，在任何时候都可能被垃圾回收器回收。
     * 虚引用必须和 引用队列(ReferenceQueue)联合使用。GC一个被虚引用的对象时，会在回收之前把这个虚引用加入到与之关联的引用队列中。
     * 程序通过判断 引用队列 中是否存在 虚引用对象，识别 对象即将被GC，从而触发一些业务逻辑操作。
     */
    private static final LoadingCache<String, TenantMeter> TENANT_METER_CACHE =
            Caffeine.newBuilder().weakValues().build(TenantMeter::new);


    public static TenantMeter get(String tenantId) {
        return TENANT_METER_CACHE.get(tenantId);
    }

    public static void cleanUp() {
        TENANT_METER_CACHE.cleanUp();
    }


    private final State state;
    private final Tags tags;
    private final Cleaner.Cleanable cleanable;

    public TenantMeter(String tenantId) {
        this.tags = Tags.of(TAG_TENANT_ID, tenantId);
        this.state = new State(tags);
        this.cleanable = CLEANER.register(this, state);
    }

    public void recordCount(TenantMetric metric) {
        recordCount(metric, 1);
    }

    public void recordCount(TenantMetric metric, double inc) {
        assert metric.meterType == Meter.Type.COUNTER;
        ((Counter) state.meters.get(metric)).increment(inc);
    }

    public Timer timer(TenantMetric metric) {
        assert metric.meterType == Meter.Type.TIMER;
        return (Timer) state.meters.get(metric);
    }

    public void recordSummary(TenantMetric metric, double value) {
        assert metric.meterType == Meter.Type.DISTRIBUTION_SUMMARY;
        ((DistributionSummary) state.meters.get(metric)).record(value);
    }

    public static void gauging(String tenantId, TenantMetric gaugeMetric, Supplier<Number> supplier) {
        assert gaugeMetric.meterType == Meter.Type.GAUGE;
        TENANT_GAUGES.compute(tenantId, (k, v) -> {
            if (v == null) {
                v = new HashMap<>();
            }
            v.put(gaugeMetric, Gauge.builder(gaugeMetric.metricName, supplier)
                    .tags(Tags.of(TAG_TENANT_ID, tenantId))
                    .register(Metrics.globalRegistry));
            return v;
        });
    }

    public static void stopGauging(String tenantId, TenantMetric gaugeMetric) {
        assert gaugeMetric.meterType == Meter.Type.GAUGE;
        TENANT_GAUGES.computeIfPresent(tenantId, (k, gaugeMap) -> {
            Gauge gauge = gaugeMap.remove(gaugeMetric);
            if (gauge != null) {
                Metrics.globalRegistry.remove(gauge);
            }
            if (gaugeMap.isEmpty()) {
                return null;
            }
            return gaugeMap;
        });
    }


    public void destroy() {
        cleanable.clean();
    }

}
