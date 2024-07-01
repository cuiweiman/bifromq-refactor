package com.zachary.bifromq.basekv.localengine.benchmark;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.basekv.localengine.TestUtil.toByteStringNativeOrder;

@Slf4j
@State(Scope.Group)
public class SingleKeyUpdateAndGet {
    @SneakyThrows
    public static void main(String[] args) {
        Options opt = new OptionsBuilder()
            .include(SingleKeyUpdateAndGet.class.getSimpleName())
            .build();
        new Runner(opt).run();
    }

    @Benchmark
    @Group("SingleKeyUpdateAndGet")
    @GroupThreads(6)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void get(SingleKeyUpdateAndGetState state, Blackhole blackhole) {
        blackhole.consume(state.kvEngine.get(state.rangeId, state.key).get());
    }

    @Benchmark
    @Group("SingleKeyUpdateAndGet")
    @GroupThreads(2)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void update(SingleKeyUpdateAndGetState state) {
        state.kvEngine.put(state.rangeId, state.key,
            toByteStringNativeOrder(ThreadLocalRandom.current().nextInt(1024)));
    }
}
