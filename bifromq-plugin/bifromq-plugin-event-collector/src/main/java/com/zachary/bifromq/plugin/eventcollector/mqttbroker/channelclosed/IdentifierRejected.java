package com.zachary.bifromq.plugin.eventcollector.mqttbroker.channelclosed;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.ToString;

@ToString(callSuper = true)
public final class IdentifierRejected extends ChannelClosedEvent<IdentifierRejected> {
    @Override
    public EventType type() {
        return EventType.IDENTIFIER_REJECTED;
    }
}
