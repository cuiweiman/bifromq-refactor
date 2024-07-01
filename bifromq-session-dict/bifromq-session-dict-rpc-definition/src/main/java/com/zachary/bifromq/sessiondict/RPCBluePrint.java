package com.zachary.bifromq.sessiondict;

import com.zachary.bifromq.baserpc.BluePrint;
import com.zachary.bifromq.sessiondict.rpc.proto.KillRequest;
import com.zachary.bifromq.sessiondict.rpc.proto.SessionDictionaryServiceGrpc;

public class RPCBluePrint {
    public static final BluePrint INSTANCE = BluePrint.builder()
        .serviceDescriptor(SessionDictionaryServiceGrpc.getServiceDescriptor())
        .methodSemantic(SessionDictionaryServiceGrpc.getJoinMethod(), BluePrint.WCHStreamingMethod.getInstance())
        .methodSemantic(SessionDictionaryServiceGrpc.getKillMethod(),
            BluePrint.WCHUnaryMethod.<KillRequest>builder()
                .keyHashFunc(r -> WCHKeyUtil.toWCHKey(r.getUserId(), r.getClientId()))
                .build())
        .build();
}
