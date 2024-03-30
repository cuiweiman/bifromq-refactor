package com.zachary.bifromq.metrics;

import io.micrometer.core.instrument.Meter;

/**
 * <a href="https://micrometer.io/docs/">Micrometer 官网</a>
 * <a href="https://www.cnblogs.com/cjsblog/p/11556029.html">Micrometer 介绍</a>
 * <p>
 * {@link Meter.Type#LONG_TASK_TIMER} 长任务计时器用于跟踪所有正在运行的长时间运行任务的总持续时间和此类任务的数量。{@link Meter.Type#TIMER}记录的是次数，{@link Meter.Type#LONG_TASK_TIMER} 记录的是任务时长和任务数
 *
 * @description: micrometer 度量器配置
 * @author: cuiweiman
 * @date: 2024/3/29 14:38
 */
public enum TenantMetric {
    /**
     * mqtt 连接数量仪表，单个指标值用 {@link Meter.Type#GAUGE}  表示
     * <p>
     * 仪表 是获取当前值的句柄。仪表的典型示例是 集合 或 映射的大小 或 处于运行状态 的线程数。
     * 仪表 对于 监视 具有自然上限的事物 很有用。
     * 不建议使用仪表来监控 诸如请求计数之类的东西，因为它们可以在应用程序实例的生命周期内 无限增长。
     * 使用示例: 监控 list 集合的 容量值。
     * List<String> list = registry.gauge("listGauge", Collections.emptyList(), new ArrayList<>(), List::size);
     * List<String> list2 = registry.gaugeCollectionSize("listSize2", Tags.empty(), new ArrayList<>());
     * Map<String, Integer> map = registry.gaugeMapSize("mapGauge", Tags.empty(), new HashMap<>());
     * <p>
     * 命名约定: 用.分隔小写单词字符。不同的监控系统有不同的命名约定。
     */
    MqttConnectionGauge("mqtt.connection.num.gauge", Meter.Type.GAUGE),

    /**
     * 统计 mqtt 连接数量
     */
    MqttConnectCount("mqtt.connect.count", Meter.Type.COUNTER),
    /**
     * 统计 mqtt 断开连接的数量
     */
    MqttDisconnectCount("mqtt.disconnect.count", Meter.Type.COUNTER),
    MqttQoS0IngressCount("mqtt.ingress.qos0.count", Meter.Type.COUNTER),

    /**
     * {@link Meter.Type#DISTRIBUTION_SUMMARY}用于跟踪分布式的事件。它在结构上类似于计时器，但是记录的值不代表时间单位。
     * 例如，记录 http 服务器上的 请求响应大小。
     */
    MqttQoS0IngressBytes("mqtt.ingress.qos0.bytes", Meter.Type.DISTRIBUTION_SUMMARY),
    MqttQoS0DistBytes("mqtt.dist.qos0.bytes", Meter.Type.DISTRIBUTION_SUMMARY),
    MqttQoS1IngressBytes("mqtt.ingress.qos1.bytes", Meter.Type.DISTRIBUTION_SUMMARY),
    MqttQoS1DistBytes("mqtt.dist.qos1.bytes", Meter.Type.DISTRIBUTION_SUMMARY),
    MqttQoS2IngressBytes("mqtt.ingress.qos2.bytes", Meter.Type.DISTRIBUTION_SUMMARY),
    MqttQoS2DistBytes("mqtt.dist.qos2.bytes", Meter.Type.DISTRIBUTION_SUMMARY),
    MqttQoS0EgressBytes("mqtt.egress.qos0.bytes", Meter.Type.DISTRIBUTION_SUMMARY),
    MqttQoS1EgressBytes("mqtt.egress.qos1.bytes", Meter.Type.DISTRIBUTION_SUMMARY),
    MqttQoS1DeliverBytes("mqtt.deliver.qos1.bytes", Meter.Type.DISTRIBUTION_SUMMARY),
    MqttQoS2EgressBytes("mqtt.egress.qos2.bytes", Meter.Type.DISTRIBUTION_SUMMARY),
    MqttQoS2DeliverBytes("mqtt.deliver.qos2.bytes", Meter.Type.DISTRIBUTION_SUMMARY),

    /**
     * 统计 qos=0 的消息延迟
     * <p>
     * {@link Meter.Type#TIMER} (计时器)适用于记录耗时比较短的事件的执行时间，通过时间分布展示事件的序列和发生频率。
     * 定时器是在 一定时间内 获取 记录的最大值, 在每个时间片段内的值是独立的, 也就是在 下个时间片段会被 清0
     * 计时、事件的次数 和 总时间 用 Timer 表示。
     * {@link Meter.Type#TIMER} 用于测量 短时间延迟 和 此类事件的频率。所有 Timer 实现至少将 总时间 和 事件次数 报告为单独的 时间序列。
     */
    MqttQoS0InternalLatency("mqtt.in.qos0.latency", Meter.Type.TIMER),
    MqttQoS1InternalLatency("mqtt.in.qos1.latency", Meter.Type.TIMER),
    MqttQoS1ExternalLatency("mqtt.ex.qos1.latency", Meter.Type.TIMER),
    MqttQoS2InternalLatency("mqtt.in.qos2.latency", Meter.Type.TIMER),
    MqttQoS2ExternalLatency("mqtt.ex.qos2.latency", Meter.Type.TIMER),
    MqttChannelLatency("mqtt.channel.latency", Meter.Type.TIMER),
    DistSubInfoSizeGauge("dist.sub.size.gauge", Meter.Type.GAUGE),
    InboxFetcherGauge("inbox.fetcher.num.gauge", Meter.Type.GAUGE);

    public final String metricName;
    public final Meter.Type meterType;

    TenantMetric(String metricName, Meter.Type meterType) {
        this.metricName = metricName;
        this.meterType = meterType;
    }
}
