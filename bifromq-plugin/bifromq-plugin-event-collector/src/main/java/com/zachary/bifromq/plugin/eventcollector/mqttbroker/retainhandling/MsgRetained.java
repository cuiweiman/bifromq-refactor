package com.zachary.bifromq.plugin.eventcollector.mqttbroker.retainhandling;

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
public final class MsgRetained extends RetainEvent<MsgRetained> {
    private String topic;

    private boolean isLastWill;

    private QoS qos;

    private int size;

    @Override
    public EventType type() {
        return EventType.MSG_RETAINED;
    }

    @Override
    public void clone(MsgRetained orig) {
        super.clone(orig);
        this.topic = orig.topic;
        this.isLastWill = orig.isLastWill;
        this.qos = orig.qos;
        this.size = orig.size;
    }
}
