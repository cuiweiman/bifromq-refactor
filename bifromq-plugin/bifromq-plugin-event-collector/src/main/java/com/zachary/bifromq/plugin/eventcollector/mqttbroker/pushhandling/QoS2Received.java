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
public final class QoS2Received extends PushEvent<QoS2Received> {
    private int messageId;

    @Override
    public EventType type() {
        return EventType.QOS2_RECEIVED;
    }

    @Override
    public void clone(QoS2Received orig) {
        super.clone(orig);
        this.messageId = orig.messageId;
    }
}
