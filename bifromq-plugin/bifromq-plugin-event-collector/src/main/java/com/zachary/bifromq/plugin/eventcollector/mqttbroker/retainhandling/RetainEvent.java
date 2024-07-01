package com.zachary.bifromq.plugin.eventcollector.mqttbroker.retainhandling;

import com.zachary.bifromq.plugin.eventcollector.ClientEvent;
import lombok.ToString;

@ToString(callSuper = true)
public abstract class RetainEvent<T extends RetainEvent<T>> extends ClientEvent<T> {
    private long reqId;

    public final long reqId() {
        return reqId;
    }

    public final T reqId(long reqId) {
        this.reqId = reqId;
        return (T) this;
    }

    @Override
    public void clone(T orig) {
        super.clone(orig);
        this.reqId = orig.reqId();
    }
}
