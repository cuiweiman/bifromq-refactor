package com.zachary.bifromq.basecrdt.store.compressor;

import com.google.protobuf.ByteString;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CompressorTest {

    @Test
    public void testGzip() {
        Compressor gzip = new GzipCompressor();
        ByteString bytes = ByteString.copyFromUtf8("gzip test");
        ByteString compressed = gzip.compress(bytes);
        Assert.assertEquals(bytes, gzip.decompress(compressed));
    }

    @Test
    public void testNoop() {
        Compressor noop = new NoopCompressor();
        ByteString bytes = ByteString.copyFromUtf8("noop test");
        ByteString compressed = noop.compress(bytes);
        Assert.assertEquals(bytes, noop.decompress(compressed));
    }

}
