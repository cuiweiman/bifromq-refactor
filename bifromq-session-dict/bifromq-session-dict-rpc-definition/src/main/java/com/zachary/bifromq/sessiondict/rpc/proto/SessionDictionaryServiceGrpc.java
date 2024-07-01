package com.zachary.bifromq.sessiondict.rpc.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.53.0)",
    comments = "Source: sessiondict/SessionDictionaryService.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class SessionDictionaryServiceGrpc {

  private SessionDictionaryServiceGrpc() {}

  public static final String SERVICE_NAME = "sessiondict.SessionDictionaryService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.sessiondict.rpc.proto.Ping,
      com.zachary.bifromq.sessiondict.rpc.proto.Quit> getJoinMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "join",
      requestType = com.zachary.bifromq.sessiondict.rpc.proto.Ping.class,
      responseType = com.zachary.bifromq.sessiondict.rpc.proto.Quit.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.sessiondict.rpc.proto.Ping,
      com.zachary.bifromq.sessiondict.rpc.proto.Quit> getJoinMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.sessiondict.rpc.proto.Ping, com.zachary.bifromq.sessiondict.rpc.proto.Quit> getJoinMethod;
    if ((getJoinMethod = SessionDictionaryServiceGrpc.getJoinMethod) == null) {
      synchronized (SessionDictionaryServiceGrpc.class) {
        if ((getJoinMethod = SessionDictionaryServiceGrpc.getJoinMethod) == null) {
          SessionDictionaryServiceGrpc.getJoinMethod = getJoinMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.sessiondict.rpc.proto.Ping, com.zachary.bifromq.sessiondict.rpc.proto.Quit>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "join"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.sessiondict.rpc.proto.Ping.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.sessiondict.rpc.proto.Quit.getDefaultInstance()))
              .setSchemaDescriptor(new SessionDictionaryServiceMethodDescriptorSupplier("join"))
              .build();
        }
      }
    }
    return getJoinMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.sessiondict.rpc.proto.KillRequest,
      com.zachary.bifromq.sessiondict.rpc.proto.KillReply> getKillMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "kill",
      requestType = com.zachary.bifromq.sessiondict.rpc.proto.KillRequest.class,
      responseType = com.zachary.bifromq.sessiondict.rpc.proto.KillReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.sessiondict.rpc.proto.KillRequest,
      com.zachary.bifromq.sessiondict.rpc.proto.KillReply> getKillMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.sessiondict.rpc.proto.KillRequest, com.zachary.bifromq.sessiondict.rpc.proto.KillReply> getKillMethod;
    if ((getKillMethod = SessionDictionaryServiceGrpc.getKillMethod) == null) {
      synchronized (SessionDictionaryServiceGrpc.class) {
        if ((getKillMethod = SessionDictionaryServiceGrpc.getKillMethod) == null) {
          SessionDictionaryServiceGrpc.getKillMethod = getKillMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.sessiondict.rpc.proto.KillRequest, com.zachary.bifromq.sessiondict.rpc.proto.KillReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "kill"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.sessiondict.rpc.proto.KillRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.sessiondict.rpc.proto.KillReply.getDefaultInstance()))
              .setSchemaDescriptor(new SessionDictionaryServiceMethodDescriptorSupplier("kill"))
              .build();
        }
      }
    }
    return getKillMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SessionDictionaryServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SessionDictionaryServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SessionDictionaryServiceStub>() {
        @java.lang.Override
        public SessionDictionaryServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SessionDictionaryServiceStub(channel, callOptions);
        }
      };
    return SessionDictionaryServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SessionDictionaryServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SessionDictionaryServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SessionDictionaryServiceBlockingStub>() {
        @java.lang.Override
        public SessionDictionaryServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SessionDictionaryServiceBlockingStub(channel, callOptions);
        }
      };
    return SessionDictionaryServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SessionDictionaryServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SessionDictionaryServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SessionDictionaryServiceFutureStub>() {
        @java.lang.Override
        public SessionDictionaryServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SessionDictionaryServiceFutureStub(channel, callOptions);
        }
      };
    return SessionDictionaryServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class SessionDictionaryServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<com.zachary.bifromq.sessiondict.rpc.proto.Ping> join(
        io.grpc.stub.StreamObserver<com.zachary.bifromq.sessiondict.rpc.proto.Quit> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getJoinMethod(), responseObserver);
    }

    /**
     */
    public void kill(com.zachary.bifromq.sessiondict.rpc.proto.KillRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.sessiondict.rpc.proto.KillReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getKillMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getJoinMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                com.zachary.bifromq.sessiondict.rpc.proto.Ping,
                com.zachary.bifromq.sessiondict.rpc.proto.Quit>(
                  this, METHODID_JOIN)))
          .addMethod(
            getKillMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.zachary.bifromq.sessiondict.rpc.proto.KillRequest,
                com.zachary.bifromq.sessiondict.rpc.proto.KillReply>(
                  this, METHODID_KILL)))
          .build();
    }
  }

  /**
   */
  public static final class SessionDictionaryServiceStub extends io.grpc.stub.AbstractAsyncStub<SessionDictionaryServiceStub> {
    private SessionDictionaryServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SessionDictionaryServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SessionDictionaryServiceStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.zachary.bifromq.sessiondict.rpc.proto.Ping> join(
        io.grpc.stub.StreamObserver<com.zachary.bifromq.sessiondict.rpc.proto.Quit> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getJoinMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public void kill(com.zachary.bifromq.sessiondict.rpc.proto.KillRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.sessiondict.rpc.proto.KillReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getKillMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class SessionDictionaryServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<SessionDictionaryServiceBlockingStub> {
    private SessionDictionaryServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SessionDictionaryServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SessionDictionaryServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.zachary.bifromq.sessiondict.rpc.proto.KillReply kill(com.zachary.bifromq.sessiondict.rpc.proto.KillRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getKillMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class SessionDictionaryServiceFutureStub extends io.grpc.stub.AbstractFutureStub<SessionDictionaryServiceFutureStub> {
    private SessionDictionaryServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SessionDictionaryServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SessionDictionaryServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.zachary.bifromq.sessiondict.rpc.proto.KillReply> kill(
        com.zachary.bifromq.sessiondict.rpc.proto.KillRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getKillMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_KILL = 0;
  private static final int METHODID_JOIN = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SessionDictionaryServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(SessionDictionaryServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_KILL:
          serviceImpl.kill((com.zachary.bifromq.sessiondict.rpc.proto.KillRequest) request,
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.sessiondict.rpc.proto.KillReply>) responseObserver);
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
        case METHODID_JOIN:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.join(
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.sessiondict.rpc.proto.Quit>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class SessionDictionaryServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SessionDictionaryServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.zachary.bifromq.sessiondict.rpc.proto.SessionDictionaryServiceProtos.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SessionDictionaryService");
    }
  }

  private static final class SessionDictionaryServiceFileDescriptorSupplier
      extends SessionDictionaryServiceBaseDescriptorSupplier {
    SessionDictionaryServiceFileDescriptorSupplier() {}
  }

  private static final class SessionDictionaryServiceMethodDescriptorSupplier
      extends SessionDictionaryServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    SessionDictionaryServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (SessionDictionaryServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SessionDictionaryServiceFileDescriptorSupplier())
              .addMethod(getJoinMethod())
              .addMethod(getKillMethod())
              .build();
        }
      }
    }
    return result;
  }
}
