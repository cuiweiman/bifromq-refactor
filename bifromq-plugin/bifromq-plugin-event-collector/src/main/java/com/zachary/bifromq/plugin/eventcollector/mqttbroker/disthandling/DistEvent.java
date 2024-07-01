package com.zachary.bifromq.plugin.eventcollector.mqttbroker.disthandling;

import com.zachary.bifromq.plugin.eventcollector.ClientEvent;
import lombok.ToString;

import java.nio.ByteBuffer;

@ToString(callSuper = true)
public abstract class DistEvent<T extends DistEvent<T>> extends ClientEvent<T> {
    private long reqId;

    private String topic;

    private ByteBuffer payload;

    private int size;

    public final long reqId() {
        return this.reqId;
    }

    public final T reqId(long reqId) {
        this.reqId = reqId;
        return (T) this;
    }

    public final String topic() {
        return this.topic;
    }

    public final T topic(String topic) {
        this.topic = topic;
        return (T) this;
    }

    public final int size() {
        return size;
    }

    public final T size(int size) {
        this.size = size;
        return (T) this;
    }

    @Override
    public void clone(T orig) {
        super.clone(orig);
        this.size = orig.size();
        this.reqId = orig.reqId();
        this.topic = orig.topic();
    }
}
