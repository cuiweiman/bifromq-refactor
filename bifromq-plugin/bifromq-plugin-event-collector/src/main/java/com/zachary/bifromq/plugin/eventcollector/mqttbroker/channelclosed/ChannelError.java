package com.zachary.bifromq.plugin.eventcollector.mqttbroker.channelclosed;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.ToString;

@ToString(callSuper = true)
public final class ChannelError extends ChannelClosedEvent<ChannelError> {
    private Throwable cause;

    public Throwable cause() {
        return cause;
    }

    public ChannelError cause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    @Override
    public EventType type() {
        return EventType.CHANNEL_ERROR;
    }

    @Override
    public void clone(ChannelError orig) {
        super.clone(orig);
        this.cause = orig.cause;
    }
}
