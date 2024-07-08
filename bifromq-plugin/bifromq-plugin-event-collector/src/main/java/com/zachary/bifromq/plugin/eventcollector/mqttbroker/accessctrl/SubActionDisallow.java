package com.zachary.bifromq.plugin.eventcollector.mqttbroker.accessctrl;

import com.zachary.bifromq.plugin.eventcollector.ClientEvent;
import com.zachary.bifromq.plugin.eventcollector.EventType;
import com.zachary.bifromq.type.QoS;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public final class SubActionDisallow extends ClientEvent<SubActionDisallow> {
    private String topicFilter;
    private QoS qos;

    @Override
    public EventType type() {
        return EventType.SUB_ACTION_DISALLOW;
    }

    @Override
    public void clone(SubActionDisallow orig) {
        super.clone(orig);
        this.topicFilter = orig.topicFilter;
        this.qos = orig.qos;
    }
}
