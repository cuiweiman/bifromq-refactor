package com.zachary.bifromq.dist.worker.scheduler;

import com.zachary.bifromq.type.TopicMessagePack;

/**
 * Using the wrapper as the map key instead of using TopicMessagePack directly to prevent heavy hashcode calculation
 */
public class MessagePackWrapper {
    public final TopicMessagePack messagePack;

    private MessagePackWrapper(TopicMessagePack messagePack) {
        this.messagePack = messagePack;
    }

    public static MessagePackWrapper wrap(TopicMessagePack msgPack) {
        return new MessagePackWrapper(msgPack);
    }
}
