package com.zachary.bifromq.basekv.store.api;

import com.zachary.bifromq.basekv.proto.LoadHint;
import com.google.protobuf.ByteString;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface IKVRangeCoProc {

    /**
     * Get current load hint
     *
     * @return
     */
    default LoadHint get() {
        return LoadHint.newBuilder().setLoad(0.0).build();
    }

    /**
     * Execute a query co-proc
     *
     * @param input
     * @return
     */
    CompletableFuture<ByteString> query(ByteString input, IKVReader client);

    /**
     * Execute a mutation co-proc, returns a supplier of mutation output. The supplier will be called after mutation is
     * persisted successfully.
     *
     * @param input
     * @param reader
     * @param writer
     * @return
     */
    Supplier<ByteString> mutate(ByteString input, IKVReader reader, IKVWriter writer);

    /**
     * Close the coproc instance, and release all related resources
     */
    void close();
}
