package com.zachary.bifromq.plugin.eventcollector.mqttbroker.disthandling;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.ToString;

@ToString(callSuper = true)
public final class QoS0DistError extends DistEvent<QoS0DistError> {
    @Override
    public EventType type() {
        return EventType.QOS0_DIST_ERROR;
    }
}
