package com.zachary.bifromq.retain.server;

import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basekv.store.proto.KVRangeROReply;
import com.zachary.bifromq.basekv.store.proto.KVRangeRORequest;
import com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest;
import com.zachary.bifromq.basekv.store.proto.ReplyCode;
import com.zachary.bifromq.baserpc.ResponsePipeline;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import com.zachary.bifromq.retain.rpc.proto.MatchCoProcRequest;
import com.zachary.bifromq.retain.rpc.proto.MatchReply;
import com.zachary.bifromq.retain.rpc.proto.MatchRequest;
import com.zachary.bifromq.retain.rpc.proto.RetainCoProcReply;
import com.zachary.bifromq.retain.rpc.proto.RetainCoProcRequest;
import com.zachary.bifromq.retain.rpc.proto.RetainReply;
import com.zachary.bifromq.retain.rpc.proto.RetainRequest;
import com.zachary.bifromq.retain.rpc.proto.RetainServiceGrpc;
import com.zachary.bifromq.retain.rpc.proto.RetainServiceROCoProcInput;
import com.zachary.bifromq.retain.rpc.proto.RetainServiceROCoProcOutput;
import com.zachary.bifromq.retain.rpc.proto.RetainServiceRWCoProcInput;
import com.zachary.bifromq.retain.rpc.proto.RetainServiceRWCoProcOutput;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zachary.bifromq.type.ClientInfo;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import static com.zachary.bifromq.baserpc.UnaryResponse.response;
import static com.zachary.bifromq.plugin.settingprovider.Setting.BoostModeEnabled;
import static com.zachary.bifromq.plugin.settingprovider.Setting.RetainedTopicLimit;
import static com.zachary.bifromq.retain.utils.KeyUtil.tenantNS;
import static com.zachary.bifromq.retain.utils.MessageUtil.buildMatchRequest;
import static com.zachary.bifromq.retain.utils.MessageUtil.buildRetainRequest;
import static com.zachary.bifromq.retain.utils.PipelineUtil.PIPELINE_ATTR_KEY_CLIENT_INFO;
import static com.zachary.bifromq.retain.utils.PipelineUtil.decode;

@Slf4j
public class RetainService extends RetainServiceGrpc.RetainServiceImplBase {
    private final ISettingProvider settingProvider;
    private final IBaseKVStoreClient kvStoreClient;

    RetainService(ISettingProvider settingProvider, IBaseKVStoreClient kvStoreClient) {
        this.settingProvider = settingProvider;
        this.kvStoreClient = kvStoreClient;
    }

    @Override
    public StreamObserver<RetainRequest> retain(StreamObserver<RetainReply> responseObserver) {
        return new ResponsePipeline<>(responseObserver) {
            private final ClientInfo clientInfo = decode(metadata.get(PIPELINE_ATTR_KEY_CLIENT_INFO));

            @Override
            protected CompletableFuture<RetainReply> handleRequest(String tenantId, RetainRequest request) {
                ByteString tenantNS = tenantNS(tenantId);
                Optional<KVRangeSetting> s = kvStoreClient.findByKey(tenantNS);
                if (s.isPresent()) {
                    log.trace("Got retain request:\n{}", request);
                    return execCoProc(s.get().leader, request.getReqId(), s.get(),
                        buildRetainRequest(RetainCoProcRequest.newBuilder()
                            .setTenantId(tenantId)
                            .setReqId(request.getReqId())
                            .setQos(request.getQos())
                            .setTopic(request.getTopic())
                            .setTimestamp(request.getTimestamp())
                            .setExpireTimestamp(request.getExpireTimestamp())
                            .setMessage(request.getPayload())
                            .setPublisher(clientInfo)
                            .setMaxRetainedTopics(settingProvider.provide(RetainedTopicLimit, clientInfo.getTenantId()))
                            .build()))
                        .whenComplete((v, e) -> log.trace("Reply retain request:\n{}", v))
                        .thenApply(r -> r.getRetainReply().getResult())
                        .exceptionally(r -> RetainCoProcReply.Result.ERROR)
                        .thenApply(r -> RetainReply.newBuilder()
                            .setReqId(request.getReqId())
                            .setResult(RetainReply.Result.forNumber(r.getNumber()))
                            .build());
                } else {
                    log.warn("No range covering key: {}", tenantId);
                    return CompletableFuture.completedFuture(RetainReply.newBuilder()
                        .setReqId(request.getReqId())
                        .setResult(RetainReply.Result.ERROR)
                        .build());
                }
            }
        };
    }

    @Override
    public void match(MatchRequest request, StreamObserver<MatchReply> responseObserver) {
        log.trace("Handling match request:\n{}", request);
        response((tenantId, metadata) -> {
            ByteString tenantNS = tenantNS(tenantId);
            ClientInfo clientInfo = decode(metadata.get(PIPELINE_ATTR_KEY_CLIENT_INFO));
            Optional<KVRangeSetting> s = kvStoreClient.findByKey(tenantNS);
            if (s.isPresent()) {
                boolean boostMode = settingProvider.provide(BoostModeEnabled, clientInfo.getTenantId());
                return execCoProc(request.getReqId(), s.get(),
                    buildMatchRequest(MatchCoProcRequest.newBuilder()
                        .setReqId(request.getReqId())
                        .setTenantNS(tenantNS)
                        .setTopicFilter(request.getTopicFilter())
                        .setLimit(request.getLimit())
                        .build()), !boostMode)
                    .handle((v, e) -> {
                        log.trace("Finish handling match request:\n{}", request);
                        if (e != null) {
                            return MatchReply.newBuilder()
                                .setReqId(request.getReqId())
                                .setResult(MatchReply.Result.ERROR)
                                .build();
                        } else {
                            return MatchReply.newBuilder()
                                .setReqId(request.getReqId())
                                .setResult(MatchReply.Result.OK)
                                .addAllMessages(v.getMatchReply().getMessagesList())
                                .build();
                        }
                    });
            } else {
                log.warn("No range covering key: {}", tenantId);
                return CompletableFuture.completedFuture(MatchReply.newBuilder()
                    .setReqId(request.getReqId())
                    .setResult(MatchReply.Result.ERROR)
                    .build());
            }
        }, responseObserver);
    }


    private CompletableFuture<RetainServiceROCoProcOutput> execCoProc(long reqId,
                                                                      KVRangeSetting setting,
                                                                      RetainServiceROCoProcInput input,
                                                                      boolean linearized) {
        KVRangeRORequest request = KVRangeRORequest.newBuilder()
            .setReqId(reqId)
            .setVer(setting.ver)
            .setKvRangeId(setting.id)
            .setRoCoProcInput(input.toByteString())
            .build();
        CompletableFuture<KVRangeROReply> replyFuture = linearized ?
            kvStoreClient.linearizedQuery(selectStore(setting), request) :
            kvStoreClient.query(selectStore(setting), request);
        return replyFuture
            .thenApply(reply -> {
                if (reply.getCode() == ReplyCode.Ok) {
                    try {
                        return RetainServiceROCoProcOutput
                            .parseFrom(reply.getRoCoProcResult());
                    } catch (InvalidProtocolBufferException e) {
                        log.error("Unable to parse rw co-proc output", e);
                        throw new RuntimeException(e);
                    }
                }
                log.warn("Failed to exec ro co-proc[code={}]", reply.getCode());
                throw new RuntimeException();
            });
    }

    private CompletableFuture<RetainServiceRWCoProcOutput> execCoProc(String serverId,
                                                                      long reqId,
                                                                      KVRangeSetting setting,
                                                                      RetainServiceRWCoProcInput input) {
        return kvStoreClient.execute(serverId, KVRangeRWRequest.newBuilder()
                .setReqId(reqId)
                .setVer(setting.ver)
                .setKvRangeId(setting.id)
                .setRwCoProc(input.toByteString())
                .build())
            .thenApply(reply -> {
                if (reply.getCode() == ReplyCode.Ok) {
                    try {
                        return RetainServiceRWCoProcOutput.parseFrom(reply.getRwCoProcResult());
                    } catch (InvalidProtocolBufferException e) {
                        log.error("Unable to parse rw co-proc output", e);
                        throw new RuntimeException(e);
                    }
                }
                log.warn("Failed to exec rw co-proc[code={}]", reply.getCode());
                throw new RuntimeException();
            });
    }

    private String selectStore(KVRangeSetting setting) {
        return setting.allReplicas.get(ThreadLocalRandom.current().nextInt(setting.allReplicas.size()));
    }
}
