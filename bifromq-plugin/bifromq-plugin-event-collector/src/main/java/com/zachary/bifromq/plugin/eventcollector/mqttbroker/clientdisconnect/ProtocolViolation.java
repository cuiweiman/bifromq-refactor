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
public final class ProtocolViolation extends ClientDisconnectEvent<ProtocolViolation> {
    private String statement;

    @Override
    public EventType type() {
        return EventType.PROTOCOL_VIOLATION;
    }

    @Override
    public void clone(ProtocolViolation orig) {
        super.clone(orig);
        this.statement = orig.statement;
    }
}
