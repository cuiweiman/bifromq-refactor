package com.zachary.bifromq.plugin.eventcollector.mqttbroker.clientdisconnect;


import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.ToString;

@ToString(callSuper = true)
public final class InboxTransientError extends ClientDisconnectEvent<InboxTransientError> {
    @Override
    public EventType type() {
        return EventType.INBOX_TRANSIENT_ERROR;
    }
}
