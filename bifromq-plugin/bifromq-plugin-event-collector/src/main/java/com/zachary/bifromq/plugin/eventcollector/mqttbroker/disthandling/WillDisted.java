package com.zachary.bifromq.plugin.eventcollector.mqttbroker.disthandling;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import com.zachary.bifromq.type.QoS;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public final class WillDisted extends DistEvent<WillDisted> {
    private QoS qos;

    @Override
    public EventType type() {
        return EventType.WILL_DISTED;
    }

    @Override
    public void clone(WillDisted orig) {
        super.clone(orig);
    }
}
