package com.zachary.bifromq.retain.utils;

import com.zachary.bifromq.retain.rpc.proto.GCRequest;
import com.zachary.bifromq.retain.rpc.proto.MatchCoProcRequest;
import com.zachary.bifromq.retain.rpc.proto.RetainCoProcRequest;
import com.zachary.bifromq.retain.rpc.proto.RetainServiceROCoProcInput;
import com.zachary.bifromq.retain.rpc.proto.RetainServiceRWCoProcInput;

public class MessageUtil {
    public static RetainServiceRWCoProcInput buildGCRequest(long reqId) {
        return RetainServiceRWCoProcInput.newBuilder()
            .setGcRequest(GCRequest.newBuilder().setReqId(reqId).build())
            .build();
    }

    public static RetainServiceRWCoProcInput buildRetainRequest(RetainCoProcRequest request) {
        return RetainServiceRWCoProcInput.newBuilder()
            .setRetainRequest(request)
            .build();
    }

    public static RetainServiceROCoProcInput buildMatchRequest(MatchCoProcRequest request) {
        return RetainServiceROCoProcInput.newBuilder()
            .setMatchRequest(request)
            .build();
    }
}
