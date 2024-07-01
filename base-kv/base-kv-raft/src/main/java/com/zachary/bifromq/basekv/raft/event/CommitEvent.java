package com.zachary.bifromq.basekv.raft.event;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CommitEvent extends RaftEvent {

    public final long index;

    public CommitEvent(String nodeId, long index) {
        super(nodeId, RaftEventType.COMMIT);
        this.index = index;
    }
}
