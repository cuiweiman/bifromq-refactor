package com.zachary.bifromq.mqtt.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@Slf4j
public final class AsyncTaskQueue {
    private static final CompletableFuture<Void> DONE = CompletableFuture.completedFuture(null);
    private final ConcurrentLinkedQueue<Supplier<CompletableFuture<Void>>> taskQueue;
    private final Executor executor;
    private final AtomicReference<State> state = new AtomicReference<>(State.EMPTY_STOP);
    private volatile CompletableFuture<Void> whenDone = DONE;

    public AsyncTaskQueue(Executor executor) {
        this.taskQueue = new ConcurrentLinkedQueue<>();
        this.executor = executor;
    }

    public void add(Supplier<CompletableFuture<Void>> taskSupplier) {
        while (true) {
            if (state.compareAndSet(State.EMPTY_STOP, State.NONEMPTY_STOP)) {
                taskQueue.add(taskSupplier);
                whenDone = new CompletableFuture<>();
                executor.execute(this::runTask);
                break;
            }
            if (state.get() == State.NONEMPTY_STOP || state.get() == State.NONEMPTY_RUNNING) {
                taskQueue.add(taskSupplier);
                break;
            }
            if (state.compareAndSet(State.EMPTY_RUNNING, State.NONEMPTY_RUNNING)) {
                taskQueue.add(taskSupplier);
                break;
            }
        }
    }

    private void runTask() {
        while (true) {
            if (state.compareAndSet(State.NONEMPTY_STOP, State.NONEMPTY_RUNNING)) {
                Supplier<CompletableFuture<Void>> taskSupplier = taskQueue.poll();
                executor.execute(() -> taskSupplier.get().whenCompleteAsync((v, e) -> this.runTask(), executor));
                break;
            }
            if (state.get() == State.NONEMPTY_RUNNING) {
                Supplier<CompletableFuture<Void>> taskSupplier = taskQueue.poll();
                if (taskSupplier != null) {
                    executor.execute(() -> taskSupplier.get().whenCompleteAsync((v, e) -> this.runTask(), executor));
                    break;
                } else if (state.compareAndSet(State.NONEMPTY_RUNNING, State.EMPTY_RUNNING)) {
                    if (!taskQueue.isEmpty()) {
                        state.set(State.NONEMPTY_RUNNING);
                    }
                    executor.execute(this::runTask);
                    break;
                }
            }
            if (state.compareAndSet(State.EMPTY_RUNNING, State.EMPTY_STOP)) {
                whenDone.complete(null);
                whenDone = DONE;
                break;
            }
        }
    }

    public void awaitDone() {
        whenDone.join();
    }

    private enum State {
        EMPTY_STOP,
        NONEMPTY_STOP,
        NONEMPTY_RUNNING,
        EMPTY_RUNNING
    }
}
