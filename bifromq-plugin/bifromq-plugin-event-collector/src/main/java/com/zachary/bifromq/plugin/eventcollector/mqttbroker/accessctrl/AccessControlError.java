package com.zachary.bifromq.plugin.eventcollector.mqttbroker.accessctrl;

import com.zachary.bifromq.plugin.eventcollector.ClientEvent;
import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public final class AccessControlError extends ClientEvent<AccessControlError> {
    private Throwable cause;

    @Override
    public EventType type() {
        return EventType.ACCESS_CONTROL_ERROR;
    }

    @Override
    public void clone(AccessControlError orig) {
        super.clone(orig);
        this.cause = orig.cause;
    }
}
