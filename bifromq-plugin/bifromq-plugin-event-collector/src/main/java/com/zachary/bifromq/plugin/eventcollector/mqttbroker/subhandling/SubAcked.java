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
public final class SubAcked extends ClientEvent<SubAcked> {
    private int messageId;

    private List<String> topicFilter;

    private List<Integer> granted;

    @Override
    public EventType type() {
        return EventType.SUB_ACKED;
    }

    @Override
    public void clone(SubAcked orig) {
        super.clone(orig);
        this.messageId = orig.messageId;
        this.topicFilter = orig.topicFilter;
        this.granted = orig.granted;
    }
}
