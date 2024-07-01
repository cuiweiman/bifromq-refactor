package com.zachary.bifromq.plugin.eventcollector.mqttbroker.channelclosed;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.ToString;

@ToString(callSuper = true)
public class MalformedClientIdentifier extends ChannelClosedEvent<MalformedClientIdentifier> {
    @Override
    public EventType type() {
        return EventType.MALFORMED_CLIENT_IDENTIFIER;
    }
}
