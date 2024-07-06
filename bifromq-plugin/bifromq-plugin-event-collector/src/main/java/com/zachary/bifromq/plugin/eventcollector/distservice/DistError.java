package com.zachary.bifromq.plugin.eventcollector.distservice;

import com.zachary.bifromq.plugin.eventcollector.Event;
import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@ToString(callSuper = true)
public class DistError extends Event<DistError> {
    public enum DistErrorCode {
        DROP_EXCEED_LIMIT, RPC_FAILURE
    }

    private long reqId;
    private Iterable<PublisherMessagePack> messages;
    private DistErrorCode code;

    @Override
    public EventType type() {
        return EventType.DIST_ERROR;
    }

    @Override
    public void clone(DistError orig) {
        super.clone(orig);
        this.reqId = orig.reqId;
        this.messages = orig.messages;
        this.code = orig.code;
    }
}
