package com.zachary.bifromq.inbox.server;

public interface IInboxQueueFetcher {

    String delivererKey();

    String tenantId();

    String inboxId();

    long lastFetchTS();

    long lastFetchQoS0Seq();

    void signalFetch();

    void touch();

    void close();
}
