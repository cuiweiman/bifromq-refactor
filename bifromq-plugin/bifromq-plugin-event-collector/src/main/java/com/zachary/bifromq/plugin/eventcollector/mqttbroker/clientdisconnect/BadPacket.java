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
public class BadPacket extends ClientDisconnectEvent<BadPacket> {
    private Throwable cause;

    @Override
    public EventType type() {
        return EventType.BAD_PACKET;
    }

    @Override
    public void clone(BadPacket orig) {
        super.clone(orig);
        this.cause = orig.cause;
    }
}
