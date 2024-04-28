package com.zachary.bifromq.basecluster.memberlist.agent;

import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import io.reactivex.rxjava3.core.Observable;

import java.util.Set;

public interface IAgentHostProvider {
    /**
     * A hot observable about the hosts of the agent
     *
     * @return
     */
    Observable<Set<HostEndpoint>> getHostEndpoints();
}
