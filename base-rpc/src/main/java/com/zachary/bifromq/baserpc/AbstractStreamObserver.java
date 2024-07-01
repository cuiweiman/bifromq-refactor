package com.zachary.bifromq.baserpc;

import com.zachary.bifromq.baserpc.metrics.RPCMeters;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public abstract class AbstractStreamObserver<InT, OutT> implements StreamObserver<InT> {
    protected final String tenantId;
    protected final Map<String, String> metadata;
    protected final ServerCallStreamObserver<OutT> responseObserver;
    protected final RPCMeters.MeterKey meterKey;

    protected AbstractStreamObserver(StreamObserver<OutT> responseObserver) {
        tenantId = RPCContext.TENANT_ID_CTX_KEY.get();
        metadata = RPCContext.CUSTOM_METADATA_CTX_KEY.get();
        meterKey = RPCContext.METER_KEY_CTX_KEY.get();
        this.responseObserver = (ServerCallStreamObserver<OutT>) responseObserver;
        log.trace("Pipeline@{} created: tenantId={}", hashCode(), tenantId);
    }

    public final Map<String, String> metadata() {
        return metadata;
    }

    public final String metadata(String key) {
        return metadata.get(key);
    }

    public final boolean hasMetadata(String key) {
        return metadata.containsKey(key);
    }

    public abstract void close();
}
