package com.zachary.bifromq.basekv.localengine.benchmark;


import com.zachary.bifromq.basekv.localengine.IKVEngine;
import com.zachary.bifromq.basekv.localengine.IKVEngineIterator;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.basekv.localengine.TestUtil.toByteString;

@Slf4j
@State(Scope.Group)
public class ContinuousKeyDeleteAndSeek_Benchmark extends BenchmarkTemplate {
    private static int keyCount = 1000000;
    private static ByteString key = ByteString.copyFromUtf8("key");
    private int rangeId;
//    IKVEngineIterator itr;

    @State(Scope.Thread)
    public static class BenchmarkThreadState {
        volatile int i = ThreadLocalRandom.current().nextInt(keyCount);

        @Setup(Level.Invocation)
        public void inc() {
            i = ThreadLocalRandom.current().nextInt(keyCount);
        }
    }

    @Override
    protected void doSetup() {
        rangeId = kvEngine.registerKeyRange(IKVEngine.DEFAULT_NS, null, null);

//        itr = kvEngine.newIterator(DEFAULT_NS);
        int batchId = kvEngine.startBatch();
        for (int i = 0; i < keyCount; i++) {
            kvEngine.insert(batchId, rangeId, key.concat(toByteString(i)), ByteString.copyFromUtf8("val" + i));
            kvEngine.delete(batchId, rangeId, key.concat(toByteString(i)));
//            kvEngine.deleteRange(batchId, DEFAULT_NS, key.concat(toByteString(i)), key.concat(toByteString(i + 1)));
        }
        kvEngine.put(batchId, rangeId, key.concat(toByteString(keyCount)), ByteString.EMPTY);
//        kvEngine.deleteRange(batchId, DEFAULT_NS, key.concat(toByteString(0)), key.concat(toByteString(keyCount)));
        kvEngine.endBatch(batchId);
    }

//    @Benchmark
//    @Group("Seek")
//    @GroupThreads(1)
//    @BenchmarkMode(Mode.Throughput)
//    @OutputTimeUnit(TimeUnit.SECONDS)
//    public void delete(BenchmarkThreadState state) {
//        kvEngine.deleteRange(DEFAULT_NS, key.concat(toByteString(state.i)), key.concat(toByteString(state.i + 1)));
//    }

    @Benchmark
    @Group("Seek")
    @GroupThreads(1)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public boolean iterator(BenchmarkThreadState state) {
        IKVEngineIterator itr = kvEngine.newIterator(rangeId, key.concat(toByteString(state.i)),
            key.concat(toByteString(state.i + 1)));
//        IKVEngineIterator itr = kvEngine.newIterator(DEFAULT_NS);
//        itr.seekToFirst();
        itr.seek(key.concat(toByteString(state.i)));
        return itr.isValid();
    }
}
