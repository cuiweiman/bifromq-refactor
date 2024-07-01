package com.zachary.bifromq.inbox.rpc.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.53.0)",
    comments = "Source: inboxservice/InboxService.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class InboxServiceGrpc {

  private InboxServiceGrpc() {}

  public static final String SERVICE_NAME = "inboxservice.InboxService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.HasInboxRequest,
      com.zachary.bifromq.inbox.rpc.proto.HasInboxReply> getHasInboxMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "hasInbox",
      requestType = com.zachary.bifromq.inbox.rpc.proto.HasInboxRequest.class,
      responseType = com.zachary.bifromq.inbox.rpc.proto.HasInboxReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.HasInboxRequest,
      com.zachary.bifromq.inbox.rpc.proto.HasInboxReply> getHasInboxMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.HasInboxRequest, com.zachary.bifromq.inbox.rpc.proto.HasInboxReply> getHasInboxMethod;
    if ((getHasInboxMethod = InboxServiceGrpc.getHasInboxMethod) == null) {
      synchronized (InboxServiceGrpc.class) {
        if ((getHasInboxMethod = InboxServiceGrpc.getHasInboxMethod) == null) {
          InboxServiceGrpc.getHasInboxMethod = getHasInboxMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.inbox.rpc.proto.HasInboxRequest, com.zachary.bifromq.inbox.rpc.proto.HasInboxReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "hasInbox"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.inbox.rpc.proto.HasInboxRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.inbox.rpc.proto.HasInboxReply.getDefaultInstance()))
              .setSchemaDescriptor(new InboxServiceMethodDescriptorSupplier("hasInbox"))
              .build();
        }
      }
    }
    return getHasInboxMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.CreateInboxRequest,
      com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply> getCreateInboxMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "createInbox",
      requestType = com.zachary.bifromq.inbox.rpc.proto.CreateInboxRequest.class,
      responseType = com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.CreateInboxRequest,
      com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply> getCreateInboxMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.CreateInboxRequest, com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply> getCreateInboxMethod;
    if ((getCreateInboxMethod = InboxServiceGrpc.getCreateInboxMethod) == null) {
      synchronized (InboxServiceGrpc.class) {
        if ((getCreateInboxMethod = InboxServiceGrpc.getCreateInboxMethod) == null) {
          InboxServiceGrpc.getCreateInboxMethod = getCreateInboxMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.inbox.rpc.proto.CreateInboxRequest, com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "createInbox"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.inbox.rpc.proto.CreateInboxRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply.getDefaultInstance()))
              .setSchemaDescriptor(new InboxServiceMethodDescriptorSupplier("createInbox"))
              .build();
        }
      }
    }
    return getCreateInboxMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.DeleteInboxRequest,
      com.zachary.bifromq.inbox.rpc.proto.DeleteInboxReply> getDeleteInboxMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "deleteInbox",
      requestType = com.zachary.bifromq.inbox.rpc.proto.DeleteInboxRequest.class,
      responseType = com.zachary.bifromq.inbox.rpc.proto.DeleteInboxReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.DeleteInboxRequest,
      com.zachary.bifromq.inbox.rpc.proto.DeleteInboxReply> getDeleteInboxMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.DeleteInboxRequest, com.zachary.bifromq.inbox.rpc.proto.DeleteInboxReply> getDeleteInboxMethod;
    if ((getDeleteInboxMethod = InboxServiceGrpc.getDeleteInboxMethod) == null) {
      synchronized (InboxServiceGrpc.class) {
        if ((getDeleteInboxMethod = InboxServiceGrpc.getDeleteInboxMethod) == null) {
          InboxServiceGrpc.getDeleteInboxMethod = getDeleteInboxMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.inbox.rpc.proto.DeleteInboxRequest, com.zachary.bifromq.inbox.rpc.proto.DeleteInboxReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "deleteInbox"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.inbox.rpc.proto.DeleteInboxRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.inbox.rpc.proto.DeleteInboxReply.getDefaultInstance()))
              .setSchemaDescriptor(new InboxServiceMethodDescriptorSupplier("deleteInbox"))
              .build();
        }
      }
    }
    return getDeleteInboxMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.SendRequest,
      com.zachary.bifromq.inbox.rpc.proto.SendReply> getReceiveMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "receive",
      requestType = com.zachary.bifromq.inbox.rpc.proto.SendRequest.class,
      responseType = com.zachary.bifromq.inbox.rpc.proto.SendReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.SendRequest,
      com.zachary.bifromq.inbox.rpc.proto.SendReply> getReceiveMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.SendRequest, com.zachary.bifromq.inbox.rpc.proto.SendReply> getReceiveMethod;
    if ((getReceiveMethod = InboxServiceGrpc.getReceiveMethod) == null) {
      synchronized (InboxServiceGrpc.class) {
        if ((getReceiveMethod = InboxServiceGrpc.getReceiveMethod) == null) {
          InboxServiceGrpc.getReceiveMethod = getReceiveMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.inbox.rpc.proto.SendRequest, com.zachary.bifromq.inbox.rpc.proto.SendReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "receive"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.inbox.rpc.proto.SendRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.inbox.rpc.proto.SendReply.getDefaultInstance()))
              .setSchemaDescriptor(new InboxServiceMethodDescriptorSupplier("receive"))
              .build();
        }
      }
    }
    return getReceiveMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.FetchHint,
      com.zachary.bifromq.inbox.storage.proto.Fetched> getFetchMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "fetch",
      requestType = com.zachary.bifromq.inbox.rpc.proto.FetchHint.class,
      responseType = com.zachary.bifromq.inbox.storage.proto.Fetched.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.FetchHint,
      com.zachary.bifromq.inbox.storage.proto.Fetched> getFetchMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.FetchHint, com.zachary.bifromq.inbox.storage.proto.Fetched> getFetchMethod;
    if ((getFetchMethod = InboxServiceGrpc.getFetchMethod) == null) {
      synchronized (InboxServiceGrpc.class) {
        if ((getFetchMethod = InboxServiceGrpc.getFetchMethod) == null) {
          InboxServiceGrpc.getFetchMethod = getFetchMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.inbox.rpc.proto.FetchHint, com.zachary.bifromq.inbox.storage.proto.Fetched>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "fetch"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.inbox.rpc.proto.FetchHint.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.inbox.storage.proto.Fetched.getDefaultInstance()))
              .setSchemaDescriptor(new InboxServiceMethodDescriptorSupplier("fetch"))
              .build();
        }
      }
    }
    return getFetchMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.CommitRequest,
      com.zachary.bifromq.inbox.rpc.proto.CommitReply> getCommitMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "commit",
      requestType = com.zachary.bifromq.inbox.rpc.proto.CommitRequest.class,
      responseType = com.zachary.bifromq.inbox.rpc.proto.CommitReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.CommitRequest,
      com.zachary.bifromq.inbox.rpc.proto.CommitReply> getCommitMethod() {
    io.grpc.MethodDescriptor<com.zachary.bifromq.inbox.rpc.proto.CommitRequest, com.zachary.bifromq.inbox.rpc.proto.CommitReply> getCommitMethod;
    if ((getCommitMethod = InboxServiceGrpc.getCommitMethod) == null) {
      synchronized (InboxServiceGrpc.class) {
        if ((getCommitMethod = InboxServiceGrpc.getCommitMethod) == null) {
          InboxServiceGrpc.getCommitMethod = getCommitMethod =
              io.grpc.MethodDescriptor.<com.zachary.bifromq.inbox.rpc.proto.CommitRequest, com.zachary.bifromq.inbox.rpc.proto.CommitReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "commit"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.inbox.rpc.proto.CommitRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zachary.bifromq.inbox.rpc.proto.CommitReply.getDefaultInstance()))
              .setSchemaDescriptor(new InboxServiceMethodDescriptorSupplier("commit"))
              .build();
        }
      }
    }
    return getCommitMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static InboxServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<InboxServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<InboxServiceStub>() {
        @java.lang.Override
        public InboxServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new InboxServiceStub(channel, callOptions);
        }
      };
    return InboxServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static InboxServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<InboxServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<InboxServiceBlockingStub>() {
        @java.lang.Override
        public InboxServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new InboxServiceBlockingStub(channel, callOptions);
        }
      };
    return InboxServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static InboxServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<InboxServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<InboxServiceFutureStub>() {
        @java.lang.Override
        public InboxServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new InboxServiceFutureStub(channel, callOptions);
        }
      };
    return InboxServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class InboxServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * for admin
     * </pre>
     */
    public void hasInbox(com.zachary.bifromq.inbox.rpc.proto.HasInboxRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.HasInboxReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHasInboxMethod(), responseObserver);
    }

    /**
     */
    public void createInbox(com.zachary.bifromq.inbox.rpc.proto.CreateInboxRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateInboxMethod(), responseObserver);
    }

    /**
     */
    public void deleteInbox(com.zachary.bifromq.inbox.rpc.proto.DeleteInboxRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.DeleteInboxReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteInboxMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.SendRequest> receive(
        io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.SendReply> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getReceiveMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.FetchHint> fetch(
        io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.storage.proto.Fetched> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getFetchMethod(), responseObserver);
    }

    /**
     */
    public void commit(com.zachary.bifromq.inbox.rpc.proto.CommitRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.CommitReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCommitMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getHasInboxMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.zachary.bifromq.inbox.rpc.proto.HasInboxRequest,
                com.zachary.bifromq.inbox.rpc.proto.HasInboxReply>(
                  this, METHODID_HAS_INBOX)))
          .addMethod(
            getCreateInboxMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.zachary.bifromq.inbox.rpc.proto.CreateInboxRequest,
                com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply>(
                  this, METHODID_CREATE_INBOX)))
          .addMethod(
            getDeleteInboxMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.zachary.bifromq.inbox.rpc.proto.DeleteInboxRequest,
                com.zachary.bifromq.inbox.rpc.proto.DeleteInboxReply>(
                  this, METHODID_DELETE_INBOX)))
          .addMethod(
            getReceiveMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                com.zachary.bifromq.inbox.rpc.proto.SendRequest,
                com.zachary.bifromq.inbox.rpc.proto.SendReply>(
                  this, METHODID_RECEIVE)))
          .addMethod(
            getFetchMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                com.zachary.bifromq.inbox.rpc.proto.FetchHint,
                com.zachary.bifromq.inbox.storage.proto.Fetched>(
                  this, METHODID_FETCH)))
          .addMethod(
            getCommitMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.zachary.bifromq.inbox.rpc.proto.CommitRequest,
                com.zachary.bifromq.inbox.rpc.proto.CommitReply>(
                  this, METHODID_COMMIT)))
          .build();
    }
  }

  /**
   */
  public static final class InboxServiceStub extends io.grpc.stub.AbstractAsyncStub<InboxServiceStub> {
    private InboxServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected InboxServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new InboxServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * for admin
     * </pre>
     */
    public void hasInbox(com.zachary.bifromq.inbox.rpc.proto.HasInboxRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.HasInboxReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHasInboxMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createInbox(com.zachary.bifromq.inbox.rpc.proto.CreateInboxRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateInboxMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteInbox(com.zachary.bifromq.inbox.rpc.proto.DeleteInboxRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.DeleteInboxReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteInboxMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.SendRequest> receive(
        io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.SendReply> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getReceiveMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.FetchHint> fetch(
        io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.storage.proto.Fetched> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getFetchMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public void commit(com.zachary.bifromq.inbox.rpc.proto.CommitRequest request,
        io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.CommitReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCommitMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class InboxServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<InboxServiceBlockingStub> {
    private InboxServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected InboxServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new InboxServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * for admin
     * </pre>
     */
    public com.zachary.bifromq.inbox.rpc.proto.HasInboxReply hasInbox(com.zachary.bifromq.inbox.rpc.proto.HasInboxRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHasInboxMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply createInbox(com.zachary.bifromq.inbox.rpc.proto.CreateInboxRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateInboxMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.zachary.bifromq.inbox.rpc.proto.DeleteInboxReply deleteInbox(com.zachary.bifromq.inbox.rpc.proto.DeleteInboxRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteInboxMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.zachary.bifromq.inbox.rpc.proto.CommitReply commit(com.zachary.bifromq.inbox.rpc.proto.CommitRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCommitMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class InboxServiceFutureStub extends io.grpc.stub.AbstractFutureStub<InboxServiceFutureStub> {
    private InboxServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected InboxServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new InboxServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * for admin
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.zachary.bifromq.inbox.rpc.proto.HasInboxReply> hasInbox(
        com.zachary.bifromq.inbox.rpc.proto.HasInboxRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHasInboxMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply> createInbox(
        com.zachary.bifromq.inbox.rpc.proto.CreateInboxRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateInboxMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.zachary.bifromq.inbox.rpc.proto.DeleteInboxReply> deleteInbox(
        com.zachary.bifromq.inbox.rpc.proto.DeleteInboxRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteInboxMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.zachary.bifromq.inbox.rpc.proto.CommitReply> commit(
        com.zachary.bifromq.inbox.rpc.proto.CommitRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCommitMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_HAS_INBOX = 0;
  private static final int METHODID_CREATE_INBOX = 1;
  private static final int METHODID_DELETE_INBOX = 2;
  private static final int METHODID_COMMIT = 3;
  private static final int METHODID_RECEIVE = 4;
  private static final int METHODID_FETCH = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final InboxServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(InboxServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_HAS_INBOX:
          serviceImpl.hasInbox((com.zachary.bifromq.inbox.rpc.proto.HasInboxRequest) request,
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.HasInboxReply>) responseObserver);
          break;
        case METHODID_CREATE_INBOX:
          serviceImpl.createInbox((com.zachary.bifromq.inbox.rpc.proto.CreateInboxRequest) request,
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply>) responseObserver);
          break;
        case METHODID_DELETE_INBOX:
          serviceImpl.deleteInbox((com.zachary.bifromq.inbox.rpc.proto.DeleteInboxRequest) request,
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.DeleteInboxReply>) responseObserver);
          break;
        case METHODID_COMMIT:
          serviceImpl.commit((com.zachary.bifromq.inbox.rpc.proto.CommitRequest) request,
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.CommitReply>) responseObserver);
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
        case METHODID_RECEIVE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.receive(
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.rpc.proto.SendReply>) responseObserver);
        case METHODID_FETCH:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.fetch(
              (io.grpc.stub.StreamObserver<com.zachary.bifromq.inbox.storage.proto.Fetched>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class InboxServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    InboxServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.zachary.bifromq.inbox.rpc.proto.InboxServiceProtos.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("InboxService");
    }
  }

  private static final class InboxServiceFileDescriptorSupplier
      extends InboxServiceBaseDescriptorSupplier {
    InboxServiceFileDescriptorSupplier() {}
  }

  private static final class InboxServiceMethodDescriptorSupplier
      extends InboxServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    InboxServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (InboxServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new InboxServiceFileDescriptorSupplier())
              .addMethod(getHasInboxMethod())
              .addMethod(getCreateInboxMethod())
              .addMethod(getDeleteInboxMethod())
              .addMethod(getReceiveMethod())
              .addMethod(getFetchMethod())
              .addMethod(getCommitMethod())
              .build();
        }
      }
    }
    return result;
  }
}
