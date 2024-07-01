package com.zachary.bifromq.baserpc;

import com.zachary.bifromq.baserpc.metrics.RPCMeters;
import com.zachary.bifromq.baserpc.metrics.RPCMetric;
import io.grpc.stub.StreamObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AckStream<AckT, MsgT> extends AbstractStreamObserver<AckT, MsgT> {
    private final PublishSubject<AckT> ackSubject = PublishSubject.create();
    private final AtomicBoolean closed = new AtomicBoolean();

    protected AckStream(StreamObserver<MsgT> responseObserver) {
        super(responseObserver);
    }

    public Observable<AckT> ack() {
        return ackSubject;
    }

    public boolean send(MsgT message) {
        if (responseObserver.isReady()) {
            responseObserver.onNext(message);
            RPCMeters.recordCount(meterKey, RPCMetric.StreamMsgSendCount);
            return true;
        }
        return false;
    }

    public void close() {
        if (closed.compareAndSet(false, true)) {
            ackSubject.onComplete();
            responseObserver.onCompleted();
        }
    }

    @Override
    public final void onNext(AckT value) {
        RPCMeters.recordCount(meterKey, RPCMetric.StreamAckReceiveCount);
        ackSubject.onNext(value);
    }

    @Override
    public final void onError(Throwable t) {
        this.onCompleted();
    }

    @Override
    public final void onCompleted() {
        close();
    }
}
