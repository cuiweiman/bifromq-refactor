package com.zachary.bifromq.plugin.eventcollector.mqttbroker.channelclosed;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.ToString;

@ToString(callSuper = true)
public final class ConnectTimeout extends ChannelClosedEvent<ConnectTimeout> {
    @Override
    public EventType type() {
        return EventType.CONNECT_TIMEOUT;
    }
}
