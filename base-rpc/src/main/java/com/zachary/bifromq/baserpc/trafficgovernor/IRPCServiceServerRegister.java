package com.zachary.bifromq.baserpc.trafficgovernor;

import com.zachary.bifromq.basecrdt.service.ICRDTService;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public interface IRPCServiceServerRegister {
    static IRPCServiceServerRegister newInstance(String serviceUniqueName, ICRDTService crdtService) {
        return new RPCServiceServerRegister(serviceUniqueName, crdtService);
    }

    /**
     * Join the registration by join a server into the service
     *
     * @param id        the id of the server
     * @param hostAddr  the hosting address
     * @param groupTags the initial group tags assigned
     * @param attrs     the associated attributes
     */
    void start(String id, InetSocketAddress hostAddr, Set<String> groupTags, Map<String, String> attrs);

    default void start(String id, InetSocketAddress hostAddr) {
        start(id, hostAddr, Collections.emptySet(), Collections.emptyMap());
    }

    void stop();
}
