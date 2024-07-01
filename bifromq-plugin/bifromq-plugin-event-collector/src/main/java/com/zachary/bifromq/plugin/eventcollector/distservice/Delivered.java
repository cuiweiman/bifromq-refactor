package com.zachary.bifromq.plugin.eventcollector.distservice;

import com.zachary.bifromq.plugin.eventcollector.Event;
import com.zachary.bifromq.plugin.eventcollector.EventType;
import com.zachary.bifromq.type.SubInfo;
import com.zachary.bifromq.type.TopicMessagePack;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public final class Delivered extends Event<Delivered> {

    private int brokerId;
    private String delivererKey;
    private SubInfo subInfo;
    private TopicMessagePack messages;

    @Override
    public EventType type() {
        return EventType.DELIVERED;
    }

    @Override
    public void clone(Delivered orig) {
        super.clone(orig);
        this.brokerId = orig.brokerId;
        this.delivererKey = orig.delivererKey;
        this.subInfo = orig.subInfo;
        this.messages = orig.messages;
    }
}
