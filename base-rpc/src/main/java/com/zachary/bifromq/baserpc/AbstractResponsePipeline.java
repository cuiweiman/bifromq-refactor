
package com.zachary.bifromq.baserpc;

import com.zachary.bifromq.baserpc.metrics.RPCMeters;
import com.zachary.bifromq.baserpc.metrics.RPCMetric;
import com.zachary.bifromq.baserpc.utils.FutureTracker;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
abstract class AbstractResponsePipeline<RequestT, ResponseT>
    extends AbstractStreamObserver<RequestT, ResponseT> {
    private final AtomicBoolean closed = new AtomicBoolean();
    // used to keep track of opening response futures returned from handleRequest which associated with current
    // ResponsePipeline
    // it's used to finish those associated futures earlier in the event of ResponsePipeline closed, so that
    // the close ResponsePipeline (and its underlying StreamObserver) could be gc'ed as soon as possible
    private final FutureTracker futureTracker = new FutureTracker();

    protected AbstractResponsePipeline(StreamObserver<ResponseT> responseObserver) {
        super(responseObserver);
    }

    @Override
    public final void onError(Throwable t) {
        close(t);
    }

    @Override
    public final void onCompleted() {
        close();
    }

    public final boolean isClosed() {
        return closed.get();
    }

    @Override
    public void close() {
        close(null);
    }

    private void close(Throwable t) {
        if (closed.compareAndSet(false, true)) {
            if (t != null) {
                log.trace("Close pipeline@{} by error", hashCode(), t);
                responseObserver.onError(t);
            } else {
                log.trace("Close pipeline@{} normally", hashCode());
                responseObserver.onCompleted();
            }
            cleanup();
        }
    }

    /**
     * Handle the request and return the result via completable future, remember always throw exception asynchronously
     * Returned future complete exceptionally will cause pipeline close
     *
     * @param tenantId the tenantId
     * @param request  the request
     * @return a future of response
     */
    protected abstract CompletableFuture<ResponseT> handleRequest(String tenantId, RequestT request);

    // this method is meant to be used in subclass
    final CompletableFuture<ResponseT> startHandlingRequest(RequestT request) {
        log.trace("Start handling request in pipeline@{}: request={}", hashCode(), request);
        RPCMeters.recordCount(meterKey, RPCMetric.PipelineReqReceivedCount);
        Timer.Sample sample = Timer.start();
        CompletableFuture<ResponseT> respFuture = futureTracker.track(handleRequest(tenantId, request));
        // track current response future until it's completed normally or exceptionally
        respFuture.whenComplete((resp, e) -> {
            sample.stop(RPCMeters.timer(meterKey, RPCMetric.PipelineReqProcessTime));
            // untrack current response future
            if (e != null) {
                log.trace("Finished request handling with error in pipeline@{}: request={}, error={}",
                    this.hashCode(), request, e.getMessage());
                RPCMeters.recordCount(meterKey, RPCMetric.PipelineReqFailCount);
                // any handling exception will cause pipeline close
                fail(e);
            } else {
                log.trace("Finished request handling in pipeline@{}: request={}", hashCode(), request);
                RPCMeters.recordCount(meterKey, RPCMetric.PipelineReqFulfillCount);
            }
        });
        return respFuture;
    }


    final void emitResponse(RequestT req, ResponseT resp) {
        if (!isClosed()) {
            log.trace("Response sent in pipeline@{}: request={}, response={}", hashCode(), req, resp);
            // the sendResponse is guaranteed to run in sequential, no need to synchronize
            responseObserver.onNext(resp);
        } else {
            log.trace("Response dropped due to pipeline@{} has been closed: request={}, response={}",
                hashCode(), req, resp);
        }
    }

    protected void afterClose() {
    }


    private void fail(Throwable throwable) {
        if (!isClosed()) {
            if (throwable instanceof CancellationException) {
                log.trace("Pipeline@{} closed due to request handler being canceled", hashCode(), throwable);
                close(Status.CANCELLED.asRuntimeException());
            } else {
                log.trace("Pipeline@{} closed due to request handler failure", hashCode(), throwable);
                close(Status.UNAVAILABLE.asRuntimeException());
            }
        } else {
            log.trace("Pipeline@{} has been closed", hashCode(), throwable);
        }
    }

    private void cleanup() {
        afterClose();
        // stop all on-going response futures if any to free current ResponsePipeline up
        futureTracker.stop();
    }
}