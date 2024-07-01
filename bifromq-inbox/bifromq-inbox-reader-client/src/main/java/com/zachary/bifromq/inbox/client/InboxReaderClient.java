package com.zachary.bifromq.inbox.client;

import com.zachary.bifromq.baserpc.IRPCClient;
import com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply;
import com.zachary.bifromq.inbox.rpc.proto.CreateInboxRequest;
import com.zachary.bifromq.inbox.rpc.proto.DeleteInboxReply;
import com.zachary.bifromq.inbox.rpc.proto.DeleteInboxRequest;
import com.zachary.bifromq.inbox.rpc.proto.HasInboxReply;
import com.zachary.bifromq.inbox.rpc.proto.HasInboxRequest;
import com.zachary.bifromq.inbox.rpc.proto.InboxServiceGrpc;
import com.zachary.bifromq.type.ClientInfo;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.INBOX_DELIVERERS;


@Slf4j
class InboxReaderClient implements IInboxReaderClient {
    private static final int INBOX_GROUPS = INBOX_DELIVERERS.get();

    private final IRPCClient rpcClient;
    private final AtomicBoolean closed = new AtomicBoolean(false);


    InboxReaderClient(@NonNull IRPCClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public IInboxReader openInboxReader(String inboxId, String delivererKey, ClientInfo clientInfo) {
        return new InboxReaderPipeline(inboxId, delivererKey, clientInfo, rpcClient);
    }

    @Override
    public Observable<IRPCClient.ConnState> connState() {
        return rpcClient.connState();
    }

    @Override
    public CompletableFuture<Boolean> has(long reqId, String inboxId, ClientInfo clientInfo) {
        return rpcClient.invoke(clientInfo.getTenantId(), null, HasInboxRequest.newBuilder()
                .setReqId(reqId)
                .setInboxId(inboxId)
                .setClientInfo(clientInfo)
                .build(), InboxServiceGrpc.getHasInboxMethod())
            .thenApply(HasInboxReply::getResult);
    }

    @Override
    public CompletableFuture<CreateInboxReply> create(long reqId, String inboxId, ClientInfo clientInfo) {
        return rpcClient.invoke(clientInfo.getTenantId(), null, CreateInboxRequest.newBuilder()
                .setReqId(reqId)
                .setInboxId(inboxId)
                .setClientInfo(clientInfo)
                .build(), InboxServiceGrpc.getCreateInboxMethod())
            .exceptionally(e -> CreateInboxReply.newBuilder()
                .setReqId(reqId)
                .setResult(CreateInboxReply.Result.ERROR).build());
    }

    @Override
    public CompletableFuture<DeleteInboxReply> delete(long reqId, String inboxId, ClientInfo clientInfo) {
        return rpcClient.invoke(clientInfo.getTenantId(), null, DeleteInboxRequest.newBuilder()
                .setReqId(reqId)
                .setInboxId(inboxId)
                .setClientInfo(clientInfo)
                .build(), InboxServiceGrpc.getDeleteInboxMethod())
            .exceptionally(e -> {
                return DeleteInboxReply.newBuilder()
                    .setReqId(reqId)
                    .setResult(DeleteInboxReply.Result.ERROR)
                    .build();
            });
    }

    @Override
    public String getDelivererKey(String inboxId, ClientInfo clientInfo) {
        int k = inboxId.hashCode() % INBOX_GROUPS;
        if (k < 0) {
            k = (k + INBOX_GROUPS) % INBOX_GROUPS;
        }
        return k + "";
    }

    @Override
    public void stop() {
        if (closed.compareAndSet(false, true)) {
            log.info("Stopping inbox reader client");
            log.debug("Stopping rpc client");
            rpcClient.stop();
            log.info("Inbox reader client stopped");
        }
    }
}
