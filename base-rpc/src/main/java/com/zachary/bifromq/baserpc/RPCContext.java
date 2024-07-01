package com.zachary.bifromq.baserpc;

import com.zachary.bifromq.baserpc.metrics.RPCMeters;
import io.grpc.Context;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class RPCContext {

    public static class ServerSelection {
        @Getter
        @Setter
        private String serverId;
    }

    public static final Context.Key<String> TENANT_ID_CTX_KEY = Context.key("TenantId");
    public static final Context.Key<RPCMeters.MeterKey> METER_KEY_CTX_KEY = Context.key("MeterKey");
    public static final Context.Key<String> DESIRED_SERVER_ID_CTX_KEY = Context.key("DesiredServerId");
    public static final Context.Key<ServerSelection> SELECTED_SERVER_ID_CTX_KEY = Context.key("SelectedServerId");
    public static final Context.Key<String> WCH_HASH_KEY_CTX_KEY = Context.key("WeightConsistentHashKey");
    public static final Context.Key<Map<String, String>> CUSTOM_METADATA_CTX_KEY = Context.key("CustomMetadata");
}
