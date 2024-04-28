package com.zachary.bifromq.basecrdt.service;

import com.zachary.bifromq.basecrdt.proto.Replica;
import com.google.protobuf.ByteString;
import lombok.SneakyThrows;

import java.util.Base64;

public class AgentUtil {
    @SneakyThrows
    public static Replica toReplica(String agentMemberName) {
        return Replica.parseFrom(fromBase64(agentMemberName));
    }

    public static String toAgentMemberName(Replica replica) {
        return toBase64(replica.toByteString());
    }

    private static String toBase64(ByteString bytes) {
        return Base64.getEncoder().encodeToString(bytes.toByteArray());
    }

    private static ByteString fromBase64(String base64Str) {
        return ByteString.copyFrom(Base64.getDecoder().decode(base64Str));
    }
}
