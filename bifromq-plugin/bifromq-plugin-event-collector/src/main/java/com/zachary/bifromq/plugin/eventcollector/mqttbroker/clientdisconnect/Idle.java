package com.zachary.bifromq.plugin.eventcollector.mqttbroker.clientdisconnect;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public final class Idle extends ClientDisconnectEvent<Idle> {
    private int keepAliveTimeSeconds;

    @Override
    public EventType type() {
        return EventType.IDLE;
    }

    @Override
    public void clone(Idle orig) {
        super.clone(orig);
        this.keepAliveTimeSeconds = orig.keepAliveTimeSeconds;
    }
}
