package com.zachary.bifromq.inbox.server;

import com.zachary.bifromq.baserpc.RPCContext;
import com.zachary.bifromq.baserpc.ResponsePipeline;
import com.zachary.bifromq.inbox.rpc.proto.SendReply;
import com.zachary.bifromq.inbox.rpc.proto.SendRequest;
import com.zachary.bifromq.inbox.rpc.proto.SendResult;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class InboxWriterPipeline extends ResponsePipeline<SendRequest, SendReply> {
    private final InboxFetcherRegistry registry;
    private final RequestHandler handler;
    private final String delivererKey;

    public InboxWriterPipeline(InboxFetcherRegistry registry, RequestHandler handler,
                               StreamObserver<SendReply> responseObserver) {
        super(responseObserver);
        this.registry = registry;
        this.handler = handler;
        this.delivererKey = RPCContext.WCH_HASH_KEY_CTX_KEY.get();
        // ensure fetch triggered when receive pipeline rebalanced
        registry.signalFetch(delivererKey);
    }

    @Override
    protected CompletableFuture<SendReply> handleRequest(String ignore, SendRequest request) {
        log.trace("Received inbox write request: deliverer={}, \n{}", delivererKey, request);
        return handler.handle(request).thenApply(v -> {
            for (SendResult result : v.getResultList()) {
                if (result.getResult() == SendResult.Result.OK) {
                    IInboxQueueFetcher f =
                        registry.get(result.getSubInfo().getTenantId(), result.getSubInfo().getInboxId(),
                            delivererKey);
                    if (f != null) {
                        f.signalFetch();
                    }
                }
            }
            return v;
        });
    }

    interface RequestHandler {
        CompletableFuture<SendReply> handle(SendRequest request);
    }
}
