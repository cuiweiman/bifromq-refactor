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
public final class QoS2PubRecDropped extends DistEvent<QoS2PubRecDropped> {
    private boolean isDup;

    @Override
    public EventType type() {
        return EventType.PUB_REC_DROPPED;
    }

    @Override
    public void clone(QoS2PubRecDropped orig) {
        super.clone(orig);
        this.isDup = orig.isDup;
    }
}
