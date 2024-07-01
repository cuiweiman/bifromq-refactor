package com.zachary.bifromq.plugin.eventcollector;

import lombok.SneakyThrows;
import lombok.ToString;

@ToString
public abstract class Event<T extends Event<T>> implements Cloneable {
    private long hlc;

    public abstract EventType type();

    /**
     * The UTC timestamp of the event in milliseconds
     *
     * @return the timestamp
     */
    public long utc() {
        return hlc >>> 16;
    }

    /**
     * The timestamp from Hybrid Logical Clock, which is usually used for causal reasoning
     *
     * @return the hlc timestamp
     */
    public long hlc() {
        return hlc;
    }

    void hlc(long hlc) {
        this.hlc = hlc;
    }

    @SneakyThrows
    @Override
    public Object clone() {
        return super.clone();
    }

    public void clone(T orig) {
        this.hlc = orig.hlc();
    }
}
