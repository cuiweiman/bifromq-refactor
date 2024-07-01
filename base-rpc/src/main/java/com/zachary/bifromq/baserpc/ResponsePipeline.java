package com.zachary.bifromq.baserpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ResponsePipeline<RequestT, ResponseT> extends AbstractResponsePipeline<RequestT, ResponseT> {
    public ResponsePipeline(StreamObserver<ResponseT> responseObserver) {
        super(responseObserver);

    }

    @Override
    public final void onNext(RequestT request) {
        startHandlingRequest(request).thenAccept((response) -> emitResponse(request, response));
    }
}
