package com.zachary.bifromq.plugin.eventcollector.distservice;

import com.zachary.bifromq.plugin.eventcollector.Event;
import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public class Disted extends Event<Disted> {
    private long reqId;
    private Iterable<PublisherMessagePack> messages;
    private int fanout;

    @Override
    public EventType type() {
        return EventType.DISTED;
    }

    @Override
    public void clone(Disted orig) {
        super.clone(orig);
        this.reqId = orig.reqId;
        this.messages = orig.messages;
        this.fanout = orig.fanout;
    }
}
