package com.zachary.bifromq.basecrdt.core.benchmark;

import com.zachary.bifromq.basecrdt.core.api.operation.CCounterOperation;
import com.zachary.bifromq.basecrdt.core.api.ICCounter;
import com.zachary.bifromq.basecrdt.core.api.IORMap;
import com.zachary.bifromq.basecrdt.proto.Replica;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.basecrdt.core.api.CRDTURI.toURI;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.ormap;

@Slf4j
@State(Scope.Group)
public class CCounterMapBenchmark extends CRDTBenchmarkTemplate {
    Replica replica;
    private ArrayList<ICCounter> counters = new ArrayList<>();

    @Override
    protected void doSetup() {
        replica = engine.host(toURI(ormap, "cctr"));
        Optional<IORMap> counterMap = engine.get(replica.getUri());
        for (int i = 0; i < 1000; i++) {
            counters.add(counterMap.get().getCCounter(ByteString.copyFromUtf8("c-" + i)));
        }
    }

    @Benchmark
    @Group("ReadWrite")
    @GroupThreads(1)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void add() {
        counters.get(ThreadLocalRandom.current().nextInt(1000)).execute(CCounterOperation.add(1));
    }

    @Benchmark
    @Group("ReadWrite")
    @GroupThreads(1)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void zerout() {
        counters.get(ThreadLocalRandom.current().nextInt(1000)).execute(CCounterOperation.zeroOut());
    }

    @Benchmark
    @Group("ReadWrite")
    @GroupThreads(1)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void read() {
        counters.get(ThreadLocalRandom.current().nextInt(1000)).read();
    }
}
