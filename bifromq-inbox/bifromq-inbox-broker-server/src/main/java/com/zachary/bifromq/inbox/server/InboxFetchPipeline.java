package com.zachary.bifromq.inbox.server;

import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.baserpc.AckStream;
import com.zachary.bifromq.baserpc.RPCContext;
import com.zachary.bifromq.inbox.rpc.proto.FetchHint;
import com.zachary.bifromq.inbox.server.scheduler.InboxFetchScheduler;
import com.zachary.bifromq.inbox.storage.proto.FetchParams;
import com.zachary.bifromq.inbox.storage.proto.Fetched;
import com.zachary.bifromq.inbox.util.KeyUtil;
import com.google.common.util.concurrent.RateLimiter;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static com.zachary.bifromq.inbox.util.PipelineUtil.PIPELINE_ATTR_KEY_INBOX_ID;
import static com.zachary.bifromq.inbox.util.PipelineUtil.PIPELINE_ATTR_KEY_QOS0_LAST_FETCH_SEQ;
import static com.zachary.bifromq.inbox.util.PipelineUtil.PIPELINE_ATTR_KEY_QOS2_LAST_FETCH_SEQ;

@Slf4j
public final class InboxFetchPipeline extends AckStream<FetchHint, Fetched> implements IInboxQueueFetcher {
    private final Fetcher fetcher;
    private final Function<ByteString, CompletableFuture<Void>> toucher;
    private final IBaseKVStoreClient kvStoreClient;
    private final AtomicBoolean fetchStarted = new AtomicBoolean(false);

    private final String delivererKey;
    private final String inboxId;
    // indicate downstream free buffer capacity, -1 stands for capacity not known yet
    private final AtomicInteger downStreamCapacity = new AtomicInteger(-1);
    private final ByteString scopedInboxId;
    private volatile boolean closed = false;
    private volatile long fetchStartTS = 0;
    private volatile long signalTS = 0;
    private volatile long lastFetchQoS0Seq = -1;
    private volatile long lastFetchQoS1Seq = -1;
    private volatile long lastFetchQoS2Seq = -1;

    public InboxFetchPipeline(StreamObserver<Fetched> responseObserver,
                              Fetcher fetcher,
                              Function<ByteString, CompletableFuture<Void>> toucher,
                              IBaseKVStoreClient kvStoreClient,
                              InboxFetcherRegistry registry,
                              RateLimiter limiter) {
        super(responseObserver);
        this.delivererKey = RPCContext.WCH_HASH_KEY_CTX_KEY.get();
        this.inboxId = metadata.get(PIPELINE_ATTR_KEY_INBOX_ID);
        this.fetcher = fetcher;
        this.toucher = toucher;
        this.kvStoreClient = kvStoreClient;
        scopedInboxId = KeyUtil.scopedInboxId(tenantId, inboxId);
        if (limiter.tryAcquire()) {
            if (hasMetadata(PIPELINE_ATTR_KEY_QOS0_LAST_FETCH_SEQ)) {
                lastFetchQoS0Seq = Long.parseLong(metadata(PIPELINE_ATTR_KEY_QOS0_LAST_FETCH_SEQ));
            }
            if (hasMetadata(PIPELINE_ATTR_KEY_QOS2_LAST_FETCH_SEQ)) {
                lastFetchQoS2Seq = Long.parseLong(metadata(PIPELINE_ATTR_KEY_QOS2_LAST_FETCH_SEQ));
            }
            registry.reg(this);
            ack().doFinally(() -> {
                    touch();
                    registry.unreg(this);
                    closed = true;
                })
                .subscribe(fetchHint -> {
                    log.trace("Got hint: tenantId={}, inboxId={}, capacity={}",
                        tenantId, inboxId, fetchHint.getCapacity());
                    downStreamCapacity.set(Math.max(0, fetchHint.getCapacity()));
                    signalFetch();
                });
            signalFetch();
        } else {
            close();
        }
    }

    @Override
    public String delivererKey() {
        return delivererKey;
    }

    @Override
    public String tenantId() {
        return tenantId;
    }

    @Override
    public String inboxId() {
        return inboxId;
    }

    @Override
    public long lastFetchTS() {
        return fetchStartTS;
    }

    @Override
    public long lastFetchQoS0Seq() {
        return lastFetchQoS0Seq;
    }

    @Override
    public void signalFetch() {
        log.trace("Signal fetch: tenantId={}, inboxId={}, hint={}, fetching={}",
            tenantId, inboxId, downStreamCapacity.get(), fetchStarted.get());
        signalTS = System.nanoTime();
        if (!closed && fetchStarted.compareAndSet(false, true)) {
            int capacity = downStreamCapacity.get();
            // fetch 100 messages if capacity not known yet
            int batchSize = capacity == -1 ? 100 : downStreamCapacity.get();
            if (batchSize > 0) {
                fetch(batchSize);
            } else {
                fetchStarted.set(false);
            }
        }
    }

    @Override
    public void touch() {
        toucher.apply(scopedInboxId);
    }

    private void fetch(int batchSize) {
        FetchParams.Builder fb = FetchParams.newBuilder().setMaxFetch(batchSize);
        if (lastFetchQoS0Seq >= 0) {
            fb.setQos0StartAfter(lastFetchQoS0Seq);
        }
        if (lastFetchQoS1Seq >= 0) {
            fb.setQos1StartAfter(lastFetchQoS1Seq);
        }
        if (lastFetchQoS2Seq >= 0) {
            fb.setQos2StartAfter(lastFetchQoS2Seq);
        }
        fetcher.fetch(new InboxFetchScheduler.InboxFetch(scopedInboxId, fb.build()))
            .whenComplete((reply, e) -> {
                log.trace("Fetch success: tenantId={}, inboxId={}\n{}", tenantId, inboxId, reply);
                int fetchedCount = 0;
                if (reply.getQos0MsgCount() > 0 || reply.getQos1MsgCount() > 0 || reply.getQos2MsgCount() > 0) {
                    if (reply.getQos0MsgCount() > 0) {
                        fetchedCount += reply.getQos0MsgCount();
                        downStreamCapacity.accumulateAndGet(reply.getQos0MsgCount(), (a, b) -> Math.max(a - b, 0));
                        lastFetchQoS0Seq = reply.getQos0Seq(reply.getQos0SeqCount() - 1);
                    }
                    if (reply.getQos1MsgCount() > 0) {
                        fetchedCount += reply.getQos1MsgCount();
                        downStreamCapacity.accumulateAndGet(reply.getQos1MsgCount(), (a, b) -> Math.max(a - b, 0));
                        lastFetchQoS1Seq = reply.getQos1Seq(reply.getQos1SeqCount() - 1);
                    }
                    if (reply.getQos2MsgCount() > 0) {
                        fetchedCount += reply.getQos2MsgCount();
                        downStreamCapacity.accumulateAndGet(reply.getQos2MsgCount(), (a, b) -> Math.max(a - b, 0));
                        lastFetchQoS2Seq = reply.getQos2Seq(reply.getQos2SeqCount() - 1);
                    }
                    if (!closed) {
                        send(reply);
                    }
                    fetchStarted.set(false);
                    if (downStreamCapacity.get() > 0 && fetchedCount >= batchSize) {
                        signalFetch();
                    }
                } else {
                    fetchStarted.set(false);
                    if (signalTS >= fetchStartTS) {
                        signalFetch();
                    }
                }
            });
        fetchStartTS = System.nanoTime();
    }

    interface Fetcher {
        CompletableFuture<Fetched> fetch(InboxFetchScheduler.InboxFetch fetch);
    }
}
