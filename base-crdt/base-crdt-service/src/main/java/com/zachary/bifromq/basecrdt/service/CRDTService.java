package com.zachary.bifromq.basecrdt.service;

import com.zachary.bifromq.basecluster.IAgentHost;
import com.zachary.bifromq.basecrdt.core.api.ICausalCRDT;
import com.zachary.bifromq.basecrdt.proto.Replica;
import com.zachary.bifromq.basecrdt.store.ICRDTStore;
import com.zachary.bifromq.basecrdt.store.proto.CRDTStoreMessage;
import com.zachary.bifromq.baseenv.EnvProvider;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Long.toUnsignedString;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

@Slf4j
public class CRDTService implements ICRDTService {

    private enum State {
        INIT, STARTING, STARTED, STOPPING, SHUTDOWN
    }

    private final ICRDTStore store;
    private IAgentHost agentHost;
    private final AtomicReference<State> state = new AtomicReference<>(State.INIT);
    private final Map<String, CRDTContext> hostedCRDT = Maps.newConcurrentMap(); // key is the uri of crdt
    private final Subject<CRDTStoreMessage> incomingStoreMessages;
    private final ExecutorService executor =
        newSingleThreadExecutor(EnvProvider.INSTANCE.newThreadFactory("crdt-service-scheduler"));
    private final Scheduler scheduler = Schedulers.from(executor);


    public CRDTService(CRDTServiceOptions options) {
        store = ICRDTStore.newInstance(options.storeOptions);
        incomingStoreMessages = PublishSubject.<CRDTStoreMessage>create().toSerialized();
    }

    @Override
    public long id() {
        return store.id();
    }

    @Override
    public Replica host(String uri) {
        checkState();
        CRDTContext crdtContext = hostedCRDT.computeIfAbsent(uri,
            k -> new CRDTContext(k, store, agentHost, scheduler, incomingStoreMessages));
        return crdtContext.id();
    }

    @Override
    public Optional<ICausalCRDT> get(String uri) {
        checkState();
        return Optional.ofNullable(hostedCRDT.get(uri).crdt());
    }

    @Override
    public CompletableFuture<Void> stopHosting(String uri) {
        checkState();
        assert hostedCRDT.containsKey(uri);
        return stopHostingInternal(uri);
    }

    @Override
    public Observable<Set<Replica>> aliveReplicas(String uri) {
        checkState();
        return hostedCRDT.get(uri).aliveReplicas();
    }

    @Override
    public boolean isStarted() {
        return state.get() == State.STARTED;
    }

    private CompletableFuture<Void> stopHostingInternal(String uri) {
        return hostedCRDT.remove(uri).close();
    }

    @Override
    public void start(IAgentHost agentHost) {
        if (state.compareAndSet(State.INIT, State.STARTING)) {
            this.agentHost = agentHost;
            store.start(incomingStoreMessages);
            state.set(State.STARTED);
            log.debug("Started CRDT service[{}]", toUnsignedString(store.id()));
        }
    }

    @Override
    public void stop() {
        if (state.compareAndSet(State.STARTED, State.STOPPING)) {
            log.info("Stopping CRDT service[{}]", id());
            log.debug("Stop hosting CRDTs");
            CompletableFuture.allOf(hostedCRDT.values()
                    .stream()
                    .map(CRDTContext::close)
                    .toArray(CompletableFuture[]::new))
                .join();
            log.debug("Stopping CRDT store");
            store.stop();
            log.info("CRDT service[{}] stopped", id());
            executor.shutdown();
            state.set(State.SHUTDOWN);
        }
    }

    private void checkState() {
        Preconditions.checkState(state.get() == State.STARTED, "Not started");
    }
}
