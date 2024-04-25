
package com.zachary.bifromq.basecrdt.store;

import com.zachary.bifromq.basecrdt.core.api.CRDTEngineOptions;
import com.zachary.bifromq.basecrdt.store.compressor.CompressAlgorithm;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.concurrent.ScheduledExecutorService;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/20 20:22
 */
@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@Accessors(chain = true, fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CRDTStoreOptions {

    @Builder.Default
    int maxEventsInDelta = 1024;

    @Builder.Default
    CRDTEngineOptions engineOptions = new CRDTEngineOptions();

    @Builder.Default
    CompressAlgorithm compressAlgorithm = CompressAlgorithm.GZIP;

    ScheduledExecutorService storeExecutor;
}
