package com.zachary.bifromq.mqtt.session.v3;

import com.zachary.bifromq.mqtt.session.IMQTTSession;
import com.zachary.bifromq.type.ClientInfo;

public interface IMQTT3Session extends IMQTTSession {
    String channelId();

    ClientInfo clientInfo();
}
