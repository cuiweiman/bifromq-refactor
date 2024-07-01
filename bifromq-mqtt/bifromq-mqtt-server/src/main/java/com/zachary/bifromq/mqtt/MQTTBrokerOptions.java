package com.zachary.bifromq.mqtt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MQTTBrokerOptions {
    private int connectTimeoutSeconds = 20;
    private int connectRateLimit = 1000;
    private int disconnectRate = 1000;
    private int maxResendTimes = 5;
    private int resendDelayMillis = 3000;
    private int defaultKeepAliveSeconds = 5 * 60; // 5 min
    private int qos2ConfirmWindowSeconds = 5;
    private long writeLimit = 512 * 1024;
    private long readLimit = 512 * 1024;
    private int maxBytesInMessage = 256 * 1024;
}
