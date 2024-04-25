
package com.zachary.bifromq.basecrdt.store;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.ICRDTEngine;
import com.zachary.bifromq.basecrdt.core.api.ICausalCRDT;
import com.zachary.bifromq.basecrdt.core.util.Log;
import com.zachary.bifromq.basecrdt.proto.Replica;
import com.zachary.bifromq.basecrdt.store.compressor.Compressor;
import com.zachary.bifromq.basecrdt.store.proto.CRDTStoreMessage;
import com.zachary.bifromq.basecrdt.store.proto.MessagePayload;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.ToDoubleFunction;

import static java.lang.Long.toUnsignedString;

@Slf4j
class CRDTStore implements ICRDTStore {

    private class MetricManager {
        final Gauge objectNumGauge;

        /**
         * {@link ToDoubleFunction}转双精度浮点型函数式，接受一个 Object 类型的参数并产生一个 double 类型的结果
         * {@link MetricManager} 本类对象，用于记录 basecrdt.objectnum 值，即 {@link #antiEntropyByURI} 的缓存刷屏
         *
         * @param tags tags
         */
        MetricManager(Tags tags) {
            objectNumGauge = Gauge.builder("basecrdt.objectnum", CRDTStore.this, new ToDoubleFunction<>() {
                                @Override
                                public double applyAsDouble(CRDTStore value) {
                                    return value.antiEntropyByURI.values().size();
                                }
                            }
                    )
                    .tags(tags)
                    .register(Metrics.globalRegistry);

        }

        /**
         * 关闭时，需要从 全局测量注册器中 移除 此 测量器
         */
        void close() {
            Metrics.globalRegistry.removeByPreFilterId(objectNumGauge.getId());
        }
    }

    private enum State {
        /**
         * 初始化, 开始中, 开始过程完成, 停止中, 已停止
         */
        INIT, STARTING, STARTED, STOPPING, STOPPED
    }

    /**
     * 状态切换, {@link AtomicReference} 确保 引用对象 的 原子 操作
     */
    private final AtomicReference<State> state = new AtomicReference<>(State.INIT);

    /**
     * CRDT Store 执行的操作
     */
    private final CRDTStoreOptions options;
    /**
     * CRDT 引擎
     */
    private final ICRDTEngine engine;
    /**
     * {@link Subject} 抽象主题类, 是 观察者模式 的实现
     * 需要包含一个 列表属性，用来保存观察者; 要有两个虚方法，用来增加和删除观察者; 要有一个待实现方法，由具体抽象类来分别实现;
     * 同时表示一个 {@link Observer} 和一个 {@link Observable}，允许将事件从单个源多播到多个子 {@link Observer}。
     */
    private final Subject<CRDTStoreMessage> storeMsgPublisher = PublishSubject.<CRDTStoreMessage>create().toSerialized();
    /**
     *
     */
    private final Map<String, AntiEntropyManager> antiEntropyByURI = Maps.newConcurrentMap();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final ScheduledExecutorService storeExecutor;
    private final Compressor compressor;
    private MetricManager metricManager;

    public CRDTStore(CRDTStoreOptions options) {
        this.options = options;
        engine = ICRDTEngine.newInstance(options.engineOptions);
        storeExecutor = options.storeExecutor() != null ?
                options.storeExecutor() : SharedAntiEntropyExecutor.getInstance();
        metricManager = new MetricManager(Tags.of("store.id", toUnsignedString(engine.id())));
        compressor = Compressor.newInstance(options.compressAlgorithm);
    }

    @Override
    public long id() {
        return engine.id();
    }

    @Override
    public Replica host(String crdtURI) {
        checkState();
        return engine.host(crdtURI);
    }

    @Override
    public Replica host(String crdtURI, ByteString replicaId) {
        checkState();
        return engine.host(crdtURI, replicaId);
    }

    @Override
    public CompletableFuture<Void> stopHosting(String uri) {
        checkState();
        antiEntropyByURI.computeIfPresent(uri, (k, v) -> {
            v.stop();
            return null;
        });
        return engine.stopHosting(uri);
    }

    @Override
    public Iterator<Replica> hosting() {
        checkState();
        return engine.hosting();
    }

    @Override
    public <T extends ICausalCRDT> Optional<T> get(String uri) {
        checkState();
        return engine.get(uri);
    }

    @Override
    public void join(String uri, ByteString localAddr, Set<ByteString> cluster) {
        checkState();
        // make sure local address is a cluster member
        cluster.add(localAddr);
        // ensure localAddr is a member
        Optional<ICausalCRDT> crdt = engine.get(uri);
        if (!crdt.isPresent()) {
            throw new IllegalArgumentException("CRDT not found");
        }
        antiEntropyByURI.compute(uri, (k, v) -> {
            if (v == null) {
                Log.debug(log, "Replica[{}] bind to address[{}]", crdt.get().id(), localAddr);
                v = new AntiEntropyManager(crdt.get(), localAddr, engine, storeExecutor,
                        storeMsgPublisher, options.maxEventsInDelta, compressor);
            } else if (!v.getLocalAddr().equals(localAddr)) {
                Log.debug(log, "Replica[id={},uri={}] relocate to new address[{}] from address[{}]",
                        crdt.get().id(), localAddr, v.getLocalAddr());
                v.stop();
                v = new AntiEntropyManager(crdt.get(), localAddr, engine, storeExecutor,
                        storeMsgPublisher, options.maxEventsInDelta, compressor);
            }
            return v;
        }).setCluster(cluster);
    }

    @Override
    public Optional<ByteString> localAddr(String uri) {
        AntiEntropyManager aeMgr = antiEntropyByURI.get(uri);
        if (aeMgr != null) {
            return Optional.of(aeMgr.getLocalAddr());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Set<ByteString>> cluster(String uri) {
        AntiEntropyManager aeMgr = antiEntropyByURI.get(uri);
        if (aeMgr != null) {
            return Optional.of(aeMgr.cluster());
        }
        return Optional.empty();
    }

    @Override
    public void sync(String uri, ByteString peerAddr) {
        antiEntropyByURI.computeIfPresent(uri, (k, v) -> {
            v.resetIfNeighbor(peerAddr);
            return v;
        });
    }

    @Override
    public Observable<CRDTStoreMessage> storeMessages() {
        checkState();
        return storeMsgPublisher;
    }

    @Override
    public void start(Observable<CRDTStoreMessage> replicaMessages) {
        if (state.compareAndSet(State.INIT, State.STARTING)) {
            disposable.add(replicaMessages
                    .subscribeOn(Schedulers.computation())
                    .subscribe(msg -> {
                        if (started()) {
                            handleStoreMessage(msg);
                        }
                    }));
            engine.start();
            state.set(State.STARTED);
            Log.debug(log, "Started CRDTStore[{}]", toUnsignedString(engine.id()));
        } else {
            log.warn("Start more than one time");
        }
    }

    @Override
    public void stop() {
        if (state.compareAndSet(State.STARTED, State.STOPPED)) {
            Log.debug(log, "Stop CRDTStore[{}]", toUnsignedString(engine.id()));
            antiEntropyByURI.forEach((uri, aaMgr) -> aaMgr.stop());
            antiEntropyByURI.clear();
            metricManager.close();
            disposable.dispose();
            engine.stop();
            state.set(State.STOPPED);
        }
    }

    private void handleStoreMessage(CRDTStoreMessage msg) {
        AntiEntropyManager antiEntropyMgr = antiEntropyByURI.get(msg.getUri());
        if (antiEntropyMgr != null) {
            Log.trace(log, "Anti-entropy manager of crdt[{}] bind to addr[{}], receive message from addr[{}]:\n{}",
                    msg.getUri(), msg.getReceiver(), msg.getSender(), msg);
            MessagePayload payload = MessagePayloadUtil.decompress(compressor, msg);
            switch (payload.getMsgTypeCase()) {
                case DELTA:
                    antiEntropyMgr.join(payload.getDelta(), msg.getSender());
                    break;
                case ACK:
                    antiEntropyMgr.receive(payload.getAck(), msg.getSender());
                    break;
            }
        } else {
            Log.debug(log, "No anti-entropy manager of crdt[{}] bind to addr[{}], ignore the message from addr[{}]",
                    msg.getUri(), msg.getReceiver(), msg.getSender());
        }
    }

    private boolean started() {
        return state.get() == State.STARTED;
    }

    private void checkState() {
        Preconditions.checkState(started(), "Not started");
    }
}
