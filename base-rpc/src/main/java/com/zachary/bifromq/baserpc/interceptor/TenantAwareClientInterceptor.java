package com.zachary.bifromq.baserpc.interceptor;

import com.zachary.bifromq.baserpc.RPCContext;
import com.zachary.bifromq.baserpc.loadbalancer.Constants;
import com.zachary.bifromq.baserpc.proto.PipelineMetadata;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TenantAwareClientInterceptor implements ClientInterceptor {
    private final String serviceUniqueName;

    public TenantAwareClientInterceptor() {
        this(null);
    }

    // used in in-process mode
    public TenantAwareClientInterceptor(String serviceUniqueName) {
        this.serviceUniqueName = serviceUniqueName;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
                                                               CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put(Constants.TENANT_ID_META_KEY, RPCContext.TENANT_ID_CTX_KEY.get());
                if (RPCContext.DESIRED_SERVER_ID_CTX_KEY.get() != null) {
                    headers.put(Constants.DESIRED_SERVER_META_KEY, RPCContext.DESIRED_SERVER_ID_CTX_KEY.get());
                }
                if (RPCContext.WCH_HASH_KEY_CTX_KEY.get() != null) {
                    headers.put(Constants.WCH_KEY_META_KEY, RPCContext.WCH_HASH_KEY_CTX_KEY.get());
                }
                if (RPCContext.CUSTOM_METADATA_CTX_KEY.get() != null) {
                    headers.put(Constants.CUSTOM_METADATA_META_KEY, PipelineMetadata.newBuilder()
                            .putAllEntry(RPCContext.CUSTOM_METADATA_CTX_KEY.get()).build().toByteArray());
                }
                headers.put(Constants.COLLECT_SELECTION_METADATA_META_KEY,
                        Boolean.toString(RPCContext.SELECTED_SERVER_ID_CTX_KEY.get() != null));
                if (RPCContext.SELECTED_SERVER_ID_CTX_KEY.get() != null && serviceUniqueName != null) {
                    // in-process mode will bypass lb
                    RPCContext.SELECTED_SERVER_ID_CTX_KEY.get().setServerId(serviceUniqueName);
                }
                super.start(responseListener, headers);
            }
        };
    }
}
