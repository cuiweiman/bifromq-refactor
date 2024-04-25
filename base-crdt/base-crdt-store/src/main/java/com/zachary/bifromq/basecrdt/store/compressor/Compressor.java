
package com.zachary.bifromq.basecrdt.store.compressor;

import com.google.protobuf.ByteString;

/**
 * @description: 压缩接口
 * @author: cuiweiman
 * @date: 2024/4/20 18:45
 */
public interface Compressor {

    /**
     * 压缩
     *
     * @param src 原数据
     * @return 压缩数据
     */
    ByteString compress(ByteString src);

    /**
     * 解压缩
     *
     * @param src 原数据
     * @return 解压数据
     */
    ByteString decompress(ByteString src);

    /**
     * 根据 文件类型 返回 压缩方法
     *
     * @param algorithm 算法
     * @return 压缩方法
     */
    static Compressor newInstance(CompressAlgorithm algorithm) {
        if (algorithm.equals(CompressAlgorithm.GZIP)) {
            return new GzipCompressor();
        }
        return new NoopCompressor();
    }

}
