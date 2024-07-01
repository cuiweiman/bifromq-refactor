package com.zachary.bifromq.basekv.balance.utils;

import com.zachary.bifromq.basekv.proto.KVRangeDescriptor;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.LoadHint;
import com.zachary.bifromq.basekv.proto.State.StateType;
import com.zachary.bifromq.basekv.raft.proto.ClusterConfig;
import com.zachary.bifromq.basekv.raft.proto.RaftNodeStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DescriptorUtils {

    public static List<KVRangeDescriptor> generateRangeDesc(KVRangeId id, Set<String> voters, Set<String> learner) {
        List<KVRangeDescriptor> descriptors = new ArrayList<>();
        for (int i = 0; i < voters.size() + learner.size(); i++) {
            RaftNodeStatus raftNodeStatus = i == 0 ? RaftNodeStatus.Leader : RaftNodeStatus.Follower;
            KVRangeDescriptor rangeDescriptor = KVRangeDescriptor.newBuilder()
                .setId(id)
                .setState(StateType.Normal)
                .setRole(raftNodeStatus)
                .setLoadHint(LoadHint.newBuilder()
                    .setLoad(100 - i)
                    .build()
                )
                .setVer(1)
                .setConfig(ClusterConfig.newBuilder()
                    .addAllVoters(voters)
                    .addAllLearners(learner)
                    .build()
                ).build();
            descriptors.add(rangeDescriptor);
        }
        return descriptors;
    }
}
