package com.zachary.bifromq.plugin.eventcollector.mqttbroker.disthandling;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public final class QoS1PubAcked extends DistEvent<QoS1PubAcked> {
    private boolean isDup;

    @Override
    public EventType type() {
        return EventType.PUB_ACKED;
    }

    @Override
    public void clone(QoS1PubAcked orig) {
        super.clone(orig);
        this.isDup = orig.isDup;
    }
}
