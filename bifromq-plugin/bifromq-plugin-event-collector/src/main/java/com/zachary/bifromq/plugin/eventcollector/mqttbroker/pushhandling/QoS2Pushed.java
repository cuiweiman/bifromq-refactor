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
public final class QoS2Pushed extends PushEvent<QoS2Pushed> {
    private int messageId;
    private boolean dup;

    @Override
    public EventType type() {
        return EventType.QOS2_PUSHED;
    }

    @Override
    public void clone(QoS2Pushed orig) {
        super.clone(orig);
        this.messageId = orig.messageId;
        this.dup = orig.dup;
    }
}
