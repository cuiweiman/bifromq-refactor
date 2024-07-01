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
public final class WillDistError extends DistEvent<WillDistError> {
    private QoS qos;

    @Override
    public EventType type() {
        return EventType.WILL_DIST_ERROR;
    }

    @Override
    public void clone(WillDistError orig) {
        super.clone(orig);
    }
}
