package com.zachary.bifromq.sessiondict.server;

public interface ISessionDictionaryServer {
    static SessionDictionaryServerBuilder.InProcServerBuilder inProcServerBuilder() {
        return new SessionDictionaryServerBuilder.InProcServerBuilder();
    }

    static SessionDictionaryServerBuilder.NonSSLServerBuilder nonSSLServerBuilder() {
        return new SessionDictionaryServerBuilder.NonSSLServerBuilder();
    }

    static SessionDictionaryServerBuilder.SSLServerBuilder sslServerBuilder() {
        return new SessionDictionaryServerBuilder.SSLServerBuilder();
    }

    void start();

    void shutdown();
}
