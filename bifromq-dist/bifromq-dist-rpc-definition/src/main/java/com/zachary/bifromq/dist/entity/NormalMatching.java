package com.zachary.bifromq.dist.entity;


import com.zachary.bifromq.type.QoS;
import com.zachary.bifromq.type.SubInfo;
import com.google.common.base.Strings;
import com.google.protobuf.ByteString;
import lombok.EqualsAndHashCode;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.zachary.bifromq.dist.util.TopicUtil.NUL;
import static com.zachary.bifromq.dist.util.TopicUtil.unescape;

@EqualsAndHashCode(callSuper = true)
public class NormalMatching extends Matching {
    public final String scopedInboxId;
    private final String originalTopicFilter;
    public final QoS subQoS;

    @EqualsAndHashCode.Exclude
    public final String delivererKey;
    @EqualsAndHashCode.Exclude
    public final int subBrokerId;
    @EqualsAndHashCode.Exclude
    public final SubInfo subInfo;

    NormalMatching(ByteString key, String scopedInboxId, QoS subQoS) {
        super(key);
        this.scopedInboxId = scopedInboxId;
        this.subQoS = subQoS;
        this.originalTopicFilter = unescape(escapedTopicFilter);

        scopedInboxId = new String(Base64.getDecoder().decode(scopedInboxId), StandardCharsets.UTF_8);
        String[] parts = scopedInboxId.split(NUL);
        subBrokerId = Integer.parseInt(parts[0]);
        delivererKey = Strings.isNullOrEmpty(parts[2]) ? null : parts[2];
        subInfo = SubInfo.newBuilder()
            .setTenantId(tenantId)
            .setInboxId(parts[1])
            .setSubQoS(subQoS)
            .setTopicFilter(originalTopicFilter)
            .build();
    }

    NormalMatching(ByteString key, String originalTopicFilter, String scopedInboxId, QoS subQoS) {
        super(key);
        this.scopedInboxId = scopedInboxId;
        this.subQoS = subQoS;
        this.originalTopicFilter = originalTopicFilter;

        scopedInboxId = new String(Base64.getDecoder().decode(scopedInboxId), StandardCharsets.UTF_8);
        String[] parts = scopedInboxId.split(NUL);
        subBrokerId = Integer.parseInt(parts[0]);
        delivererKey = Strings.isNullOrEmpty(parts[2]) ? null : parts[2];
        subInfo = SubInfo.newBuilder()
            .setTenantId(tenantId)
            .setInboxId(parts[1])
            .setSubQoS(subQoS)
            .setTopicFilter(originalTopicFilter)
            .build();
    }

    @Override
    public String originalTopicFilter() {
        return originalTopicFilter;
    }
}
