package com.zachary.bifromq.inbox.store;

public interface IInboxStore {
    String CLUSTER_NAME = "inbox.store";

    static InboxStoreBuilder.InProcStore inProcBuilder() {
        return new InboxStoreBuilder.InProcStore();
    }

    static InboxStoreBuilder.NonSSLInboxBuilder nonSSLBuilder() {
        return new InboxStoreBuilder.NonSSLInboxBuilder();
    }

    static InboxStoreBuilder.SSLInboxBuilder sslBuilder() {
        return new InboxStoreBuilder.SSLInboxBuilder();
    }


    String id();

    void start(boolean bootstrap);

    void stop();
}
