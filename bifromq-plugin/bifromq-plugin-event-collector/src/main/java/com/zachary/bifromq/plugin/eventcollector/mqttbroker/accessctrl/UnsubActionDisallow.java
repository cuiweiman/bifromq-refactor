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
public final class UnsubActionDisallow extends ClientEvent<UnsubActionDisallow> {
    private String topicFilter;

    @Override
    public EventType type() {
        return EventType.UNSUB_ACTION_DISALLOW;
    }

    @Override
    public void clone(UnsubActionDisallow orig) {
        super.clone(orig);
        this.topicFilter = orig.topicFilter;
    }
}
