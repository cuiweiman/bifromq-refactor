package com.zachary.bifromq.inbox;

import com.zachary.bifromq.baserpc.BluePrint;
import com.zachary.bifromq.inbox.rpc.proto.CommitRequest;
import com.zachary.bifromq.inbox.rpc.proto.HasInboxRequest;
import com.zachary.bifromq.inbox.rpc.proto.InboxServiceGrpc;

public class RPCBluePrint {
    public static final BluePrint INSTANCE = BluePrint.builder()
        .serviceDescriptor(InboxServiceGrpc.getServiceDescriptor())
        // broker client rpc
        .methodSemantic(InboxServiceGrpc.getReceiveMethod(), BluePrint.WCHPipelineUnaryMethod.getInstance())
        // both broker and reader client rpc
        .methodSemantic(InboxServiceGrpc.getHasInboxMethod(), BluePrint.WCHUnaryMethod.<HasInboxRequest>builder()
            .keyHashFunc(HasInboxRequest::getInboxId).build())
        // reader client rpc
        .methodSemantic(InboxServiceGrpc.getCreateInboxMethod(), BluePrint.WRUnaryMethod.getInstance())
        .methodSemantic(InboxServiceGrpc.getDeleteInboxMethod(), BluePrint.WRUnaryMethod.getInstance())
        .methodSemantic(InboxServiceGrpc.getFetchMethod(), BluePrint.WCHStreamingMethod.getInstance())
        .methodSemantic(InboxServiceGrpc.getCommitMethod(), BluePrint.WCHUnaryMethod
            .<CommitRequest>builder().keyHashFunc(CommitRequest::getInboxId).build())
        .build();
}
