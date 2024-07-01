package com.zachary.bifromq.plugin.subbroker;

public interface ISubBrokerManager {
    ISubBroker get(int subBrokerId);

    void stop();
}
