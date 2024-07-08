package com.zachary.bifromq.plugin.eventcollector.mqttbroker.clientdisconnect;

import com.zachary.bifromq.plugin.eventcollector.EventType;
import com.zachary.bifromq.type.QoS;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public final class NoPubPermission extends ClientDisconnectEvent<NoPubPermission> {
    private String topic;

    private QoS qos;

    private boolean retain;

    @Override
    public EventType type() {
        return EventType.NO_PUB_PERMISSION;
    }

    @Override
    public void clone(NoPubPermission orig) {
        super.clone(orig);
        this.topic = orig.topic;
        this.qos = orig.qos;
        this.retain = orig.retain;
    }
}
