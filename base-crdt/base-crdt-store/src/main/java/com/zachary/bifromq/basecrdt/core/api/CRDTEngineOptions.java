package com.zachary.bifromq.basecrdt.core.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/20 17:51
 */
@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@Accessors(chain = true, fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CRDTEngineOptions {
    @Builder.Default
    private Long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;

    @Builder.Default
    private Duration orHistoryExpireTime = Duration.ofSeconds(20);

    @Builder.Default
    private Duration inflationInterval = Duration.ofMillis(200);

    @Builder.Default
    private Duration maxCompactionTime = Duration.ofMillis(200);

    private ScheduledExecutorService inflationExecutor;

}
