package com.zachary.bifromq.basekv.localengine.benchmark;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.basekv.localengine.TestUtil.toByteString;

@Slf4j
public class ContinuousKeySingleDeleteAndSeek {
    @SneakyThrows
    public static void main(String[] args) {
        Options opt = new OptionsBuilder()
            .include(ContinuousKeySingleDeleteAndSeek.class.getSimpleName())
            .build();
        new Runner(opt).run();
    }

    @Benchmark
    @Warmup(iterations = 3)
    @Measurement(iterations = 8)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void iterator(ContinuousKeySingleDeleteAndSeekState state, Blackhole bl) {
//        IKVEngineIterator itr = kvEngine.newIterator(DEFAULT_NS, key.concat(toByteString(state.i)),
//                key.concat(toByteString(state.i + 1)));
//        try (IKVEngineIterator itr = state.kvEngine.newIterator(DEFAULT_NS)) {
        state.itr.refresh();
//        state.itr.seekToLast();
        state.itr.seek(state.key.concat(toByteString(ThreadLocalRandom.current().nextInt(state.keyCount))));
        bl.consume(state.itr.isValid());
//    }

    }
}
