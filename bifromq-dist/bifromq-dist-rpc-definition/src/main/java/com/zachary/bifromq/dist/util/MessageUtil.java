package com.zachary.bifromq.dist.util;

import com.zachary.bifromq.dist.rpc.proto.AddTopicFilter;
import com.zachary.bifromq.dist.rpc.proto.BatchDist;
import com.zachary.bifromq.dist.rpc.proto.ClearSubInfo;
import com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest;
import com.zachary.bifromq.dist.rpc.proto.DeleteMatchRecord;
import com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput;
import com.zachary.bifromq.dist.rpc.proto.DistServiceRWCoProcInput;
import com.zachary.bifromq.dist.rpc.proto.GCRequest;
import com.zachary.bifromq.dist.rpc.proto.GroupMatchRecord;
import com.zachary.bifromq.dist.rpc.proto.InboxSubInfo;
import com.zachary.bifromq.dist.rpc.proto.InsertMatchRecord;
import com.zachary.bifromq.dist.rpc.proto.JoinMatchGroup;
import com.zachary.bifromq.dist.rpc.proto.LeaveMatchGroup;
import com.zachary.bifromq.dist.rpc.proto.QInboxIdList;
import com.zachary.bifromq.dist.rpc.proto.RemoveTopicFilter;
import com.zachary.bifromq.dist.rpc.proto.SubRequest;
import com.zachary.bifromq.dist.rpc.proto.TopicFilterList;
import com.zachary.bifromq.dist.rpc.proto.UnsubRequest;
import com.zachary.bifromq.dist.rpc.proto.UpdateRequest;
import com.google.protobuf.ByteString;

import static com.zachary.bifromq.dist.entity.EntityUtil.matchRecordKey;
import static com.zachary.bifromq.dist.entity.EntityUtil.subInfoKey;
import static com.zachary.bifromq.dist.entity.EntityUtil.toQualifiedInboxId;

public class MessageUtil {
    public static DistServiceRWCoProcInput buildAddTopicFilterRequest(SubRequest request) {
        return DistServiceRWCoProcInput.newBuilder()
            .setUpdateRequest(UpdateRequest.newBuilder()
                .setReqId(request.getReqId())
                .setAddTopicFilter(AddTopicFilter.newBuilder()
                    .putTopicFilter(subInfoKey(request.getTenantId(),
                            toQualifiedInboxId(request.getBroker(), request.getInboxId(),
                                request.getDelivererKey())).toStringUtf8(),
                        InboxSubInfo.newBuilder()
                            .putTopicFilters(request.getTopicFilter(), request.getSubQoS())
                            .build())
                    .build())
                .build())
            .build();
    }

    public static DistServiceRWCoProcInput buildInsertMatchRecordRequest(SubRequest request) {
        assert TopicUtil.isNormalTopicFilter(request.getTopicFilter());
        return DistServiceRWCoProcInput.newBuilder()
            .setUpdateRequest(UpdateRequest.newBuilder()
                .setReqId(request.getReqId())
                .setInsertMatchRecord(InsertMatchRecord.newBuilder().putRecord(
                    matchRecordKey(request.getTenantId(), request.getTopicFilter(),
                        toQualifiedInboxId(request.getBroker(), request.getInboxId(),
                            request.getDelivererKey())).toStringUtf8(),
                    request.getSubQoS()).build())
                .build())
            .build();
    }

    public static DistServiceRWCoProcInput buildJoinMatchGroupRequest(SubRequest request) {
        assert !TopicUtil.isNormalTopicFilter(request.getTopicFilter());
        return DistServiceRWCoProcInput.newBuilder()
            .setUpdateRequest(UpdateRequest.newBuilder()
                .setReqId(request.getReqId())
                .setJoinMatchGroup(JoinMatchGroup.newBuilder()
                    .putRecord(
                        matchRecordKey(request.getTenantId(), request.getTopicFilter(),
                            toQualifiedInboxId(request.getBroker(), request.getInboxId(),
                                request.getDelivererKey())).toStringUtf8(),
                        GroupMatchRecord.newBuilder()
                            .putEntry(toQualifiedInboxId(request.getBroker(),
                                request.getInboxId(),
                                request.getDelivererKey()), request.getSubQoS())
                            .build())
                    .build())
                .build())
            .build();

    }

    public static DistServiceRWCoProcInput buildRemoveTopicFilterRequest(UnsubRequest request) {
        return DistServiceRWCoProcInput.newBuilder()
            .setUpdateRequest(UpdateRequest.newBuilder()
                .setReqId(request.getReqId())
                .setRemoveTopicFilter(RemoveTopicFilter.newBuilder()
                    .putTopicFilter(
                        subInfoKey(request.getTenantId(),
                            toQualifiedInboxId(request.getBroker(), request.getInboxId(),
                                request.getDelivererKey())).toStringUtf8(),
                        TopicFilterList.newBuilder().addTopicFilter(request.getTopicFilter()).build())
                    .build())
                .build())
            .build();
    }

    public static DistServiceRWCoProcInput buildDeleteMatchRecordRequest(long reqId, String tenantId,
                                                                         String scopedInboxId,
                                                                         String topicFilter) {
        return DistServiceRWCoProcInput.newBuilder()
            .setUpdateRequest(UpdateRequest.newBuilder()
                .setReqId(reqId)
                .setDeleteMatchRecord(DeleteMatchRecord.newBuilder()
                    .addMatchRecordKey(matchRecordKey(tenantId, topicFilter, scopedInboxId).toStringUtf8())
                    .build())
                .build())
            .build();
    }

    public static DistServiceRWCoProcInput buildLeaveMatchGroupRequest(long reqId, String tenantId,
                                                                       String scopedInboxId,
                                                                       String topicFilter) {
        return DistServiceRWCoProcInput.newBuilder()
            .setUpdateRequest(UpdateRequest.newBuilder()
                .setReqId(reqId)
                .setLeaveMatchGroup(LeaveMatchGroup.newBuilder()
                    .putRecord(matchRecordKey(tenantId, topicFilter, scopedInboxId).toStringUtf8(),
                        QInboxIdList.newBuilder().addQInboxId(scopedInboxId).build())
                    .build())
                .build())
            .build();
    }

    public static DistServiceRWCoProcInput buildClearSubInfoRequest(long reqId, ByteString scopedInboxId) {
        return DistServiceRWCoProcInput.newBuilder()
            .setUpdateRequest(UpdateRequest.newBuilder()
                .setReqId(reqId)
                .setClearSubInfo(ClearSubInfo.newBuilder().addSubInfoKey(scopedInboxId).build())
                .build())
            .build();
    }

    public static DistServiceROCoProcInput buildBatchDistRequest(BatchDist request) {
        return DistServiceROCoProcInput.newBuilder().setDist(request).build();
    }

    public static DistServiceROCoProcInput buildGCRequest(long reqId) {
        return DistServiceROCoProcInput.newBuilder()
            .setGcRequest(GCRequest.newBuilder().setReqId(reqId).build())
            .build();
    }

    public static DistServiceROCoProcInput buildCollectMetricsRequest(long reqId) {
        return DistServiceROCoProcInput.newBuilder()
            .setCollectMetricsRequest(CollectMetricsRequest.newBuilder().setReqId(reqId).build())
            .build();
    }
}
