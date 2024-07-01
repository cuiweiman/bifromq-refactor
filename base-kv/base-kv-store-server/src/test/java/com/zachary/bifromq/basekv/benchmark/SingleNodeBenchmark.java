package com.zachary.bifromq.basekv.benchmark;

import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.store.KVRangeStoreTestCluster;
import com.zachary.bifromq.basekv.store.option.KVRangeStoreOptions;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.basekv.utils.KVRangeIdUtil.toShortString;
import static com.google.protobuf.ByteString.copyFromUtf8;
import static org.awaitility.Awaitility.await;

@Slf4j
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Measurement(iterations = 100, time = 5)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class SingleNodeBenchmark {
    protected KVRangeStoreTestCluster cluster;
    private int count = 3;
    private KVRangeStoreOptions options = new KVRangeStoreOptions();
    private List<KVRangeId> ranges;

    @Setup
    public void setup() {
        log.info("Starting test cluster");
        options.getKvRangeOptions().getWalRaftConfig()
            .setAsyncAppend(false)
            .setMaxUncommittedProposals(Integer.MAX_VALUE);
        cluster = new KVRangeStoreTestCluster(options);
        String store0 = cluster.bootstrapStore();
        KVRangeId rangeId = cluster.genesisKVRangeId();
        long start = System.currentTimeMillis();
        cluster.awaitKVRangeReady(store0, rangeId);
        log.info("KVRange ready in {}ms: kvRangeId={}", System.currentTimeMillis() - start, toShortString(rangeId));
        KVRangeSetting rangeSettings = cluster.awaitAllKVRangeReady(rangeId, 0, 5000);
        cluster.split(store0, rangeSettings.ver, rangeId, ByteString.copyFromUtf8("Key1")).toCompletableFuture()
            .join();
        await().atMost(Duration.ofSeconds(10)).until(() -> cluster.allKVRangeIds().size() == 2);
        ranges = Lists.newArrayList(cluster.allKVRangeIds());
        for (KVRangeId r : ranges) {
            cluster.awaitKVRangeReady(store0, r);
        }
    }

    @TearDown
    public void teardown() {
        if (cluster != null) {
            log.info("Shutting down test cluster");
            cluster.shutdown();
        }
    }

    @Benchmark
    @Group("WriteOnly")
    @GroupThreads(20)
    public void putRange0() {
        cluster.put(cluster.bootstrapStore(),
            1,
            ranges.get(0),
            copyFromUtf8("key0" + count),
            copyFromUtf8("value" + count)).toCompletableFuture().join();
        count++;
    }

    @Benchmark
    @Group("WriteOnly")
    @GroupThreads(20)
    public void putRange1() {
        cluster.put(cluster.bootstrapStore(),
            1,
            ranges.get(1),
            copyFromUtf8("key1" + count),
            copyFromUtf8("value" + count)).toCompletableFuture().join();
        count++;
    }

    public static void main(String[] args) {
        Options opt = new OptionsBuilder()
            .include(SingleNodeBenchmark.class.getSimpleName())
            .forks(1)
            .build();
        try {
            new Runner(opt).run();
        } catch (RunnerException e) {
            System.out.println(e);
        }
    }
}
