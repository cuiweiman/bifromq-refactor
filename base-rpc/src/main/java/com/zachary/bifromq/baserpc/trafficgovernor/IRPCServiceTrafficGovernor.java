package com.zachary.bifromq.baserpc.trafficgovernor;

import com.zachary.bifromq.basecrdt.service.ICRDTService;

import java.util.Map;
import java.util.Set;

public interface IRPCServiceTrafficGovernor extends IRPCServiceTrafficDirector {
    static IRPCServiceTrafficGovernor newInstance(String serviceUniqueName, ICRDTService crdtService) {
        return new RPCServiceTrafficGovernor(serviceUniqueName, crdtService);
    }

    /**
     * Update the groupTags for a server. If the server not join yet, nothing happens
     *
     * @param id        the id of the server
     * @param groupTags
     */
    void assignLBGroups(String id, Set<String> groupTags);

    /**
     * Update the traffic directive in the form of mapping tenantIdPrefix to a set of group tags
     *
     * @param trafficDirective
     */
    void updateTrafficDirective(Map<String, Map<String, Integer>> trafficDirective);
}
