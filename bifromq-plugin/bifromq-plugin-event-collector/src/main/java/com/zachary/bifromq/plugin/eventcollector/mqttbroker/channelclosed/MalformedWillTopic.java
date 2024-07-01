package com.zachary.bifromq.plugin.eventcollector.mqttbroker.channelclosed;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.ToString;

@ToString(callSuper = true)
public class MalformedWillTopic extends ChannelClosedEvent<MalformedWillTopic> {
    @Override
    public EventType type() {
        return EventType.MALFORMED_WILL_TOPIC;
    }
}
