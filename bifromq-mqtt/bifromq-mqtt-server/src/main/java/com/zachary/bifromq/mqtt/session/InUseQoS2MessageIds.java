package com.zachary.bifromq.mqtt.session;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.Duration;

public class InUseQoS2MessageIds {
    private final LoadingCache<String, Cache<MessageId, MessageId>> msgIdCache;

    InUseQoS2MessageIds(Duration lifetime) {
        msgIdCache = Caffeine.newBuilder()
            .expireAfterAccess(lifetime.multipliedBy(2))
            .build(k -> Caffeine.newBuilder()
                .expireAfterWrite(lifetime)
                .build());
    }

    public void use(String tenantId, String channelId, int messageId) {
        MessageId msgId = new MessageId(channelId, messageId);
        msgIdCache.get(tenantId).put(msgId, msgId);
    }

    public boolean inUse(String tenantId, String channelId, int messageId) {
        return msgIdCache.get(tenantId).getIfPresent(new MessageId(channelId, messageId)) != null;
    }

    public void release(String tenantId, String channelId, int messageId) {
        msgIdCache.get(tenantId).invalidate(new MessageId(channelId, messageId));
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    private static final class MessageId {
        final String channelId;
        final int messageId;
    }
}
