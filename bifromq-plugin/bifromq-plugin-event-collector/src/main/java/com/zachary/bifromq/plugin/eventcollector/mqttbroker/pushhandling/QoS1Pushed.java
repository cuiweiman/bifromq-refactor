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
public final class QoS1Pushed extends PushEvent<QoS1Pushed> {
    private int messageId;
    private boolean dup;

    @Override
    public EventType type() {
        return EventType.QOS1_PUSHED;
    }

    @Override
    public void clone(QoS1Pushed orig) {
        super.clone(orig);
        this.messageId = orig.messageId;
        this.dup = orig.dup;
    }
}
