package com.zachary.bifromq.dist.server.scheduler;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DistCall {
    public final String tenatId;
    public final List<PublisherMessagePack> publisherMsgPacks;
    public final Integer callQueueIdx;
    public final int fanout;
}
