package com.zachary.bifromq.basecluster.fd;


import com.zachary.bifromq.basecluster.proto.ClusterMessage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DirectProbingInfo {

    public final Optional<IProbingTarget> target;
    public final List<ClusterMessage> piggybacked;

    public DirectProbingInfo(Optional<IProbingTarget> target) {
        this(target, Collections.emptyList());
    }

    public DirectProbingInfo(Optional<IProbingTarget> target, List<ClusterMessage> piggybacked) {
        this.target = target;
        this.piggybacked = piggybacked;
    }
}
