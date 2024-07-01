/*
 * Copyright (c) 2023. Baidu, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.proto.KVRangeDescriptor;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.State;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.google.protobuf.ByteString;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

class KVRangeMetricManager {
    private final Counter dumpBytesCounter;
    private final Counter restoreBytesCounter;
    private final Gauge stateGauge;
    private final Gauge verGauge;
    private final Gauge lastAppliedIndexGauge;
    private final Gauge dataSizeGauge;
    private final Gauge walSizeGauge;
    private final Timer configChangeTimer;
    private final Timer transferLeaderTimer;
    private final Timer splitTimer;
    private final Timer mergeTimer;
    private final Timer putTimer;
    private final Timer deleteTimer;
    private final Timer mutateCoProcTimer;
    private final Timer existTimer;
    private final Timer getTimer;
    private final Timer queryCoProcTimer;
    private final AtomicReference<KVRangeDescriptor> currentDesc = new AtomicReference<>();
    private final AtomicLong currentLastAppliedIndex = new AtomicLong(-1);

    KVRangeMetricManager(String storeId, KVRangeId rangeId) {
        Tags tags = Tags.of("storeId", storeId).and("rangeId", KVRangeIdUtil.toString(rangeId));
        dumpBytesCounter = Metrics.counter("basekv.snap.dump", tags);
        restoreBytesCounter = Metrics.counter("basekv.snap.restore", tags);
        stateGauge = Gauge.builder("basekv.meta.state", () -> {
                KVRangeDescriptor desc = currentDesc.get();
                if (desc != null) {
                    return desc.getState().ordinal();
                }
                return State.StateType.NoUse.ordinal();
            })
            .tags(tags)
            .register(Metrics.globalRegistry);
        verGauge = Gauge.builder("basekv.meta.ver", () -> {
                KVRangeDescriptor desc = currentDesc.get();
                if (desc != null) {
                    return desc.getVer();
                }
                return -1;
            })
            .tags(tags)
            .register(Metrics.globalRegistry);
        lastAppliedIndexGauge = Gauge.builder("basekv.meta.appidx", currentLastAppliedIndex::get)
            .tags(tags)
            .register(Metrics.globalRegistry);
        dataSizeGauge = Gauge.builder("basekv.meta.size", () -> {
                KVRangeDescriptor desc = currentDesc.get();
                if (desc != null) {
                    return desc.getStatisticsMap().getOrDefault("dataSize", 0.0).longValue();
                }
                return 0;
            })
            .tags(tags)
            .register(Metrics.globalRegistry);
        walSizeGauge = Gauge.builder("basekv.meta.walsize", () -> {
                KVRangeDescriptor desc = currentDesc.get();
                if (desc != null) {
                    return desc.getStatisticsMap().getOrDefault("walSize", 0.0).longValue();
                }
                return 0;
            })
            .tags(tags)
            .register(Metrics.globalRegistry);
        configChangeTimer = Timer.builder("basekv.cmd.configchange")
            .tags(tags)
            .register(Metrics.globalRegistry);
        transferLeaderTimer = Timer.builder("basekv.cmd.transferleader")
            .tags(tags)
            .register(Metrics.globalRegistry);
        splitTimer = Timer.builder("basekv.cmd.split")
            .tags(tags)
            .register(Metrics.globalRegistry);
        mergeTimer = Timer.builder("basekv.cmd.merge")
            .tags(tags)
            .register(Metrics.globalRegistry);
        putTimer = Timer.builder("basekv.cmd.put")
            .tags(tags)
            .register(Metrics.globalRegistry);
        deleteTimer = Timer.builder("basekv.cmd.delete")
            .tags(tags)
            .register(Metrics.globalRegistry);
        mutateCoProcTimer = Timer.builder("basekv.cmd.mutatecoproc")
            .tags(tags)
            .register(Metrics.globalRegistry);
        existTimer = Timer.builder("basekv.cmd.exist")
            .tags(tags)
            .register(Metrics.globalRegistry);
        getTimer = Timer.builder("basekv.cmd.get")
            .tags(tags)
            .register(Metrics.globalRegistry);
        queryCoProcTimer = Timer.builder("basekv.cmd.querycoproc")
            .tags(tags)
            .register(Metrics.globalRegistry);
    }

    void report(KVRangeDescriptor descriptor) {
        currentDesc.set(descriptor);
    }

    void reportDump(int bytes) {
        dumpBytesCounter.increment(bytes);
    }

    void reportRestore(int bytes) {
        restoreBytesCounter.increment(bytes);
    }

    void reportLastAppliedIndex(long index) {
        currentLastAppliedIndex.set(index);
    }

    private <T> CompletionStage<T> recordDuration(Supplier<CompletionStage<T>> supplier, Timer timer) {
        Timer.Sample sample = Timer.start();
        return supplier.get().whenComplete((v, e) -> sample.stop(timer));
    }

    CompletionStage<Void> recordConfigChange(Supplier<CompletionStage<Void>> supplier) {
        return recordDuration(supplier, configChangeTimer);
    }

    CompletionStage<Void> recordTransferLeader(Supplier<CompletionStage<Void>> supplier) {
        return recordDuration(supplier, transferLeaderTimer);
    }

    CompletionStage<Void> recordSplit(Supplier<CompletionStage<Void>> supplier) {
        return recordDuration(supplier, splitTimer);
    }

    CompletionStage<Void> recordMerge(Supplier<CompletionStage<Void>> supplier) {
        return recordDuration(supplier, mergeTimer);
    }

    CompletionStage<ByteString> recordPut(Supplier<CompletionStage<ByteString>> supplier) {
        return recordDuration(supplier, putTimer);
    }

    CompletionStage<ByteString> recordDelete(Supplier<CompletionStage<ByteString>> supplier) {
        return recordDuration(supplier, deleteTimer);
    }

    CompletionStage<ByteString> recordMutateCoProc(Supplier<CompletionStage<ByteString>> supplier) {
        return recordDuration(supplier, mutateCoProcTimer);
    }

    CompletionStage<Boolean> recordExist(Supplier<CompletionStage<Boolean>> supplier) {
        return recordDuration(supplier, existTimer);
    }

    CompletionStage<Optional<ByteString>> recordGet(Supplier<CompletionStage<Optional<ByteString>>> supplier) {
        return recordDuration(supplier, getTimer);
    }

    CompletionStage<ByteString> recordQueryCoProc(Supplier<CompletionStage<ByteString>> supplier) {
        return recordDuration(supplier, queryCoProcTimer);
    }

    void close() {
        Metrics.globalRegistry.removeByPreFilterId(dumpBytesCounter.getId());
        Metrics.globalRegistry.removeByPreFilterId(restoreBytesCounter.getId());
        Metrics.globalRegistry.removeByPreFilterId(stateGauge.getId());
        Metrics.globalRegistry.removeByPreFilterId(lastAppliedIndexGauge.getId());
        Metrics.globalRegistry.removeByPreFilterId(verGauge.getId());
        Metrics.globalRegistry.removeByPreFilterId(dataSizeGauge.getId());
        Metrics.globalRegistry.removeByPreFilterId(walSizeGauge.getId());
        Metrics.globalRegistry.removeByPreFilterId(configChangeTimer.getId());
        Metrics.globalRegistry.removeByPreFilterId(transferLeaderTimer.getId());
        Metrics.globalRegistry.removeByPreFilterId(splitTimer.getId());
        Metrics.globalRegistry.removeByPreFilterId(mergeTimer.getId());
        Metrics.globalRegistry.removeByPreFilterId(putTimer.getId());
        Metrics.globalRegistry.removeByPreFilterId(deleteTimer.getId());
        Metrics.globalRegistry.removeByPreFilterId(mutateCoProcTimer.getId());
        Metrics.globalRegistry.removeByPreFilterId(existTimer.getId());
        Metrics.globalRegistry.removeByPreFilterId(getTimer.getId());
        Metrics.globalRegistry.removeByPreFilterId(queryCoProcTimer.getId());
    }
}
