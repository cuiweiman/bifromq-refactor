package com.zachary.bifromq.basescheduler;

import java.util.concurrent.CompletableFuture;

/**
 * A call scheduler is used for scheduling call before actually being invoked
 *
 * @param <Req> the request type
 */
public interface ICallScheduler<Req> {
    /**
     * Schedule a call to be called in the future when the turned future completed.
     * The returned future could be completed with DropException
     *
     * @param request the request to be scheduled
     * @return the future of the request to be invoked
     */
    default CompletableFuture<Req> submit(Req request) {
        return CompletableFuture.completedFuture(request);
    }

    /**
     * Close the scheduler
     */
    default void close() {
    }
}
