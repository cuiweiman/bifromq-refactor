package com.zachary.bifromq.mqtt.inbox.rpc.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.53.0)",
    comments = "Source: mqttbroker/MessageReceiver.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class OnlineInboxBrokerGrpc {

  private OnlineInboxBrokerGrpc() {}

  public static final String SERVICE_NAME = "mqttbroker.OnlineInboxBroker";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteRequest,
      com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteReply> getWriteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "write",
      requestType = com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteRequest.class,
      responseType = com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteRequest,
      com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteReply> getWriteMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteRequest, com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteReply> getWriteMethod;
    if ((getWriteMethod = OnlineInboxBrokerGrpc.getWriteMethod) == null) {
      synchronized (OnlineInboxBrokerGrpc.class) {
        if ((getWriteMethod = OnlineInboxBrokerGrpc.getWriteMethod) == null) {
          OnlineInboxBrokerGrpc.getWriteMethod = getWriteMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteRequest, com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "write"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteReply.getDefaultInstance()))
              .setSchemaDescriptor(new OnlineInboxBrokerMethodDescriptorSupplier("write"))
              .build();
        }
      }
    }
    return getWriteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxRequest,
      com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxReply> getHasInboxMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "hasInbox",
      requestType = com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxRequest.class,
      responseType = com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxRequest,
      com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxReply> getHasInboxMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxRequest, com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxReply> getHasInboxMethod;
    if ((getHasInboxMethod = OnlineInboxBrokerGrpc.getHasInboxMethod) == null) {
      synchronized (OnlineInboxBrokerGrpc.class) {
        if ((getHasInboxMethod = OnlineInboxBrokerGrpc.getHasInboxMethod) == null) {
          OnlineInboxBrokerGrpc.getHasInboxMethod = getHasInboxMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxRequest, com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "hasInbox"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxReply.getDefaultInstance()))
              .setSchemaDescriptor(new OnlineInboxBrokerMethodDescriptorSupplier("hasInbox"))
              .build();
        }
      }
    }
    return getHasInboxMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static OnlineInboxBrokerStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OnlineInboxBrokerStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OnlineInboxBrokerStub>() {
        @java.lang.Override
        public OnlineInboxBrokerStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OnlineInboxBrokerStub(channel, callOptions);
        }
      };
    return OnlineInboxBrokerStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static OnlineInboxBrokerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OnlineInboxBrokerBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OnlineInboxBrokerBlockingStub>() {
        @java.lang.Override
        public OnlineInboxBrokerBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OnlineInboxBrokerBlockingStub(channel, callOptions);
        }
      };
    return OnlineInboxBrokerBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static OnlineInboxBrokerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OnlineInboxBrokerFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OnlineInboxBrokerFutureStub>() {
        @java.lang.Override
        public OnlineInboxBrokerFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OnlineInboxBrokerFutureStub(channel, callOptions);
        }
      };
    return OnlineInboxBrokerFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class OnlineInboxBrokerImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteRequest> write(
        io.grpc.stub.StreamObserver<com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteReply> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getWriteMethod(), responseObserver);
    }

    /**
     */
    public void hasInbox(com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHasInboxMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getWriteMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteRequest,
                com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteReply>(
                  this, METHODID_WRITE)))
          .addMethod(
            getHasInboxMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxRequest,
                com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxReply>(
                  this, METHODID_HAS_INBOX)))
          .build();
    }
  }

  /**
   */
  public static final class OnlineInboxBrokerStub extends io.grpc.stub.AbstractAsyncStub<OnlineInboxBrokerStub> {
    private OnlineInboxBrokerStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OnlineInboxBrokerStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OnlineInboxBrokerStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteRequest> write(
        io.grpc.stub.StreamObserver<com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteReply> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getWriteMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public void hasInbox(com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHasInboxMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class OnlineInboxBrokerBlockingStub extends io.grpc.stub.AbstractBlockingStub<OnlineInboxBrokerBlockingStub> {
    private OnlineInboxBrokerBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OnlineInboxBrokerBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OnlineInboxBrokerBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxReply hasInbox(com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHasInboxMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class OnlineInboxBrokerFutureStub extends io.grpc.stub.AbstractFutureStub<OnlineInboxBrokerFutureStub> {
    private OnlineInboxBrokerFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OnlineInboxBrokerFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OnlineInboxBrokerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxReply> hasInbox(
        com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHasInboxMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_HAS_INBOX = 0;
  private static final int METHODID_WRITE = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final OnlineInboxBrokerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(OnlineInboxBrokerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_HAS_INBOX:
          serviceImpl.hasInbox((com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxRequest) request,
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.mqtt.inbox.rpc.proto.HasInboxReply>) responseObserver);
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
        case METHODID_WRITE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.write(
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.mqtt.inbox.rpc.proto.WriteReply>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class OnlineInboxBrokerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    OnlineInboxBrokerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.zachary.bifromq.mqtt.inbox.rpc.proto.MessageReceiverProtos.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("OnlineInboxBroker");
    }
  }

  private static final class OnlineInboxBrokerFileDescriptorSupplier
      extends OnlineInboxBrokerBaseDescriptorSupplier {
    OnlineInboxBrokerFileDescriptorSupplier() {}
  }

  private static final class OnlineInboxBrokerMethodDescriptorSupplier
      extends OnlineInboxBrokerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    OnlineInboxBrokerMethodDescriptorSupplier(String methodName) {
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
      synchronized (OnlineInboxBrokerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new OnlineInboxBrokerFileDescriptorSupplier())
              .addMethod(getWriteMethod())
              .addMethod(getHasInboxMethod())
              .build();
        }
      }
    }
    return result;
  }
}
