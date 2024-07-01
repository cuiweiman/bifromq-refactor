package com.zachary.bifromq.plugin.eventcollector.mqttbroker;

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
public final class PingReq extends ClientEvent<PingReq> {

    /**
     * If ping resp send successfully
     */
    private boolean pong;

    @Override
    public EventType type() {
        return EventType.PING_REQ;
    }

    @Override
    public void clone(PingReq orig) {
        super.clone(orig);
        this.pong = orig.pong;
    }
}
