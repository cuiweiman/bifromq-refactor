package com.zachary.bifromq.basecrdt.store.compressor;

import com.google.protobuf.ByteString;

/**
 * @description: 不压缩
 * @author: cuiweiman
 * @date: 2024/4/20 18:45
 */
public class NoopCompressor implements Compressor {

    @Override
    public ByteString compress(ByteString src) {
        return src;
    }

    @Override
    public ByteString decompress(ByteString src) {
        return src;
    }
}
