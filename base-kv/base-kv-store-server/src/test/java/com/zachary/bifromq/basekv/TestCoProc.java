package com.zachary.bifromq.basekv;

import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProc;
import com.zachary.bifromq.basekv.store.api.IKVRangeReader;
import com.zachary.bifromq.basekv.store.api.IKVReader;
import com.zachary.bifromq.basekv.store.api.IKVWriter;
import com.google.protobuf.ByteString;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class TestCoProc implements IKVRangeCoProc {
    private final Supplier<IKVRangeReader> rangeReaderProvider;

    public TestCoProc(KVRangeId id, Supplier<IKVRangeReader> rangeReaderProvider) {
        this.rangeReaderProvider = rangeReaderProvider;
    }

    @Override
    public CompletableFuture<ByteString> query(ByteString input, IKVReader reader) {
        // get
        return CompletableFuture.completedFuture(reader.get(input).orElse(ByteString.EMPTY));
    }

    @Override
    public Supplier<ByteString> mutate(ByteString input, IKVReader reader, IKVWriter client) {
        String[] str = input.toStringUtf8().split("_");
        ByteString key = ByteString.copyFromUtf8(str[0]);
        ByteString value = ByteString.copyFromUtf8(str[1]);
        // update
        Optional<ByteString> existing = reader.get(key);
        client.put(key, value);
        return () -> existing.orElse(ByteString.EMPTY);
    }

    @Override
    public void close() {

    }
}
