package com.zachary.bifromq.basekv.localengine.benchmark;

import lombok.SneakyThrows;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class HybridWorkload {
    @SneakyThrows
    public static void main(String[] args) {
        Options opt = new OptionsBuilder()
            .include(HybridWorkload.class.getSimpleName())
            .warmupIterations(3)
            .measurementIterations(8)
            .forks(1)
            .build();
        new Runner(opt).run();
    }

    @Benchmark
    @Group("Hybrid")
    @BenchmarkMode(Mode.Throughput)
    @GroupThreads(4)
    public void randomPut(HybridWorkloadState state) {
        state.randomPut();
    }

    @Benchmark
    @Group("Hybrid")
    @BenchmarkMode(Mode.Throughput)
    @GroupThreads(4)
    public void randomDelete(HybridWorkloadState state) {
        state.randomDelete();
    }

    @Benchmark
    @Group("Hybrid")
    @BenchmarkMode(Mode.Throughput)
    @GroupThreads(4)
    public void randomPutAndDelete(HybridWorkloadState state) {
        state.randomPutAndDelete();
    }

    @Benchmark
    @Group("Hybrid")
    @BenchmarkMode(Mode.Throughput)
    @GroupThreads(2)
    public void randomGet(HybridWorkloadState state, Blackhole blackhole) {
        blackhole.consume(state.randomGet());
    }

    @Benchmark
    @Group("Hybrid")
    @GroupThreads(3)
    @BenchmarkMode(Mode.Throughput)
    public void seekToFirst(HybridWorkloadState state) {
        state.seekToFirst();
    }

    @Benchmark
    @Group("Hybrid")
    @GroupThreads(3)
    @BenchmarkMode(Mode.Throughput)
    public void randomSeek(HybridWorkloadState state) {
        state.randomSeek();
    }
}
