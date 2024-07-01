package com.zachary.bifromq.baserpc.trafficgovernor;

import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.baserpc.proto.RPCServer;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

final class RPCServiceTrafficGovernor extends RPCServiceTrafficDirector implements IRPCServiceTrafficGovernor {

    public RPCServiceTrafficGovernor(String serviceUniqueName, ICRDTService crdtService) {
        super(serviceUniqueName, crdtService);
    }

    @Override
    public void assignLBGroups(String id, Set<String> groupTags) {
        Optional<RPCServer> announced = announcedServer(id);
        if (announced.isPresent() && (!groupTags.containsAll(announced.get().getGroupList()) ||
            !announced.get().getGroupList().containsAll(groupTags))) {
            RPCServer updated = announced.get().toBuilder()
                .clearGroup()
                .addAllGroup(groupTags)
                .setAnnouncedTS(System.currentTimeMillis())
                .build();
            announce(updated);
        }
    }

    @Override
    public void updateTrafficDirective(Map<String, Map<String, Integer>> trafficDirective) {
        announce(trafficDirective);
    }
}
