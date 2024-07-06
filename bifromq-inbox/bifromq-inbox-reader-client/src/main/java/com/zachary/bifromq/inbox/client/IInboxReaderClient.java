package com.zachary.bifromq.inbox.client;

import com.zachary.bifromq.baserpc.IRPCClient;
import com.zachary.bifromq.inbox.rpc.proto.CommitReply;
import com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply;
import com.zachary.bifromq.inbox.rpc.proto.DeleteInboxReply;
import com.zachary.bifromq.inbox.storage.proto.Fetched;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface IInboxReaderClient {
    static InboxReaderClientBuilder.InProcClientBuilder inProcClientBuilder() {
        return new InboxReaderClientBuilder.InProcClientBuilder();
    }

    static InboxReaderClientBuilder.NonSSLClientBuilder nonSSLClientBuilder() {
        return new InboxReaderClientBuilder.NonSSLClientBuilder();
    }

    static InboxReaderClientBuilder.SSLClientBuilder sslClientBuilder() {
        return new InboxReaderClientBuilder.SSLClientBuilder();
    }

    Observable<IRPCClient.ConnState> connState();

    CompletableFuture<Boolean> has(long reqId, String inboxId, ClientInfo clientInfo);

    CompletableFuture<CreateInboxReply> create(long reqId, String inboxId, ClientInfo clientInfo);

    CompletableFuture<DeleteInboxReply> delete(long reqId, String inboxId, ClientInfo clientInfo);

    String getDelivererKey(String inboxId, ClientInfo clientInfo);

    IInboxReader openInboxReader(String inboxId, String delivererKey, ClientInfo clientInfo);

    interface IInboxReader {
        void fetch(Consumer<Fetched> consumer);

        void hint(int bufferCapacity);

        CompletableFuture<CommitReply> commit(long reqId, QoS qos, long upToSeq);

        void close();
    }

    void stop();
}
