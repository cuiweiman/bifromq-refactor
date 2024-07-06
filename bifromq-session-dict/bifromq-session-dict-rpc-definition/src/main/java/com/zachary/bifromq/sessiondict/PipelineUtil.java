package com.zachary.bifromq.sessiondict;

import lombok.SneakyThrows;

import java.util.Base64;

public class PipelineUtil {
    public static final String CLIENT_INFO = "0";

    public static String encode(ClientInfo clientInfo) {
        return Base64.getEncoder().encodeToString(clientInfo.toByteArray());
    }

    @SneakyThrows
    public static ClientInfo decode(String clientInfoBase64) {
        return ClientInfo.parseFrom(Base64.getDecoder().decode(clientInfoBase64));
    }
}
