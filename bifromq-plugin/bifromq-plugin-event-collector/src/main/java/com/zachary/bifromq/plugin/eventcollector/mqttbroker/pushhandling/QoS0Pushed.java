package com.zachary.bifromq.plugin.eventcollector.mqttbroker.pushhandling;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.ToString;


@ToString(callSuper = true)
public final class QoS0Pushed extends PushEvent<QoS0Pushed> {
    @Override
    public EventType type() {
        return EventType.QOS0_PUSHED;
    }
}
