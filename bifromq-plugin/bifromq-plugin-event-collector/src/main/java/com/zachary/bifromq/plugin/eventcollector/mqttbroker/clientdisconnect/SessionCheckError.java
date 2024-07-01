package com.zachary.bifromq.plugin.eventcollector.mqttbroker.clientdisconnect;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.ToString;

@ToString(callSuper = true)
public final class SessionCheckError extends ClientDisconnectEvent<SessionCheckError> {

    @Override
    public EventType type() {
        return EventType.SESSION_CHECK_ERROR;
    }
}
