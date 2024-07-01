package com.zachary.bifromq.plugin.eventcollector.mqttbroker.clientdisconnect;


import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public final class MalformedTopicFilter extends ClientDisconnectEvent<MalformedTopicFilter> {
    private String topicFilter;

    @Override
    public EventType type() {
        return EventType.MALFORMED_TOPIC_FILTER;
    }

    @Override
    public void clone(MalformedTopicFilter orig) {
        super.clone(orig);
        this.topicFilter = orig.topicFilter;
    }
}
