package com.zachary.bifromq.inbox.util;

import com.zachary.bifromq.inbox.storage.proto.CreateRequest;
import com.zachary.bifromq.inbox.storage.proto.GCRequest;
import com.zachary.bifromq.inbox.storage.proto.HasRequest;
import com.zachary.bifromq.inbox.storage.proto.InboxCommitRequest;
import com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest;
import com.zachary.bifromq.inbox.storage.proto.InboxInsertRequest;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceROCoProcInput;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceRWCoProcInput;
import com.zachary.bifromq.inbox.storage.proto.QueryRequest;
import com.zachary.bifromq.inbox.storage.proto.TouchRequest;
import com.zachary.bifromq.inbox.storage.proto.UpdateRequest;

public class MessageUtil {
    public static InboxServiceRWCoProcInput buildGCRequest(long reqId) {
        return InboxServiceRWCoProcInput.newBuilder()
            .setRequest(UpdateRequest.newBuilder()
                .setReqId(reqId)
                .setGc(GCRequest.newBuilder().build())
                .build())
            .build();
    }

    public static InboxServiceRWCoProcInput buildCreateRequest(long reqId, CreateRequest request) {
        return InboxServiceRWCoProcInput.newBuilder()
            .setRequest(UpdateRequest.newBuilder()
                .setReqId(reqId)
                .setCreateInbox(request)
                .build())
            .build();
    }

    public static InboxServiceROCoProcInput buildHasRequest(long reqId, HasRequest request) {
        return InboxServiceROCoProcInput.newBuilder()
            .setRequest(QueryRequest.newBuilder()
                .setReqId(reqId)
                .setHas(request)
                .build())
            .build();
    }

    public static InboxServiceRWCoProcInput buildBatchInboxInsertRequest(long reqId, InboxInsertRequest request) {
        return InboxServiceRWCoProcInput.newBuilder()
            .setRequest(UpdateRequest.newBuilder()
                .setReqId(reqId)
                .setInsert(request)
                .build())
            .build();
    }

    public static InboxServiceROCoProcInput buildInboxFetchRequest(long reqId, InboxFetchRequest request) {
        return InboxServiceROCoProcInput.newBuilder()
            .setRequest(QueryRequest.newBuilder()
                .setReqId(reqId)
                .setFetch(request)
                .build())
            .build();
    }

    public static InboxServiceRWCoProcInput buildTouchRequest(long reqId, TouchRequest request) {
        return InboxServiceRWCoProcInput.newBuilder()
            .setRequest(UpdateRequest.newBuilder()
                .setReqId(reqId)
                .setTouch(request)
                .build())
            .build();
    }

    public static InboxServiceRWCoProcInput buildBatchCommitRequest(long reqId, InboxCommitRequest request) {
        return InboxServiceRWCoProcInput.newBuilder()
            .setRequest(UpdateRequest.newBuilder()
                .setReqId(reqId)
                .setCommit(request)
                .build())
            .build();
    }
}
