package com.zachary.bifromq.dist.worker;

import com.zachary.bifromq.basekv.proto.Range;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import static com.zachary.bifromq.basekv.utils.KeyRangeUtil.intersect;
import static com.zachary.bifromq.dist.entity.EntityUtil.matchRecordKeyPrefix;
import static com.zachary.bifromq.dist.entity.EntityUtil.tenantUpperBound;

@EqualsAndHashCode
public class ScopedTopic {
    public final String tenantId;
    public final String topic;
    public final Range matchRecordRange;

    @Builder
    private ScopedTopic(String tenantId, String topic, Range range) {
        this.tenantId = tenantId;
        this.topic = topic;
        this.matchRecordRange = intersect(Range.newBuilder()
            .setStartKey(matchRecordKeyPrefix(tenantId))
            .setEndKey(tenantUpperBound(tenantId))
            .build(), range);
    }
}
