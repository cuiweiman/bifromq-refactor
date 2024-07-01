package com.zachary.bifromq.mqtt.service;

import com.zachary.bifromq.mqtt.session.IMQTTSession;

import java.util.List;

public interface ILocalSessionRegistry {
    void add(String sessionId, IMQTTSession session);

    boolean remove(String sessionId, IMQTTSession session);

    List<IMQTTSession> removeAll();
}
