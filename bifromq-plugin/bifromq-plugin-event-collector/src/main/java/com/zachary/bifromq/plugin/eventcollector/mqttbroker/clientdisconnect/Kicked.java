package com.zachary.bifromq.plugin.eventcollector.mqttbroker.clientdisconnect;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import com.zachary.bifromq.type.ClientInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public final class Kicked extends ClientDisconnectEvent<Kicked> {
    private ClientInfo kicker;

    @Override
    public EventType type() {
        return EventType.KICKED;
    }

    @Override
    public void clone(Kicked orig) {
        super.clone(orig);
        this.kicker = orig.kicker;
    }
}
