package com.zachary.bifromq.plugin.eventcollector.mqttbroker.clientdisconnect;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public final class ClientChannelError extends ClientDisconnectEvent<ClientChannelError> {
    private Throwable cause;

    @Override
    public EventType type() {
        return EventType.CLIENT_CHANNEL_ERROR;
    }

    @Override
    public void clone(ClientChannelError orig) {
        super.clone(orig);
        this.cause = orig.cause;
    }
}
