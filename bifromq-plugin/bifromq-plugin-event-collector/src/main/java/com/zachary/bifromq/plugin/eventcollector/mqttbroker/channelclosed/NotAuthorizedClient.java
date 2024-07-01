package com.zachary.bifromq.plugin.eventcollector.mqttbroker.channelclosed;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.ToString;

@ToString(callSuper = true)
public final class NotAuthorizedClient extends ChannelClosedEvent<NotAuthorizedClient> {
    @Override
    public EventType type() {
        return EventType.NOT_AUTHORIZED_CLIENT;
    }
}
