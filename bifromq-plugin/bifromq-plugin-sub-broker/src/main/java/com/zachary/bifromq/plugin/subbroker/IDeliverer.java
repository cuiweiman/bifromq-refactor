package com.zachary.bifromq.plugin.subbroker;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface IDeliverer {
    /**
     * Deliver a pack of messages into the subscriber inboxes.
     *
     * @param packs a pack of messages to be delivered
     * @return a future of result
     */
    CompletableFuture<Map<SubInfo, DeliveryResult>> deliver(Iterable<DeliveryPack> packs);

    /**
     * This method will be called whenever it's determined in IDLE state, release any resources associated if any.
     */
    void close();
}
