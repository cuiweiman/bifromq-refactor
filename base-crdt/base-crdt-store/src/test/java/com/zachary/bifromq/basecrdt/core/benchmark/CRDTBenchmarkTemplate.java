package com.zachary.bifromq.basecrdt.core.benchmark;

import com.zachary.bifromq.basecrdt.core.api.CRDTEngineOptions;
import com.zachary.bifromq.basecrdt.core.api.ICRDTEngine;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

import static com.google.protobuf.UnsafeByteOperations.unsafeWrap;

@Slf4j
public abstract class CRDTBenchmarkTemplate {
    protected ICRDTEngine engine;

    @Setup
    public void setup() throws IOException {
        engine = ICRDTEngine.newInstance(new CRDTEngineOptions());
        engine.start();
        doSetup();
    }

    @TearDown
    public void tearDown() {
        log.info("Stop engine");
        engine.stop();
    }

    protected abstract void doSetup();

    @Test
    @Ignore
    public void test() {
        Options opt = new OptionsBuilder()
            .include(this.getClass().getSimpleName())
            .warmupIterations(0)
            .measurementIterations(100)
            .forks(1)
            .build();
        try {
            new Runner(opt).run();
        } catch (RunnerException e) {
            throw new RuntimeException(e);
        }
    }

    protected ByteString toByteString(long l) {
        return unsafeWrap(ByteBuffer.allocate(Long.BYTES).putLong(l).array());
    }
}
