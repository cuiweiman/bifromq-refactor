package com.zachary.bifromq.plugin.eventcollector.mqttbroker.channelclosed;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.ToString;

@ToString(callSuper = true)
public final class AuthError extends ChannelClosedEvent<AuthError> {
    private String reason;

    public String cause() {
        return reason;
    }

    public AuthError cause(String cause) {
        this.reason = cause;
        return this;
    }

    @Override
    public EventType type() {
        return EventType.AUTH_ERROR;
    }

    @Override
    public void clone(AuthError orig) {
        super.clone(orig);
        this.reason = orig.reason;
    }
}
