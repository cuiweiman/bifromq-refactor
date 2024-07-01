package com.zachary.bifromq.dist.worker;

public interface IDistWorker {
    String CLUSTER_NAME = "dist.worker";

    static DistWorkerBuilder.InProcDistWorker inProcBuilder() {
        return new DistWorkerBuilder.InProcDistWorker();
    }

    static DistWorkerBuilder.NonSSLDistWorkerBuilder nonSSLBuilder() {
        return new DistWorkerBuilder.NonSSLDistWorkerBuilder();
    }

    static DistWorkerBuilder.SSLDistWorkerBuilder sslBuilder() {
        return new DistWorkerBuilder.SSLDistWorkerBuilder();
    }

    String id();

    void start(boolean bootstrap);

    void stop();
}
