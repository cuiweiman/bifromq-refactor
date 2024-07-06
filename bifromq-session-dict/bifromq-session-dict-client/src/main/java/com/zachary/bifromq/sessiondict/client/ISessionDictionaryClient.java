package com.zachary.bifromq.sessiondict.client;

import com.zachary.bifromq.baserpc.IRPCClient;
import com.zachary.bifromq.sessiondict.rpc.proto.KillReply;
import com.zachary.bifromq.sessiondict.rpc.proto.Ping;
import com.zachary.bifromq.sessiondict.rpc.proto.Quit;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.CompletableFuture;

public interface ISessionDictionaryClient {
    static SessionDictionaryClientBuilder.InProcClientBuilder inProcBuilder() {
        return new SessionDictionaryClientBuilder.InProcClientBuilder();
    }

    static SessionDictionaryClientBuilder.NonSSLClientBuilder nonSSLBuilder() {
        return new SessionDictionaryClientBuilder.NonSSLClientBuilder();
    }

    static SessionDictionaryClientBuilder.SSLClientBuilder sslBuilder() {
        return new SessionDictionaryClientBuilder.SSLClientBuilder();
    }

    Observable<IRPCClient.ConnState> connState();

    /**
     * Register an IMessagePipeline for one session
     *
     * @param clientInfo
     * @return
     */
    IRPCClient.IMessageStream<Quit, Ping> reg(ClientInfo clientInfo);

    CompletableFuture<KillReply> kill(long reqId, String tenantId, String userId, String clientId, ClientInfo killer);

    void stop();
}
