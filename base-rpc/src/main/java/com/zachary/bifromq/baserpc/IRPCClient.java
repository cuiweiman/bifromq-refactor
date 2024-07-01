package com.zachary.bifromq.baserpc;

import io.grpc.MethodDescriptor;
import io.reactivex.rxjava3.core.Observable;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import static java.util.Collections.emptyMap;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/28 16:31
 */
public interface IRPCClient {
    enum ConnState {
        CONNECTING,

        READY,

        TRANSIENT_FAILURE,

        IDLE,

        SHUTDOWN
    }

    static RPCClientBuilder builder() {
        return new RPCClientBuilder();
    }

    interface IRequestPipeline<ReqT, RespT> {
        boolean isClosed();

        CompletableFuture<RespT> invoke(ReqT req);

        void close();
    }

    interface IMessageStream<MsgT, AckT> {
        boolean isClosed();

        void ack(AckT msg);

        Observable<MsgT> msg();

        void close();
    }

    /**
     * The observable of live servers
     *
     * @return
     */
    Observable<Set<String>> serverList();

    /**
     * The observable of rpc connectivity state
     *
     * @return an observable of connection state
     */
    Observable<ConnState> connState();

    default <ReqT, RespT> CompletableFuture<RespT> invoke(String tenantId,
                                                          @Nullable String desiredServerId,
                                                          ReqT req,
                                                          MethodDescriptor<ReqT, RespT> methodDesc) {
        return invoke(tenantId, desiredServerId, req, emptyMap(), methodDesc);
    }

    <ReqT, RespT> CompletableFuture<RespT> invoke(String tenantId,
                                                  @Nullable String desiredServerId,
                                                  ReqT req,
                                                  Map<String, String> metadata,
                                                  MethodDescriptor<ReqT, RespT> methodDesc);

    /**
     * Create a caller-managed auto-rebalanced request-response pipeline
     *
     * @param tenantId        the tenant id
     * @param desiredServerId the desired server id
     * @param wchKey          key for calculating weighted consistent hash
     * @param metadata        associated with the pipeline
     * @param methodDesc      the method descriptor
     * @return a request pipeline
     */
    default <ReqT, RespT> IRequestPipeline<ReqT, RespT> createRequestPipeline(String tenantId,
                                                                              @Nullable String desiredServerId,
                                                                              @Nullable String wchKey,
                                                                              Map<String, String> metadata,
                                                                              MethodDescriptor<ReqT, RespT>
                                                                                      methodDesc) {
        return createRequestPipeline(tenantId, desiredServerId, wchKey, () -> metadata, methodDesc);
    }

    default <ReqT, RespT> IRequestPipeline<ReqT, RespT> createRequestPipeline(String tenantId,
                                                                              @Nullable String desiredServerId,
                                                                              @Nullable String wchKey,
                                                                              Map<String, String> metadata,
                                                                              MethodDescriptor<ReqT, RespT> methodDesc,
                                                                              Executor executor) {
        return createRequestPipeline(tenantId, desiredServerId, wchKey, () -> metadata, methodDesc, executor);
    }


    /**
     * Create a caller-managed auto-rebalanced request-response pipeline with default executor
     *
     * @param tenantId         the tenant id
     * @param desiredServerId  the desired server id
     * @param wchKey           key for calculating weighted consistent hash
     * @param metadataSupplier supply the metadata of the pipeline
     * @param methodDesc       the method descriptor
     * @return a request pipeline
     */
    <ReqT, RespT> IRequestPipeline<ReqT, RespT> createRequestPipeline(String tenantId,
                                                                      @Nullable String desiredServerId,
                                                                      @Nullable String wchKey,
                                                                      Supplier<Map<String, String>> metadataSupplier,
                                                                      MethodDescriptor<ReqT, RespT> methodDesc);

    /**
     * Create a caller-managed auto-rebalanced request-response pipeline with specified executor
     *
     * @param tenantId         the tenant id
     * @param desiredServerId  the desired server id
     * @param wchKey           key for calculating weighted consistent hash
     * @param metadataSupplier supply the metadata of the pipeline
     * @param methodDesc       the method descriptor
     * @param executor         the executor for async callback
     * @return a request pipeline
     */
    <ReqT, RespT> IRequestPipeline<ReqT, RespT> createRequestPipeline(String tenantId,
                                                                      @Nullable String desiredServerId,
                                                                      @Nullable String wchKey,
                                                                      Supplier<Map<String, String>> metadataSupplier,
                                                                      MethodDescriptor<ReqT, RespT> methodDesc,
                                                                      Executor executor);

    /**
     * Create a caller-managed auto-rebalanced bi-directional message stream with at-most-once delivery guarantee.
     *
     * @param tenantId        the tenant id
     * @param desiredServerId the desired server id
     * @param wchKey          key for calculating weighted consistent hash
     * @param metadata        the metadata of the message stream
     * @param methodDesc      the method descriptor
     * @return a message stream
     */
    default <MsgT, AckT> IMessageStream<MsgT, AckT> createMessageStream(String tenantId,
                                                                        @Nullable String desiredServerId,
                                                                        @Nullable String wchKey,
                                                                        Map<String, String> metadata,
                                                                        MethodDescriptor<AckT, MsgT> methodDesc) {
        return createMessageStream(tenantId, desiredServerId, wchKey, () -> metadata, methodDesc);
    }

    <MsgT, AckT> IMessageStream<MsgT, AckT> createMessageStream(String tenantId,
                                                                @Nullable String desiredServerId,
                                                                @Nullable String wchKey,
                                                                Supplier<Map<String, String>> metadataSupplier,
                                                                MethodDescriptor<AckT, MsgT> methodDesc);


    void stop();
}
