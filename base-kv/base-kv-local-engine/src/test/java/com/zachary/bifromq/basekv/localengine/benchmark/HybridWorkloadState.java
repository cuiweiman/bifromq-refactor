package com.zachary.bifromq.basekv.localengine.benchmark;

import com.zachary.bifromq.basekv.localengine.IKVEngineIterator;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import static com.zachary.bifromq.basekv.localengine.IKVEngine.DEFAULT_NS;

@Slf4j
@State(Scope.Benchmark)
public class HybridWorkloadState extends BenchmarkState {
    int rangeId;
    ConcurrentHashMap<Long, IKVEngineIterator> itrMap = new ConcurrentHashMap<>();

    @Override
    protected void afterSetup() {
        rangeId = kvEngine.registerKeyRange(DEFAULT_NS, null, null);
//        itr = kvEngine.newIterator(rangeId);
    }


    @Override
    protected void beforeTeardown() {
//        itr.close();
        itrMap.values().forEach(IKVEngineIterator::close);
        kvEngine.unregisterKeyRange(rangeId);
    }

    public void randomPut() {
        kvEngine.put(rangeId, randomBS(), randomBS());
    }

    public void randomDelete() {
        kvEngine.delete(rangeId, randomBS());
    }

    public void randomPutAndDelete() {
        ByteString key = randomBS();
        int batchId = kvEngine.startBatch();
        kvEngine.put(batchId, rangeId, key, randomBS());
        kvEngine.delete(batchId, rangeId, key);
        kvEngine.endBatch(batchId);
    }

    public Optional<ByteString> randomGet() {
        return kvEngine.get(rangeId, randomBS());
    }

    public boolean randomExist() {
        return kvEngine.exist(rangeId, randomBS());
    }

    public void seekToFirst() {
        IKVEngineIterator itr = itrMap.computeIfAbsent(Thread.currentThread().getId(),
            k -> kvEngine.newIterator(rangeId));
        itr.refresh();
        itr.seekToFirst();
    }

    public void randomSeek() {
        IKVEngineIterator itr = itrMap.computeIfAbsent(Thread.currentThread().getId(),
            k -> kvEngine.newIterator(rangeId));
        itr.refresh();
        itr.seek(randomBS());
    }

    private ByteString randomBS() {
        return ByteString.copyFromUtf8(ThreadLocalRandom.current().nextInt(0, 1000000000) + "");
    }
}
