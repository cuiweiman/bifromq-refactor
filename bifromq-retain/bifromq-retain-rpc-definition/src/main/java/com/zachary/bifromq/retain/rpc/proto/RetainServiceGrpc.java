package com.zachary.bifromq.retain.rpc.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.53.0)",
    comments = "Source: retainservice/RetainService.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class RetainServiceGrpc {

  private RetainServiceGrpc() {}

  public static final String SERVICE_NAME = "retainservice.RetainService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<RetainRequest,
          RetainReply> getRetainMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "retain",
      requestType = RetainRequest.class,
      responseType = RetainReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<RetainRequest,
          RetainReply> getRetainMethod() {
    io.grpc.MethodDescriptor<RetainRequest, RetainReply> getRetainMethod;
    if ((getRetainMethod = RetainServiceGrpc.getRetainMethod) == null) {
      synchronized (RetainServiceGrpc.class) {
        if ((getRetainMethod = RetainServiceGrpc.getRetainMethod) == null) {
          RetainServiceGrpc.getRetainMethod = getRetainMethod =
              io.grpc.MethodDescriptor.<RetainRequest, RetainReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "retain"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RetainRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RetainReply.getDefaultInstance()))
              .setSchemaDescriptor(new RetainServiceMethodDescriptorSupplier("retain"))
              .build();
        }
      }
    }
    return getRetainMethod;
  }

  private static volatile io.grpc.MethodDescriptor<MatchRequest,
          MatchReply> getMatchMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "match",
      requestType = MatchRequest.class,
      responseType = MatchReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<MatchRequest,
          MatchReply> getMatchMethod() {
    io.grpc.MethodDescriptor<MatchRequest, MatchReply> getMatchMethod;
    if ((getMatchMethod = RetainServiceGrpc.getMatchMethod) == null) {
      synchronized (RetainServiceGrpc.class) {
        if ((getMatchMethod = RetainServiceGrpc.getMatchMethod) == null) {
          RetainServiceGrpc.getMatchMethod = getMatchMethod =
              io.grpc.MethodDescriptor.<MatchRequest, MatchReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "match"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  MatchRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  MatchReply.getDefaultInstance()))
              .setSchemaDescriptor(new RetainServiceMethodDescriptorSupplier("match"))
              .build();
        }
      }
    }
    return getMatchMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RetainServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RetainServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RetainServiceStub>() {
        @java.lang.Override
        public RetainServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RetainServiceStub(channel, callOptions);
        }
      };
    return RetainServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RetainServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RetainServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RetainServiceBlockingStub>() {
        @java.lang.Override
        public RetainServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RetainServiceBlockingStub(channel, callOptions);
        }
      };
    return RetainServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RetainServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RetainServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RetainServiceFutureStub>() {
        @java.lang.Override
        public RetainServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RetainServiceFutureStub(channel, callOptions);
        }
      };
    return RetainServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class RetainServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<RetainRequest> retain(
        io.grpc.stub.StreamObserver<RetainReply> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getRetainMethod(), responseObserver);
    }

    /**
     */
    public void match(MatchRequest request,
                      io.grpc.stub.StreamObserver<MatchReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getMatchMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRetainMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                      RetainRequest,
                      RetainReply>(
                  this, METHODID_RETAIN)))
          .addMethod(
            getMatchMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                      MatchRequest,
                      MatchReply>(
                  this, METHODID_MATCH)))
          .build();
    }
  }

  /**
   */
  public static final class RetainServiceStub extends io.grpc.stub.AbstractAsyncStub<RetainServiceStub> {
    private RetainServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RetainServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RetainServiceStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<RetainRequest> retain(
        io.grpc.stub.StreamObserver<RetainReply> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getRetainMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public void match(MatchRequest request,
                      io.grpc.stub.StreamObserver<MatchReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getMatchMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RetainServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<RetainServiceBlockingStub> {
    private RetainServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RetainServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RetainServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public MatchReply match(MatchRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getMatchMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RetainServiceFutureStub extends io.grpc.stub.AbstractFutureStub<RetainServiceFutureStub> {
    private RetainServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RetainServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RetainServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<MatchReply> match(
        MatchRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getMatchMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_MATCH = 0;
  private static final int METHODID_RETAIN = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RetainServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RetainServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_MATCH:
          serviceImpl.match((MatchRequest) request,
              (io.grpc.stub.StreamObserver<MatchReply>) responseObserver);
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
        case METHODID_RETAIN:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.retain(
              (io.grpc.stub.StreamObserver<RetainReply>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RetainServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RetainServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return RetainServiceProtos.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RetainService");
    }
  }

  private static final class RetainServiceFileDescriptorSupplier
      extends RetainServiceBaseDescriptorSupplier {
    RetainServiceFileDescriptorSupplier() {}
  }

  private static final class RetainServiceMethodDescriptorSupplier
      extends RetainServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RetainServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (RetainServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RetainServiceFileDescriptorSupplier())
              .addMethod(getRetainMethod())
              .addMethod(getMatchMethod())
              .build();
        }
      }
    }
    return result;
  }
}
