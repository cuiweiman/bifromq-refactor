package com.zachary.bifromq.plugin.eventcollector.mqttbroker.channelclosed;

import com.zachary.bifromq.plugin.eventcollector.Event;
import lombok.ToString;

import java.net.InetSocketAddress;

/**
 * ChannelClosed event will be reported during session establishment process, especially before client being identified.
 * Examine the reason enum and downcast to corresponding subclass for concrete details
 */
@ToString(callSuper = true)
public abstract class ChannelClosedEvent<T extends ChannelClosedEvent<T>> extends Event<T> {
    protected InetSocketAddress peerAddress;

    @Override
    public void clone(T orig) {
        this.peerAddress = orig.peerAddress;
    }

    public final InetSocketAddress peerAddress() {
        return this.peerAddress;
    }

    public final T peerAddress(InetSocketAddress peerAddress) {
        this.peerAddress = peerAddress;
        return (T) this;
    }
}
