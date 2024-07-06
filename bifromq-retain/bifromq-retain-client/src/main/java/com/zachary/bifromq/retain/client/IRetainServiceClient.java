package com.zachary.bifromq.retain.client;

import com.zachary.bifromq.baserpc.IRPCClient;
import com.zachary.bifromq.retain.rpc.proto.MatchReply;
import com.zachary.bifromq.retain.rpc.proto.RetainReply;
import io.reactivex.rxjava3.core.Observable;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

public interface IRetainServiceClient {
    static RetainServiceClientBuilder.InProcRetainServiceClientBuilder inProcClientBuilder() {
        return new RetainServiceClientBuilder.InProcRetainServiceClientBuilder();
    }

    static RetainServiceClientBuilder.NonSSLRetainServiceClientBuilder nonSSLClientBuilder() {
        return new RetainServiceClientBuilder.NonSSLRetainServiceClientBuilder();
    }

    static RetainServiceClientBuilder.SSLRetainServiceClientBuilder sslClientBuilder() {
        return new RetainServiceClientBuilder.SSLRetainServiceClientBuilder();
    }

    interface IClientPipeline {
        CompletableFuture<RetainReply> retain(long reqId, String topic, QoS qos, ByteBuffer payload, int expirySeconds);

        void close();
    }

    Observable<IRPCClient.ConnState> connState();

    IClientPipeline open(ClientInfo clientInfo);

    CompletableFuture<MatchReply> match(long reqId, String tenantId, String topicFilter,
                                        int limit, ClientInfo clientInfo);

    void stop();
}
