
package com.zachary.bifromq.baserpc.loadbalancer;

import com.zachary.bifromq.baserpc.exception.RequestCanceledException;
import com.zachary.bifromq.baserpc.exception.RequestThrottledException;
import com.zachary.bifromq.baserpc.exception.ServerNotFoundException;
import com.zachary.bifromq.baserpc.exception.ServerUnreachableException;
import com.zachary.bifromq.baserpc.exception.ServiceUnavailableException;
import com.zachary.bifromq.baserpc.exception.TransientFailureException;
import io.grpc.Attributes;
import io.grpc.ConnectivityStateInfo;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class Constants {

    public static final Attributes.Key<AtomicReference<ConnectivityStateInfo>> STATE_INFO =
            Attributes.Key.create("state-info");

    public static final Attributes.Key<Map<String, Map<String, Integer>>> TRAFFIC_DIRECTIVE_ATTR_KEY =
            Attributes.Key.create("TrafficDirective");
    public static final Attributes.Key<String> SERVER_ID_ATTR_KEY = Attributes.Key.create("ServerId");
    public static final Attributes.Key<Set<String>> SERVER_GROUP_TAG_ATTR_KEY = Attributes.Key.create("ServerGroupTag");

    public static final Metadata.Key<String> TENANT_ID_META_KEY =
            Metadata.Key.of("tenant_id", Metadata.ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<String> DESIRED_SERVER_META_KEY =
            Metadata.Key.of("desired_server_id", Metadata.ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<String> WCH_KEY_META_KEY =
            Metadata.Key.of("wch_hash_key", Metadata.ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<String> COLLECT_SELECTION_METADATA_META_KEY =
            Metadata.Key.of("collect_selection", Metadata.ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<byte[]> CUSTOM_METADATA_META_KEY =
            Metadata.Key.of("custom_metadata-bin", Metadata.BINARY_BYTE_MARSHALLER);

    /**
     * 异常信息
     */
    public static final Status TRANSIENT_FAILURE = Status.UNAVAILABLE.withDescription("transient failure");
    public static final Status SERVICE_UNAVAILABLE = Status.UNAVAILABLE.withDescription("service unavailable");
    public static final Status REQUEST_THROTTLED = Status.RESOURCE_EXHAUSTED.withDescription("request dropped due to downstream overloaded");
    public static final Status SERVER_NOT_FOUND = Status.UNAVAILABLE.withDescription("direct targeted server not found");
    public static final Status SERVER_UNREACHABLE = Status.UNAVAILABLE.withDescription("server is unreachable now");

    public static Throwable toConcreteException(Throwable throwable) {
        if (throwable instanceof StatusRuntimeException statusRuntimeException) {
            if (statusRuntimeException.getStatus().equals(TRANSIENT_FAILURE)) {
                return new TransientFailureException(throwable);
            }
            if (statusRuntimeException.getStatus().equals(SERVICE_UNAVAILABLE)) {
                return new ServiceUnavailableException(throwable);
            }
            if (statusRuntimeException.getStatus().equals(REQUEST_THROTTLED)) {
                return new RequestThrottledException(throwable);
            }
            if (statusRuntimeException.getStatus().equals(SERVER_NOT_FOUND)) {
                return new ServerNotFoundException(throwable);
            }
            if (statusRuntimeException.getStatus().equals(SERVER_UNREACHABLE)) {
                return new ServerUnreachableException(throwable);
            }
            if (statusRuntimeException.getStatus().equals(Status.CANCELLED)) {
                return new RequestCanceledException(throwable);
            }
        }
        return throwable;
    }
}
