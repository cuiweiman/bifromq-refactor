package com.zachary.bifromq.baserpc.trafficgovernor;

import com.zachary.bifromq.basecrdt.service.ICRDTService;
import io.reactivex.rxjava3.core.Observable;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;

public interface IRPCServiceTrafficDirector {
    static IRPCServiceTrafficDirector newInstance(String serviceUniqueName, ICRDTService crdtService) {
        return new RPCServiceTrafficDirector(serviceUniqueName, crdtService);
    }

    @AllArgsConstructor
    @ToString
    class Server {
        public final String id;
        public final InetSocketAddress hostAddr;
        public final Set<String> groupTags;
        public final Map<String, String> attrs;
    }

    /**
     * Current traffic directive of the service
     *
     * @return
     */
    Observable<Map<String, Map<String, Integer>>> trafficDirective();

    /**
     * Watch the ever-updating server list of the service
     *
     * @return
     */
    Observable<Set<Server>> serverList();

    void destroy();
}
