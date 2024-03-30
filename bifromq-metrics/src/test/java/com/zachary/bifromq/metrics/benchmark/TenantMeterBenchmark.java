/*
 * Copyright (c) 2023. Baidu, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zachary.bifromq.metrics.benchmark;

import com.sun.net.httpserver.HttpServer;
import com.zachary.bifromq.metrics.TenantMetric;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.SneakyThrows;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 没完全看懂，暂时跳过。已知主要是测试 度量的性能。
 *
 * <a href="https://blog.csdn.net/FeenixOne/article/details/128791863">JMH 方法的性能测试</a>
 * <p>
 * {@link Benchmark}标示 方法测试 类似 {@link org.testng.annotations.Test}，并生成详细的测试报告打印在控制台上。只使用{@link Benchmark}注解，默认生成的测试非常的长，测试时间也非常久，所以一般都会加上一些其它的注解指定测试的参数。
 * <p>
 * {@link BenchmarkMode} 基准测试模式。用的最多的就是{@link Mode#Throughput}模式，即吞吐量测试，指的是被测试的方法每秒钟能执行多少次。当然也会有反过来，每执行一次需要耗时多少秒的模式。
 * {@link Mode#Throughput}: operations per unit of time. 吞吐量，每秒执行多少次。
 * {@link Mode#AverageTime}: average time per per operation. 每次操作平均耗费时间。
 * {@link Mode#SampleTime}: samples the time for each operation. 随机取样，最后输出取样结果的分布，例如"99%的调用在xxx毫秒以内，99.99%的调用在xxx毫秒以内"（sample）
 * {@link Mode#SingleShotTime}: measures the time for a single operation. 只运行一次。往往同时把 warmup 次数设为0，用于测试冷启动时的性能。
 * {@link Mode#All}: 以上所有指标。
 * <p>
 * {@link Warmup} 预热，Java代码是混合执行模式，会将一些高频率多循环执行的代码直接编译好，从而大大增高执行的效率。
 * {@link Warmup#iterations()} 和 {@link Warmup#time()} 分别指的是预热的次数和每次预热的间隔时间（单位是秒）
 * <p>
 * {@link Measurement} 用于控制压测的次数
 * <p>
 * {@link Threads} 指定使用多少个线程来执行基准测试方法，如果 value = 2，那么每次测量都会创建两个线程来执行基准测试方法。
 * <p>
 * {@link Fork#value()} 指定 fork 出多少个子进程来执行同一基准测试方法。
 *
 * @description: 性能测试
 * @author: cuiweiman
 * @date: 2024/3/30 19:51
 */
public class TenantMeterBenchmark {
    /**
     * Prometheus 度量监控
     */
    private static PrometheusMeterRegistry registry;
    private static HttpServer prometheusExportServer;
    private static Thread serverThread;

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 3)
    @Measurement(iterations = 4)
    @Threads(4)
    @Fork(0)
    public void timer(TenantMeterBenchmarkState state) {

        state.meter.timer(TenantMetric.MqttQoS0InternalLatency)
                .record(ThreadLocalRandom.current().nextLong(0, 10000), TimeUnit.MILLISECONDS);
    }


    @SneakyThrows
    public static void main(String[] args) {
        registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        registry.config().meterFilter(new MeterFilter() {
            @Override
            public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
                /*
                 * 配置 度量过滤器，匹配 TIMER 和 DISTRIBUTION_SUMMARY 类型的监控指标
                 */
                return switch (id.getType()) {
                    case TIMER, DISTRIBUTION_SUMMARY -> DistributionStatisticConfig.builder()
                            .percentiles(0.5, 0.95, 0.99)
                            .build();
                    default -> config;
                };
            }
        });
        Metrics.addRegistry(registry);
        try {
            // 用来干啥呢？
            // 创建一个 轻量级 HttpServer 服务器 实例，并绑定到指定的IP地址和端口号
            prometheusExportServer = HttpServer.create(new InetSocketAddress(8888), 0);
            // 创建上下文监听, "/" 表示匹配所有 URI 请求, 将路径为 "/" 请求映射到 httpExchange 处理器
            prometheusExportServer.createContext("/", httpExchange -> {
                // 设置响应头, 发送响应码 和 响应长度, 响应内容
                String response = registry.scrape();
                httpExchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            });
            // 启动服务器
            serverThread = new Thread(prometheusExportServer::start);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Options opt = new OptionsBuilder().include(TenantMeterBenchmark.class.getSimpleName()).build();
        new Runner(opt).run();
    }
}
