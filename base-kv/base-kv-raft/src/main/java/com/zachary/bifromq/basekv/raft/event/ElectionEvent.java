package com.zachary.bifromq.basekv.raft.event;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class ElectionEvent extends RaftEvent {

    public final String leaderId;
    public final long term;

    public ElectionEvent(String nodeId, String leaderId, long term) {
        super(nodeId, RaftEventType.ELECTION);
        this.leaderId = leaderId;
        this.term = term;
    }
}
