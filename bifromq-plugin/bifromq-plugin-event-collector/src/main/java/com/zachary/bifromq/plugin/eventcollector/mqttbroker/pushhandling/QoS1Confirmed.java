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
public final class QoS1Confirmed extends PushEvent<QoS1Confirmed> {
    private int messageId;

    /**
     * Whether the qos1 message has been delivered to client, or dropped due to max resend times
     *
     * @return
     */
    private boolean delivered;

    @Override
    public EventType type() {
        return EventType.QOS1_CONFIRMED;
    }

    @Override
    public void clone(QoS1Confirmed orig) {
        super.clone(orig);
        this.messageId = orig.messageId;
        this.delivered = orig.delivered;
    }
}
