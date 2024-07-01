package com.zachary.bifromq.dist.entity;

import com.google.common.base.Strings;
import lombok.EqualsAndHashCode;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.zachary.bifromq.dist.util.TopicUtil.NUL;

@EqualsAndHashCode
public class Inbox {
    public final int broker;
    public final String inboxId;
    public final String delivererKey;

    Inbox(String scopedInboxId) {
        scopedInboxId = new String(Base64.getDecoder().decode(scopedInboxId), StandardCharsets.UTF_8);
        String[] parts = scopedInboxId.split(NUL);
        assert parts.length >= 3;
        broker = Integer.parseInt(parts[0]);
        inboxId = parts[1];
        this.delivererKey = Strings.isNullOrEmpty(parts[2]) ? null : parts[2];
    }
}
