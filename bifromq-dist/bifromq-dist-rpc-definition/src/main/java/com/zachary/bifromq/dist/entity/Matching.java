package com.zachary.bifromq.dist.entity;

import com.google.protobuf.ByteString;
import lombok.EqualsAndHashCode;

import static com.zachary.bifromq.dist.util.TopicUtil.NUL;

@EqualsAndHashCode
public abstract class Matching {
    @EqualsAndHashCode.Exclude
    public final ByteString key;
    @EqualsAndHashCode.Exclude
    public final String escapedTopicFilter;

    public final String tenantId;

    protected Matching(ByteString matchRecordKey) {
        this.key = matchRecordKey;
        String matchRecordKeyStr = matchRecordKey.toStringUtf8();
        int firstSplit = matchRecordKeyStr.indexOf(NUL);
        tenantId = matchRecordKeyStr.substring(0, firstSplit);
        int lastSplit = matchRecordKeyStr.lastIndexOf(NUL);
        escapedTopicFilter = matchRecordKeyStr.substring(firstSplit + 2, lastSplit);
    }

    public abstract String originalTopicFilter();
}
