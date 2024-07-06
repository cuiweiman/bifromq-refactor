package com.zachary.bifromq.dist.entity;

import com.google.protobuf.ByteString;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zachary.bifromq.dist.util.TopicUtil.ORDERED_SHARE;
import static com.zachary.bifromq.dist.util.TopicUtil.TOPIC_SEPARATOR;
import static com.zachary.bifromq.dist.util.TopicUtil.UNORDERED_SHARE;
import static com.zachary.bifromq.dist.util.TopicUtil.unescape;

@EqualsAndHashCode(callSuper = true)
public class GroupMatching extends Matching {
    @EqualsAndHashCode.Exclude
    public final String group;
    @EqualsAndHashCode.Exclude
    public final boolean ordered;
    @EqualsAndHashCode.Exclude
    public final List<NormalMatching> inboxList;

    public final Map<String, QoS> inboxMap;

    private final String origTopicFilter;

    GroupMatching(ByteString key, String group, boolean ordered, Map<String, QoS> inboxes) {
        super(key);
        this.group = group;
        this.ordered = ordered;
        this.inboxMap = inboxes;
        if (ordered) {
            origTopicFilter =
                ORDERED_SHARE + TOPIC_SEPARATOR + group + TOPIC_SEPARATOR + unescape(escapedTopicFilter);
        } else {
            origTopicFilter =
                UNORDERED_SHARE + TOPIC_SEPARATOR + group + TOPIC_SEPARATOR + unescape(escapedTopicFilter);
        }
        this.inboxList = inboxes.entrySet().stream()
            .map(e -> new NormalMatching(key, origTopicFilter, e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }

    @Override
    public String originalTopicFilter() {
        return origTopicFilter;
    }
}
