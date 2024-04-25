
package com.zachary.bifromq.basecrdt.store.compressor;

import com.google.protobuf.ByteString;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @description: GZip 解压缩
 * @author: cuiweiman
 * @date: 2024/4/20 18:45
 */
public class GzipCompressor implements Compressor {

    @Override
    public ByteString compress(ByteString src) {
        // 输出流 是 GZIPOutputStream 压缩流
        ByteString.Output out = ByteString.newOutput();
        try (GZIPOutputStream defl = new GZIPOutputStream(out);
             InputStream newInput = src.newInput()) {
            newInput.transferTo(defl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out.toByteString();
    }

    @Override
    public ByteString decompress(ByteString src) {
        ByteString.Output out = ByteString.newOutput();
        // 输入流是 GZIPInputStream 压缩流，输出 正常流
        try (GZIPInputStream infl = new GZIPInputStream(src.newInput())) {
            infl.transferTo(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out.toByteString();
    }
}
