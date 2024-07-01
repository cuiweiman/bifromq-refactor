package com.zachary.bifromq.baserpc.loadbalancer;

import com.zachary.bifromq.baserpc.BluePrint;
import com.zachary.bifromq.baserpc.RPCContext;
import com.zachary.bifromq.baserpc.utils.TrieMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.grpc.LoadBalancer;
import io.grpc.MethodDescriptor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static io.grpc.ConnectivityState.READY;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

@Slf4j
class TrafficDirectiveAwarePicker extends LoadBalancer.SubchannelPicker implements IUpdateListener.IServerSelector {
    private static class WeightedServerSelector {
        private final SortedMap<String, Integer> weightedServers;
        private final List<String> serverLists;
        private final ConsistentHashRouter<Map.Entry<String, Integer>> chRouter;
        private final AtomicInteger rrIndex = new AtomicInteger(0);

        WeightedServerSelector(SortedMap<String, Integer> weightedServers) {
            this.weightedServers = weightedServers;
            List<LBUtils.Tuple<String>> tuples = Lists.newArrayList();
            weightedServers.forEach((server, weight) -> tuples.add(LBUtils.Tuple.of(weight, server)));
            serverLists = LBUtils.toWeightedRRSequence(tuples);
            chRouter = new ConsistentHashRouter<>(weightedServers.entrySet(), e -> e.getKey() + e.getValue(), 60);
        }

        boolean contains(String serverId) {
            return weightedServers.containsKey(serverId);
        }

        Optional<Map.Entry<String, Integer>> random() {
            if (serverLists.isEmpty()) {
                return Optional.empty();
            }
            String selected = serverLists.get(ThreadLocalRandom.current().nextInt(0, serverLists.size()));
            return Optional.of(new Map.Entry<>() {
                @Override
                public String getKey() {
                    return selected;
                }

                @Override
                public Integer getValue() {
                    return weightedServers.get(selected);
                }

                @Override
                public Integer setValue(Integer value) {
                    return null;
                }
            });

        }

        Optional<Map.Entry<String, Integer>> roundRobin() {
            int size = serverLists.size();
            if (size == 0) {
                return Optional.empty();
            }
            int i = rrIndex.incrementAndGet();
            if (i >= size) {
                int oldi = i;
                i %= size;
                rrIndex.compareAndSet(oldi, i);
            }
            String selected = serverLists.get(i);
            return Optional.of(new Map.Entry<>() {
                @Override
                public String getKey() {
                    return selected;
                }

                @Override
                public Integer getValue() {
                    return weightedServers.get(selected);
                }

                @Override
                public Integer setValue(Integer value) {
                    return null;
                }
            });
        }

        Optional<Map.Entry<String, Integer>> hashing(String key) {
            return Optional.ofNullable(chRouter.routeNode(key));
        }
    }

    private interface ITrafficMatcher {
        WeightedServerSelector match(String tenantId);

        Optional<LoadBalancer.Subchannel> getSubchannel(String serverId);
    }

    private static class TrafficMatcher implements ITrafficMatcher {
        private final TrieMap<WeightedServerSelector> matcher = new TrieMap<>();

        private final Map<String, List<LoadBalancer.Subchannel>> subchannelMap;

        TrafficMatcher(Map<String, Map<String, Integer>> directive,
                       Map<String, List<LoadBalancer.Subchannel>> subchannelMap,
                       Map<String, Set<String>> lbGroupAssignment) {
            Set<String> defaultLBGroup = Sets.newHashSet();
            Map<String, Set<String>> lbGroups = Maps.newHashMap();
            for (String serverId : lbGroupAssignment.keySet()) {
                Set<String> lbGroupTags = lbGroupAssignment.get(serverId);
                if (lbGroupTags.isEmpty()) {
                    // no group tag assigned, add it to default group
                    defaultLBGroup.add(serverId);
                } else {
                    lbGroupTags.forEach(lbGroupTag ->
                        lbGroups.computeIfAbsent(lbGroupTag, l -> Sets.newHashSet()).add(serverId));
                }
            }
            // default group is used as fallback assignment
            // make sure there is always a matcher for any tenantId
            prepareMatcher("", singletonMap("", 1), singletonMap("", defaultLBGroup));

            for (String tenantIdPrefix : directive.keySet()) {
                prepareMatcher(tenantIdPrefix, directive.get(tenantIdPrefix), lbGroups);
            }
            this.subchannelMap = Maps.newHashMap(subchannelMap);
        }

        private void prepareMatcher(String tenantIdPrefix,
                                    Map<String, Integer> trafficAssignment,
                                    Map<String, Set<String>> groupAssignment) {
            SortedMap<String, Integer> weightedServers = Maps.newTreeMap();
            for (String group : trafficAssignment.keySet()) {
                int weight = Math.abs(trafficAssignment.get(group)) % 11; // weight range: 0-11
                groupAssignment.get(group).forEach(serverId ->
                    weightedServers.compute(serverId, (k, w) -> {
                        if (w == null) {
                            w = 0;
                        }
                        w += weight;
                        return w;
                    }));
            }
            matcher.put(tenantIdPrefix, new WeightedServerSelector(weightedServers));

        }

        public WeightedServerSelector match(String tenantId) {
            return matcher.bestMatch(tenantId);
        }

        public Optional<LoadBalancer.Subchannel> getSubchannel(String serverId) {
            List<LoadBalancer.Subchannel> subchannels = subchannelMap.get(serverId).stream()
                .filter(sc -> sc.getAttributes().get(Constants.STATE_INFO).get().getState() == READY)
                .toList();
            if (subchannels.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(subchannels.get(ThreadLocalRandom.current().nextInt(subchannels.size())));
        }
    }

    private final BluePrint bluePrint;

    private final AtomicReference<ITrafficMatcher> currentMatcher =
        new AtomicReference<>(new TrafficMatcher(emptyMap(), emptyMap(), emptyMap()));

    @Builder
    public TrafficDirectiveAwarePicker(BluePrint bluePrint) {
        this.bluePrint = bluePrint;
    }

    public void refresh(Map<String, Map<String, Integer>> trafficDirective,
                        Map<String, List<LoadBalancer.Subchannel>> subchannelMap,
                        Map<String, Set<String>> lbGroupAssighment) {
        currentMatcher.set(new TrafficMatcher(trafficDirective, subchannelMap, lbGroupAssighment));
    }

    @Override
    public LoadBalancer.PickResult pickSubchannel(LoadBalancer.PickSubchannelArgs pickSubchannelArgs) {
        boolean collectSelection = Boolean.parseBoolean(pickSubchannelArgs.getHeaders()
            .get(Constants.COLLECT_SELECTION_METADATA_META_KEY));
        if (collectSelection && RPCContext.SELECTED_SERVER_ID_CTX_KEY.get() == null) {
            return LoadBalancer.PickResult.withDrop(Constants.TRANSIENT_FAILURE);
        }
        pickSubchannelArgs.getHeaders().remove(Constants.COLLECT_SELECTION_METADATA_META_KEY,
            Boolean.toString(collectSelection));
        String tenantId = pickSubchannelArgs.getHeaders().get(Constants.TENANT_ID_META_KEY);
        MethodDescriptor<?, ?> methodDescriptor = pickSubchannelArgs.getMethodDescriptor();
        ITrafficMatcher matcher = currentMatcher.get();
        // if this is a direct lb request
        if (pickSubchannelArgs.getHeaders().containsKey(Constants.DESIRED_SERVER_META_KEY)) {
            // TODO add back in the future
//            if (!(bluePrint.getMethodSemantics().get(methodDescriptor) instanceof BluePrint.DDBalanced)) {
//                log.warn("Method is not marked DDBalanced semantic");
//            }
            WeightedServerSelector selector = matcher.match(tenantId);
            String designatedServerId = pickSubchannelArgs.getHeaders().get(Constants.DESIRED_SERVER_META_KEY);
            if (selector.contains(designatedServerId)) {
                trace("Direct pick sub-channel by serverId:{}", designatedServerId);
                if (collectSelection) {
                    RPCContext.SELECTED_SERVER_ID_CTX_KEY.get().setServerId(designatedServerId);
                }
                // remove DESIRED_SERVER_META_KEY from header
                pickSubchannelArgs.getHeaders().remove(Constants.DESIRED_SERVER_META_KEY, designatedServerId);
                Optional<LoadBalancer.Subchannel> selectedSubchannel = matcher.getSubchannel(designatedServerId);
                return selectedSubchannel.map(LoadBalancer.PickResult::withSubchannel)
                    .orElseGet(() -> LoadBalancer.PickResult.withDrop(Constants.SERVER_UNREACHABLE));
            }
            return LoadBalancer.PickResult.withDrop(Constants.SERVER_NOT_FOUND);
        } else {
            WeightedServerSelector selector = matcher.match(tenantId);
            Optional<Map.Entry<String, Integer>> selection;
            if (bluePrint.semantic(methodDescriptor.getFullMethodName()) instanceof BluePrint.WCHBalanced) {
                // weighted-consistent-hashing mode
                String hashKey = pickSubchannelArgs.getHeaders().get(Constants.WCH_KEY_META_KEY);
                selection = selector.hashing(hashKey);
            } else if (bluePrint.semantic(methodDescriptor.getFullMethodName()) instanceof BluePrint.WRBalanced) {
                selection = selector.random();
            } else {
                // weighted-round-robin mode
                selection = selector.roundRobin();
            }
            if (selection.isEmpty()) {
                return LoadBalancer.PickResult.withDrop(Constants.SERVICE_UNAVAILABLE);
            }
            trace("Picked subchannel:{} for tenant:{}", selection.get(), tenantId);
            if (collectSelection) {
                RPCContext.SELECTED_SERVER_ID_CTX_KEY.get().setServerId(selection.get().getKey());
            }
            Optional<LoadBalancer.Subchannel> selectedSubchannel = matcher.getSubchannel(selection.get().getKey());
            return selectedSubchannel.map(LoadBalancer.PickResult::withSubchannel)
                .orElseGet(() -> LoadBalancer.PickResult.withDrop(Constants.SERVER_UNREACHABLE));
        }
    }

    @Override
    public boolean direct(String tenantId, String serverId, MethodDescriptor methodDescriptor) {
        assert bluePrint.semantic(methodDescriptor.getFullMethodName()) instanceof BluePrint.DDBalanced;
        WeightedServerSelector selector = currentMatcher.get().match(tenantId);
        return selector.contains(serverId);
    }

    @Override
    public Optional<String> hashing(String tenantId, String key, MethodDescriptor<?, ?> methodDescriptor) {
        assert bluePrint.semantic(methodDescriptor.getFullMethodName()) instanceof BluePrint.WCHBalanced;
        // weighted-consistent-hashing mode
        WeightedServerSelector selector = currentMatcher.get().match(tenantId);
        Optional<Map.Entry<String, Integer>> selection = selector.hashing(key);
        return selection.map(Map.Entry::getKey);
    }

    @Override
    public Optional<String> roundRobin(String tenantId, MethodDescriptor<?, ?> methodDescriptor) {
        assert bluePrint.semantic(methodDescriptor.getFullMethodName()) instanceof BluePrint.WRRBalanced;
        // weighted-round-robin mode
        WeightedServerSelector selector = currentMatcher.get().match(tenantId);
        Optional<Map.Entry<String, Integer>> selection = selector.roundRobin();
        return selection.map(Map.Entry::getKey);
    }

    @Override
    public Optional<String> random(String tenantId, MethodDescriptor<?, ?> methodDescriptor) {
        assert bluePrint.semantic(methodDescriptor.getFullMethodName()) instanceof BluePrint.WRBalanced;
        // weighted-random mode
        WeightedServerSelector selector = currentMatcher.get().match(tenantId);
        Optional<Map.Entry<String, Integer>> selection = selector.random();
        return selection.map(Map.Entry::getKey);
    }

    private void trace(String msg, Object... args) {
        if (log.isTraceEnabled()) {
            log.trace(msg, args);
        }
    }
}
