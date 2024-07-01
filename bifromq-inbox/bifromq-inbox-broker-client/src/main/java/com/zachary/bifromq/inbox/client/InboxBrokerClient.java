package com.zachary.bifromq.inbox.client;

import com.zachary.bifromq.baserpc.IRPCClient;
import com.zachary.bifromq.inbox.rpc.proto.HasInboxReply;
import com.zachary.bifromq.inbox.rpc.proto.HasInboxRequest;
import com.zachary.bifromq.inbox.rpc.proto.InboxServiceGrpc;
import com.zachary.bifromq.plugin.subbroker.IDeliverer;
import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
class InboxBrokerClient implements IInboxBrokerClient {
    private final AtomicBoolean hasStopped = new AtomicBoolean();
    private final IRPCClient rpcClient;

    InboxBrokerClient(@NonNull IRPCClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public IDeliverer open(String delivererKey) {
        Preconditions.checkState(!hasStopped.get());
        return new DeliveryPipeline(delivererKey, rpcClient);
    }

    @Override
    public CompletableFuture<Boolean> hasInbox(long reqId,
                                               @NonNull String tenantId,
                                               @NonNull String inboxId,
                                               @Nullable String delivererKey) {
        Preconditions.checkState(!hasStopped.get());
        return rpcClient.invoke(tenantId, delivererKey,
                HasInboxRequest.newBuilder().setReqId(reqId).setInboxId(inboxId).build(),
                InboxServiceGrpc.getHasInboxMethod())
            .thenApply(HasInboxReply::getResult);
    }


    @Override
    public void close() {
        if (hasStopped.compareAndSet(false, true)) {
            log.info("Closing inbox broker client");
            log.debug("Stopping rpc client");
            rpcClient.stop();
            log.info("Inbox broker client closed");
        }
    }
}
