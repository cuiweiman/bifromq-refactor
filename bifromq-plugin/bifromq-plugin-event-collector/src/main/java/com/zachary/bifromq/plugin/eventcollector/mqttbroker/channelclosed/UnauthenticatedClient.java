package com.zachary.bifromq.plugin.eventcollector.mqttbroker.channelclosed;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.ToString;

@ToString(callSuper = true)
public final class UnauthenticatedClient extends ChannelClosedEvent<UnauthenticatedClient> {
    @Override
    public EventType type() {
        return EventType.UNAUTHENTICATED_CLIENT;
    }
}
