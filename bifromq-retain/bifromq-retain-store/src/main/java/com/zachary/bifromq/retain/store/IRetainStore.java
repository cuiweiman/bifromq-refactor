package com.zachary.bifromq.retain.store;

public interface IRetainStore {
    String CLUSTER_NAME = "retain.store";

    static RetainStoreBuilder.InProcInboxStore inProcBuilder() {
        return new RetainStoreBuilder.InProcInboxStore();
    }

    static RetainStoreBuilder.NonSSLRetainStoreBuilder nonSSLBuilder() {
        return new RetainStoreBuilder.NonSSLRetainStoreBuilder();
    }

    static RetainStoreBuilder.SSLRetainStoreBuilder sslBuilder() {
        return new RetainStoreBuilder.SSLRetainStoreBuilder();
    }


    String id();

    void start(boolean bootstrap);

    void stop();
}
