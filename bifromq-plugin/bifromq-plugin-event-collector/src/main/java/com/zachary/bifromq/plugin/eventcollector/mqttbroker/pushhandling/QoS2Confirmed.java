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
public final class QoS2Confirmed extends PushEvent<QoS2Confirmed> {
    private int messageId;

    /**
     * Whether the qos2 message has been delivered to client, or dropped due to max resend times
     */
    private boolean delivered;

    @Override
    public EventType type() {
        return EventType.QOS2_CONFIRMED;
    }

    @Override
    public void clone(QoS2Confirmed orig) {
        super.clone(orig);
        this.messageId = orig.messageId;
        this.delivered = orig.delivered;
    }
}
