package com.zachary.bifromq.mqtt.handler.event;

import com.zachary.bifromq.plugin.eventcollector.Event;

public class ConnectionWillClose {

    public final Event reason;

    public ConnectionWillClose(Event reason) {
        this.reason = reason;
    }
}
