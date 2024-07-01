package com.zachary.bifromq.basekv.store.range;

import com.google.protobuf.ByteString;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface IKVRangeQueryRunner {
    CompletionStage<Boolean> exist(long ver, ByteString key, boolean linearized);

    CompletionStage<Optional<ByteString>> get(long ver, ByteString key, boolean linearized);

    CompletableFuture<ByteString> queryCoProc(long ver, ByteString query, boolean linearized);

    void close();
}
