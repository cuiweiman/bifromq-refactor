package com.zachary.bifromq.basekv.server;

public interface IBaseKVStoreServer {
    static BaseKVStoreServerBuilder.InProcBaseKVStoreServerBuilder inProcServerBuilder() {
        return new BaseKVStoreServerBuilder.InProcBaseKVStoreServerBuilder();
    }

    static BaseKVStoreServerBuilder.NonSSLBaseKVStoreServerBuilder nonSSLServerBuilder() {
        return new BaseKVStoreServerBuilder.NonSSLBaseKVStoreServerBuilder();
    }

    static BaseKVStoreServerBuilder.SSLBaseKVStoreServerBuilder sslServerBuilder() {
        return new BaseKVStoreServerBuilder.SSLBaseKVStoreServerBuilder();
    }

    String id();

    void start(boolean bootstrap);

    void stop();
}
