package com.zachary.bifromq.plugin.eventcollector.mqttbroker.channelclosed;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.ToString;

@ToString(callSuper = true)
public final class UnacceptedProtocolVer extends ChannelClosedEvent<UnacceptedProtocolVer> {
    @Override
    public EventType type() {
        return EventType.UNACCEPTED_PROTOCOL_VER;
    }
}
