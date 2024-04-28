package com.zachary.bifromq.basecluster;

import com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr;
import com.zachary.bifromq.basecluster.agent.proto.AgentMemberMetadata;
import com.zachary.bifromq.basecluster.memberlist.agent.IAgent;
import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class AgentTestCluster {
    @AllArgsConstructor
    private static class AgentHostMeta {
        final AgentHostOptions options;
    }

    private final Map<String, AgentHostMeta> hostMetaMap = Maps.newConcurrentMap();
    private final Map<String, HostEndpoint> hostEndpointMap = Maps.newConcurrentMap();
    private final Map<HostEndpoint, IAgentHost> hostMap = Maps.newConcurrentMap();
    private final Map<String, List<ByteString>> inflationLogs = Maps.newConcurrentMap();
    private final CompositeDisposable disposables = new CompositeDisposable();

    public AgentTestCluster() {
    }

    public String newHost(String hostId, AgentHostOptions options) {
        hostMetaMap.computeIfAbsent(hostId, k -> {
            loadStore(hostId, options);
            return new AgentHostMeta(options);
        });
        return hostId;
    }

    public void startHost(String hostId) {
        Preconditions.checkArgument(hostMetaMap.containsKey(hostId), "Unknown store %s", hostId);
        if (hostMetaMap.containsKey(hostId)) {
            AgentHostMeta meta = hostMetaMap.get(hostId);
            loadStore(hostId, meta.options);
        }
    }

    public void join(String joinerId, String joineeId) {
        checkHost(joinerId);
        checkHost(joineeId);
        hostMap.get(hostEndpointMap.get(joinerId))
            .join(Sets.newHashSet(new InetSocketAddress(hostMetaMap.get(joineeId).options.addr(),
                hostMetaMap.get(joineeId).options.port())));
    }

    public void stopHost(String hostId) {
        checkHost(hostId);
        inflationLogs.remove(hostId);
        hostMap.remove(hostEndpointMap.get(hostId)).shutdown();
    }

    public HostEndpoint endpoint(String hostId) {
        checkHost(hostId);
        return getHost(hostId).local();
    }

    public IAgent host(String hostId, String agentId) {
        checkHost(hostId);
        return getHost(hostId).host(agentId);
    }

    public Observable<Map<AgentMemberAddr, AgentMemberMetadata>> agent(String hostId, String agentId) {
        checkHost(hostId);
        return getHost(hostId).host(agentId).membership();
    }

    public Set<HostEndpoint> membership(String hostId) {
        checkHost(hostId);
        return getHost(hostId).membership().blockingFirst();
    }

    public List<ByteString> inflationLog(String storeId) {
        checkHost(storeId);
        return Collections.unmodifiableList(inflationLogs.get(storeId));
    }

    private HostEndpoint loadStore(String storeId, AgentHostOptions options) {
        inflationLogs.putIfAbsent(storeId, new LinkedList<>());
        IAgentHost host = IAgentHost.newInstance(options);
        host.start();
        hostEndpointMap.put(storeId, host.local());
        hostMap.put(host.local(), host);
        return host.local();
    }

    public void shutdown() {
        disposables.dispose();
        hostEndpointMap.keySet().forEach(this::stopHost);
    }

    public IAgentHost getHost(String hostId) {
        checkHost(hostId);
        return hostMap.get(hostEndpointMap.get(hostId));
    }

    private void checkHost(String hostId) {
        Preconditions.checkArgument(hostEndpointMap.containsKey(hostId));
    }

    private static <T> T getField(Object targetObject, String fieldName) {
        try {
            Field field = targetObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(targetObject);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.warn("get field {} from {} failed: {}", targetObject, fieldName, e.getMessage());
        }
        return null;
    }
}
