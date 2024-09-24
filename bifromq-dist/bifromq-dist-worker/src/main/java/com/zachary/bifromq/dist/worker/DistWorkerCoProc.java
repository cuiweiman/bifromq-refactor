package com.zachary.bifromq.dist.worker;

import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.LoadHint;
import com.zachary.bifromq.basekv.proto.Range;
import com.zachary.bifromq.basekv.store.api.IKVIterator;
import com.zachary.bifromq.basekv.store.api.IKVRangeCoProc;
import com.zachary.bifromq.basekv.store.api.IKVRangeReader;
import com.zachary.bifromq.basekv.store.api.IKVReader;
import com.zachary.bifromq.basekv.store.api.IKVWriter;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.zachary.bifromq.dist.client.IDistClient;
import com.zachary.bifromq.dist.entity.EntityUtil;
import com.zachary.bifromq.dist.entity.GroupMatching;
import com.zachary.bifromq.dist.entity.Inbox;
import com.zachary.bifromq.dist.entity.Matching;
import com.zachary.bifromq.dist.rpc.proto.AddTopicFilter;
import com.zachary.bifromq.dist.rpc.proto.AddTopicFilterReply;
import com.zachary.bifromq.dist.rpc.proto.BatchDist;
import com.zachary.bifromq.dist.rpc.proto.BatchDistReply;
import com.zachary.bifromq.dist.rpc.proto.ClearSubInfo;
import com.zachary.bifromq.dist.rpc.proto.ClearSubInfoReply;
import com.zachary.bifromq.dist.rpc.proto.CollectMetricsReply;
import com.zachary.bifromq.dist.rpc.proto.DeleteMatchRecord;
import com.zachary.bifromq.dist.rpc.proto.DeleteMatchRecordReply;
import com.zachary.bifromq.dist.rpc.proto.DistPack;
import com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput;
import com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcOutput;
import com.zachary.bifromq.dist.rpc.proto.DistServiceRWCoProcInput;
import com.zachary.bifromq.dist.rpc.proto.DistServiceRWCoProcOutput;
import com.zachary.bifromq.dist.rpc.proto.GCReply;
import com.zachary.bifromq.dist.rpc.proto.GCRequest;
import com.zachary.bifromq.dist.rpc.proto.GroupMatchRecord;
import com.zachary.bifromq.dist.rpc.proto.InboxSubInfo;
import com.zachary.bifromq.dist.rpc.proto.InsertMatchRecord;
import com.zachary.bifromq.dist.rpc.proto.InsertMatchRecordReply;
import com.zachary.bifromq.dist.rpc.proto.JoinMatchGroup;
import com.zachary.bifromq.dist.rpc.proto.JoinMatchGroupReply;
import com.zachary.bifromq.dist.rpc.proto.LeaveMatchGroup;
import com.zachary.bifromq.dist.rpc.proto.LeaveMatchGroupReply;
import com.zachary.bifromq.dist.rpc.proto.MatchRecord;
import com.zachary.bifromq.dist.rpc.proto.RemoveTopicFilter;
import com.zachary.bifromq.dist.rpc.proto.RemoveTopicFilterReply;
import com.zachary.bifromq.dist.rpc.proto.TopicFanout;
import com.zachary.bifromq.dist.rpc.proto.UpdateReply;
import com.zachary.bifromq.dist.rpc.proto.UpdateRequest;
import com.zachary.bifromq.dist.worker.scheduler.DeliveryScheduler;
import com.zachary.bifromq.dist.worker.scheduler.MessagePackWrapper;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import com.zachary.bifromq.plugin.settingprovider.Setting;
import com.zachary.bifromq.plugin.subbroker.ISubBrokerManager;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zachary.bifromq.type.ClientInfo;
import com.zachary.bifromq.type.QoS;
import com.zachary.bifromq.type.TopicMessagePack;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.zachary.bifromq.basekv.utils.KeyRangeUtil.intersect;
import static com.zachary.bifromq.basekv.utils.KeyRangeUtil.isEmptyRange;
import static com.zachary.bifromq.dist.entity.EntityUtil.isSubInfoKey;
import static com.zachary.bifromq.dist.entity.EntityUtil.matchRecordKeyPrefix;
import static com.zachary.bifromq.dist.entity.EntityUtil.parseInbox;
import static com.zachary.bifromq.dist.entity.EntityUtil.parseMatchRecord;
import static com.zachary.bifromq.dist.entity.EntityUtil.parseTenantId;
import static com.zachary.bifromq.dist.entity.EntityUtil.parseTopicFilter;
import static com.zachary.bifromq.dist.entity.EntityUtil.tenantPrefix;
import static com.zachary.bifromq.dist.entity.EntityUtil.tenantUpperBound;
import static com.zachary.bifromq.dist.util.TopicUtil.isWildcardTopicFilter;
import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.DIST_FAN_OUT_PARALLELISM;
import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.DIST_LOAD_TRACKING_SECONDS;
import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.DIST_MAX_RANGE_LOAD;
import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.DIST_SPLIT_KEY_EST_THRESHOLD;
import static java.util.Collections.singletonMap;
import static java.util.concurrent.CompletableFuture.allOf;

@Slf4j
class DistWorkerCoProc implements IKVRangeCoProc {
    private final KVRangeId id;
    private final IDistClient distClient;
    private final ISettingProvider settingProvider;
    private final ISubBrokerManager subBrokerManager;
    private final DeliveryScheduler scheduler;
    private final SubscriptionCache routeCache;
    private final LoadEstimator loadEstimator;
    private final Gauge loadGauge;
    private final FanoutExecutorGroup fanoutExecutorGroup;

    public DistWorkerCoProc(KVRangeId id,
                            Supplier<IKVRangeReader> readClientProvider,
                            IEventCollector eventCollector,
                            ISettingProvider settingProvider,
                            IDistClient distClient,
                            ISubBrokerManager subBrokerManager,
                            DeliveryScheduler scheduler,
                            Executor matchExecutor) {
        this.id = id;
        this.distClient = distClient;
        this.settingProvider = settingProvider;
        this.subBrokerManager = subBrokerManager;
        this.scheduler = scheduler;
        this.loadEstimator = new LoadEstimator(DIST_MAX_RANGE_LOAD.get(), DIST_SPLIT_KEY_EST_THRESHOLD.get(),
            DIST_LOAD_TRACKING_SECONDS.get());
        this.routeCache = new SubscriptionCache(id, readClientProvider, matchExecutor, loadEstimator);
        loadGauge = Gauge.builder("dist.worker.load", () -> loadEstimator.estimate().getLoad())
            .tags("id", KVRangeIdUtil.toString(id))
            .register(Metrics.globalRegistry);
        fanoutExecutorGroup = new FanoutExecutorGroup(this.subBrokerManager, scheduler, eventCollector, distClient,
            DIST_FAN_OUT_PARALLELISM.get());
    }

    @Override
    public LoadHint get() {
        return loadEstimator.estimate();
    }

    @Override
    public CompletableFuture<ByteString> query(ByteString input, IKVReader reader) {
        try {
            CodedInputStream cis = input.newCodedInput();
            cis.enableAliasing(true);
            DistServiceROCoProcInput coProcInput = DistServiceROCoProcInput.parseFrom(cis);
            switch (coProcInput.getInputCase()) {
                case DIST: {
                    return dist(coProcInput.getDist(), reader)
                        .thenApply(v -> DistServiceROCoProcOutput.newBuilder()
                            .setDistReply(v).build().toByteString());
                }
                case GCREQUEST: {
                    return gc(coProcInput.getGcRequest(), reader)
                        .thenApply(v -> DistServiceROCoProcOutput.newBuilder()
                            .setGcReply(v).build().toByteString());
                }
                case COLLECTMETRICSREQUEST: {
                    return collect(coProcInput.getCollectMetricsRequest().getReqId(), reader)
                        .thenApply(v -> DistServiceROCoProcOutput.newBuilder()
                            .setCollectMetricsReply(v).build().toByteString());
                }
                default:
                    log.error("Unknown co proc type {}", coProcInput.getInputCase());
                    CompletableFuture<ByteString> f = new CompletableFuture();
                    f.completeExceptionally(
                        new IllegalStateException("Unknown co proc type " + coProcInput.getInputCase()));
                    return f;
            }
        } catch (Throwable e) {
            log.error("Unable to parse ro co-proc", e);
            CompletableFuture<ByteString> f = new CompletableFuture();
            f.completeExceptionally(new IllegalStateException("Unable to parse ro co-proc", e));
            return f;
        }
    }

    @SneakyThrows
    @Override
    public Supplier<ByteString> mutate(ByteString input, IKVReader reader, IKVWriter writer) {
        CodedInputStream cis = input.newCodedInput();
        cis.enableAliasing(true);
        DistServiceRWCoProcInput coProcInput = DistServiceRWCoProcInput.parseFrom(cis);
        log.trace("Receive rw co-proc request\n{}", coProcInput);
        UpdateRequest request = coProcInput.getUpdateRequest();
        Set<String> touchedTenants = Sets.newHashSet();
        Set<ScopedTopic> touchedTopics = Sets.newHashSet();
        ByteString output = DistServiceRWCoProcOutput.newBuilder()
            .setUpdateReply(batchUpdate(request, reader, writer, touchedTenants, touchedTopics))
            .build().toByteString();
        return () -> {
            touchedTopics.forEach(routeCache::invalidate);
            touchedTenants.forEach(routeCache::touch);
            return output;
        };
    }

    public void close() {
        routeCache.close();
        fanoutExecutorGroup.shutdown();
        Metrics.globalRegistry.remove(loadGauge);
    }

    private UpdateReply batchUpdate(UpdateRequest request, IKVReader reader, IKVWriter writer,
                                    Set<String> touchedTenants, Set<ScopedTopic> touchedTopics) {
        UpdateReply.Builder replyBuilder = UpdateReply.newBuilder().setReqId(request.getReqId());
        if (request.hasAddTopicFilter()) {
            replyBuilder.setAddTopicFilter(
                addTopicFilter(request.getAddTopicFilter(), reader, writer));
        }
        if (request.hasRemoveTopicFilter()) {
            replyBuilder.setRemoveTopicFilter(
                removeTopicFilter(request.getRemoveTopicFilter(), reader, writer));
        }
        if (request.hasInsertMatchRecord()) {
            replyBuilder.setInsertMatchRecord(
                insertMatchRecord(request.getInsertMatchRecord(), reader, writer, touchedTenants, touchedTopics));
        }
        if (request.hasDeleteMatchRecord()) {
            replyBuilder.setDeleteMatchRecord(
                deleteMatchRecord(request.getDeleteMatchRecord(), reader, writer, touchedTenants, touchedTopics));
        }
        if (request.hasJoinMatchGroup()) {
            replyBuilder.setJoinMatchGroup(
                joinMatchGroup(request.getJoinMatchGroup(), reader, writer, touchedTenants, touchedTopics));
        }
        if (request.hasLeaveMatchGroup()) {
            replyBuilder.setLeaveMatchGroup(
                leaveMatchGroup(request.getLeaveMatchGroup(), reader, writer, touchedTenants, touchedTopics));
        }
        if (request.hasClearSubInfo()) {
            replyBuilder.setClearSubInfo(clearSubInfo(request.getClearSubInfo(), reader, writer));
        }
        return replyBuilder.build();
    }

    private AddTopicFilterReply addTopicFilter(AddTopicFilter request, IKVReader reader, IKVWriter writer) {
        AddTopicFilterReply.Builder replyBuilder = AddTopicFilterReply.newBuilder();
        for (String subInfoKeyUtf8 : request.getTopicFilterMap().keySet()) {
            String tenantId = parseTenantId(subInfoKeyUtf8);
            ByteString key = ByteString.copyFromUtf8(subInfoKeyUtf8);
            InboxSubInfo subInfo = reader.get(key).map(b -> {
                try {
                    return InboxSubInfo.parseFrom(b);
                } catch (InvalidProtocolBufferException e) {
                    log.error("Unable to parse SubInfo", e);
                    return InboxSubInfo.getDefaultInstance();
                }
            }).orElse(InboxSubInfo.getDefaultInstance());
            loadEstimator.track(key, LoadUnits.KEY_GET);

            AddTopicFilterReply.Results.Builder subResultBuilder = AddTopicFilterReply.Results.newBuilder();
            InboxSubInfo newTopicFilters = request.getTopicFilterMap().get(subInfoKeyUtf8);
            boolean update = false;
            for (String topicFilter : newTopicFilters.getTopicFiltersMap().keySet()) {
                QoS subQoS = newTopicFilters.getTopicFiltersMap().get(topicFilter);
                if (subInfo.getTopicFiltersMap().get(topicFilter) != subQoS) {
                    if (subInfo.getTopicFiltersCount() <
                        (int) settingProvider.provide(Setting.MaxTopicFiltersPerInbox, tenantId)) {
                        subInfo = subInfo.toBuilder().putTopicFilters(topicFilter, subQoS).build();
                        subResultBuilder.putResults(topicFilter, AddTopicFilterReply.Result.OK);
                        update = true;
                    } else {
                        subResultBuilder.putResults(topicFilter, AddTopicFilterReply.Result.ExceedQuota);
                    }
                } else {
                    subResultBuilder.putResults(topicFilter, AddTopicFilterReply.Result.OK);
                }
            }
            if (update) {
                loadEstimator.track(key, LoadUnits.KEY_PUT);
                writer.put(key, subInfo.toByteString());
            }
            replyBuilder.putResult(subInfoKeyUtf8, subResultBuilder.build());
        }
        return replyBuilder.build();
    }

    private RemoveTopicFilterReply removeTopicFilter(RemoveTopicFilter request, IKVReader reader, IKVWriter writer) {
        RemoveTopicFilterReply.Builder replyBuilder = RemoveTopicFilterReply.newBuilder();
        for (String subInfoKeyUtf8 : request.getTopicFilterMap().keySet()) {
            ByteString key = ByteString.copyFromUtf8(subInfoKeyUtf8);
            boolean ok = false;
            Optional<InboxSubInfo> subInfo = reader.get(key).map(b -> {
                try {
                    return InboxSubInfo.parseFrom(b);
                } catch (InvalidProtocolBufferException e) {
                    log.error("Unable to parse SubInfo", e);
                    return null;
                }
            });
            loadEstimator.track(key, LoadUnits.KEY_GET);

            InboxSubInfo.Builder subInfoBuilder = subInfo.orElse(InboxSubInfo.getDefaultInstance()).toBuilder();
            RemoveTopicFilterReply.Results.Builder resultBuilder = RemoveTopicFilterReply.Results.newBuilder();
            for (String topicFilter : request.getTopicFilterMap().get(subInfoKeyUtf8).getTopicFilterList()) {
                if (subInfoBuilder.getTopicFiltersMap().containsKey(topicFilter)) {
                    subInfoBuilder.removeTopicFilters(topicFilter);
                    ok = true;
                    resultBuilder.putResult(topicFilter, true);
                } else {
                    resultBuilder.putResult(topicFilter, false);
                }
            }

            if (ok) {
                if (subInfoBuilder.getTopicFiltersCount() == 0) {
                    loadEstimator.track(key, LoadUnits.KEY_DEL);
                    writer.delete(key);
                } else {
                    loadEstimator.track(key, LoadUnits.KEY_PUT);
                    writer.put(key, subInfoBuilder.build().toByteString());
                }
            }
            replyBuilder.putResult(subInfoKeyUtf8, resultBuilder.build());
        }
        return replyBuilder.build();
    }

    private InsertMatchRecordReply insertMatchRecord(InsertMatchRecord request, IKVReader reader, IKVWriter writer,
                                                     Set<String> touchedTenants, Set<ScopedTopic> touchedTopics) {
        InsertMatchRecordReply.Builder replyBuilder = InsertMatchRecordReply.newBuilder();
        for (String matchRecordKey : request.getRecordMap().keySet()) {
            ByteString key = ByteString.copyFromUtf8(matchRecordKey);
            QoS subQoS = request.getRecordMap().get(matchRecordKey);
            switch (subQoS) {
                case AT_MOST_ONCE:
                case AT_LEAST_ONCE:
                case EXACTLY_ONCE:
                    if (!reader.exist(key)) {
                        writer.put(key, MatchRecord.newBuilder().setNormal(subQoS).build().toByteString());
                        loadEstimator.track(key, LoadUnits.KEY_PUT);

                        String tenantId = EntityUtil.parseTenantId(matchRecordKey);
                        String topicFilter = parseTopicFilter(matchRecordKey);
                        if (isWildcardTopicFilter(topicFilter)) {
                            touchedTenants.add(EntityUtil.parseTenantId(matchRecordKey));
                        } else {
                            touchedTopics.add(ScopedTopic.builder()
                                .tenantId(tenantId)
                                .topic(topicFilter)
                                .range(reader.range())
                                .build());
                        }
                    }
                    loadEstimator.track(key, LoadUnits.KEY_EXIST);
                    break;
            }
        }
        return replyBuilder.build();
    }

    private JoinMatchGroupReply joinMatchGroup(JoinMatchGroup request, IKVReader reader, IKVWriter writer,
                                               Set<String> touchedTenants, Set<ScopedTopic> touchedTopics) {
        JoinMatchGroupReply.Builder replyBuilder = JoinMatchGroupReply.newBuilder();
        for (String matchRecordKeyUtf8 : request.getRecordMap().keySet()) {
            String tenantId = parseTenantId(matchRecordKeyUtf8);
            ByteString matchRecordKey = ByteString.copyFromUtf8(matchRecordKeyUtf8);
            GroupMatchRecord newMembers = request.getRecordMap().get(matchRecordKeyUtf8);
            GroupMatchRecord.Builder matchGroup = reader.get(matchRecordKey)
                .map(b -> {
                    try {
                        return MatchRecord.parseFrom(b).getGroup();
                    } catch (InvalidProtocolBufferException e) {
                        log.error("Unable to parse GroupMatchRecord", e);
                        return GroupMatchRecord.getDefaultInstance();
                    }
                })
                .orElse(GroupMatchRecord.getDefaultInstance()).toBuilder();
            loadEstimator.track(matchRecordKey, LoadUnits.KEY_GET);

            JoinMatchGroupReply.Results.Builder resultBuilder = JoinMatchGroupReply.Results.newBuilder();
            boolean updated = false;
            for (String qInboxId : newMembers.getEntryMap().keySet()) {
                QoS subQoS = newMembers.getEntryMap().get(qInboxId);
                if (!matchGroup.containsEntry(qInboxId) &&
                    matchGroup.getEntryCount() <
                        (int) settingProvider.provide(Setting.MaxSharedGroupMembers, tenantId)) {
                    matchGroup.putEntry(qInboxId, subQoS);
                    resultBuilder.putResult(qInboxId, JoinMatchGroupReply.Result.OK);
                    updated = true;
                } else {
                    resultBuilder.putResult(qInboxId, JoinMatchGroupReply.Result.ExceedLimit);
                }
            }
            if (updated) {
                writer.put(matchRecordKey, MatchRecord.newBuilder().setGroup(matchGroup).build().toByteString());
                loadEstimator.track(matchRecordKey, LoadUnits.KEY_PUT);
                String topicFilter = parseTopicFilter(matchRecordKeyUtf8);
                if (isWildcardTopicFilter(topicFilter)) {
                    touchedTenants.add(parseTenantId(matchRecordKey));
                } else {
                    touchedTopics.add(ScopedTopic.builder()
                        .tenantId(tenantId)
                        .topic(topicFilter)
                        .range(reader.range())
                        .build());
                }
            }
            replyBuilder.putResult(matchRecordKeyUtf8, resultBuilder.build());
        }
        return replyBuilder.build();
    }

    private DeleteMatchRecordReply deleteMatchRecord(DeleteMatchRecord request, IKVReader reader, IKVWriter writer,
                                                     Set<String> touchedTenants, Set<ScopedTopic> touchedTopics) {
        DeleteMatchRecordReply.Builder replyBuilder = DeleteMatchRecordReply.newBuilder();
        for (String matchRecordKeyUtf8 : request.getMatchRecordKeyList()) {
            ByteString matchRecordKey = ByteString.copyFromUtf8(matchRecordKeyUtf8);
            Optional<ByteString> value = reader.get(matchRecordKey);
            loadEstimator.track(matchRecordKey, LoadUnits.KEY_GET);

            if (value.isPresent()) {
                writer.delete(matchRecordKey);
                loadEstimator.track(matchRecordKey, LoadUnits.KEY_DEL);

                String tenantId = parseTenantId(matchRecordKey);
                String topicFilter = parseTopicFilter(matchRecordKeyUtf8);
                if (isWildcardTopicFilter(topicFilter)) {
                    touchedTenants.add(parseTenantId(matchRecordKey));
                } else {
                    touchedTopics.add(ScopedTopic.builder()
                        .tenantId(tenantId)
                        .topic(topicFilter)
                        .range(reader.range())
                        .build());
                }
                replyBuilder.putExist(matchRecordKeyUtf8, true);
            } else {
                replyBuilder.putExist(matchRecordKeyUtf8, false);
            }
        }
        return replyBuilder.build();
    }

    private LeaveMatchGroupReply leaveMatchGroup(LeaveMatchGroup request, IKVReader reader, IKVWriter writer,
                                                 Set<String> touchedTenants, Set<ScopedTopic> touchedTopics) {
        for (String matchRecordKeyUtf8 : request.getRecordMap().keySet()) {
            ByteString matchRecordKey = ByteString.copyFromUtf8(matchRecordKeyUtf8);
            Optional<ByteString> value = reader.get(matchRecordKey);
            loadEstimator.track(matchRecordKey, LoadUnits.KEY_GET);

            if (value.isPresent()) {
                Matching matching = parseMatchRecord(matchRecordKey, value.get());
                assert matching instanceof GroupMatching;
                GroupMatching groupMatching = (GroupMatching) matching;
                Map<String, QoS> existing = Maps.newHashMap(groupMatching.inboxMap);
                for (String qualifiedInboxId : request.getRecordMap()
                    .get(matchRecordKeyUtf8)
                    .getQInboxIdList()) {
                    existing.remove(qualifiedInboxId);
                }
                if (existing.size() != groupMatching.inboxMap.size()) {
                    if (existing.isEmpty()) {
                        writer.delete(matchRecordKey);
                        loadEstimator.track(matchRecordKey, LoadUnits.KEY_DEL);
                    } else {
                        writer.put(matchRecordKey, MatchRecord.newBuilder()
                            .setGroup(GroupMatchRecord.newBuilder()
                                .putAllEntry(existing)
                                .build()).build()
                            .toByteString());
                        loadEstimator.track(matchRecordKey, LoadUnits.KEY_PUT);
                    }
                    String tenantId = parseTenantId(matchRecordKey);
                    String topicFilter = parseTopicFilter(matchRecordKeyUtf8);
                    if (isWildcardTopicFilter(topicFilter)) {
                        touchedTenants.add(parseTenantId(matchRecordKey));
                    } else {
                        touchedTopics.add(ScopedTopic.builder()
                            .tenantId(tenantId)
                            .topic(topicFilter)
                            .range(reader.range())
                            .build());
                    }
                }
            }
        }
        return LeaveMatchGroupReply.getDefaultInstance();
    }

    private ClearSubInfoReply clearSubInfo(ClearSubInfo request, IKVReader reader, IKVWriter writer) {
        ClearSubInfoReply.Builder replyBuilder = ClearSubInfoReply.newBuilder();
        for (ByteString subInfoKey : request.getSubInfoKeyList()) {
            Optional<InboxSubInfo> subInfo = reader.get(subInfoKey).map(b -> {
                try {
                    return InboxSubInfo.parseFrom(b);
                } catch (InvalidProtocolBufferException e) {
                    return null;
                }
            });
            if (subInfo.isPresent()) {
                writer.delete(subInfoKey);
            }
            replyBuilder.addSubInfo(subInfo.orElse(InboxSubInfo.getDefaultInstance()));
        }
        return replyBuilder.build();
    }

    private CompletableFuture<BatchDistReply> dist(BatchDist request, IKVReader reader) {
        List<DistPack> distPackList = request.getDistPackList();
        if (distPackList.isEmpty()) {
            return CompletableFuture.completedFuture(BatchDistReply.newBuilder()
                .setReqId(request.getReqId())
                .build());
        }
        List<CompletableFuture<Map<String, Map<String, Integer>>>> distFanOutFutures = new ArrayList<>();
        for (DistPack distPack : distPackList) {
            String tenantId = distPack.getTenantId();
            Range range = intersect(Range.newBuilder()
                .setStartKey(matchRecordKeyPrefix(tenantId))
                .setEndKey(tenantUpperBound(tenantId))
                .build(), reader.range());
            if (isEmptyRange(range)) {
                continue;
            }
            for (TopicMessagePack topicMsgPack : distPack.getMsgPackList()) {
                String topic = topicMsgPack.getTopic();
                ScopedTopic scopedTopic = ScopedTopic.builder()
                    .tenantId(tenantId)
                    .topic(topic)
                    .range(reader.range())
                    .build();
                Map<ClientInfo, TopicMessagePack.PublisherPack> senderMsgPackMap = topicMsgPack.getMessageList()
                    .stream().collect(Collectors.toMap(TopicMessagePack.PublisherPack::getPublisher, e -> e));
                distFanOutFutures.add(routeCache.get(scopedTopic, senderMsgPackMap.keySet())
                    .thenApply(routeMap -> {
                        fanoutExecutorGroup.submit(Objects.hash(tenantId, topic, request.getOrderKey()), routeMap,
                            MessagePackWrapper.wrap(topicMsgPack), senderMsgPackMap);
                        return singletonMap(tenantId, singletonMap(topic, routeMap.size()));
                    }));
            }
        }
        return CompletableFuture.allOf(distFanOutFutures.toArray(CompletableFuture[]::new))
            .thenApply(v -> distFanOutFutures.stream().map(CompletableFuture::join).collect(Collectors.toList()))
            .thenApply(v -> {
                // tenantId -> topic -> fanOut
                Map<String, Map<String, Integer>> tenantfanout = new HashMap<>();
                v.forEach(fanoutMap -> fanoutMap.forEach((tenantId, topicFanout) ->
                    tenantfanout.computeIfAbsent(tenantId, k -> new HashMap<>()).putAll(topicFanout)));
                return BatchDistReply.newBuilder()
                    .setReqId(request.getReqId())
                    .putAllResult(Maps.transformValues(tenantfanout,
                        f -> TopicFanout.newBuilder().putAllFanout(f).build()))
                    .build();
            });
    }

    private CompletableFuture<GCReply> gc(GCRequest request, IKVReader reader) {
        List<CompletableFuture<Void>> clearFutures = new ArrayList<>();
        IKVIterator itr = reader.iterator();
        for (itr.seekToFirst(); itr.isValid(); ) {
            String tenantId = parseTenantId(itr.key());
            if (isSubInfoKey(itr.key())) {
                Inbox inbox = parseInbox(itr.key());
                clearFutures.add(subBrokerManager.get(inbox.broker)
                    .hasInbox(request.getReqId(), tenantId, inbox.inboxId, inbox.delivererKey)
                    .thenCompose(exist -> {
                        if (!exist) {
                            return distClient.clear(request.getReqId(),
                                tenantId, inbox.inboxId, inbox.delivererKey, inbox.broker);
                        }
                        return CompletableFuture.completedFuture(null);
                    }));
                itr.next();
            } else {
                itr.seek(tenantUpperBound(tenantId));
            }
        }
        return allOf(clearFutures.toArray(new CompletableFuture[0]))
            .handle((v, e) -> GCReply.newBuilder().setReqId(request.getReqId()).build());
    }

    private CompletableFuture<CollectMetricsReply> collect(long reqId, IKVReader reader) {
        CollectMetricsReply.Builder builder = CollectMetricsReply.newBuilder().setReqId(reqId);
        IKVIterator itr = reader.iterator();
        for (itr.seekToFirst(); itr.isValid(); ) {
            String tenantId = parseTenantId(itr.key());
            builder.putUsedSpaces(tenantId,
                reader.size(intersect(reader.range(), Range.newBuilder()
                    .setStartKey(tenantPrefix(tenantId))
                    .setEndKey(tenantUpperBound(tenantId))
                    .build())));
            itr.seek(tenantUpperBound(tenantId));
        }
        return CompletableFuture.completedFuture(builder.build());
    }
}
