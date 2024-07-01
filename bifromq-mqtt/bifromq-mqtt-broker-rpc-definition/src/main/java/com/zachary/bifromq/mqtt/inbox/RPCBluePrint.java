package com.zachary.bifromq.mqtt.inbox;

import com.zachary.bifromq.baserpc.BluePrint;
import com.zachary.bifromq.mqtt.inbox.rpc.proto.OnlineInboxBrokerGrpc;

public class RPCBluePrint {

    public static final BluePrint INSTANCE = BluePrint.builder()
        .serviceDescriptor(OnlineInboxBrokerGrpc.getServiceDescriptor())
        .methodSemantic(OnlineInboxBrokerGrpc.getWriteMethod(), BluePrint.DDPipelineUnaryMethod.getInstance())
        .methodSemantic(OnlineInboxBrokerGrpc.getHasInboxMethod(), BluePrint.DDUnaryMethod.getInstance())
        .build();

}
