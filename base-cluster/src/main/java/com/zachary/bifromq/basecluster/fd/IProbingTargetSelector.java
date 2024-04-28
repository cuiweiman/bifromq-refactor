package com.zachary.bifromq.basecluster.fd;

import java.util.Collection;

public interface IProbingTargetSelector {

    DirectProbingInfo targetForProbe();

    Collection<IProbingTarget> targetForIndirectProbes(IProbingTarget skip, int num);
}
