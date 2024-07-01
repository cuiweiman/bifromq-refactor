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
public final class QoS1Dropped extends PushEvent<QoS1Dropped> {
    private DropReason reason;
    private int count;

    @Override
    public EventType type() {
        return EventType.QOS1_DROPPED;
    }

    @Override
    public void clone(QoS1Dropped orig) {
        super.clone(orig);
        this.reason = orig.reason;
        this.count = orig.count;
    }
}
