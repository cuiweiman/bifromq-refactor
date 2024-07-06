package com.zachary.bifromq.plugin.eventcollector.mqttbroker.retainhandling;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.nio.ByteBuffer;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
public final class MsgRetainedError extends RetainEvent<MsgRetainedError> {
    private String topic;

    private boolean isLastWill;

    private QoS qos;

    private ByteBuffer payload;

    private int size;

    @Override
    public EventType type() {
        return EventType.MSG_RETAINED_ERROR;
    }

    @Override
    public void clone(MsgRetainedError orig) {
        super.clone(orig);
        this.topic = orig.topic;
        this.isLastWill = orig.isLastWill;
        this.qos = orig.qos;
        this.payload = orig.payload;
        this.size = orig.size;
    }
}
