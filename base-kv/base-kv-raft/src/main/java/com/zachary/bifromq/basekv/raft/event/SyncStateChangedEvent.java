package com.zachary.bifromq.basekv.raft.event;

import com.zachary.bifromq.basekv.raft.proto.RaftNodeSyncState;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SyncStateChangedEvent extends RaftEvent {
    public final Map<String, RaftNodeSyncState> states;

    public SyncStateChangedEvent(String nodeId, Map<String, RaftNodeSyncState> states) {
        super(nodeId, RaftEventType.SYNC_STATE_CHANGED);
        this.states = Collections.unmodifiableMap(states);
    }
}
