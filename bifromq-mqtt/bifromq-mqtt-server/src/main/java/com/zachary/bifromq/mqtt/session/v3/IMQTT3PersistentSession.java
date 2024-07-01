package com.zachary.bifromq.mqtt.session.v3;

public interface IMQTT3PersistentSession extends IMQTT3Session {
    boolean sessionPresent();
}
