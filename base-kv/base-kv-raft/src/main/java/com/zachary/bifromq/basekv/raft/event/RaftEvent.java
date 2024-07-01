package com.zachary.bifromq.basekv.raft.event;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public abstract class RaftEvent {

    public final String nodeId;
    public final RaftEventType type;

    protected RaftEvent(String nodeId, RaftEventType type) {
        this.nodeId = nodeId;
        this.type = type;
    }

}
