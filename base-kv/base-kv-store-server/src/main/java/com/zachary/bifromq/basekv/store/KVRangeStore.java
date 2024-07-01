package com.zachary.bifromq.basekv.store;

import com.zachary.bifromq.basehlc.HLC;
import com.zachary.bifromq.basekv.localengine.IKVEngine;
import com.zachary.bifromq.basekv.localengine.KVEngineFactory;
import com.zachary.bifromq.basekv.proto.EnsureRange;
import com.zachary.bifromq.basekv.proto.EnsureRangeReply;
import com.zachary.bifromq.basekv.proto.KVRangeDescriptor;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.KVRangeMessage;
import com.zachary.bifromq.basekv.proto.KVRangeSnapshot;
import com.zachary.bifromq.basekv.proto.KVRangeStoreDescriptor;
import com.zachary.bifromq.basekv.proto.State;
import com.zachary.bifromq.basekv.proto.StoreMessage;
import com.zachary.bifromq.basekv.raft.proto.ClusterConfig;
import com.zachary.bifromq.basekv.raft.proto.Snapshot;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProcFactory;
import com.zachary.bifromq.basekv.store.exception.KVRangeStoreException;
import com.zachary.bifromq.basekv.store.option.KVRangeStoreOptions;
import com.zachary.bifromq.basekv.store.range.IKVRange;
import com.zachary.bifromq.basekv.store.range.KVRange;
import com.zachary.bifromq.basekv.store.stats.IStatsCollector;
import com.zachary.bifromq.basekv.store.util.AsyncRunner;
import com.zachary.bifromq.basekv.store.wal.KVRangeWALStorageEngine;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.zachary.bifromq.basekv.utils.KeyRangeUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.zachary.bifromq.basekv.Constants.EMPTY_RANGE;
import static com.zachary.bifromq.basekv.Constants.FULL_RANGE;
import static com.zachary.bifromq.basekv.localengine.IKVEngine.DEFAULT_NS;
import static com.zachary.bifromq.basekv.proto.State.StateType.Normal;
import static com.zachary.bifromq.basekv.store.exception.KVRangeStoreException.RANGE_NOT_FOUND;
import static com.zachary.bifromq.basekv.utils.KVRangeIdUtil.toShortString;
import static java.util.Collections.emptyList;

@Slf4j
public class KVRangeStore implements IKVRangeStore {
    private enum Status {
        INIT, // store is created but cannot serve requests
        STARTING, // store is starting
        STARTED, // store can serve requests
        FATAL_FAILURE, // fatal failure happened during starting
        CLOSING, // store closing, no more outgoing messages
        CLOSED, // store closed, no tasks running
        TERMINATING, // releasing all resources
        TERMINATED // resource released
    }

    private final String id;
    private final AtomicReference<Status> status = new AtomicReference<>(Status.INIT);
    private final Map<KVRangeId, IKVRange> kvRangeMap = Maps.newConcurrentMap();
    private final Set<IKVRange> quitRanges = Sets.newConcurrentHashSet();
    private final Subject<List<Observable<KVRangeDescriptor>>> descriptorListSubject =
        BehaviorSubject.<List<Observable<KVRangeDescriptor>>>create().toSerialized();
    private final IKVRangeCoProcFactory coProcFactory;
    private final KVRangeWALStorageEngine walStorageEngine;
    private final IKVEngine kvRangeEngine;
    private final IStatsCollector storeStatsCollector;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final Executor queryExecutor;
    private final Executor mutationExecutor;
    private final ScheduledExecutorService tickExecutor;
    private volatile ScheduledFuture<?> tickFuture;
    private final ScheduledExecutorService bgTaskExecutor;
    private final AsyncRunner rangeMgmtTaskRunner;
    private final KVRangeStoreOptions opts;
    private IStoreMessenger messenger;

    public KVRangeStore(KVRangeStoreOptions opts,
                        IKVRangeCoProcFactory coProcFactory,
                        @NonNull Executor queryExecutor,
                        @NonNull Executor mutationExecutor,
                        @NonNull ScheduledExecutorService tickExecutor,
                        @NonNull ScheduledExecutorService bgTaskExecutor) {
        this.coProcFactory = coProcFactory;
        this.opts = opts.toBuilder().build();
        this.walStorageEngine = new KVRangeWALStorageEngine(
            opts.getOverrideIdentity(), this.opts.getWalFlushBufferSize(), opts.getWalEngineConfigurator());
        id = walStorageEngine.id();
        if (opts.getOverrideIdentity() != null
            && !opts.getOverrideIdentity().trim().isEmpty()
            && !opts.getOverrideIdentity().equals(id)) {
            log.warn("KVRangeStore has been initialized with identity[{}], the override[{}] is ignored",
                id, opts.getOverrideIdentity());
        }
        kvRangeEngine = KVEngineFactory.create(null, Arrays.asList(DEFAULT_NS),
            this::check, opts.getDataEngineConfigurator());
        this.queryExecutor = queryExecutor;
        this.mutationExecutor = mutationExecutor;
        this.tickExecutor = tickExecutor;
        this.bgTaskExecutor = bgTaskExecutor;
        this.rangeMgmtTaskRunner = new AsyncRunner(this.bgTaskExecutor);
        storeStatsCollector =
            new KVRangeStoreStatsCollector(opts, Duration.ofSeconds(opts.getStatsCollectIntervalSec()),
                this.bgTaskExecutor);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public boolean isStarted() {
        return status.get() == Status.STARTED;
    }

    @Override
    public void start(IStoreMessenger messenger) {
        if (status.compareAndSet(Status.INIT, Status.STARTING)) {
            try {
                this.messenger = messenger;
                walStorageEngine.start(bgTaskExecutor);
                kvRangeEngine.start(bgTaskExecutor, "storeId", id, "type", "data");
                log.debug("KVRangeStore[{}] started", id);
                status.set(Status.STARTED);
                disposable.add(messenger.receive().subscribe(this::receive));
                loadExisting();
                scheduleTick(0);
            } catch (Throwable e) {
                status.set(Status.FATAL_FAILURE);
                throw new KVRangeStoreException("Failed to start kv range store", e);
            }
        } else {
            log.warn("KVRangeStore cannot start in {} status", status.get().name());
        }
    }

    private void loadExisting() {
        rangeMgmtTaskRunner.add(() -> {
            walStorageEngine.allKVRangeIds()
                .forEach(kvRangeId -> kvRangeMap.computeIfAbsent(kvRangeId, key -> initKVRange(key, null)));
            updateDescriptorList();
        }).toCompletableFuture().join();
    }

    @Override
    public void stop() {
        if (status.compareAndSet(Status.STARTED, Status.CLOSING)) {
            try {
                log.debug("Stop KVRange store[{}]", id);
                CompletableFuture.allOf(kvRangeMap.values().stream()
                        .map(IKVRange::close)
                        .toArray(CompletableFuture[]::new))
                    .join();
                disposable.dispose();
                storeStatsCollector.stop().toCompletableFuture().join();
                rangeMgmtTaskRunner.awaitDone().toCompletableFuture().join();
                log.debug("Stopping WAL Engine");
                walStorageEngine.stop();
                log.debug("Stopping KVRange Engine");
                kvRangeEngine.stop();
                descriptorListSubject.onComplete();
                status.set(Status.CLOSED);
                status.set(Status.TERMINATING);
                tickFuture.get();
            } catch (Throwable e) {
                log.error("Error occurred during stopping range store", e);
            } finally {
                messenger.close();
                status.set(Status.TERMINATED);
            }
        }
    }

    @Override
    public boolean bootstrap() {
        checkStarted();
        if (kvRangeMap.isEmpty()) {
            // build the genesis "full" KVRange in the cluster
            KVRangeId genesisId = KVRangeIdUtil.generate();
            log.debug("Creating the genesis KVRange[{}]", toShortString(genesisId));
            ensureKVRange(genesisId, Snapshot.newBuilder()
                .setClusterConfig(ClusterConfig.newBuilder()
                    .addVoters(id())
                    .build())
                .setTerm(0)
                .setIndex(5)
                .setData(KVRangeSnapshot.newBuilder()
                    .setId(genesisId)
                    .setVer(0)
                    .setLastAppliedIndex(5)
                    .setState(State.newBuilder().setType(Normal).build())
                    .setRange(FULL_RANGE)
                    .build().toByteString())
                .build()).toCompletableFuture().join();
            return true;
        }
        return false;
    }

    @Override
    public CompletionStage<Void> recover() {
        checkStarted();
        return rangeMgmtTaskRunner.add(() -> CompletableFuture.allOf(kvRangeMap.values().stream()
            .map(kvRange -> kvRange.recover().handle((v, e) -> {
                if (e != null) {
                    log.warn("KVRange[{}] recover failed for some reason",
                        KVRangeIdUtil.toString(kvRange.id()), e);
                }
                return null;
            }).toCompletableFuture())
            .toArray(CompletableFuture[]::new)));
    }

    @Override
    public Observable<KVRangeStoreDescriptor> describe() {
        checkStarted();
        return descriptorListSubject
            .distinctUntilChanged()
            .switchMap(descriptorList -> {
                Observable<List<KVRangeDescriptor>> descListObservable = descriptorList.isEmpty() ?
                    BehaviorSubject.createDefault(emptyList()) :
                    Observable.combineLatest(descriptorList, descs ->
                        Arrays.stream(descs).map(desc -> (KVRangeDescriptor) desc).collect(Collectors.toList()));
                return Observable.combineLatest(storeStatsCollector.collect(), descListObservable,
                    (storeStats, descList) -> KVRangeStoreDescriptor.newBuilder()
                        .setId(id)
                        .putAllStatistics(storeStats)
                        .addAllRanges(descList)
                        .setHlc(HLC.INST.get())
                        .build());
            })
            .distinctUntilChanged();
    }

    private void receive(StoreMessage storeMessage) {
        if (status.get() == Status.STARTED) {
            KVRangeMessage payload = storeMessage.getPayload();
            if (payload.hasEnsureRange()) {
                EnsureRange request = storeMessage.getPayload().getEnsureRange();
                ensureKVRange(payload.getRangeId(), request.getInitSnapshot())
                    .whenComplete((v, e) -> messenger.send(StoreMessage.newBuilder()
                        .setFrom(id)
                        .setSrcRange(payload.getRangeId())
                        .setPayload(KVRangeMessage.newBuilder()
                            .setRangeId(storeMessage.getSrcRange())
                            .setHostStoreId(storeMessage.getFrom())
                            .setEnsureRangeReply(EnsureRangeReply.newBuilder()
                                .setResult(EnsureRangeReply.Result.OK)
                                .build())
                            .build())
                        .build()));
            }
        }
    }

    @Override
    public CompletionStage<Void> transferLeadership(long ver, KVRangeId rangeId, String newLeader) {
        checkStarted();
        IKVRange kvRange = kvRangeMap.get(rangeId);
        if (kvRange != null) {
            return kvRange.transferLeadership(ver, newLeader);
        }
        return CompletableFuture.failedFuture(RANGE_NOT_FOUND);
    }

    @Override
    public CompletionStage<Void> changeReplicaConfig(long ver, KVRangeId rangeId,
                                                     Set<String> newVoters,
                                                     Set<String> newLearners) {
        checkStarted();
        IKVRange kvRange = kvRangeMap.get(rangeId);
        if (kvRange != null) {
            return kvRange.changeReplicaConfig(ver, newVoters, newLearners);
        }
        return CompletableFuture.failedFuture(RANGE_NOT_FOUND);
    }

    @Override
    public CompletionStage<Void> split(long ver, KVRangeId rangeId, ByteString splitKey) {
        checkStarted();
        IKVRange kvRange = kvRangeMap.get(rangeId);
        if (kvRange != null) {
            return kvRange.split(ver, splitKey);
        }
        return CompletableFuture.failedFuture(RANGE_NOT_FOUND);
    }

    @Override
    public CompletionStage<Void> merge(long ver, KVRangeId mergerId, KVRangeId mergeeId) {
        checkStarted();
        IKVRange kvRange = kvRangeMap.get(mergerId);
        if (kvRange != null) {
            return kvRange.merge(ver, mergeeId);
        }
        return CompletableFuture.failedFuture(RANGE_NOT_FOUND);
    }

    @Override
    public CompletionStage<Boolean> exist(long ver, KVRangeId id, ByteString key, boolean linearized) {
        checkStarted();
        IKVRange kvRange = kvRangeMap.get(id);
        if (kvRange != null) {
            return kvRange.exist(ver, key, linearized);
        }
        return CompletableFuture.failedFuture(RANGE_NOT_FOUND);
    }

    @Override
    public CompletionStage<Optional<ByteString>> get(long ver, KVRangeId id, ByteString key,
                                                     boolean linearized) {
        checkStarted();
        IKVRange kvRange = kvRangeMap.get(id);
        if (kvRange != null) {
            return kvRange.get(ver, key, linearized);
        }
        return CompletableFuture.failedFuture(RANGE_NOT_FOUND);
    }

    @Override
    public CompletionStage<ByteString> queryCoProc(long ver, KVRangeId id, ByteString query,
                                                   boolean linearized) {
        checkStarted();
        IKVRange kvRange = kvRangeMap.get(id);
        if (kvRange != null) {
            return kvRange.queryCoProc(ver, query, linearized);
        }
        return CompletableFuture.failedFuture(RANGE_NOT_FOUND);
    }

    @Override
    public CompletionStage<ByteString> put(long ver, KVRangeId id, ByteString key, ByteString value) {
        checkStarted();
        IKVRange kvRange = kvRangeMap.get(id);
        if (kvRange != null) {
            return kvRange.put(ver, key, value);
        }
        return CompletableFuture.failedFuture(RANGE_NOT_FOUND);
    }

    @Override
    public CompletionStage<ByteString> delete(long ver, KVRangeId id, ByteString key) {
        checkStarted();
        IKVRange kvRange = kvRangeMap.get(id);
        if (kvRange != null) {
            return kvRange.delete(ver, key);
        }
        return CompletableFuture.failedFuture(RANGE_NOT_FOUND);
    }

    @Override
    public CompletionStage<ByteString> mutateCoProc(long ver, KVRangeId id, ByteString mutate) {
        checkStarted();
        IKVRange kvRange = kvRangeMap.get(id);
        if (kvRange != null) {
            return kvRange.mutateCoProc(ver, mutate);
        }
        return CompletableFuture.failedFuture(RANGE_NOT_FOUND);
    }

    private void scheduleTick(long delayInMS) {
        tickFuture = tickExecutor.schedule(this::tick, delayInMS, TimeUnit.MILLISECONDS);
    }

    private void tick() {
        if (status.get() != Status.STARTED && status.get() != Status.CLOSING) {
            return;
        }
        kvRangeMap.forEach((v, r) -> {
            r.tick();
            if (r.readyToQuit()) {
                kvRangeMap.remove(v, r);
                quitKVRange(r);
            }
        });
        quitRanges.forEach(IKVRange::tick);
        storeStatsCollector.tick();
        scheduleTick(opts.getKvRangeOptions().getTickUnitInMS());
    }

    private CompletionStage<Void> ensureKVRange(KVRangeId rangeId, Snapshot snapshot) {
        return rangeMgmtTaskRunner.add(() -> addKVRange(rangeId, snapshot));
    }

    private void addKVRange(KVRangeId rangeId, Snapshot snapshot) {
        kvRangeMap.computeIfAbsent(rangeId, k -> {
            log.debug("Init range[{}] in store[{}]", toShortString(rangeId), id);
            return initKVRange(k, snapshot);
        });
        updateDescriptorList();
    }

    private void quitKVRange(IKVRange range) {
        quitRanges.add(range);
        rangeMgmtTaskRunner.add(() -> range.quit()
            .whenCompleteAsync((v, e) -> {
                if (e != null) {
                    quitKVRange(range);
                } else {
                    quitRanges.remove(range);
                    updateDescriptorList();
                }
            }, bgTaskExecutor));
    }

    private KVRange initKVRange(KVRangeId kvRangeId, Snapshot initSnapshot) {
        checkStarted();
        log.debug("Create range: storeId={}, rangeId={}", id, toShortString(kvRangeId));
        KVRange kvRange = new KVRange(kvRangeId, id,
            coProcFactory,
            this::checkSnapshot,
            kvRangeEngine,
            walStorageEngine,
            queryExecutor,
            mutationExecutor,
            bgTaskExecutor,
            opts.getKvRangeOptions(),
            initSnapshot);
        log.debug("Open range: storeId={}, rangeId={}", id, toShortString(kvRange.id()));
        kvRange.open(new KVRangeMessenger(id, kvRange.id(), messenger));
        return kvRange;
    }

    private void updateDescriptorList() {
        descriptorListSubject.onNext(kvRangeMap.values().stream().map(IKVRange::describe).collect(Collectors.toList()));
    }

    private CompletionStage<Void> checkSnapshot(KVRangeSnapshot snapshot) {
        return rangeMgmtTaskRunner.add(() -> {
                CompletableFuture<List<KVRangeDescriptor>> onDone = new CompletableFuture<>();
                List<KVRangeDescriptor> overlapped = kvRangeMap.values().stream()
                    .map(r -> r.describe().blockingFirst())
                    .filter(r -> KeyRangeUtil.isOverlap(r.getRange(), snapshot.getRange())
                        && !r.getId().equals(snapshot.getId()))
                    .collect(Collectors.toList());
                if (overlapped.stream().anyMatch(r -> r.getVer() > snapshot.getVer())) {
                    onDone.completeExceptionally(new KVRangeStoreException("Staled snapshot"));
                } else {
                    onDone.complete(overlapped);
                    // destroy overlapped staled range
                }
                return onDone;
            })
            .thenCompose(overlapped -> rangeMgmtTaskRunner.add(() ->
                CompletableFuture.allOf(overlapped.stream()
                        .map(r -> kvRangeMap.remove(r.getId()).destroy())
                        .toArray(CompletableFuture[]::new))
                    .thenApply(v -> overlapped)))
            .thenCompose(overlapped -> {
                if (overlapped.isEmpty()) {
                    return CompletableFuture.completedFuture(null);
                }
                return rangeMgmtTaskRunner.add(() -> {
                    for (KVRangeDescriptor r : overlapped) {
                        addKVRange(r.getId(), Snapshot.newBuilder()
                            .setClusterConfig(ClusterConfig.getDefaultInstance())
                            .setTerm(0)
                            .setIndex(0)
                            .setData(KVRangeSnapshot.newBuilder()
                                .setVer(0)
                                .setId(r.getId())
                                .setLastAppliedIndex(0)
                                .setRange(EMPTY_RANGE)
                                .setState(State.newBuilder().setType(Normal).build())
                                .build()
                                .toByteString())
                            .build());
                    }
                });
            });
    }

    private boolean check(String checkpointId) {
        return kvRangeMap.values().stream().anyMatch(tb -> {
            try {
                return tb.isOccupying(checkpointId);
            } catch (Throwable e) {
                log.error("Failed to check checkpoint's[{}] occupation", checkpointId, e);
                return true;
            }
        });
    }

    private void checkStarted() {
        Preconditions.checkState(status.get() == Status.STARTED, "Store not running");
    }
}
