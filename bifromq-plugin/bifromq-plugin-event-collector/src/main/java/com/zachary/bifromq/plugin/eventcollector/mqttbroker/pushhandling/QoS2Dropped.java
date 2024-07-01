package com.zachary.bifromq.plugin.eventcollector.mqttbroker.pushhandling;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public final class QoS2Dropped extends PushEvent<QoS2Dropped> {
    private DropReason reason;

    @Override
    public EventType type() {
        return EventType.QOS2_DROPPED;
    }

    @Override
    public void clone(QoS2Dropped orig) {
        super.clone(orig);
        this.reason = orig.reason;
    }
}
