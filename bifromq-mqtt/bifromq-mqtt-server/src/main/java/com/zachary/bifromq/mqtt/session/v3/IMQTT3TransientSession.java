package com.zachary.bifromq.mqtt.session.v3;

import com.zachary.bifromq.type.SubInfo;
import com.zachary.bifromq.type.TopicMessagePack;

public interface IMQTT3TransientSession extends IMQTT3Session {

    void publish(SubInfo subInfo, TopicMessagePack messagePack);
}
