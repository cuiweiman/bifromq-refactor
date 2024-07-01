package com.zachary.bifromq.basekv.server;

import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.store.IKVRangeStore;
import com.zachary.bifromq.basekv.store.exception.KVRangeException;
import com.zachary.bifromq.basekv.store.proto.KVRangeROReply;
import com.zachary.bifromq.basekv.store.proto.KVRangeRORequest;
import com.zachary.bifromq.basekv.store.proto.ReplyCode;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.google.protobuf.ByteString;
import io.grpc.stub.ServerCallStreamObserver;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class QueryPipelineTest {
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
    public void get() {
        get(false);
        get(true);
    }

    private void get(boolean linearized) {
        QueryPipeline pipeline = new QueryPipeline(rangeStore, linearized, streamObserver);
        KVRangeId rangeId = KVRangeIdUtil.generate();
        ByteString getKey = ByteString.copyFromUtf8("get");
        KVRangeRORequest getRequest = KVRangeRORequest.newBuilder()
            .setReqId(1)
            .setVer(1)
            .setKvRangeId(rangeId)
            .setGetKey(getKey)
            .build();

        when(rangeStore.get(1, rangeId, getKey, linearized))
            .thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        KVRangeROReply getReply = pipeline.handleRequest("_", getRequest).join();

        assertEquals(getReply.getReqId(), 1);
        assertEquals(getReply.getCode(), ReplyCode.Ok);
        assertFalse(getReply.getGetResult().hasValue());
    }

    @Test
    public void exist() {
        exist(false);
        exist(true);
    }

    private void exist(boolean linearized) {
        QueryPipeline pipeline = new QueryPipeline(rangeStore, linearized, streamObserver);
        KVRangeId rangeId = KVRangeIdUtil.generate();
        ByteString existKey = ByteString.copyFromUtf8("exist");
        KVRangeRORequest existRequest = KVRangeRORequest.newBuilder()
            .setReqId(1)
            .setVer(1)
            .setKvRangeId(rangeId)
            .setExistKey(existKey)
            .build();

        when(rangeStore.exist(1, rangeId, existKey, linearized))
            .thenReturn(CompletableFuture.completedFuture(true));

        KVRangeROReply existReply = pipeline.handleRequest("_", existRequest).join();

        assertEquals(existReply.getReqId(), 1);
        assertEquals(existReply.getCode(), ReplyCode.Ok);
        assertTrue(existReply.getExistResult());
    }

    @Test
    public void queryCoProc() {
        queryCoProc(false);
        queryCoProc(true);
    }

    private void queryCoProc(boolean linearized) {
        QueryPipeline pipeline = new QueryPipeline(rangeStore, linearized, streamObserver);
        KVRangeId rangeId = KVRangeIdUtil.generate();
        ByteString coProcInput = ByteString.copyFromUtf8("coProc");
        KVRangeRORequest coProcRequest = KVRangeRORequest.newBuilder()
            .setReqId(1)
            .setVer(1)
            .setKvRangeId(rangeId)
            .setRoCoProcInput(coProcInput)
            .build();

        when(rangeStore.queryCoProc(1, rangeId, coProcInput, linearized))
            .thenReturn(CompletableFuture.completedFuture(ByteString.empty()));

        KVRangeROReply coProcReply = pipeline.handleRequest("_", coProcRequest).join();

        assertEquals(coProcReply.getReqId(), 1);
        assertEquals(coProcReply.getCode(), ReplyCode.Ok);
        assertEquals(coProcReply.getRoCoProcResult(), ByteString.empty());
    }


    @Test
    public void multiQueries() {
        multiQueries(false);
        multiQueries(true);
    }

    private void multiQueries(boolean linearized) {
        QueryPipeline pipeline = new QueryPipeline(rangeStore, linearized, streamObserver);
        KVRangeId rangeId = KVRangeIdUtil.generate();
        int reqCount = 10;
        List<KVRangeRORequest> requests = new ArrayList<>();
        List<KVRangeROReply> replies = new ArrayList<>();
        List<CompletableFuture<KVRangeROReply>> replyFutures = new ArrayList<>();
        for (int i = 0; i < reqCount; i++) {
            ByteString getKey = ByteString.copyFromUtf8("get-" + i);
            KVRangeRORequest getRequest = KVRangeRORequest.newBuilder()
                .setReqId(i)
                .setVer(1)
                .setKvRangeId(rangeId)
                .setGetKey(getKey)
                .build();

            when(rangeStore.get(1, rangeId, getKey, linearized))
                .thenReturn(new CompletableFuture<Optional<ByteString>>()
                    .completeOnTimeout(Optional.empty(), ThreadLocalRandom.current().nextInt(0, 100),
                        TimeUnit.MILLISECONDS));
            requests.add(getRequest);
            replyFutures.add(pipeline.handleRequest("_", getRequest)
                .whenComplete((v, e) -> replies.add(v)));
        }
        CompletableFuture.allOf(replyFutures.toArray(new CompletableFuture[] {})).join();
        assertEquals(replies.size(), requests.size());
        for (int i = 0; i < reqCount; i++) {
            assertEquals(replies.get(i).getReqId(), requests.get(i).getReqId());
        }
    }

    @Test
    public void errorCodeConversion() {
        QueryPipeline pipeline = new QueryPipeline(rangeStore, false, streamObserver);
        KVRangeId rangeId = KVRangeIdUtil.generate();
        ByteString getKey = ByteString.copyFromUtf8("get");

        // bad version
        KVRangeRORequest getRequest = KVRangeRORequest.newBuilder()
            .setReqId(1)
            .setVer(1)
            .setKvRangeId(rangeId)
            .setGetKey(getKey)
            .build();
        when(rangeStore.get(1, rangeId, getKey, false))
            .thenReturn(CompletableFuture.failedFuture(new KVRangeException.BadVersion("bad version")));
        KVRangeROReply getReply = pipeline.handleRequest("_", getRequest).join();
        assertEquals(getReply.getCode(), ReplyCode.BadVersion);

        // bad request
        getRequest = KVRangeRORequest.newBuilder()
            .setReqId(1)
            .setVer(2)
            .setKvRangeId(rangeId)
            .setGetKey(getKey)
            .build();
        when(rangeStore.get(2, rangeId, getKey, false))
            .thenReturn(CompletableFuture.failedFuture(new KVRangeException.BadRequest("bad request")));
        getReply = pipeline.handleRequest("_", getRequest).join();
        assertEquals(getReply.getCode(), ReplyCode.BadRequest);

        // try later
        getRequest = KVRangeRORequest.newBuilder()
            .setReqId(1)
            .setVer(3)
            .setKvRangeId(rangeId)
            .setGetKey(getKey)
            .build();
        when(rangeStore.get(3, rangeId, getKey, false))
            .thenReturn(CompletableFuture.failedFuture(new KVRangeException.TryLater("try later")));
        getReply = pipeline.handleRequest("_", getRequest).join();
        assertEquals(getReply.getCode(), ReplyCode.TryLater);

        // internal error
        getRequest = KVRangeRORequest.newBuilder()
            .setReqId(1)
            .setVer(4)
            .setKvRangeId(rangeId)
            .setGetKey(getKey)
            .build();
        when(rangeStore.get(4, rangeId, getKey, false))
            .thenReturn(CompletableFuture.failedFuture(new KVRangeException.InternalException("internal error")));
        getReply = pipeline.handleRequest("_", getRequest).join();
        assertEquals(getReply.getCode(), ReplyCode.InternalError);
    }
}
