package com.zachary.bifromq.basescheduler;

import java.util.concurrent.CompletableFuture;

public interface IBatchCallScheduler<Req, Resp> {
    CompletableFuture<Resp> schedule(Req request);

    void close();
}
