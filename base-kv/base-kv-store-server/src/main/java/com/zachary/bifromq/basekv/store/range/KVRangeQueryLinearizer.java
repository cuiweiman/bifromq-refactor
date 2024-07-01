package com.zachary.bifromq.basekv.store.range;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

class KVRangeQueryLinearizer implements IKVRangeQueryLinearizer {
    private final ConcurrentMap<CompletableFuture<Long>, CompletableFuture<Void>> readIndexes = Maps.newConcurrentMap();
    private final ConcurrentLinkedDeque<ToLinearize> toBeLinearized = new ConcurrentLinkedDeque();
    private final Supplier<CompletableFuture<Long>> readIndexProvider;
    private final Executor executor;
    private final AtomicBoolean linearizing = new AtomicBoolean();
    private volatile long lastAppliedIndex = 0;

    KVRangeQueryLinearizer(Supplier<CompletableFuture<Long>> readIndexProvider, Executor executor) {
        this.readIndexProvider = readIndexProvider;
        this.executor = executor;
    }

    @Override
    public CompletionStage<Void> linearize() {
        CompletableFuture<Void> onDone = new CompletableFuture<>();
        CompletableFuture<Long> readIndex = readIndexProvider.get();
        readIndexes.put(readIndex, onDone);
        readIndex.whenCompleteAsync((ri, e) -> {
            if (ri <= lastAppliedIndex) {
                readIndexes.remove(readIndex).complete(null);
            } else {
                readIndexes.remove(readIndex, onDone);
                if (!onDone.isDone()) {
                    toBeLinearized.add(new ToLinearize(ri, onDone));
                    schedule();
                }
            }
        }, executor);
        return onDone;
    }

    public void afterLogApplied(long logIndex) {
        assert lastAppliedIndex <= logIndex;
        lastAppliedIndex = logIndex;
        schedule();
    }

    private void schedule() {
        if (linearizing.compareAndSet(false, true)) {
            executor.execute(this::doLinearize);
        }
    }

    private void doLinearize() {
        ToLinearize toLinearize;
        while ((toLinearize = toBeLinearized.poll()) != null) {
            if (toLinearize.readIndex <= lastAppliedIndex) {
                toLinearize.onDone.complete(null);
            } else {
                // put it back
                toBeLinearized.addFirst(toLinearize);
                break;
            }
        }
        linearizing.set(false);
        if ((toLinearize = toBeLinearized.peek()) != null && toLinearize.readIndex <= lastAppliedIndex) {
            schedule();
        }
    }

    @AllArgsConstructor
    private static class ToLinearize {
        final long readIndex;
        final CompletableFuture<Void> onDone;
    }
}
