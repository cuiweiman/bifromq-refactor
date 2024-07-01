package com.zachary.bifromq.plugin.eventcollector.mqttbroker.retainhandling;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public final class RetainMsgCleared extends RetainEvent<RetainMsgCleared> {
    private String topic;

    private boolean isLastWill;

    @Override
    public EventType type() {
        return EventType.RETAIN_MSG_CLEARED;
    }

    @Override
    public void clone(RetainMsgCleared orig) {
        super.clone(orig);
        this.topic = orig.topic;
        this.isLastWill = orig.isLastWill;
    }
}
