package com.zachary.bifromq.sessiondict.client;

import com.zachary.bifromq.baserpc.IRPCClient;
import com.zachary.bifromq.sessiondict.PipelineUtil;
import com.zachary.bifromq.sessiondict.rpc.proto.KillReply;
import com.zachary.bifromq.sessiondict.rpc.proto.KillRequest;
import com.zachary.bifromq.sessiondict.rpc.proto.Ping;
import com.zachary.bifromq.sessiondict.rpc.proto.Quit;
import com.zachary.bifromq.sessiondict.rpc.proto.SessionDictionaryServiceGrpc;
import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.zachary.bifromq.sessiondict.WCHKeyUtil.toWCHKey;
import static com.zachary.bifromq.type.MQTTClientInfoConstants.MQTT_TYPE_VALUE;

@Slf4j
class SessionDictionaryClient implements ISessionDictionaryClient {
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final IRPCClient rpcClient;

    SessionDictionaryClient(IRPCClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public Observable<IRPCClient.ConnState> connState() {
        return rpcClient.connState();
    }

    @Override
    public IRPCClient.IMessageStream<Quit, Ping> reg(ClientInfo clientInfo) {
        assert MQTT_TYPE_VALUE.equalsIgnoreCase(clientInfo.getType());
        Map<String, String> metadata = new HashMap<>();
        metadata.put(PipelineUtil.CLIENT_INFO, PipelineUtil.encode(clientInfo));
        return rpcClient.createMessageStream(clientInfo.getTenantId(),
            null,
            toWCHKey(clientInfo),
            metadata,
            SessionDictionaryServiceGrpc.getJoinMethod());
    }

    @Override
    public CompletableFuture<KillReply> kill(long reqId,
                                             String tenantId,
                                             String userId,
                                             String clientId,
                                             ClientInfo killer) {
        return rpcClient.invoke(tenantId, null, KillRequest.newBuilder()
                .setReqId(reqId)
                .setUserId(userId)
                .setClientId(clientId)
                .setKiller(killer)
                .build(), SessionDictionaryServiceGrpc.getKillMethod())
            .exceptionally(e -> KillReply.newBuilder()
                .setReqId(reqId)
                .setResult(KillReply.Result.ERROR)
                .build());
    }

    @Override
    public void stop() {
        if (closed.compareAndSet(false, true)) {
            log.info("Stopping session dict client");
            log.debug("Stopping rpc client");
            rpcClient.stop();
            log.info("Session dict client stopped");
        }
    }
}
