package com.zachary.bifromq.plugin.eventcollector.mqttbroker.subhandling;

import com.zachary.bifromq.plugin.eventcollector.ClientEvent;
import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public final class UnsubAcked extends ClientEvent<UnsubAcked> {
    private int messageId;

    private List<String> topicFilter;

    @Override
    public EventType type() {
        return EventType.UNSUB_ACKED;
    }

    @Override
    public void clone(UnsubAcked orig) {
        super.clone(orig);
        this.messageId = orig.messageId;
        this.topicFilter = orig.topicFilter;
    }
}
