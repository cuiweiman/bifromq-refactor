package com.zachary.bifromq.retain.server;

public interface IRetainServer {
    static RetainServerBuilder.InProcServerBuilder inProcBuilder() {
        return new RetainServerBuilder.InProcServerBuilder();
    }

    static RetainServerBuilder.NonSSLServerBuilder nonSSLBuilder() {
        return new RetainServerBuilder.NonSSLServerBuilder();
    }

    static RetainServerBuilder.SSLServerBuilder sslBuilder() {
        return new RetainServerBuilder.SSLServerBuilder();
    }


    void start();

    void shutdown();
}
