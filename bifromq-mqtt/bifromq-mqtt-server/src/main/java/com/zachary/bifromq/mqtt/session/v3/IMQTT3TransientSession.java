package com.zachary.bifromq.mqtt.session.v3;

public interface IMQTT3TransientSession extends IMQTT3Session {

    void publish(SubInfo subInfo, TopicMessagePack messagePack);
}
