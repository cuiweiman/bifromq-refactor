package com.zachary.bifromq.plugin.eventcollector.mqttbroker.clientdisconnect;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.ToString;

@ToString(callSuper = true)
public final class ByClient extends ClientDisconnectEvent<ByClient> {
    @Override
    public EventType type() {
        return EventType.BY_CLIENT;
    }
}
