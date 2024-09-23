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
 * @description: CRDT Engine 配置项
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

    /**
     * 或历史过期时间 默认20s
     */
    @Builder.Default
    private Duration orHistoryExpireTime = Duration.ofSeconds(20);

    /**
     * 膨胀间隔
     */
    @Builder.Default
    private Duration inflationInterval = Duration.ofMillis(200);

    /**
     * 最大压缩时间
     */
    @Builder.Default
    private Duration maxCompactionTime = Duration.ofMillis(200);

    /**
     * 定时任务组件(可配线程池): 膨胀任务
     */
    private ScheduledExecutorService inflationExecutor;

}
