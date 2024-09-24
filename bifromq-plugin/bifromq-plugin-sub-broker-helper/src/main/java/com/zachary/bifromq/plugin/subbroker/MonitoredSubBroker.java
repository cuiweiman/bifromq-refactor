package com.zachary.bifromq.plugin.subbroker;

import com.google.common.base.Preconditions;
import com.zachary.bifromq.type.SubInfo;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
final class MonitoredSubBroker implements ISubBroker {
    private final AtomicBoolean hasStopped = new AtomicBoolean();
    private final ISubBroker delegate;
    private final Timer hasInboxCallTimer;
    private final Timer deliverCallTimer;

    MonitoredSubBroker(ISubBroker delegate) {
        this.delegate = delegate;
        hasInboxCallTimer = Timer.builder("ib.call.time")
            .tag("type", delegate.getClass().getName())
            .tag("call", "hasInbox")
            .register(Metrics.globalRegistry);
        deliverCallTimer = Timer.builder("ib.call.time")
            .tag("type", delegate.getClass().getName())
            .tag("call", "deliver")
            .register(Metrics.globalRegistry);
    }

    @Override
    public int id() {
        return delegate.id();
    }

    @Override
    public IDeliverer open(String delivererKey) {
        Preconditions.checkState(!hasStopped.get());
        return new MonitoredDeliverer(delivererKey);
    }

    @Override
    public CompletableFuture<Boolean> hasInbox(long reqId,
                                               @NonNull String tenantId,
                                               @NonNull String inboxId,
                                               @Nullable String delivererKey) {
        Preconditions.checkState(!hasStopped.get());
        try {
            Timer.Sample start = Timer.start();
            return delegate.hasInbox(reqId, tenantId, inboxId, delivererKey)
                .whenComplete((v, e) -> start.stop(hasInboxCallTimer));
        } catch (Throwable e) {
            return CompletableFuture.failedFuture(e);
        }
    }


    @Override
    public void close() {
        if (hasStopped.compareAndSet(false, true)) {
            delegate.close();
            Metrics.globalRegistry.remove(hasInboxCallTimer);
            Metrics.globalRegistry.remove(deliverCallTimer);
        }
    }

    private class MonitoredDeliverer implements IDeliverer {
        private final IDeliverer deliverer;

        MonitoredDeliverer(String delivererKey) {
            deliverer = delegate.open(delivererKey);
        }

        @Override
        public CompletableFuture<Map<SubInfo, DeliveryResult>> deliver(Iterable<DeliveryPack> packs) {
            try {
                Timer.Sample start = Timer.start();
                return deliverer.deliver(packs).whenComplete((v, e) -> start.stop(deliverCallTimer));
            } catch (Throwable e) {
                return CompletableFuture.failedFuture(e);
            }
        }

        @Override
        public void close() {
            deliverer.close();
        }
    }
}
