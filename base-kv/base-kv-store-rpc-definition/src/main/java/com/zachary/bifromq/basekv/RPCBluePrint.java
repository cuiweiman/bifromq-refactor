package com.zachary.bifromq.basekv;

import com.zachary.bifromq.basekv.store.proto.BaseKVStoreServiceGrpc;
import com.zachary.bifromq.baserpc.BluePrint;

import static com.zachary.bifromq.basekv.store.proto.BaseKVStoreServiceGrpc.getBootstrapMethod;
import static com.zachary.bifromq.basekv.store.proto.BaseKVStoreServiceGrpc.getChangeReplicaConfigMethod;
import static com.zachary.bifromq.basekv.store.proto.BaseKVStoreServiceGrpc.getExecuteMethod;
import static com.zachary.bifromq.basekv.store.proto.BaseKVStoreServiceGrpc.getLinearizedQueryMethod;
import static com.zachary.bifromq.basekv.store.proto.BaseKVStoreServiceGrpc.getMergeMethod;
import static com.zachary.bifromq.basekv.store.proto.BaseKVStoreServiceGrpc.getQueryMethod;
import static com.zachary.bifromq.basekv.store.proto.BaseKVStoreServiceGrpc.getRecoverMethod;
import static com.zachary.bifromq.basekv.store.proto.BaseKVStoreServiceGrpc.getSplitMethod;
import static com.zachary.bifromq.basekv.store.proto.BaseKVStoreServiceGrpc.getTransferLeadershipMethod;

public class RPCBluePrint {
    public static final BluePrint INSTANCE = BluePrint.builder()
        .serviceDescriptor(BaseKVStoreServiceGrpc.getServiceDescriptor())
        .methodSemantic(getBootstrapMethod(), BluePrint.DDUnaryMethod.getInstance())
        .methodSemantic(getRecoverMethod(), BluePrint.DDUnaryMethod.getInstance())
        .methodSemantic(getChangeReplicaConfigMethod(), BluePrint.DDUnaryMethod.getInstance())
        .methodSemantic(getSplitMethod(), BluePrint.DDUnaryMethod.getInstance())
        .methodSemantic(getMergeMethod(), BluePrint.DDUnaryMethod.getInstance())
        .methodSemantic(getTransferLeadershipMethod(), BluePrint.DDUnaryMethod.getInstance())
        .methodSemantic(getExecuteMethod(), BluePrint.DDPipelineUnaryMethod.getInstance())
        .methodSemantic(getQueryMethod(), BluePrint.DDPipelineUnaryMethod.getInstance())
        .methodSemantic(getLinearizedQueryMethod(), BluePrint.DDPipelineUnaryMethod.getInstance())
        .build();
}
