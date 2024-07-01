package com.zachary.bifromq.dist.server;

import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basescheduler.ICallScheduler;
import com.zachary.bifromq.dist.rpc.proto.AddTopicFilterReply;
import com.zachary.bifromq.dist.rpc.proto.ClearReply;
import com.zachary.bifromq.dist.rpc.proto.ClearRequest;
import com.zachary.bifromq.dist.rpc.proto.DistReply;
import com.zachary.bifromq.dist.rpc.proto.DistRequest;
import com.zachary.bifromq.dist.rpc.proto.DistServiceGrpc;
import com.zachary.bifromq.dist.rpc.proto.JoinMatchGroupReply;
import com.zachary.bifromq.dist.rpc.proto.SubReply;
import com.zachary.bifromq.dist.rpc.proto.SubRequest;
import com.zachary.bifromq.dist.rpc.proto.UnsubReply;
import com.zachary.bifromq.dist.rpc.proto.UnsubRequest;
import com.zachary.bifromq.dist.server.scheduler.DistCall;
import com.zachary.bifromq.dist.server.scheduler.DistCallScheduler;
import com.zachary.bifromq.dist.server.scheduler.IGlobalDistCallRateSchedulerFactory;
import com.zachary.bifromq.dist.server.scheduler.SubCall;
import com.zachary.bifromq.dist.server.scheduler.SubCallResult;
import com.zachary.bifromq.dist.server.scheduler.SubCallScheduler;
import com.zachary.bifromq.dist.util.TopicUtil;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.zachary.bifromq.plugin.eventcollector.distservice.SubscribeError;
import com.zachary.bifromq.plugin.eventcollector.distservice.Subscribed;
import com.zachary.bifromq.plugin.eventcollector.distservice.UnsubscribeError;
import com.zachary.bifromq.plugin.eventcollector.distservice.Unsubscribed;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.zachary.bifromq.baserpc.UnaryResponse.response;
import static com.zachary.bifromq.plugin.eventcollector.ThreadLocalEventPool.getLocal;

@Slf4j
public class DistService extends DistServiceGrpc.DistServiceImplBase {
    private final IEventCollector eventCollector;
    private final ICallScheduler<DistCall> distCallRateScheduler;
    private final DistCallScheduler distCallScheduler;
    private final SubCallScheduler subCallScheduler;
    private final LoadingCache<String, RunningAverage> tenantFanouts;

    DistService(IBaseKVStoreClient kvStoreClient,
                ISettingProvider settingProvider,
                IEventCollector eventCollector,
                ICRDTService crdtService,
                IGlobalDistCallRateSchedulerFactory distCallRateScheduler) {
        this.eventCollector = eventCollector;
        this.distCallRateScheduler = distCallRateScheduler.createScheduler(settingProvider, crdtService);
        this.distCallScheduler = new DistCallScheduler(kvStoreClient, this.distCallRateScheduler);
        this.subCallScheduler = new SubCallScheduler(kvStoreClient);
        tenantFanouts = Caffeine.newBuilder()
            .expireAfterAccess(120, TimeUnit.SECONDS)
            .build(k -> new RunningAverage(5));
    }

    @Override
    public void sub(SubRequest request, StreamObserver<SubReply> responseObserver) {
        // each sub request consists of two async concurrent sub-tasks. Ideally the two sub-tasks need to be executed
        // in transaction context for data consistency, but that will be too complex. so here is the
        // trade-off: the two sub-tasks are executed atomically but not in transaction, so the subInfo and
        // matchRecord data may inconsistent due to some transient failure, we need a background job for rectification.
        response(tenantId -> {
            List<CompletableFuture<SubCallResult>> futures = new ArrayList<>();
            futures.add(subCallScheduler.schedule(new SubCall.AddTopicFilter(request)));
            if (TopicUtil.isNormalTopicFilter(request.getTopicFilter())) {
                futures.add(subCallScheduler.schedule(new SubCall.InsertMatchRecord(request)));
            } else {
                futures.add(subCallScheduler.schedule(new SubCall.JoinMatchGroup(request)));
            }
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .handle((v, e) -> {
                    if (e != null) {
                        log.error("Failed to exec SubRequest, tenantId={}, req={}", tenantId, request, e);
                        return SubReply.SubResult.Failure;
                    } else {
                        SubCallResult.AddTopicFilterResult atfr =
                            ((SubCallResult.AddTopicFilterResult) futures.get(0).join());
                        if (atfr.result != AddTopicFilterReply.Result.OK) {
                            return SubReply.SubResult.Failure;
                        } else {
                            if (TopicUtil.isNormalTopicFilter(request.getTopicFilter())) {
                                return SubReply.SubResult.forNumber(request.getSubQoS().getNumber());
                            } else {
                                SubCallResult.JoinMatchGroupResult jmgr =
                                    ((SubCallResult.JoinMatchGroupResult) futures.get(1).join());
                                if (jmgr.result == JoinMatchGroupReply.Result.OK) {
                                    return SubReply.SubResult.forNumber(request.getSubQoS().getNumber());
                                }
                                return SubReply.SubResult.Failure;
                            }
                        }
                    }
                })
                .thenApply(result -> {
                    if (result == SubReply.SubResult.Failure) {
                        eventCollector.report(getLocal(SubscribeError.class)
                            .reqId(request.getReqId())
                            .qos(request.getSubQoS())
                            .topicFilter(request.getTopicFilter())
                            .tenantId(request.getTenantId())
                            .inboxId(request.getInboxId())
                            .subBrokerId(request.getBroker())
                            .delivererKey(request.getDelivererKey()));
                    } else {
                        eventCollector.report(getLocal(Subscribed.class)
                            .reqId(request.getReqId())
                            .qos(request.getSubQoS())
                            .topicFilter(request.getTopicFilter())
                            .tenantId(request.getTenantId())
                            .inboxId(request.getInboxId())
                            .subBrokerId(request.getBroker())
                            .delivererKey(request.getDelivererKey()));
                    }
                    return SubReply.newBuilder().setReqId(request.getReqId()).setResult(result).build();
                });
        }, responseObserver);
    }

    public void unsub(UnsubRequest request, StreamObserver<UnsubReply> responseObserver) {
        response(tenantId -> {
            List<CompletableFuture<SubCallResult>> futures = new ArrayList<>();
            futures.add(subCallScheduler.schedule(new SubCall.RemoveTopicFilter(request)));
            if (TopicUtil.isNormalTopicFilter(request.getTopicFilter())) {
                futures.add(subCallScheduler.schedule(new SubCall.DeleteMatchRecord(request)));
            } else {
                futures.add(subCallScheduler.schedule(new SubCall.LeaveJoinGroup(request)));
            }
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .handle((v, e) -> {
                    if (e != null) {
                        log.error("Failed to exec UnsubRequest, tenantId={}, req={}", tenantId, request, e);
                        eventCollector.report(getLocal(UnsubscribeError.class)
                            .reqId(request.getReqId())
                            .topicFilter(request.getTopicFilter())
                            .tenantId(request.getTenantId())
                            .inboxId(request.getInboxId())
                            .subBrokerId(request.getBroker())
                            .delivererKey(request.getDelivererKey()));
                        return UnsubReply.newBuilder()
                            .setReqId(request.getReqId())
                            .setExist(true)
                            .build();
                    } else {
                        eventCollector.report(getLocal(Unsubscribed.class)
                            .reqId(request.getReqId())
                            .topicFilter(request.getTopicFilter())
                            .tenantId(request.getTenantId())
                            .inboxId(request.getInboxId())
                            .subBrokerId(request.getBroker())
                            .delivererKey(request.getDelivererKey()));
                        boolean removed = ((SubCallResult.RemoveTopicFilterResult) futures.get(0).join()).exist;
                        SubCallResult result = futures.get(1).join();
                        if (result instanceof SubCallResult.DeleteMatchRecordResult) {
                            removed |= ((SubCallResult.DeleteMatchRecordResult) result).exist;
                        }
                        return UnsubReply.newBuilder()
                            .setReqId(request.getReqId())
                            .setExist(removed)
                            .build();
                    }
                });
        }, responseObserver);
    }

    @Override
    public void clear(ClearRequest request, StreamObserver<ClearReply> responseObserver) {
        response(tenantId -> subCallScheduler.schedule(new SubCall.Clear(request))
            .thenApply(v -> ((SubCallResult.ClearResult) v).subInfo)
            .thenCompose(subInfo -> {
                List<CompletableFuture<?>> delFutures = subInfo.getTopicFiltersMap()
                    .keySet()
                    .stream()
                    .map(tf -> {
                        if (TopicUtil.isNormalTopicFilter(tf)) {
                            return subCallScheduler.schedule(
                                new SubCall.RemoveTopicFilter(UnsubRequest.newBuilder()
                                    .setReqId(request.getReqId())
                                    .setTenantId(request.getTenantId())
                                    .setInboxId(request.getInboxId())
                                    .setTopicFilter(tf)
                                    .setBroker(request.getBroker())
                                    .setDelivererKey(request.getDelivererKey())
                                    .build()));
                        } else {
                            return subCallScheduler.schedule(
                                new SubCall.LeaveJoinGroup(UnsubRequest.newBuilder()
                                    .setReqId(request.getReqId())
                                    .setTenantId(request.getTenantId())
                                    .setInboxId(request.getInboxId())
                                    .setTopicFilter(tf)
                                    .setBroker(request.getBroker())
                                    .setDelivererKey(request.getDelivererKey())
                                    .build()));
                        }
                    })
                    .collect(Collectors.toList());
                return CompletableFuture.allOf(delFutures.toArray(new CompletableFuture[0]));
            }).handle((result, e) -> {
                if (e != null) {
                    log.error("Failed to exec ClearRequest, tenantId={}, req={}", tenantId, request, e);
                }
                return ClearReply.newBuilder().setReqId(request.getReqId()).build();
            }), responseObserver);
    }

    @Override
    public StreamObserver<DistRequest> dist(StreamObserver<DistReply> responseObserver) {
        return new DistResponsePipeline(distCallScheduler, responseObserver, eventCollector, tenantFanouts);
    }

    public void stop() {
        distCallScheduler.close();
        distCallRateScheduler.close();
    }
}
