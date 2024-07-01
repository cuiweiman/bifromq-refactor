package com.zachary.bifromq.plugin.eventcollector.mqttbroker.pushhandling;

import com.zachary.bifromq.plugin.eventcollector.ClientEvent;
import com.zachary.bifromq.type.ClientInfo;
import lombok.ToString;

@ToString(callSuper = true)
public abstract class PushEvent<T extends PushEvent<T>> extends ClientEvent<T> {
    private long reqId;

    private boolean isRetain;

    private ClientInfo sender;

    private String topic;

    private String matchedFilter;

    private int size;

    public final long reqId() {
        return this.reqId;
    }

    public final T reqId(long reqId) {
        this.reqId = reqId;
        return (T) this;
    }

    public final boolean isRetain() {
        return this.isRetain;
    }

    public final T isRetain(boolean isRetain) {
        this.isRetain = isRetain;
        return (T) this;
    }

    public final ClientInfo sender() {
        return this.sender;
    }

    public final T sender(ClientInfo sender) {
        this.sender = sender;
        return (T) this;
    }

    public final String topic() {
        return this.topic;
    }

    public final T topic(String topic) {
        this.topic = topic;
        return (T) this;
    }

    public final String matchedFilter() {
        return matchedFilter;
    }

    public final T matchedFilter(String matchedFilter) {
        this.matchedFilter = matchedFilter;
        return (T) this;
    }

    public final int size() {
        return this.size;
    }

    public final T size(int size) {
        this.size = size;
        return (T) this;
    }

    @Override
    public void clone(T orig) {
        super.clone(orig);
        this.reqId = orig.reqId();
        this.isRetain = orig.isRetain();
        this.sender = orig.sender();
        this.topic = orig.topic();
        this.matchedFilter = orig.matchedFilter();
        this.size = orig.size();
    }
}
