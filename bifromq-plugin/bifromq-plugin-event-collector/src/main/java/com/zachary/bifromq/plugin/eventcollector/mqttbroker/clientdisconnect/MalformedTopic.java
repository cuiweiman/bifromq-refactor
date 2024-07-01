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
public final class MalformedTopic extends ClientDisconnectEvent<MalformedTopic> {
    private String topic;

    @Override
    public EventType type() {
        return EventType.MALFORMED_TOPIC;
    }

    @Override
    public void clone(MalformedTopic orig) {
        super.clone(orig);
        this.topic = orig.topic;
    }
}
