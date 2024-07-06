package com.zachary.bifromq.plugin.eventcollector.inboxservice;

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
public class Overflowed extends ClientEvent<Overflowed> {
    private boolean oldest;
    private int dropCount;

    protected QoS qos;

    @Override
    public EventType type() {
        return EventType.OVERFLOWED;
    }

    @Override
    public void clone(Overflowed orig) {
        super.clone(orig);
        this.oldest = orig.oldest;
        this.dropCount = orig.dropCount;
        this.qos = orig.qos;
    }
}
