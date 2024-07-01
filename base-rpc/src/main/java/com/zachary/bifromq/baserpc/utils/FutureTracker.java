package com.zachary.bifromq.baserpc.utils;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/6/25 11:08
 */
public final class FutureTracker {

    private final Set<CompletableFuture<?>> track = ConcurrentHashMap.newKeySet();

    public <T> CompletableFuture<T> track(CompletableFuture<T> trackedFuture) {
        track.add(trackedFuture);
        trackedFuture.whenComplete((v, e) -> track.remove(trackedFuture));
        return trackedFuture;
    }

    public <T> CompletableFuture<T> track(Supplier<CompletableFuture<T>> futureSupplier) {
        return track(futureSupplier.get());
    }

    public void stop() {
        for (CompletableFuture<?> tracked : track) {
            tracked.cancel(true);
        }
    }

    public CompletableFuture<Void> whenComplete(BiConsumer<Void, Throwable> biConsumer) {
        return CompletableFuture.allOf(track.toArray(new CompletableFuture[0]))
                .whenComplete(biConsumer);
    }

    public CompletableFuture<Void> whenCompleteAsync(BiConsumer<Void, Throwable> biConsumer, Executor executor) {
        return CompletableFuture.allOf(track.toArray(new CompletableFuture[0]))
                .whenCompleteAsync(biConsumer, executor);
    }
}
