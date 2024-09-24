package com.zachary.bifromq.inbox.util;

import com.zachary.bifromq.type.ClientInfo;
import lombok.SneakyThrows;

import java.util.Base64;

public class PipelineUtil {
    public static final String PIPELINE_ATTR_KEY_INBOX_ID = "0";
    public static final String PIPELINE_ATTR_KEY_CLIENT_INFO = "1";
    public static final String PIPELINE_ATTR_KEY_QOS0_LAST_FETCH_SEQ = "2";
    public static final String PIPELINE_ATTR_KEY_QOS2_LAST_FETCH_SEQ = "3";

    public static String encode(ClientInfo clientInfo) {
        return Base64.getEncoder().encodeToString(clientInfo.toByteArray());
    }

    @SneakyThrows
    public static ClientInfo decode(String clientInfoBase64) {
        return ClientInfo.parseFrom(Base64.getDecoder().decode(clientInfoBase64));
    }
}
