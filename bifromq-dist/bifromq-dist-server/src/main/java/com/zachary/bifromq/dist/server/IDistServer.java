package com.zachary.bifromq.dist.server;

public interface IDistServer {
    static DistServerBuilder.InProcDistServerBuilder inProcBuilder() {
        return new DistServerBuilder.InProcDistServerBuilder();
    }

    static DistServerBuilder.NonSSLDistServerBuilder nonSSLBuilder() {
        return new DistServerBuilder.NonSSLDistServerBuilder();
    }

    static DistServerBuilder.SSLDistServerBuilder sslBuilder() {
        return new DistServerBuilder.SSLDistServerBuilder();
    }

    void start();

    void shutdown();
}
