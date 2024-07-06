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
public final class Discard extends DistEvent<Discard> {
    private int rateLimit;

    private QoS qos;

    @Override
    public EventType type() {
        return EventType.DISCARD;
    }

    @Override
    public void clone(Discard orig) {
        super.clone(orig);
        this.rateLimit = orig.rateLimit;
        this.qos = orig.qos;
    }
}
