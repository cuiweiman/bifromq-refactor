package com.zachary.bifromq.basecrdt.service;

import com.zachary.bifromq.basecluster.AgentHostOptions;
import com.zachary.bifromq.basecluster.IAgentHost;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;

@Slf4j
public class CRDTServiceTestCluster {
    @AllArgsConstructor
    private static class CRDTServiceMeta {
        final AgentHostOptions hostOptions;
        final CRDTServiceOptions serviceOption;
    }

    private final Map<String, CRDTServiceMeta> serviceMetaMap = Maps.newConcurrentMap();
    private final Map<String, IAgentHost> serviceHostMap = Maps.newConcurrentMap();
    private final Map<String, Long> serviceIdMap = Maps.newConcurrentMap();
    private final Map<Long, ICRDTService> serviceMap = Maps.newConcurrentMap();

    public void newService(String serviceId, AgentHostOptions hostOptions, CRDTServiceOptions serviceOptions) {
        serviceMetaMap.computeIfAbsent(serviceId, k -> {
            loadService(serviceId, hostOptions, serviceOptions);
            return new CRDTServiceMeta(hostOptions, serviceOptions);
        });
    }

    public void stopService(String serviceId) {
        checkService(serviceId);
        serviceMap.remove(serviceIdMap.remove(serviceId)).stop();
        serviceHostMap.remove(serviceId).shutdown();
    }

    public ICRDTService getService(String serviceId) {
        checkService(serviceId);
        return serviceMap.get(serviceIdMap.get(serviceId));
    }

    public void join(String joinerId, String joineeId) {
        checkService(joinerId);
        checkService(joineeId);
        serviceHostMap.get(joinerId)
            .join(Sets.newHashSet(
                new InetSocketAddress(
                    serviceMetaMap.get(joineeId).hostOptions.addr(),
                    serviceMetaMap.get(joineeId).hostOptions.port()))
            )
            .join();
    }

    private void checkService(String serviceId) {
        Preconditions.checkArgument(serviceIdMap.containsKey(serviceId));
    }

    private long loadService(String serviceId, AgentHostOptions hostOptions, CRDTServiceOptions serviceOptions) {
        log.info("Load service {}", serviceId);
        IAgentHost host = serviceHostMap.computeIfAbsent(serviceId, id -> {
            IAgentHost newHost = IAgentHost.newInstance(hostOptions);
            newHost.start();
            return newHost;
        });
        ICRDTService service = ICRDTService.newInstance(serviceOptions);
        service.start(host);
        serviceIdMap.put(serviceId, service.id());
        serviceMap.put(service.id(), service);
        return service.id();
    }

    public void shutdown() {
        serviceIdMap.keySet().forEach(this::stopService);
    }
}
