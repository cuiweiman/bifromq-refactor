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
public final class SubscribeError extends Event<SubscribeError> {
    private long reqId;
    private String topicFilter;
    private QoS qos;
    private String tenantId;
    private String inboxId;
    private int subBrokerId;
    private String delivererKey;

    @Override
    public EventType type() {
        return EventType.SUBSCRIBE_ERROR;
    }

    @Override
    public void clone(SubscribeError orig) {
        super.clone(orig);
        this.reqId = orig.reqId;
        this.topicFilter = orig.topicFilter;
        this.qos = orig.qos;
        this.tenantId = orig.tenantId;
        this.inboxId = orig.inboxId;
        this.subBrokerId = orig.subBrokerId;
        this.delivererKey = orig.delivererKey;
    }
}
