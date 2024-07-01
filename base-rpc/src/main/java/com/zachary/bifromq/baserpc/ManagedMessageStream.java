package com.zachary.bifromq.baserpc;

import com.zachary.bifromq.baserpc.exception.RequestRejectedException;
import com.zachary.bifromq.baserpc.exception.ServiceUnavailableException;
import com.zachary.bifromq.baserpc.metrics.RPCMeters;
import com.zachary.bifromq.baserpc.metrics.RPCMetric;
import com.zachary.bifromq.baserpc.utils.Backoff;
import com.zachary.bifromq.baserpc.utils.BehaviorSubject;
import io.grpc.CallOptions;
import io.grpc.Context;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;


@Slf4j
class ManagedMessageStream<MsgT, AckT> implements IRPCClient.IMessageStream<MsgT, AckT> {

    private enum State {
        Normal,
        ServiceUnavailable,
        Closed
    }

    private final AtomicReference<State> state = new AtomicReference<>(State.Normal);
    private final ConcurrentLinkedQueue<AckT> ackSendingBuffers;
    private final PublishSubject<MsgT> msgSubject = PublishSubject.create();
    private final RPCMeters.MeterKey meterKey;
    private final String tenantId;
    private final String wchKey;
    private final Supplier<Map<String, String>> metadataSupplier;
    private final BluePrint.MethodSemantic<MsgT> semantic;
    private final MethodDescriptor<AckT, MsgT> methodDescriptor;
    private final BluePrint bluePrint;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final BehaviorSubject<Long> signal = BehaviorSubject.createDefault(System.nanoTime());
    private final RPCClient.ChannelHolder channelHolder;
    private final CallOptions callOptions;
    private final AtomicReference<ClientCallStreamObserver<AckT>> requester = new AtomicReference<>();
    private final AtomicReference<String> desiredServerId = new AtomicReference<>();
    private final AtomicReference<String> selectedServerId = new AtomicReference<>();
    // traffic control
    private final AtomicBoolean sending = new AtomicBoolean(false);

    private final Backoff retargetBackoff = new Backoff(5, 10, 60000);

    ManagedMessageStream(
            String tenantId,
            @Nullable String wchKey,
            @Nullable String targetServerId,
            Supplier<Map<String, String>> metadataSupplier,
            String serviceUniqueName,
            RPCClient.ChannelHolder channelHolder,
            CallOptions callOptions,
            MethodDescriptor<AckT, MsgT> methodDescriptor,
            BluePrint bluePrint) {
        assert methodDescriptor.getType() == MethodDescriptor.MethodType.BIDI_STREAMING;
        this.bluePrint = bluePrint;
        semantic = bluePrint.semantic(methodDescriptor.getFullMethodName());
        assert semantic instanceof BluePrint.Streaming;
        if (semantic instanceof BluePrint.DDBalanced) {
            assert targetServerId != null;
            this.desiredServerId.set(targetServerId);
        } else if (semantic instanceof BluePrint.WCHBalanced) {
            assert wchKey != null;
        }
        this.tenantId = tenantId;
        this.wchKey = wchKey;
        this.metadataSupplier = metadataSupplier;
        this.meterKey = RPCMeters.MeterKey.builder()
                .service(serviceUniqueName)
                .method(methodDescriptor.getBareMethodName())
                .tenantId(tenantId)
                .build();
        ackSendingBuffers = new ConcurrentLinkedQueue<>();
        this.methodDescriptor = methodDescriptor;
        this.channelHolder = channelHolder;
        this.callOptions = callOptions;
        // react to CHash ring change
        disposables.add(Observable.combineLatest(channelHolder.serverSelectorObservable()
                        // reset backoff when new selector available
                        .doOnNext(s -> retargetBackoff.reset()), signal, (s, t) -> s)
                .subscribeOn(Schedulers.from(channelHolder.rpcExecutor()))
                .subscribe(selector -> {
                    synchronized (this) {
                        if (state.get() == State.Closed) {
                            return;
                        }
                        if (semantic instanceof BluePrint.DDBalanced) {
                            boolean available = selector.direct(tenantId, desiredServerId.get(),
                                    methodDescriptor);
                            if (available) {
                                state.set(State.Normal);
                                if (selectedServerId.get() == null) {
                                    log.debug("MsgStream@{} targeting to server[{}]",
                                            this.hashCode(), desiredServerId.get());
                                    target();
                                } else {
                                    assert desiredServerId.get().equals(selectedServerId.get());
                                }
                            } else {
                                state.set(State.ServiceUnavailable);
                                if (selectedServerId.get() != null) {
                                    log.debug("MsgStream@{} stop targeting to server[{}]",
                                            this.hashCode(), selectedServerId.get());
                                    requester.getAndSet(null).onCompleted();
                                    selectedServerId.set(null);
                                }
                            }
                        } else if (semantic instanceof BluePrint.WCHBalanced) {
                            Optional<String> newServer = selector.hashing(tenantId, wchKey, methodDescriptor);
                            if (newServer.isEmpty()) {
                                state.set(State.ServiceUnavailable);
                                if (selectedServerId.get() != null) {
                                    log.debug("MsgStream@{} stop targeting to server[{}]",
                                            this.hashCode(), selectedServerId.get());
                                    desiredServerId.set(null);
                                    if (selectedServerId.get() != null) {
                                        requester.getAndSet(null).onCompleted();
                                        selectedServerId.set(null);
                                    }
                                }
                            } else {
                                state.set(State.Normal);
                                if (!newServer.get().equals(desiredServerId.get())) {
                                    log.debug("MsgStream@{} retargeting to server[{}] from server[{}]",
                                            this.hashCode(), newServer.get(), selectedServerId.get());
                                    desiredServerId.set(newServer.get());
                                    if (selectedServerId.get() != null) {
                                        requester.getAndSet(null).onCompleted();
                                        selectedServerId.set(null);
                                    } else {
                                        target();
                                    }
                                } else if (!desiredServerId.get().equals(selectedServerId.get())) {
                                    target();
                                }
                            }
                        } else if (semantic instanceof BluePrint.WRBalanced) {
                            Optional<String> newServer = selector.random(tenantId, methodDescriptor);
                            if (newServer.isEmpty()) {
                                state.set(State.ServiceUnavailable);
                                if (selectedServerId.get() != null) {
                                    log.debug("MsgStream@{} stop targeting to server[{}]",
                                            this.hashCode(), selectedServerId.get());
                                    requester.getAndSet(null).onCompleted();
                                    selectedServerId.set(null);
                                }
                            } else {
                                state.set(State.Normal);
                                if (selectedServerId.get() == null) {
                                    target();
                                }
                            }
                        } else if (semantic instanceof BluePrint.WRRBalanced) {
                            Optional<String> newServer = selector.roundRobin(tenantId, methodDescriptor);
                            if (newServer.isEmpty()) {
                                state.set(State.ServiceUnavailable);
                                if (selectedServerId.get() != null) {
                                    log.debug("MsgStream@{} stop targeting to server[{}]",
                                            this.hashCode(), selectedServerId.get());
                                    requester.getAndSet(null).onCompleted();
                                    selectedServerId.set(null);
                                }
                            } else {
                                state.set(State.Normal);
                                if (selectedServerId.get() == null) {
                                    target();
                                }
                            }
                        }
                    }
                }));
        RPCMeters.recordCount(meterKey, RPCMetric.MsgStreamCreateCount);
    }

    @Override
    public boolean isClosed() {
        return state.get() == State.Closed;
    }

    @Override
    public void ack(AckT ack) {
        switch (state.get()) {
            case Normal:
                ackSendingBuffers.offer(ack);
                // check if pipeline is still open
                sendUntilStreamNotReadyOrNoTask();
                RPCMeters.recordCount(meterKey, RPCMetric.StreamAckAcceptCount);
                break;
            case ServiceUnavailable:
                throw new ServiceUnavailableException("Service unavailable");
            case Closed:
                // pipeline has already closed, finish it with close reason
                throw new RequestRejectedException("Pipeline has closed");
        }
    }

    @Override
    public Observable<MsgT> msg() {
        return msgSubject;
    }

    @Override
    public void close() {
        state.set(State.Closed);
        ackSendingBuffers.clear();
        disposables.dispose();
        msgSubject.onComplete();
        ClientCallStreamObserver<AckT> r = requester.getAndSet(null);
        if (r != null) {
            r.onCompleted();
        }
    }

    private void target() {
        // currently, context attributes from caller are not allowed
        // start a new context by forking from ROOT,so no scaring warning should appear in the log
        Context ctx = Context.ROOT.fork()
                .withValue(RPCContext.TENANT_ID_CTX_KEY, tenantId)
                .withValue(RPCContext.SELECTED_SERVER_ID_CTX_KEY, new RPCContext.ServerSelection())
                .withValue(RPCContext.CUSTOM_METADATA_CTX_KEY, metadataSupplier.get());
        if (semantic instanceof BluePrint.DDBalanced) {
            ctx = ctx.withValue(RPCContext.DESIRED_SERVER_ID_CTX_KEY, desiredServerId.get());
        } else if (semantic instanceof BluePrint.WCHBalanced) {
            ctx = ctx.withValue(RPCContext.WCH_HASH_KEY_CTX_KEY, wchKey);
        }
        ctx.run(() -> {
            log.trace("MsgStream@{} creating request stream", hashCode());
            ClientCallStreamObserver<AckT> reqStream = (ClientCallStreamObserver<AckT>)
                    asyncBidiStreamingCall(channelHolder.channel()
                            .newCall(bluePrint.methodDesc(methodDescriptor.getFullMethodName(),
                                            channelHolder.inProc()),
                                    callOptions), new ResponseObserver());
            if (RPCContext.SELECTED_SERVER_ID_CTX_KEY.get().getServerId() != null) {
                requester.set(reqStream);
                log.trace("MsgStream@{} request stream@{} created", hashCode(), reqStream.hashCode());
                selectedServerId.set(RPCContext.SELECTED_SERVER_ID_CTX_KEY.get().getServerId());
                sendUntilStreamNotReadyOrNoTask();
            } else {
                log.trace("MsgStream@{} retry request stream creation in 5 seconds", hashCode());
                scheduleSignal(5, TimeUnit.SECONDS);
            }
        });
    }

    private void scheduleSignal(long delay, TimeUnit timeUnit) {
        Observable.timer(delay, timeUnit).subscribe(t -> signal.onNext(System.nanoTime()));
    }

    private void scheduleSignal() {
        scheduleSignal(retargetBackoff.backoff(), TimeUnit.MILLISECONDS);
    }

    private void sendUntilStreamNotReadyOrNoTask() {
        if (sending.compareAndSet(false, true)) {
            synchronized (this) {
                ClientCallStreamObserver<AckT> requestStream = requester.get();
                if (requestStream == null) {
                    sending.set(false);
                    return;
                }
                while (requestStream.isReady() && !ackSendingBuffers.isEmpty()) {
                    AckT ack = ackSendingBuffers.poll();
                    requestStream.onNext(ack);
                    RPCMeters.recordCount(meterKey, RPCMetric.StreamAckSendCount);
                }
                sending.set(false);
                if (requestStream.isReady() && !ackSendingBuffers.isEmpty()) {
                    // deal with the spurious notification
                    sendUntilStreamNotReadyOrNoTask();
                }
            }
        }
    }

    private class ResponseObserver implements ClientResponseObserver<AckT, MsgT> {
        private ClientCallStreamObserver<AckT> requestStream;

        @Override
        public void beforeStart(ClientCallStreamObserver<AckT> requestStream) {
            this.requestStream = requestStream;
            requestStream.setOnReadyHandler(() -> {
                if (requestStream == requester.get()) {
                    sendUntilStreamNotReadyOrNoTask();
                }
            });
        }

        @Override
        public void onNext(MsgT resp) {
            ClientCallStreamObserver<AckT> currentRequestStream = requester.get();
            if (currentRequestStream == null || currentRequestStream == requestStream) {
                msgSubject.onNext(resp);
                RPCMeters.recordCount(meterKey, RPCMetric.StreamMsgReceiveCount);
            } else {
                log.debug("Drop response from orphan stream");
                throw new IllegalStateException();
            }
        }

        @Override
        public void onError(Throwable throwable) {
            if (throwable.getCause() instanceof IllegalStateException) {
                // orphan stream
                return;
            }
            log.trace("MsgStream@{} internal stream@{} error: state={}",
                    ManagedMessageStream.this.hashCode(), requestStream.hashCode(), state.get(), throwable);
            RPCMeters.recordCount(meterKey, RPCMetric.MsgStreamErrorCount);
            this.onCompleted();
        }

        @Override
        public void onCompleted() {
            log.trace("MsgStream@{} internal stream@{} completed",
                    ManagedMessageStream.this.hashCode(), requestStream.hashCode());
            synchronized (ManagedMessageStream.this) {
                if (requestStream.isReady()) {
                    requestStream.onCompleted();
                }
                if (!requester.compareAndSet(requestStream, null)) {
                    // don't schedule target if the completed one is not the active one
                    return;
                }
                if (state.get() != State.Normal) {
                    return;
                }
                if (semantic instanceof BluePrint.DDBalanced) {
                    if (selectedServerId.get() != null) {
                        log.trace("MsgStream@{} schedule targeting to server[{}]",
                                ManagedMessageStream.this.hashCode(), selectedServerId.get());
                        selectedServerId.set(null);
                        scheduleSignal();
                    }
                } else if (semantic instanceof BluePrint.WCHBalanced) {
                    if (desiredServerId.get() != null) {
                        log.trace("MsgStream@{} schedule targeting to server[{}]",
                                ManagedMessageStream.this.hashCode(), desiredServerId.get());
                        selectedServerId.set(null);
                        scheduleSignal();
                    }
                } else if (semantic instanceof BluePrint.WRBalanced) {
                    if (selectedServerId.get() != null) {
                        log.trace("MsgStream@{} schedule targeting to random server",
                                ManagedMessageStream.this.hashCode());
                        selectedServerId.set(null);
                        scheduleSignal();
                    }
                } else if (semantic instanceof BluePrint.WRRBalanced) {
                    if (selectedServerId.get() != null) {
                        log.trace("MsgStream@{} schedule targeting to next server",
                                ManagedMessageStream.this.hashCode());
                        selectedServerId.set(null);
                        scheduleSignal();
                    }
                }
            }
        }
    }
}
