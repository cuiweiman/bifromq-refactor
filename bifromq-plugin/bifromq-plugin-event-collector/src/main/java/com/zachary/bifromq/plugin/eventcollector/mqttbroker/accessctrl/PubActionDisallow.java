package com.zachary.bifromq.plugin.eventcollector.mqttbroker.accessctrl;

import com.zachary.bifromq.plugin.eventcollector.ClientEvent;
import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public final class PubActionDisallow extends ClientEvent<PubActionDisallow> {
    private String topic;
    private QoS qos;
    private boolean isLastWill;
    private boolean isRetain;

    @Override
    public EventType type() {
        return EventType.PUB_ACTION_DISALLOW;
    }

    @Override
    public void clone(PubActionDisallow orig) {
        super.clone(orig);
        this.topic = orig.topic;
        this.qos = orig.qos;
        this.isLastWill = orig.isLastWill;
        this.isRetain = orig.isRetain;
    }
}
