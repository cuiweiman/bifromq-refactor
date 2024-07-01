package com.zachary.bifromq.baserpc;

import com.zachary.bifromq.baserpc.metrics.RPCMeters;
import com.zachary.bifromq.baserpc.metrics.RPCMetric;
import io.grpc.stub.StreamObserver;
import io.micrometer.core.instrument.Timer;

import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class UnaryResponse {
    public static <Resp> void response(Function<String, CompletionStage<Resp>> reqHandler,
                                       StreamObserver<Resp> observer) {
        response((tenantId, metadata) -> reqHandler.apply(tenantId), observer);
    }

    public static <Resp> void response(BiFunction<String, Map<String, String>, CompletionStage<Resp>> reqHandler,
                                       StreamObserver<Resp> observer) {
        RPCMeters.MeterKey meterKey = RPCContext.METER_KEY_CTX_KEY.get();
        String tenantId = RPCContext.TENANT_ID_CTX_KEY.get();
        Map<String, String> metadata = RPCContext.CUSTOM_METADATA_CTX_KEY.get();
        Timer.Sample sample = Timer.start();
        RPCMeters.recordCount(meterKey, RPCMetric.UnaryReqReceivedCount);
        reqHandler.apply(tenantId, metadata)
                .whenComplete((v, e) -> {
                    sample.stop(RPCMeters.timer(meterKey, RPCMetric.UnaryReqProcessLatency));
                    if (e != null) {
                        observer.onError(e);
                        RPCMeters.recordCount(meterKey, RPCMetric.UnaryReqFailCount);
                    } else {
                        observer.onNext(v);
                        observer.onCompleted();
                        RPCMeters.recordCount(meterKey, RPCMetric.UnaryReqFulfillCount);
                    }
                });
    }
}
