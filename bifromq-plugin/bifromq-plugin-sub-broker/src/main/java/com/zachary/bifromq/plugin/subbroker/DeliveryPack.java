package com.zachary.bifromq.plugin.subbroker;

import com.zachary.bifromq.type.SubInfo;
import com.zachary.bifromq.type.TopicMessagePack;

public class DeliveryPack {
    public final TopicMessagePack messagePack;
    public final Iterable<SubInfo> inboxes;

    public DeliveryPack(TopicMessagePack messagePack, Iterable<SubInfo> inboxes) {
        this.messagePack = messagePack;
        this.inboxes = inboxes;
    }
}
