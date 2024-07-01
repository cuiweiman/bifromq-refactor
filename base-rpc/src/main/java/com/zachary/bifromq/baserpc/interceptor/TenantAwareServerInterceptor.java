package com.zachary.bifromq.baserpc.interceptor;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.zachary.bifromq.baserpc.RPCContext;
import com.zachary.bifromq.baserpc.loadbalancer.Constants;
import com.zachary.bifromq.baserpc.metrics.RPCMeters;
import com.zachary.bifromq.baserpc.proto.PipelineMetadata;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.ServerServiceDefinition;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 对服务端的请求进行拦截
 *
 * @description: gRPC 服务端 拦截器
 * @author: cuiweiman
 * @date: 2024/4/28 17:45
 */
@Slf4j
public class TenantAwareServerInterceptor implements ServerInterceptor {
    private static final ServerCall.Listener NOOP_LISTENER = new ServerCall.Listener<>() {
    };
    private final Map<String, LoadingCache<String, RPCMeters.MeterKey>> meterKeys = new HashMap<>();

    public TenantAwareServerInterceptor(String serviceUniqueName, ServerServiceDefinition serviceDefinition) {
        for (MethodDescriptor<?, ?> methodDesc : serviceDefinition.getServiceDescriptor().getMethods()) {
            meterKeys.put(methodDesc.getFullMethodName(), Caffeine.newBuilder()
                    .expireAfterAccess(Duration.ofSeconds(30))
                    .build(tenantId -> RPCMeters.MeterKey.builder()
                            .service(serviceUniqueName)
                            .method(methodDesc.getBareMethodName())
                            .tenantId(tenantId)
                            .build()));
        }
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
                                                                 ServerCallHandler<ReqT, RespT> next) {
        try {
            Context ctx = Context.current();
            assert headers.containsKey(Constants.TENANT_ID_META_KEY);
            String tenantId = headers.get(Constants.TENANT_ID_META_KEY);
            ctx = ctx.withValue(RPCContext.TENANT_ID_CTX_KEY, tenantId);

            if (headers.containsKey(Constants.WCH_KEY_META_KEY)) {
                ctx = ctx.withValue(RPCContext.WCH_HASH_KEY_CTX_KEY, headers.get(Constants.WCH_KEY_META_KEY));
            }

            if (headers.containsKey(Constants.CUSTOM_METADATA_META_KEY)) {
                PipelineMetadata metadata = PipelineMetadata.parseFrom(headers.get(Constants.CUSTOM_METADATA_META_KEY));
                ctx = ctx.withValue(RPCContext.CUSTOM_METADATA_CTX_KEY, metadata.getEntryMap());
            } else {
                ctx = ctx.withValue(RPCContext.CUSTOM_METADATA_CTX_KEY, Collections.emptyMap());
            }
            ctx = ctx.withValue(RPCContext.METER_KEY_CTX_KEY,
                    meterKeys.get(call.getMethodDescriptor().getFullMethodName()).get(tenantId));

            ServerCall.Listener<ReqT> listener = Contexts.interceptCall(ctx, call, headers, next);
            return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(listener) {
                @Override
                public void onHalfClose() {
                    try {
                        super.onHalfClose();
                    } catch (Exception e) {
                        log.error("Failed to execute server call.", e);
                        call.close(Status.INTERNAL.withCause(e).withDescription(e.getMessage()), headers);
                    }
                }
            };
        } catch (UnsupportedOperationException e) {
            log.error("Failed to determine traffic identifier from the call", e);
            call.close(Status.UNAUTHENTICATED.withDescription("Invalid Client Certificate"), headers);
            return NOOP_LISTENER;
        } catch (Throwable e) {
            log.error("Failed to make server call", e);
            call.close(Status.INTERNAL.withDescription("Server handling request error"), headers);
            return NOOP_LISTENER;
        }
    }
}
