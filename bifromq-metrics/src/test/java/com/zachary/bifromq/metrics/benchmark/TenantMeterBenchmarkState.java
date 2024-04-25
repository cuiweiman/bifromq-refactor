
package com.zachary.bifromq.metrics.benchmark;

import com.zachary.bifromq.metrics.TenantMeter;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

/**
 * {@link State)} 用于定义测试对象的作用域
 * <p>
 * {@link Scope#Benchmark} 作用域为本次JMH测试，线程共享
 * {@link Scope#Benchmark} 作用域为线程，可以理解为一个ThreadLocal变量
 * {@link Scope#Benchmark} 作用域为 group
 *
 * @description: 定义测试对象的作用域
 * @author: cuiweiman
 * @date: 2024/3/30 20:06
 */
@State(Scope.Benchmark)
public class TenantMeterBenchmarkState {

    public final TenantMeter meter = TenantMeter.get("trafficA");

}
