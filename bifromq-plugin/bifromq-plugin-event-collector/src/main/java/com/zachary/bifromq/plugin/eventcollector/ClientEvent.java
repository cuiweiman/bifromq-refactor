package com.zachary.bifromq.plugin.eventcollector;

import com.zachary.bifromq.type.ClientInfo;
import lombok.ToString;

@ToString
public abstract class ClientEvent<T extends ClientEvent<T>> extends Event<T> {
    private ClientInfo clientInfo;

    public final ClientInfo clientInfo() {
        return this.clientInfo;
    }

    public final T clientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
        return (T) this;
    }

    @Override
    public void clone(T orig) {
        super.clone(orig);
        this.clientInfo = orig.clientInfo();
    }
}
