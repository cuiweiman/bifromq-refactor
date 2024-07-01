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
public final class TooLargeSubscription extends ClientDisconnectEvent<TooLargeSubscription> {
    private int max;

    private int actual;

    @Override
    public EventType type() {
        return EventType.TOO_LARGE_SUBSCRIPTION;
    }

    @Override
    public void clone(TooLargeSubscription orig) {
        super.clone(orig);
        this.max = orig.max;
        this.actual = orig.actual;
    }
}
