package com.zachary.bifromq.retain;

import com.zachary.bifromq.baserpc.BluePrint;
import com.zachary.bifromq.retain.rpc.proto.RetainServiceGrpc;

public class RPCBluePrint {
    public static final BluePrint INSTANCE = BluePrint.builder()
        .serviceDescriptor(RetainServiceGrpc.getServiceDescriptor())
        .methodSemantic(RetainServiceGrpc.getRetainMethod(), BluePrint.WRPipelineUnaryMethod.getInstance())
        .methodSemantic(RetainServiceGrpc.getMatchMethod(), BluePrint.WRUnaryMethod.getInstance())
        .build();
}
