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
public final class DeliverError extends Event<DeliverError> {

    private int brokerId;
    private String delivererKey;
    private SubInfo subInfo;
    private TopicMessagePack messages;

    @Override
    public EventType type() {
        return EventType.DELIVER_ERROR;
    }

    @Override
    public void clone(DeliverError orig) {
        super.clone(orig);
        this.brokerId = orig.brokerId;
        this.delivererKey = orig.delivererKey;
        this.subInfo = orig.subInfo;
        this.messages = orig.messages;
    }
}
