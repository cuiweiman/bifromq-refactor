package com.zachary.bifromq.plugin.eventcollector.mqttbroker.clientconnected;

import com.zachary.bifromq.plugin.eventcollector.ClientEvent;
import com.zachary.bifromq.plugin.eventcollector.EventType;
import com.zachary.bifromq.type.QoS;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.nio.ByteBuffer;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public final class ClientConnected extends ClientEvent<ClientConnected> {
    private String serverId;

    private String userSessionId;

    private int keepAliveTimeSeconds;

    private boolean cleanSession;

    private boolean sessionPresent;

    private WillInfo lastWill;

    @Override
    public EventType type() {
        return EventType.CLIENT_CONNECTED;
    }

    @Override
    public void clone(ClientConnected orig) {
        super.clone(orig);
        this.serverId = orig.serverId;
        this.userSessionId = orig.userSessionId;
        this.keepAliveTimeSeconds = orig.keepAliveTimeSeconds;
        this.cleanSession = orig.cleanSession;
        this.sessionPresent = orig.sessionPresent;
        this.lastWill = orig.lastWill;
    }

    @Getter
    @Setter
    @Accessors(fluent = true, chain = true)
    public static class WillInfo {
        private String topic;

        private QoS qos;

        private boolean isRetain;

        private ByteBuffer payload;
    }
}
