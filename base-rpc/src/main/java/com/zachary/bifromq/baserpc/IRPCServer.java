package com.zachary.bifromq.baserpc;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/28 16:31
 */
public interface IRPCServer {

    static RPCServerBuilder.InProcServerBuilder inProcServerBuilder() {
        return new RPCServerBuilder.InProcServerBuilder();
    }

    static RPCServerBuilder.NonSSLServerBuilder nonSSLServerBuilder() {
        return new RPCServerBuilder.NonSSLServerBuilder();
    }

    static RPCServerBuilder.SSLServerBuilder sslServerBuilder() {
        return new RPCServerBuilder.SSLServerBuilder();
    }

    String id();

    String serviceUniqueName();

    void start();

    void shutdown();
}
