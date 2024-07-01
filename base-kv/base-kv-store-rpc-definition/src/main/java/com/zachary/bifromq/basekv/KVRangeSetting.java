package com.zachary.bifromq.basekv;

import com.zachary.bifromq.basekv.proto.KVRangeDescriptor;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.Range;
import com.zachary.bifromq.basekv.raft.proto.RaftNodeSyncState;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode
@ToString
public class KVRangeSetting {
    public final KVRangeId id;
    public final long ver;
    public final Range range;
    public final String leader;
    public final List<String> followers;
    public final List<String> allReplicas;

    public KVRangeSetting(String leaderStoreId, KVRangeDescriptor desc) {
        id = desc.getId();
        ver = desc.getVer();
        range = desc.getRange();
        leader = leaderStoreId;
        List<String> followers = new ArrayList<>();
        List<String> allReplicas = new ArrayList<>();
        for (String v : desc.getConfig().getVotersList()) {
            if (desc.getSyncStateMap().get(v) == RaftNodeSyncState.Replicating) {
                if (!v.equals(leaderStoreId)) {
                    followers.add(v);
                }
                allReplicas.add(v);
            }
        }
        for (String v : desc.getConfig().getLearnersList()) {
            if (desc.getSyncStateMap().get(v) == RaftNodeSyncState.Replicating) {
                allReplicas.add(v);
            }
        }
        this.followers = Collections.unmodifiableList(followers);
        this.allReplicas = Collections.unmodifiableList(allReplicas);
    }

}
