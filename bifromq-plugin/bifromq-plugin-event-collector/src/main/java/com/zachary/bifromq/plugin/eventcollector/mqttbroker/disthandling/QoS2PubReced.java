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
public final class QoS2PubReced extends DistEvent<QoS2PubReced> {
    private boolean isDup;

    @Override
    public EventType type() {
        return EventType.PUB_RECED;
    }

    @Override
    public void clone(QoS2PubReced orig) {
        super.clone(orig);
        this.isDup = orig.isDup;
    }
}
