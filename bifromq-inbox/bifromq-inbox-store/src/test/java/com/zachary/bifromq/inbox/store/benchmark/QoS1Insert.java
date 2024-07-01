package com.zachary.bifromq.inbox.store.benchmark;

import com.zachary.bifromq.inbox.storage.proto.InboxInsertReply;
import lombok.SneakyThrows;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class QoS1Insert {

    @SneakyThrows
    public static void main(String[] args) {
        Options opt = new OptionsBuilder()
            .include(QoS1Insert.class.getSimpleName())
            .build();
        new Runner(opt).run();
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 3)
    @Measurement(iterations = 8)
    @Threads(4)
    @Fork(1)
    public void testInsert(QoS1InsertState state, Blackhole blackhole) {
        InboxInsertReply reply = state.insert();
        blackhole.consume(reply);
    }
}
