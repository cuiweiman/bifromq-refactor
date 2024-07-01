package com.zachary.bifromq.plugin.eventcollector.mqttbroker.retainhandling;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public final class MatchRetainError extends RetainEvent<MatchRetainError> {
    private String topicFilter;

    @Override
    public EventType type() {
        return EventType.MATCH_RETAIN_ERROR;
    }

    @Override
    public void clone(MatchRetainError orig) {
        super.clone(orig);
        this.topicFilter = orig.topicFilter;
    }
}
