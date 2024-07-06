package com.zachary.bifromq.dist.worker.scheduler;

public class DeliveryRequest {
    public final SubInfo subInfo;
    public final MessagePackWrapper msgPackWrapper;
    public final DelivererKey writerKey;

    public DeliveryRequest(SubInfo subInfo, int brokerId, String delivererKey, MessagePackWrapper msgPackWrapper) {
        this.subInfo = subInfo;
        this.msgPackWrapper = msgPackWrapper;
        writerKey = new DelivererKey(brokerId, delivererKey);
    }

    public record DelivererKey(int subBrokerId, String delivererKey) {
    }
}
