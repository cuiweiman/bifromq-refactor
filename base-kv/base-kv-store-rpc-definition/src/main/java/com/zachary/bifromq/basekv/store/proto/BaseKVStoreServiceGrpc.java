package com.zachary.bifromq.basekv.store.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.53.0)",
    comments = "Source: basekv/BaseKVStoreService.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class BaseKVStoreServiceGrpc {

  private BaseKVStoreServiceGrpc() {}

  public static final String SERVICE_NAME = "basekv.BaseKVStoreService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.BootstrapRequest,
      com.zachary.bifromq.basekv.store.proto.BootstrapReply> getBootstrapMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "bootstrap",
      requestType = com.zachary.bifromq.basekv.store.proto.BootstrapRequest.class,
      responseType = com.zachary.bifromq.basekv.store.proto.BootstrapReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.BootstrapRequest,
      com.zachary.bifromq.basekv.store.proto.BootstrapReply> getBootstrapMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.BootstrapRequest, com.zachary.bifromq.basekv.store.proto.BootstrapReply> getBootstrapMethod;
    if ((getBootstrapMethod = BaseKVStoreServiceGrpc.getBootstrapMethod) == null) {
      synchronized (BaseKVStoreServiceGrpc.class) {
        if ((getBootstrapMethod = BaseKVStoreServiceGrpc.getBootstrapMethod) == null) {
          BaseKVStoreServiceGrpc.getBootstrapMethod = getBootstrapMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.basekv.store.proto.BootstrapRequest, com.zachary.bifromq.basekv.store.proto.BootstrapReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "bootstrap"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.BootstrapRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.BootstrapReply.getDefaultInstance()))
              .setSchemaDescriptor(new BaseKVStoreServiceMethodDescriptorSupplier("bootstrap"))
              .build();
        }
      }
    }
    return getBootstrapMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.RecoverRequest,
      com.zachary.bifromq.basekv.store.proto.RecoverReply> getRecoverMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "recover",
      requestType = com.zachary.bifromq.basekv.store.proto.RecoverRequest.class,
      responseType = com.zachary.bifromq.basekv.store.proto.RecoverReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.RecoverRequest,
      com.zachary.bifromq.basekv.store.proto.RecoverReply> getRecoverMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.RecoverRequest, com.zachary.bifromq.basekv.store.proto.RecoverReply> getRecoverMethod;
    if ((getRecoverMethod = BaseKVStoreServiceGrpc.getRecoverMethod) == null) {
      synchronized (BaseKVStoreServiceGrpc.class) {
        if ((getRecoverMethod = BaseKVStoreServiceGrpc.getRecoverMethod) == null) {
          BaseKVStoreServiceGrpc.getRecoverMethod = getRecoverMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.basekv.store.proto.RecoverRequest, com.zachary.bifromq.basekv.store.proto.RecoverReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "recover"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.RecoverRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.RecoverReply.getDefaultInstance()))
              .setSchemaDescriptor(new BaseKVStoreServiceMethodDescriptorSupplier("recover"))
              .build();
        }
      }
    }
    return getRecoverMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.TransferLeadershipRequest,
      com.zachary.bifromq.basekv.store.proto.TransferLeadershipReply> getTransferLeadershipMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "transferLeadership",
      requestType = com.zachary.bifromq.basekv.store.proto.TransferLeadershipRequest.class,
      responseType = com.zachary.bifromq.basekv.store.proto.TransferLeadershipReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.TransferLeadershipRequest,
      com.zachary.bifromq.basekv.store.proto.TransferLeadershipReply> getTransferLeadershipMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.TransferLeadershipRequest, com.zachary.bifromq.basekv.store.proto.TransferLeadershipReply> getTransferLeadershipMethod;
    if ((getTransferLeadershipMethod = BaseKVStoreServiceGrpc.getTransferLeadershipMethod) == null) {
      synchronized (BaseKVStoreServiceGrpc.class) {
        if ((getTransferLeadershipMethod = BaseKVStoreServiceGrpc.getTransferLeadershipMethod) == null) {
          BaseKVStoreServiceGrpc.getTransferLeadershipMethod = getTransferLeadershipMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.basekv.store.proto.TransferLeadershipRequest, com.zachary.bifromq.basekv.store.proto.TransferLeadershipReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "transferLeadership"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.TransferLeadershipRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.TransferLeadershipReply.getDefaultInstance()))
              .setSchemaDescriptor(new BaseKVStoreServiceMethodDescriptorSupplier("transferLeadership"))
              .build();
        }
      }
    }
    return getTransferLeadershipMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigRequest,
      com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigReply> getChangeReplicaConfigMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "changeReplicaConfig",
      requestType = com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigRequest.class,
      responseType = com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigRequest,
      com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigReply> getChangeReplicaConfigMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigRequest, com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigReply> getChangeReplicaConfigMethod;
    if ((getChangeReplicaConfigMethod = BaseKVStoreServiceGrpc.getChangeReplicaConfigMethod) == null) {
      synchronized (BaseKVStoreServiceGrpc.class) {
        if ((getChangeReplicaConfigMethod = BaseKVStoreServiceGrpc.getChangeReplicaConfigMethod) == null) {
          BaseKVStoreServiceGrpc.getChangeReplicaConfigMethod = getChangeReplicaConfigMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigRequest, com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "changeReplicaConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigReply.getDefaultInstance()))
              .setSchemaDescriptor(new BaseKVStoreServiceMethodDescriptorSupplier("changeReplicaConfig"))
              .build();
        }
      }
    }
    return getChangeReplicaConfigMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.KVRangeSplitRequest,
      com.zachary.bifromq.basekv.store.proto.KVRangeSplitReply> getSplitMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "split",
      requestType = com.zachary.bifromq.basekv.store.proto.KVRangeSplitRequest.class,
      responseType = com.zachary.bifromq.basekv.store.proto.KVRangeSplitReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.KVRangeSplitRequest,
      com.zachary.bifromq.basekv.store.proto.KVRangeSplitReply> getSplitMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.KVRangeSplitRequest, com.zachary.bifromq.basekv.store.proto.KVRangeSplitReply> getSplitMethod;
    if ((getSplitMethod = BaseKVStoreServiceGrpc.getSplitMethod) == null) {
      synchronized (BaseKVStoreServiceGrpc.class) {
        if ((getSplitMethod = BaseKVStoreServiceGrpc.getSplitMethod) == null) {
          BaseKVStoreServiceGrpc.getSplitMethod = getSplitMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.basekv.store.proto.KVRangeSplitRequest, com.zachary.bifromq.basekv.store.proto.KVRangeSplitReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "split"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.KVRangeSplitRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.KVRangeSplitReply.getDefaultInstance()))
              .setSchemaDescriptor(new BaseKVStoreServiceMethodDescriptorSupplier("split"))
              .build();
        }
      }
    }
    return getSplitMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.KVRangeMergeRequest,
      com.zachary.bifromq.basekv.store.proto.KVRangeMergeReply> getMergeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "merge",
      requestType = com.zachary.bifromq.basekv.store.proto.KVRangeMergeRequest.class,
      responseType = com.zachary.bifromq.basekv.store.proto.KVRangeMergeReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.KVRangeMergeRequest,
      com.zachary.bifromq.basekv.store.proto.KVRangeMergeReply> getMergeMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.KVRangeMergeRequest, com.zachary.bifromq.basekv.store.proto.KVRangeMergeReply> getMergeMethod;
    if ((getMergeMethod = BaseKVStoreServiceGrpc.getMergeMethod) == null) {
      synchronized (BaseKVStoreServiceGrpc.class) {
        if ((getMergeMethod = BaseKVStoreServiceGrpc.getMergeMethod) == null) {
          BaseKVStoreServiceGrpc.getMergeMethod = getMergeMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.basekv.store.proto.KVRangeMergeRequest, com.zachary.bifromq.basekv.store.proto.KVRangeMergeReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "merge"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.KVRangeMergeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.KVRangeMergeReply.getDefaultInstance()))
              .setSchemaDescriptor(new BaseKVStoreServiceMethodDescriptorSupplier("merge"))
              .build();
        }
      }
    }
    return getMergeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest,
      com.zachary.bifromq.basekv.store.proto.KVRangeRWReply> getExecuteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "execute",
      requestType = com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest.class,
      responseType = com.zachary.bifromq.basekv.store.proto.KVRangeRWReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest,
      com.zachary.bifromq.basekv.store.proto.KVRangeRWReply> getExecuteMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest, com.zachary.bifromq.basekv.store.proto.KVRangeRWReply> getExecuteMethod;
    if ((getExecuteMethod = BaseKVStoreServiceGrpc.getExecuteMethod) == null) {
      synchronized (BaseKVStoreServiceGrpc.class) {
        if ((getExecuteMethod = BaseKVStoreServiceGrpc.getExecuteMethod) == null) {
          BaseKVStoreServiceGrpc.getExecuteMethod = getExecuteMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest, com.zachary.bifromq.basekv.store.proto.KVRangeRWReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "execute"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.KVRangeRWReply.getDefaultInstance()))
              .setSchemaDescriptor(new BaseKVStoreServiceMethodDescriptorSupplier("execute"))
              .build();
        }
      }
    }
    return getExecuteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.KVRangeRORequest,
      com.zachary.bifromq.basekv.store.proto.KVRangeROReply> getQueryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "query",
      requestType = com.zachary.bifromq.basekv.store.proto.KVRangeRORequest.class,
      responseType = com.zachary.bifromq.basekv.store.proto.KVRangeROReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.KVRangeRORequest,
      com.zachary.bifromq.basekv.store.proto.KVRangeROReply> getQueryMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.KVRangeRORequest, com.zachary.bifromq.basekv.store.proto.KVRangeROReply> getQueryMethod;
    if ((getQueryMethod = BaseKVStoreServiceGrpc.getQueryMethod) == null) {
      synchronized (BaseKVStoreServiceGrpc.class) {
        if ((getQueryMethod = BaseKVStoreServiceGrpc.getQueryMethod) == null) {
          BaseKVStoreServiceGrpc.getQueryMethod = getQueryMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.basekv.store.proto.KVRangeRORequest, com.zachary.bifromq.basekv.store.proto.KVRangeROReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "query"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.KVRangeRORequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.KVRangeROReply.getDefaultInstance()))
              .setSchemaDescriptor(new BaseKVStoreServiceMethodDescriptorSupplier("query"))
              .build();
        }
      }
    }
    return getQueryMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.KVRangeRORequest,
      com.zachary.bifromq.basekv.store.proto.KVRangeROReply> getLinearizedQueryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "linearizedQuery",
      requestType = com.zachary.bifromq.basekv.store.proto.KVRangeRORequest.class,
      responseType = com.zachary.bifromq.basekv.store.proto.KVRangeROReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.KVRangeRORequest,
      com.zachary.bifromq.basekv.store.proto.KVRangeROReply> getLinearizedQueryMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.basekv.store.proto.KVRangeRORequest, com.zachary.bifromq.basekv.store.proto.KVRangeROReply> getLinearizedQueryMethod;
    if ((getLinearizedQueryMethod = BaseKVStoreServiceGrpc.getLinearizedQueryMethod) == null) {
      synchronized (BaseKVStoreServiceGrpc.class) {
        if ((getLinearizedQueryMethod = BaseKVStoreServiceGrpc.getLinearizedQueryMethod) == null) {
          BaseKVStoreServiceGrpc.getLinearizedQueryMethod = getLinearizedQueryMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.basekv.store.proto.KVRangeRORequest, com.zachary.bifromq.basekv.store.proto.KVRangeROReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "linearizedQuery"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.KVRangeRORequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.basekv.store.proto.KVRangeROReply.getDefaultInstance()))
              .setSchemaDescriptor(new BaseKVStoreServiceMethodDescriptorSupplier("linearizedQuery"))
              .build();
        }
      }
    }
    return getLinearizedQueryMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BaseKVStoreServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BaseKVStoreServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BaseKVStoreServiceStub>() {
        @java.lang.Override
        public BaseKVStoreServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BaseKVStoreServiceStub(channel, callOptions);
        }
      };
    return BaseKVStoreServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BaseKVStoreServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BaseKVStoreServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BaseKVStoreServiceBlockingStub>() {
        @java.lang.Override
        public BaseKVStoreServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BaseKVStoreServiceBlockingStub(channel, callOptions);
        }
      };
    return BaseKVStoreServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BaseKVStoreServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BaseKVStoreServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BaseKVStoreServiceFutureStub>() {
        @java.lang.Override
        public BaseKVStoreServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BaseKVStoreServiceFutureStub(channel, callOptions);
        }
      };
    return BaseKVStoreServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class BaseKVStoreServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void bootstrap(com.zachary.bifromq.basekv.store.proto.BootstrapRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.BootstrapReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getBootstrapMethod(), responseObserver);
    }

    /**
     */
    public void recover(com.zachary.bifromq.basekv.store.proto.RecoverRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.RecoverReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRecoverMethod(), responseObserver);
    }

    /**
     */
    public void transferLeadership(com.zachary.bifromq.basekv.store.proto.TransferLeadershipRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.TransferLeadershipReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getTransferLeadershipMethod(), responseObserver);
    }

    /**
     */
    public void changeReplicaConfig(com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getChangeReplicaConfigMethod(), responseObserver);
    }

    /**
     */
    public void split(com.zachary.bifromq.basekv.store.proto.KVRangeSplitRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeSplitReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSplitMethod(), responseObserver);
    }

    /**
     */
    public void merge(com.zachary.bifromq.basekv.store.proto.KVRangeMergeRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeMergeReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getMergeMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest> execute(
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeRWReply> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getExecuteMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeRORequest> query(
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeROReply> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getQueryMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeRORequest> linearizedQuery(
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeROReply> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getLinearizedQueryMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getBootstrapMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.zachary.bifromq.basekv.store.proto.BootstrapRequest,
                com.zachary.bifromq.basekv.store.proto.BootstrapReply>(
                  this, METHODID_BOOTSTRAP)))
          .addMethod(
            getRecoverMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.zachary.bifromq.basekv.store.proto.RecoverRequest,
                com.zachary.bifromq.basekv.store.proto.RecoverReply>(
                  this, METHODID_RECOVER)))
          .addMethod(
            getTransferLeadershipMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.zachary.bifromq.basekv.store.proto.TransferLeadershipRequest,
                com.zachary.bifromq.basekv.store.proto.TransferLeadershipReply>(
                  this, METHODID_TRANSFER_LEADERSHIP)))
          .addMethod(
            getChangeReplicaConfigMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigRequest,
                com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigReply>(
                  this, METHODID_CHANGE_REPLICA_CONFIG)))
          .addMethod(
            getSplitMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.zachary.bifromq.basekv.store.proto.KVRangeSplitRequest,
                com.zachary.bifromq.basekv.store.proto.KVRangeSplitReply>(
                  this, METHODID_SPLIT)))
          .addMethod(
            getMergeMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.zachary.bifromq.basekv.store.proto.KVRangeMergeRequest,
                com.zachary.bifromq.basekv.store.proto.KVRangeMergeReply>(
                  this, METHODID_MERGE)))
          .addMethod(
            getExecuteMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest,
                com.zachary.bifromq.basekv.store.proto.KVRangeRWReply>(
                  this, METHODID_EXECUTE)))
          .addMethod(
            getQueryMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                com.zachary.bifromq.basekv.store.proto.KVRangeRORequest,
                com.zachary.bifromq.basekv.store.proto.KVRangeROReply>(
                  this, METHODID_QUERY)))
          .addMethod(
            getLinearizedQueryMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                com.zachary.bifromq.basekv.store.proto.KVRangeRORequest,
                com.zachary.bifromq.basekv.store.proto.KVRangeROReply>(
                  this, METHODID_LINEARIZED_QUERY)))
          .build();
    }
  }

  /**
   */
  public static final class BaseKVStoreServiceStub extends io.grpc.stub.AbstractAsyncStub<BaseKVStoreServiceStub> {
    private BaseKVStoreServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BaseKVStoreServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BaseKVStoreServiceStub(channel, callOptions);
    }

    /**
     */
    public void bootstrap(com.zachary.bifromq.basekv.store.proto.BootstrapRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.BootstrapReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getBootstrapMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void recover(com.zachary.bifromq.basekv.store.proto.RecoverRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.RecoverReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRecoverMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void transferLeadership(com.zachary.bifromq.basekv.store.proto.TransferLeadershipRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.TransferLeadershipReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getTransferLeadershipMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void changeReplicaConfig(com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getChangeReplicaConfigMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void split(com.zachary.bifromq.basekv.store.proto.KVRangeSplitRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeSplitReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSplitMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void merge(com.zachary.bifromq.basekv.store.proto.KVRangeMergeRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeMergeReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getMergeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest> execute(
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeRWReply> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getExecuteMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeRORequest> query(
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeROReply> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getQueryMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeRORequest> linearizedQuery(
        io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeROReply> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getLinearizedQueryMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class BaseKVStoreServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<BaseKVStoreServiceBlockingStub> {
    private BaseKVStoreServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BaseKVStoreServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BaseKVStoreServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.zachary.bifromq.basekv.store.proto.BootstrapReply bootstrap(com.zachary.bifromq.basekv.store.proto.BootstrapRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getBootstrapMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.zachary.bifromq.basekv.store.proto.RecoverReply recover(com.zachary.bifromq.basekv.store.proto.RecoverRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRecoverMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.zachary.bifromq.basekv.store.proto.TransferLeadershipReply transferLeadership(com.zachary.bifromq.basekv.store.proto.TransferLeadershipRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getTransferLeadershipMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigReply changeReplicaConfig(com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getChangeReplicaConfigMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.zachary.bifromq.basekv.store.proto.KVRangeSplitReply split(com.zachary.bifromq.basekv.store.proto.KVRangeSplitRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSplitMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.zachary.bifromq.basekv.store.proto.KVRangeMergeReply merge(com.zachary.bifromq.basekv.store.proto.KVRangeMergeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getMergeMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class BaseKVStoreServiceFutureStub extends io.grpc.stub.AbstractFutureStub<BaseKVStoreServiceFutureStub> {
    private BaseKVStoreServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BaseKVStoreServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BaseKVStoreServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.zachary.bifromq.basekv.store.proto.BootstrapReply> bootstrap(
        com.zachary.bifromq.basekv.store.proto.BootstrapRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getBootstrapMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.zachary.bifromq.basekv.store.proto.RecoverReply> recover(
        com.zachary.bifromq.basekv.store.proto.RecoverRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRecoverMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.zachary.bifromq.basekv.store.proto.TransferLeadershipReply> transferLeadership(
        com.zachary.bifromq.basekv.store.proto.TransferLeadershipRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getTransferLeadershipMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigReply> changeReplicaConfig(
        com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getChangeReplicaConfigMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.zachary.bifromq.basekv.store.proto.KVRangeSplitReply> split(
        com.zachary.bifromq.basekv.store.proto.KVRangeSplitRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSplitMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.zachary.bifromq.basekv.store.proto.KVRangeMergeReply> merge(
        com.zachary.bifromq.basekv.store.proto.KVRangeMergeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getMergeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_BOOTSTRAP = 0;
  private static final int METHODID_RECOVER = 1;
  private static final int METHODID_TRANSFER_LEADERSHIP = 2;
  private static final int METHODID_CHANGE_REPLICA_CONFIG = 3;
  private static final int METHODID_SPLIT = 4;
  private static final int METHODID_MERGE = 5;
  private static final int METHODID_EXECUTE = 6;
  private static final int METHODID_QUERY = 7;
  private static final int METHODID_LINEARIZED_QUERY = 8;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final BaseKVStoreServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(BaseKVStoreServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_BOOTSTRAP:
          serviceImpl.bootstrap((com.zachary.bifromq.basekv.store.proto.BootstrapRequest) request,
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.BootstrapReply>) responseObserver);
          break;
        case METHODID_RECOVER:
          serviceImpl.recover((com.zachary.bifromq.basekv.store.proto.RecoverRequest) request,
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.RecoverReply>) responseObserver);
          break;
        case METHODID_TRANSFER_LEADERSHIP:
          serviceImpl.transferLeadership((com.zachary.bifromq.basekv.store.proto.TransferLeadershipRequest) request,
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.TransferLeadershipReply>) responseObserver);
          break;
        case METHODID_CHANGE_REPLICA_CONFIG:
          serviceImpl.changeReplicaConfig((com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigRequest) request,
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigReply>) responseObserver);
          break;
        case METHODID_SPLIT:
          serviceImpl.split((com.zachary.bifromq.basekv.store.proto.KVRangeSplitRequest) request,
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeSplitReply>) responseObserver);
          break;
        case METHODID_MERGE:
          serviceImpl.merge((com.zachary.bifromq.basekv.store.proto.KVRangeMergeRequest) request,
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeMergeReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_EXECUTE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.execute(
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeRWReply>) responseObserver);
        case METHODID_QUERY:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.query(
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeROReply>) responseObserver);
        case METHODID_LINEARIZED_QUERY:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.linearizedQuery(
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.basekv.store.proto.KVRangeROReply>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class BaseKVStoreServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BaseKVStoreServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.zachary.bifromq.basekv.store.proto.BaseKVStoreServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BaseKVStoreService");
    }
  }

  private static final class BaseKVStoreServiceFileDescriptorSupplier
      extends BaseKVStoreServiceBaseDescriptorSupplier {
    BaseKVStoreServiceFileDescriptorSupplier() {}
  }

  private static final class BaseKVStoreServiceMethodDescriptorSupplier
      extends BaseKVStoreServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    BaseKVStoreServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (BaseKVStoreServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BaseKVStoreServiceFileDescriptorSupplier())
              .addMethod(getBootstrapMethod())
              .addMethod(getRecoverMethod())
              .addMethod(getTransferLeadershipMethod())
              .addMethod(getChangeReplicaConfigMethod())
              .addMethod(getSplitMethod())
              .addMethod(getMergeMethod())
              .addMethod(getExecuteMethod())
              .addMethod(getQueryMethod())
              .addMethod(getLinearizedQueryMethod())
              .build();
        }
      }
    }
    return result;
  }
}
