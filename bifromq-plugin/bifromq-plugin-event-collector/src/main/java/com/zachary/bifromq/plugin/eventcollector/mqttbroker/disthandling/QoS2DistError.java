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
public final class QoS2DistError extends DistEvent<QoS2DistError> {
    private boolean isDup;

    @Override
    public EventType type() {
        return EventType.QOS2_DIST_ERROR;
    }

    @Override
    public void clone(QoS2DistError orig) {
        super.clone(orig);
        this.isDup = orig.isDup;
    }
}
