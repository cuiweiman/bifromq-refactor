package com.zachary.bifromq.basescheduler;

import java.util.concurrent.CompletableFuture;

public interface IBatchCall<Req, Resp> {
    /**
     * If no task batched so far
     *
     * @return boolean indicating if the batch is empty
     */
    boolean isEmpty();

    /**
     * If there are enough requests batched return a new builder, otherwise returns empty
     *
     * @return boolean indicating if the batch is full
     */
    boolean isEnough();

    /**
     * Add an async request to this batch
     *
     * @param request add request to the batch for executing
     * @return a future to receive response asynchronously
     */
    CompletableFuture<Resp> add(Req request);

    /**
     * Reset the batch call object to initial state to be reused again
     */
    void reset();

    /**
     * Execute the async batch call
     *
     * @return a future which will complete when batch is done
     */
    CompletableFuture<Void> execute();
}

