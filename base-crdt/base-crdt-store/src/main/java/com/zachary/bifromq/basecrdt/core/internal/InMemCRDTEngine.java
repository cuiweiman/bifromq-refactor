
package com.zachary.bifromq.basecrdt.core.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.CRDTEngineOptions;
import com.zachary.bifromq.basecrdt.core.api.CRDTURI;
import com.zachary.bifromq.basecrdt.core.api.ICRDTEngine;
import com.zachary.bifromq.basecrdt.core.api.ICausalCRDT;
import com.zachary.bifromq.basecrdt.core.exception.CRDTEngineException;
import com.zachary.bifromq.basecrdt.proto.Replacement;
import com.zachary.bifromq.basecrdt.proto.Replica;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Long.toUnsignedString;

/**
 * @description: InMemCRDTEngine
 * @author: cuiweiman
 * @date: 2024/4/20 18:41
 */
@Slf4j
public final class InMemCRDTEngine implements ICRDTEngine {

    private enum State {
        /**
         * 已创建, 运行中, 停止中, 已关闭
         */
        CREATED, RUNNING, STOPPING, SHUTDOWN
    }

    private final long id;
    private final AtomicReference<State> status = new AtomicReference<>(State.CREATED);

    private final AtomicLong seqNo = new AtomicLong();
    private final Map<String, CausalCRDTInflater> uriToCRDTInflater = Maps.newConcurrentMap();
    private final ScheduledExecutorService inflationExecutor;
    private final CRDTEngineOptions options;

    public InMemCRDTEngine(CRDTEngineOptions options) {
        this.options = options;
        this.id = options.id();
        inflationExecutor = options.inflationExecutor() != null ?
                options.inflationExecutor() : SharedInflationExecutor.getInstance();
    }

    @Override
    public long id() {
        return id;
    }

    @Override
    public Iterator<Replica> hosting() {
        checkServingState();
        return Iterators.transform(uriToCRDTInflater.values().iterator(), CausalCRDTInflater::id);
    }

    @Override
    public Replica host(String crdtURI) {
        return host(crdtURI, replicaAddr(id, seqNo.incrementAndGet()));
    }

    @Override
    public Replica host(String crdtURI, ByteString replicaId) {
        checkServingState();
        CRDTURI.checkURI(crdtURI);
        CausalCRDTInflater crdt = uriToCRDTInflater.computeIfAbsent(crdtURI, key -> {
            Replica replica = Replica.newBuilder().setUri(crdtURI).setId(replicaId).build();
            IReplicaStateLattice lattice = new InMemReplicaStateLattice(replicaId,
                    options.orHistoryExpireTime(), options.maxCompactionTime());

            return switch (CRDTURI.parseType(crdtURI)) {
                case aworset ->
                        new AWORSetInflater(id, replica, lattice, inflationExecutor, options.inflationInterval());
                case rworset ->
                        new RWORSetInflater(id, replica, lattice, inflationExecutor, options.inflationInterval());
                case ormap -> new ORMapInflater(id, replica, lattice, inflationExecutor, options.inflationInterval());
                case cctr -> new CCounterInflater(id, replica, lattice, inflationExecutor, options.inflationInterval());
                case dwflag -> new DWFlagInflater(id, replica, lattice, inflationExecutor, options.inflationInterval());
                case ewflag -> new EWFlagInflater(id, replica, lattice, inflationExecutor, options.inflationInterval());
                case mvreg -> new MVRegInflater(id, replica, lattice, inflationExecutor, options.inflationInterval());
                default -> throw new UnsupportedOperationException();
            };
        });
        if (!crdt.id().getId().equals(replicaId)) {
            log.warn("Replica with id[{}] already host for URI[{}]", crdt.id().getId(), crdtURI);
        }
        return crdt.id();
    }

    @Override
    public CompletableFuture<Void> stopHosting(String crdtURI) {
        checkServingState();
        CRDTURI.checkURI(crdtURI);
        CausalCRDTInflater inflater = uriToCRDTInflater.remove(crdtURI);
        if (inflater != null) {
            return inflater.stop();
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public <T extends ICausalCRDT> Optional<T> get(String crdtURI) {
        checkServingState();
        CausalCRDTInflater inflater = uriToCRDTInflater.get(crdtURI);
        if (inflater != null) {
            return Optional.of((T) inflater.getCRDT());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Map<ByteString, NavigableMap<Long, Long>>> latticeEvents(String crdtURI) {
        checkServingState();
        CausalCRDTInflater inflater = uriToCRDTInflater.get(crdtURI);
        if (inflater != null) {
            return Optional.of(inflater.latticeEvents());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Map<ByteString, NavigableMap<Long, Long>>> historyEvents(String crdtURI) {
        checkServingState();
        CausalCRDTInflater inflater = uriToCRDTInflater.get(crdtURI);
        if (inflater != null) {
            return Optional.of(inflater.historyEvents());
        }
        return Optional.empty();

    }

    @Override
    public CompletableFuture<Void> join(String crdtURI, Iterable<Replacement> delta) {
        checkServingState();
        CausalCRDTInflater crdt = uriToCRDTInflater.get(crdtURI);
        if (crdt != null) {
            return crdt.join(delta);
        }
        return CompletableFuture.failedFuture(CRDTEngineException.CRDT_NOT_FOUND);
    }

    @Override
    public CompletableFuture<Optional<Iterable<Replacement>>>
    delta(String crdtURI, Map<ByteString, NavigableMap<Long, Long>> coveredLatticeEvents,
          Map<ByteString, NavigableMap<Long, Long>> coveredHistoryEvents, int maxEvents) {
        checkServingState();
        CausalCRDTInflater crdt = uriToCRDTInflater.get(crdtURI);
        if (crdt != null) {
            return crdt.delta(coveredLatticeEvents, coveredHistoryEvents, maxEvents);
        }
        return CompletableFuture.completedFuture(Optional.empty());
    }

    @Override
    public void start() {
        if (status.compareAndSet(State.CREATED, State.RUNNING)) {
            log.debug("Start CRDTEngine[{}]", toUnsignedString(id));
        }
    }

    @Override
    public void stop() {
        if (status.compareAndSet(State.RUNNING, State.STOPPING)) {
            log.debug("Shutting down CRDTEngine[{}]", toUnsignedString(id));
            log.debug("Stop all hosted CRDTs[{}]", toUnsignedString(id));
            stopAllCRDTs();
            uriToCRDTInflater.clear();
            status.set(State.SHUTDOWN);
            log.debug("CRDTEngine[{}] is terminated", toUnsignedString(id));
        }
    }

    private void stopAllCRDTs() {
        List<CompletableFuture<Void>> stopFutures = new ArrayList<>();
        uriToCRDTInflater.forEach((uri, inflater) -> stopFutures.add(inflater.stop()));
        CompletableFuture.allOf(stopFutures.toArray(new CompletableFuture[]{})).join();
    }

    private static ByteString replicaAddr(long hostId, long rId) {
        return ProtoUtils.toByteString(hostId).concat(Varint.encodeLong(rId));
    }

    private void checkServingState() {
        Preconditions.checkState(status.get() == State.RUNNING, "Not started");
    }
}
