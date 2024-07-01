package com.zachary.bifromq.dist.rpc.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.53.0)",
    comments = "Source: distservice/DistService.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DistServiceGrpc {

  private DistServiceGrpc() {}

  public static final String SERVICE_NAME = "distservice.DistService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.dist.rpc.proto.SubRequest,
      com.zachary.bifromq.dist.rpc.proto.SubReply> getSubMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sub",
      requestType = com.zachary.bifromq.dist.rpc.proto.SubRequest.class,
      responseType = com.zachary.bifromq.dist.rpc.proto.SubReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.dist.rpc.proto.SubRequest,
      com.zachary.bifromq.dist.rpc.proto.SubReply> getSubMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.dist.rpc.proto.SubRequest, com.zachary.bifromq.dist.rpc.proto.SubReply> getSubMethod;
    if ((getSubMethod = DistServiceGrpc.getSubMethod) == null) {
      synchronized (DistServiceGrpc.class) {
        if ((getSubMethod = DistServiceGrpc.getSubMethod) == null) {
          DistServiceGrpc.getSubMethod = getSubMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.dist.rpc.proto.SubRequest, com.zachary.bifromq.dist.rpc.proto.SubReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sub"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.dist.rpc.proto.SubRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.dist.rpc.proto.SubReply.getDefaultInstance()))
              .setSchemaDescriptor(new DistServiceMethodDescriptorSupplier("sub"))
              .build();
        }
      }
    }
    return getSubMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.dist.rpc.proto.UnsubRequest,
      com.zachary.bifromq.dist.rpc.proto.UnsubReply> getUnsubMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "unsub",
      requestType = com.zachary.bifromq.dist.rpc.proto.UnsubRequest.class,
      responseType = com.zachary.bifromq.dist.rpc.proto.UnsubReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.dist.rpc.proto.UnsubRequest,
      com.zachary.bifromq.dist.rpc.proto.UnsubReply> getUnsubMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.dist.rpc.proto.UnsubRequest, com.zachary.bifromq.dist.rpc.proto.UnsubReply> getUnsubMethod;
    if ((getUnsubMethod = DistServiceGrpc.getUnsubMethod) == null) {
      synchronized (DistServiceGrpc.class) {
        if ((getUnsubMethod = DistServiceGrpc.getUnsubMethod) == null) {
          DistServiceGrpc.getUnsubMethod = getUnsubMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.dist.rpc.proto.UnsubRequest, com.zachary.bifromq.dist.rpc.proto.UnsubReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "unsub"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.dist.rpc.proto.UnsubRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.dist.rpc.proto.UnsubReply.getDefaultInstance()))
              .setSchemaDescriptor(new DistServiceMethodDescriptorSupplier("unsub"))
              .build();
        }
      }
    }
    return getUnsubMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.dist.rpc.proto.ClearRequest,
      com.zachary.bifromq.dist.rpc.proto.ClearReply> getClearMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "clear",
      requestType = com.zachary.bifromq.dist.rpc.proto.ClearRequest.class,
      responseType = com.zachary.bifromq.dist.rpc.proto.ClearReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.dist.rpc.proto.ClearRequest,
      com.zachary.bifromq.dist.rpc.proto.ClearReply> getClearMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.dist.rpc.proto.ClearRequest, com.zachary.bifromq.dist.rpc.proto.ClearReply> getClearMethod;
    if ((getClearMethod = DistServiceGrpc.getClearMethod) == null) {
      synchronized (DistServiceGrpc.class) {
        if ((getClearMethod = DistServiceGrpc.getClearMethod) == null) {
          DistServiceGrpc.getClearMethod = getClearMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.dist.rpc.proto.ClearRequest, com.zachary.bifromq.dist.rpc.proto.ClearReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "clear"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.dist.rpc.proto.ClearRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.dist.rpc.proto.ClearReply.getDefaultInstance()))
              .setSchemaDescriptor(new DistServiceMethodDescriptorSupplier("clear"))
              .build();
        }
      }
    }
    return getClearMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.dist.rpc.proto.DistRequest,
      com.zachary.bifromq.dist.rpc.proto.DistReply> getDistMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "dist",
      requestType = com.zachary.bifromq.dist.rpc.proto.DistRequest.class,
      responseType = com.zachary.bifromq.dist.rpc.proto.DistReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.dist.rpc.proto.DistRequest,
      com.zachary.bifromq.dist.rpc.proto.DistReply> getDistMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.dist.rpc.proto.DistRequest, com.zachary.bifromq.dist.rpc.proto.DistReply> getDistMethod;
    if ((getDistMethod = DistServiceGrpc.getDistMethod) == null) {
      synchronized (DistServiceGrpc.class) {
        if ((getDistMethod = DistServiceGrpc.getDistMethod) == null) {
          DistServiceGrpc.getDistMethod = getDistMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.dist.rpc.proto.DistRequest, com.zachary.bifromq.dist.rpc.proto.DistReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "dist"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.dist.rpc.proto.DistRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.dist.rpc.proto.DistReply.getDefaultInstance()))
              .setSchemaDescriptor(new DistServiceMethodDescriptorSupplier("dist"))
              .build();
        }
      }
    }
    return getDistMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DistServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DistServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DistServiceStub>() {
        @java.lang.Override
        public DistServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DistServiceStub(channel, callOptions);
        }
      };
    return DistServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DistServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DistServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DistServiceBlockingStub>() {
        @java.lang.Override
        public DistServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DistServiceBlockingStub(channel, callOptions);
        }
      };
    return DistServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DistServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DistServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DistServiceFutureStub>() {
        @java.lang.Override
        public DistServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DistServiceFutureStub(channel, callOptions);
        }
      };
    return DistServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class DistServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void sub(com.zachary.bifromq.dist.rpc.proto.SubRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.dist.rpc.proto.SubReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSubMethod(), responseObserver);
    }

    /**
     */
    public void unsub(com.zachary.bifromq.dist.rpc.proto.UnsubRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.dist.rpc.proto.UnsubReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUnsubMethod(), responseObserver);
    }

    /**
     */
    public void clear(com.zachary.bifromq.dist.rpc.proto.ClearRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.dist.rpc.proto.ClearReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getClearMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.zachary.bifromq.dist.rpc.proto.DistRequest> dist(
        io.grpc.stub.StreamObserver<com.zachary.bifromq.dist.rpc.proto.DistReply> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getDistMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSubMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.zachary.bifromq.dist.rpc.proto.SubRequest,
                com.zachary.bifromq.dist.rpc.proto.SubReply>(
                  this, METHODID_SUB)))
          .addMethod(
            getUnsubMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.zachary.bifromq.dist.rpc.proto.UnsubRequest,
                com.zachary.bifromq.dist.rpc.proto.UnsubReply>(
                  this, METHODID_UNSUB)))
          .addMethod(
            getClearMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.zachary.bifromq.dist.rpc.proto.ClearRequest,
                com.zachary.bifromq.dist.rpc.proto.ClearReply>(
                  this, METHODID_CLEAR)))
          .addMethod(
            getDistMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                com.zachary.bifromq.dist.rpc.proto.DistRequest,
                com.zachary.bifromq.dist.rpc.proto.DistReply>(
                  this, METHODID_DIST)))
          .build();
    }
  }

  /**
   */
  public static final class DistServiceStub extends io.grpc.stub.AbstractAsyncStub<DistServiceStub> {
    private DistServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DistServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DistServiceStub(channel, callOptions);
    }

    /**
     */
    public void sub(com.zachary.bifromq.dist.rpc.proto.SubRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.dist.rpc.proto.SubReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSubMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void unsub(com.zachary.bifromq.dist.rpc.proto.UnsubRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.dist.rpc.proto.UnsubReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUnsubMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void clear(com.zachary.bifromq.dist.rpc.proto.ClearRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.dist.rpc.proto.ClearReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getClearMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.zachary.bifromq.dist.rpc.proto.DistRequest> dist(
        io.grpc.stub.StreamObserver<com.zachary.bifromq.dist.rpc.proto.DistReply> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getDistMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class DistServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<DistServiceBlockingStub> {
    private DistServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DistServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DistServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.zachary.bifromq.dist.rpc.proto.SubReply sub(com.zachary.bifromq.dist.rpc.proto.SubRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSubMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.zachary.bifromq.dist.rpc.proto.UnsubReply unsub(com.zachary.bifromq.dist.rpc.proto.UnsubRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUnsubMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.zachary.bifromq.dist.rpc.proto.ClearReply clear(com.zachary.bifromq.dist.rpc.proto.ClearRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getClearMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class DistServiceFutureStub extends io.grpc.stub.AbstractFutureStub<DistServiceFutureStub> {
    private DistServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DistServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DistServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.zachary.bifromq.dist.rpc.proto.SubReply> sub(
        com.zachary.bifromq.dist.rpc.proto.SubRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSubMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.zachary.bifromq.dist.rpc.proto.UnsubReply> unsub(
        com.zachary.bifromq.dist.rpc.proto.UnsubRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUnsubMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.zachary.bifromq.dist.rpc.proto.ClearReply> clear(
        com.zachary.bifromq.dist.rpc.proto.ClearRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getClearMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SUB = 0;
  private static final int METHODID_UNSUB = 1;
  private static final int METHODID_CLEAR = 2;
  private static final int METHODID_DIST = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final DistServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DistServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SUB:
          serviceImpl.sub((com.zachary.bifromq.dist.rpc.proto.SubRequest) request,
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.dist.rpc.proto.SubReply>) responseObserver);
          break;
        case METHODID_UNSUB:
          serviceImpl.unsub((com.zachary.bifromq.dist.rpc.proto.UnsubRequest) request,
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.dist.rpc.proto.UnsubReply>) responseObserver);
          break;
        case METHODID_CLEAR:
          serviceImpl.clear((com.zachary.bifromq.dist.rpc.proto.ClearRequest) request,
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.dist.rpc.proto.ClearReply>) responseObserver);
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
        case METHODID_DIST:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.dist(
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.dist.rpc.proto.DistReply>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class DistServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DistServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.zachary.bifromq.dist.rpc.proto.DistServiceProtos.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DistService");
    }
  }

  private static final class DistServiceFileDescriptorSupplier
      extends DistServiceBaseDescriptorSupplier {
    DistServiceFileDescriptorSupplier() {}
  }

  private static final class DistServiceMethodDescriptorSupplier
      extends DistServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    DistServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (DistServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DistServiceFileDescriptorSupplier())
              .addMethod(getSubMethod())
              .addMethod(getUnsubMethod())
              .addMethod(getClearMethod())
              .addMethod(getDistMethod())
              .build();
        }
      }
    }
    return result;
  }
}
