package com.zachary.bifromq.basekv.server;

import com.zachary.bifromq.basekv.store.IKVRangeStore;
import com.zachary.bifromq.basekv.store.exception.KVRangeException;
import com.zachary.bifromq.basekv.store.proto.KVRangeRWReply;
import com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest;
import com.zachary.bifromq.basekv.store.proto.ReplyCode;
import com.zachary.bifromq.baserpc.ResponsePipeline;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@Slf4j
class MutatePipeline extends ResponsePipeline<KVRangeRWRequest, KVRangeRWReply> {
    private final IKVRangeStore kvRangeStore;

    MutatePipeline(IKVRangeStore kvRangeStore, StreamObserver<KVRangeRWReply> responseObserver) {
        super(responseObserver);
        this.kvRangeStore = kvRangeStore;
    }

    @Override
    protected CompletableFuture<KVRangeRWReply> handleRequest(String s, KVRangeRWRequest request) {
        log.trace("Handling rw range request:req={}", request);
        switch (request.getRequestTypeCase()) {
            case DELETE:
                return mutate(request, this::delete).toCompletableFuture();
            case PUT:
                return mutate(request, this::put).toCompletableFuture();
            case RWCOPROC:
            default:
                return mutate(request, this::mutateCoProc).toCompletableFuture();
        }
    }

    private CompletionStage<KVRangeRWReply> delete(KVRangeRWRequest request) {
        return kvRangeStore.delete(request.getVer(), request.getKvRangeId(), request.getDelete())
            .thenApply(v -> KVRangeRWReply.newBuilder()
                .setReqId(request.getReqId())
                .setCode(ReplyCode.Ok)
                .setDeleteResult(v)
                .build());
    }

    private CompletionStage<KVRangeRWReply> put(KVRangeRWRequest request) {
        return kvRangeStore.put(request.getVer(), request.getKvRangeId(), request.getPut().getKey(),
                request.getPut().getValue())
            .thenApply(v -> KVRangeRWReply.newBuilder()
                .setReqId(request.getReqId())
                .setCode(ReplyCode.Ok)
                .setPutResult(v)
                .build());
    }

    private CompletionStage<KVRangeRWReply> mutateCoProc(KVRangeRWRequest request) {
        return kvRangeStore.mutateCoProc(request.getVer(), request.getKvRangeId(), request.getRwCoProc())
            .thenApply(v -> KVRangeRWReply.newBuilder()
                .setReqId(request.getReqId())
                .setCode(ReplyCode.Ok)
                .setRwCoProcResult(v)
                .build());
    }


    private CompletionStage<KVRangeRWReply> mutate(KVRangeRWRequest request, Function<KVRangeRWRequest,
        CompletionStage<KVRangeRWReply>> mutateFn) {
        return mutateFn.apply(request)
            .exceptionally(e -> {
                log.error("Handle rw request error: reqId={}", request.getReqId(), e);
                if (e instanceof KVRangeException.BadVersion || e.getCause() instanceof KVRangeException.BadVersion) {
                    return KVRangeRWReply.newBuilder()
                        .setReqId(request.getReqId())
                        .setCode(ReplyCode.BadVersion)
                        .build();
                }
                if (e instanceof KVRangeException.TryLater || e.getCause() instanceof KVRangeException.TryLater) {
                    return KVRangeRWReply.newBuilder()
                        .setReqId(request.getReqId())
                        .setCode(ReplyCode.TryLater)
                        .build();
                }
                if (e instanceof KVRangeException.BadRequest || e.getCause() instanceof KVRangeException.BadRequest) {
                    return KVRangeRWReply.newBuilder()
                        .setReqId(request.getReqId())
                        .setCode(ReplyCode.BadRequest)
                        .build();
                }
                return KVRangeRWReply.newBuilder()
                    .setReqId(request.getReqId())
                    .setCode(ReplyCode.InternalError)
                    .build();
            });
    }
}
