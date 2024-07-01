package com.zachary.bifromq.basekv.server;

import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.Put;
import com.zachary.bifromq.basekv.store.IKVRangeStore;
import com.zachary.bifromq.basekv.store.exception.KVRangeException;
import com.zachary.bifromq.basekv.store.proto.KVRangeRWReply;
import com.zachary.bifromq.basekv.store.proto.KVRangeRWRequest;
import com.zachary.bifromq.basekv.store.proto.ReplyCode;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.google.protobuf.ByteString;
import io.grpc.stub.ServerCallStreamObserver;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class MutatePipelineTest {
    @Mock
    private IKVRangeStore rangeStore;

    @Mock
    private ServerCallStreamObserver streamObserver;
    private AutoCloseable closeable;
    @BeforeMethod
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterMethod
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void put() {
        MutatePipeline pipeline = new MutatePipeline(rangeStore, streamObserver);
        KVRangeId rangeId = KVRangeIdUtil.generate();
        ByteString putKey = ByteString.copyFromUtf8("put");
        ByteString putVal = ByteString.copyFromUtf8("val");
        Put put = Put.newBuilder()
            .setKey(putKey)
            .setValue(putVal)
            .build();
        KVRangeRWRequest putRequest = KVRangeRWRequest.newBuilder()
            .setReqId(1)
            .setVer(1)
            .setKvRangeId(rangeId)
            .setPut(put)
            .build();

        when(rangeStore.put(1, rangeId, putKey, putVal)).thenReturn(
            CompletableFuture.completedFuture(ByteString.empty()));

        KVRangeRWReply putReply = pipeline.handleRequest("_", putRequest).join();

        assertEquals(putReply.getReqId(), 1);
        assertEquals(putReply.getCode(), ReplyCode.Ok);
        assertTrue(putReply.getPutResult().isEmpty());
    }

    @Test
    public void delete() {
        MutatePipeline pipeline = new MutatePipeline(rangeStore, streamObserver);
        KVRangeId rangeId = KVRangeIdUtil.generate();
        ByteString delKey = ByteString.copyFromUtf8("del");
        KVRangeRWRequest delRequest = KVRangeRWRequest.newBuilder()
            .setReqId(1)
            .setVer(1)
            .setKvRangeId(rangeId)
            .setDelete(delKey)
            .build();

        when(rangeStore.delete(1, rangeId, delKey)).thenReturn(CompletableFuture.completedFuture(ByteString.empty()));

        KVRangeRWReply delReply = pipeline.handleRequest("_", delRequest).join();

        assertEquals(delReply.getReqId(), 1);
        assertEquals(delReply.getCode(), ReplyCode.Ok);
        assertTrue(delReply.getDeleteResult().isEmpty());
    }

    @Test
    public void mutateCoProc() {
        MutatePipeline pipeline = new MutatePipeline(rangeStore, streamObserver);
        KVRangeId rangeId = KVRangeIdUtil.generate();
        ByteString mutateCoProcInput = ByteString.copyFromUtf8("mutate");
        KVRangeRWRequest mutateRequest = KVRangeRWRequest.newBuilder()
            .setReqId(1)
            .setVer(1)
            .setKvRangeId(rangeId)
            .setRwCoProc(mutateCoProcInput)
            .build();

        when(rangeStore.mutateCoProc(1, rangeId, mutateCoProcInput)).thenReturn(
            CompletableFuture.completedFuture(ByteString.empty()));

        KVRangeRWReply mutateReply = pipeline.handleRequest("_", mutateRequest).join();

        assertEquals(mutateReply.getReqId(), 1);
        assertEquals(mutateReply.getCode(), ReplyCode.Ok);
        assertTrue(mutateReply.getRwCoProcResult().isEmpty());
    }

    @Test
    public void errorCodeConversion() {
        MutatePipeline pipeline = new MutatePipeline(rangeStore, streamObserver);
        KVRangeId rangeId = KVRangeIdUtil.generate();
        ByteString putKey = ByteString.copyFromUtf8("put");
        ByteString putVal = ByteString.copyFromUtf8("val");
        Put put = Put.newBuilder()
            .setKey(putKey)
            .setValue(putVal)
            .build();
        // bad version
        KVRangeRWRequest putRequest = KVRangeRWRequest.newBuilder()
            .setReqId(1)
            .setVer(1)
            .setKvRangeId(rangeId)
            .setPut(put)
            .build();
        when(rangeStore.put(1, rangeId, putKey, putVal)).thenReturn(
            CompletableFuture.failedFuture(new KVRangeException.BadVersion("bad version")));
        KVRangeRWReply putReply = pipeline.handleRequest("_", putRequest).join();
        assertEquals(putReply.getCode(), ReplyCode.BadVersion);

        // bad request
        putRequest = KVRangeRWRequest.newBuilder()
            .setReqId(1)
            .setVer(2)
            .setKvRangeId(rangeId)
            .setPut(put)
            .build();
        when(rangeStore.put(2, rangeId, putKey, putVal)).thenReturn(
            CompletableFuture.failedFuture(new KVRangeException.BadRequest("bad request")));
        putReply = pipeline.handleRequest("_", putRequest).join();
        assertEquals(putReply.getCode(), ReplyCode.BadRequest);

        // try later
        putRequest = KVRangeRWRequest.newBuilder()
            .setReqId(1)
            .setVer(3)
            .setKvRangeId(rangeId)
            .setPut(put)
            .build();
        when(rangeStore.put(3, rangeId, putKey, putVal)).thenReturn(
            CompletableFuture.failedFuture(new KVRangeException.TryLater("try later")));
        putReply = pipeline.handleRequest("_", putRequest).join();
        assertEquals(putReply.getCode(), ReplyCode.TryLater);

        putRequest = KVRangeRWRequest.newBuilder()
            .setReqId(1)
            .setVer(4)
            .setKvRangeId(rangeId)
            .setPut(put)
            .build();
        when(rangeStore.put(4, rangeId, putKey, putVal)).thenReturn(
            CompletableFuture.failedFuture(new KVRangeException.InternalException("internal error")));
        putReply = pipeline.handleRequest("_", putRequest).join();
        assertEquals(putReply.getCode(), ReplyCode.InternalError);
    }
}
