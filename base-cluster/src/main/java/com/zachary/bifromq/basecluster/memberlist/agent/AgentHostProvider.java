package com.zachary.bifromq.basecluster.memberlist.agent;

import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import com.zachary.bifromq.basecluster.membership.proto.HostMember;
import io.reactivex.rxjava3.core.Observable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AgentHostProvider implements IAgentHostProvider {
    private final String agentId;
    private final Observable<Map<HostEndpoint, HostMember>> aliveHosts;

    public AgentHostProvider(String agentId, Observable<Map<HostEndpoint, HostMember>> aliveHosts) {
        this.agentId = agentId;
        this.aliveHosts = aliveHosts;
    }

    @Override
    public Observable<Set<HostEndpoint>> getHostEndpoints() {
        return aliveHosts
            .map(aliveHostList -> {
                Set<HostEndpoint> agentHosts = new HashSet<>();
                for (HostMember record : aliveHostList.values()) {
                    if (record.getAgentIdList().contains(agentId)) {
                        agentHosts.add(record.getEndpoint());
                    }
                }
                return agentHosts;
            })
            .distinctUntilChanged();
    }
}
