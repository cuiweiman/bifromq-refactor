package com.zachary.bifromq.basekv.raft.event;

import com.zachary.bifromq.basekv.raft.proto.RaftNodeStatus;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StatusChangedEvent extends RaftEvent {
    public final RaftNodeStatus status;

    public StatusChangedEvent(String nodeId, RaftNodeStatus status) {
        super(nodeId, RaftEventType.STATUS_CHANGED);
        this.status = status;
    }
}
