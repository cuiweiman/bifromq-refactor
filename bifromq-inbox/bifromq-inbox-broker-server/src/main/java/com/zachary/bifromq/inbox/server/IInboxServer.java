package com.zachary.bifromq.inbox.server;

public interface IInboxServer {
    static InboxServerBuilder.InProcServerBuilder inProcBuilder() {
        return new InboxServerBuilder.InProcServerBuilder();
    }

    static InboxServerBuilder.NonSSLServerBuilder nonSSLBuilder() {
        return new InboxServerBuilder.NonSSLServerBuilder();
    }

    static InboxServerBuilder.SSLServerBuilder sslBuilder() {
        return new InboxServerBuilder.SSLServerBuilder();
    }


    void start();

    void shutdown();
}
