package com.zachary.bifromq.plugin.subbroker;

import org.pf4j.ExtensionPoint;

import java.util.concurrent.CompletableFuture;

/**
 * A sub broker is a downstream multi-tenant system which is capable of receiving subscribed messages in a batched way.
 */
public interface ISubBroker extends ExtensionPoint {
    /**
     * The id of the subscription broker
     *
     * @return the statically assigned id for the downstream sub broker system
     */
    int id();

    /**
     * Open deliverer instance for delivering messages to the containing inboxes. It's guaranteed to have singleton
     * instance for each deliverer key.
     *
     * @param delivererKey the key of delivery group
     * @return the deliverer instance
     */
    IDeliverer open(String delivererKey);

    /**
     * Check the existence of an inbox asynchronously.
     *
     * @param reqId        the request id
     * @param tenantId     the id of the tenant to which the inbox belongs
     * @param inboxId      the inbox id
     * @param delivererKey the key of the deliverer who is responsible for delivering subscribed messages to the inbox
     * @return boolean indicating if the inbox still exists
     */
    CompletableFuture<Boolean> hasInbox(long reqId, String tenantId, String inboxId, String delivererKey);

    /**
     * Close the inbox broker
     */
    void close();
}
