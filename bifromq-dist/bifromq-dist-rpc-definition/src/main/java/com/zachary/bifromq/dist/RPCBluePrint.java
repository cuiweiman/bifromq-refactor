package com.zachary.bifromq.dist;

import com.zachary.bifromq.baserpc.BluePrint;
import com.zachary.bifromq.dist.rpc.proto.DistServiceGrpc;

public class RPCBluePrint {
    public static final BluePrint INSTANCE = BluePrint.builder()
        .serviceDescriptor(DistServiceGrpc.getServiceDescriptor())
        .methodSemantic(DistServiceGrpc.getSubMethod(), BluePrint.WRUnaryMethod.getInstance())
        .methodSemantic(DistServiceGrpc.getUnsubMethod(), BluePrint.WRUnaryMethod.getInstance())
        .methodSemantic(DistServiceGrpc.getClearMethod(), BluePrint.WRUnaryMethod.getInstance())
        .methodSemantic(DistServiceGrpc.getDistMethod(), BluePrint.WRPipelineUnaryMethod.getInstance())
        .build();
}
