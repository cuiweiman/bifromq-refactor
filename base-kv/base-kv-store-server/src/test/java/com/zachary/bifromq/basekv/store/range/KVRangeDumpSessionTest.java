package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.KVRangeMessage;
import com.zachary.bifromq.basekv.proto.KVRangeSnapshot;
import com.zachary.bifromq.basekv.proto.SaveSnapshotDataReply;
import com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest;
import com.zachary.bifromq.basekv.proto.SnapshotSyncRequest;
import com.zachary.bifromq.basekv.store.api.IKVIterator;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.SneakyThrows;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class KVRangeDumpSessionTest {
    @Mock
    private IKVRangeState rangeAccessor;
    @Mock
    private IKVRangeMessenger messenger;
    @Mock
    private IKVIterator snapItr;

    @Mock
    private KVRangeDumpSession.DumpBytesRecorder dumpBytesRecorder;
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
    public void dumpEmptySnapshot() {
        String peerStoreId = "follower";
        String sessionId = "session";
        KVRangeId rangeId = KVRangeIdUtil.generate();
        SnapshotSyncRequest request = SnapshotSyncRequest.newBuilder()
            .setSessionId(sessionId)
            .setSnapshot(KVRangeSnapshot.newBuilder()
                .setId(rangeId)
                .build())
            .build();
        KVRangeDumpSession dumpSession = new KVRangeDumpSession(peerStoreId, request, rangeAccessor, messenger,
            MoreExecutors.directExecutor(), Duration.ofSeconds(5), 1024, dumpBytesRecorder);
        assertTrue(dumpSession.awaitDone().toCompletableFuture().isDone());
        ArgumentCaptor<KVRangeMessage> messageCap = ArgumentCaptor.forClass(KVRangeMessage.class);
        verify(messenger).send(messageCap.capture());
        KVRangeMessage message = messageCap.getValue();
        assertEquals(message.getHostStoreId(), peerStoreId);
        assertEquals(message.getRangeId(), rangeId);
        assertEquals(message.getSaveSnapshotDataRequest().getSessionId(), sessionId);
        assertEquals(message.getSaveSnapshotDataRequest().getFlag(), SaveSnapshotDataRequest.Flag.End);
    }

    @Test
    public void dumpNonExistSnapshot() {
        String peerStoreId = "follower";
        String sessionId = "session";
        KVRangeId rangeId = KVRangeIdUtil.generate();
        KVRangeSnapshot snapshot = KVRangeSnapshot.newBuilder()
            .setId(rangeId)
            .setCheckpointId("checkpoint")
            .build();
        SnapshotSyncRequest request = SnapshotSyncRequest.newBuilder()
            .setSessionId(sessionId)
            .setSnapshot(snapshot)
            .build();
        when(rangeAccessor.hasCheckpoint(snapshot)).thenReturn(false);
        KVRangeDumpSession dumpSession = new KVRangeDumpSession(peerStoreId, request, rangeAccessor, messenger,
            MoreExecutors.directExecutor(), Duration.ofSeconds(5), 1024, dumpBytesRecorder);
        assertTrue(dumpSession.awaitDone().toCompletableFuture().isDone());
        ArgumentCaptor<KVRangeMessage> messageCap = ArgumentCaptor.forClass(KVRangeMessage.class);
        verify(messenger).send(messageCap.capture());
        KVRangeMessage message = messageCap.getValue();
        assertEquals(message.getHostStoreId(), peerStoreId);
        assertEquals(message.getRangeId(), rangeId);
        assertEquals(message.getSaveSnapshotDataRequest().getSessionId(), sessionId);
        assertEquals(message.getSaveSnapshotDataRequest().getFlag(), SaveSnapshotDataRequest.Flag.Error);
    }

    @Test
    public void noSessionFound() {
        sessionEndWithFlag(SaveSnapshotDataReply.Result.NoSessionFound);
    }

    @Test
    public void sessionEndAbnormally() {
        sessionEndWithFlag(SaveSnapshotDataReply.Result.Error);
    }

    @Test
    public void sessionEndNormally() {
        sessionEndWithFlag(SaveSnapshotDataReply.Result.OK);
    }

    private void sessionEndWithFlag(SaveSnapshotDataReply.Result flag) {
        String localStoreId = "leader";
        String peerStoreId = "follower";
        String sessionId = "session";
        String checkpointId = "checkpoint";
        KVRangeId rangeId = KVRangeIdUtil.generate();
        KVRangeSnapshot snapshot = KVRangeSnapshot.newBuilder()
            .setId(rangeId)
            .setCheckpointId(checkpointId)
            .build();
        SnapshotSyncRequest request = SnapshotSyncRequest.newBuilder()
            .setSessionId(sessionId)
            .setSnapshot(snapshot)
            .build();
        PublishSubject<KVRangeMessage> incomingMsgs = PublishSubject.create();

        when(rangeAccessor.hasCheckpoint(snapshot)).thenReturn(true);
        when(rangeAccessor.open(snapshot)).thenReturn(snapItr);
        when(messenger.receive()).thenReturn(incomingMsgs);

        when(snapItr.isValid()).thenReturn(true, false);
        when(snapItr.key()).thenReturn(ByteString.copyFromUtf8("key"));
        when(snapItr.value()).thenReturn(ByteString.copyFromUtf8("value"));
        KVRangeDumpSession dumpSession = new KVRangeDumpSession(peerStoreId, request, rangeAccessor, messenger,
            MoreExecutors.directExecutor(), Duration.ofSeconds(5), 1024, dumpBytesRecorder);
        assertEquals(dumpSession.checkpointId(), checkpointId);
        verify(snapItr).seekToFirst();
        verify(snapItr).next();
        assertFalse(dumpSession.awaitDone().toCompletableFuture().isDone());
        ArgumentCaptor<KVRangeMessage> messageCap = ArgumentCaptor.forClass(KVRangeMessage.class);
        verify(messenger).send(messageCap.capture());

        incomingMsgs.onNext(KVRangeMessage.newBuilder()
            .setHostStoreId(localStoreId)
            .setRangeId(rangeId)
            .setSaveSnapshotDataReply(SaveSnapshotDataReply.newBuilder()
                .setReqId(messageCap.getValue().getSaveSnapshotDataRequest().getReqId())
                .setSessionId(sessionId)
                .setResult(flag)
                .build())
            .build());
        verify(dumpBytesRecorder).record(anyInt());
        assertTrue(dumpSession.awaitDone().toCompletableFuture().isDone());
    }

    @Test
    public void rateLimit() {
        String peerStoreId = "follower";
        String sessionId = "session";
        String checkpointId = "checkpoint";
        KVRangeId rangeId = KVRangeIdUtil.generate();
        KVRangeSnapshot snapshot = KVRangeSnapshot.newBuilder()
            .setId(rangeId)
            .setCheckpointId(checkpointId)
            .build();
        SnapshotSyncRequest request = SnapshotSyncRequest.newBuilder()
            .setSessionId(sessionId)
            .setSnapshot(snapshot)
            .build();
        PublishSubject<KVRangeMessage> incomingMsgs = PublishSubject.create();

        when(rangeAccessor.hasCheckpoint(snapshot)).thenReturn(true);
        when(rangeAccessor.open(snapshot)).thenReturn(snapItr);
        when(messenger.receive()).thenReturn(incomingMsgs);

        when(snapItr.isValid()).thenReturn(true);
        when(snapItr.key()).thenReturn(ByteString.copyFromUtf8("key"));
        when(snapItr.value()).thenReturn(ByteString.copyFromUtf8("value"));
        KVRangeDumpSession dumpSession = new KVRangeDumpSession(peerStoreId, request, rangeAccessor, messenger,
            MoreExecutors.directExecutor(), Duration.ofMillis(100), 5, dumpBytesRecorder);
        ArgumentCaptor<KVRangeMessage> messageCap = ArgumentCaptor.forClass(KVRangeMessage.class);
        verify(messenger, times(1)).send(messageCap.capture());
        assertEquals(messageCap.getValue().getSaveSnapshotDataRequest().getFlag(), SaveSnapshotDataRequest.Flag.More);
    }

    @SneakyThrows
    @Test
    public void resend() {
        String peerStoreId = "follower";
        String sessionId = "session";
        String checkpointId = "checkpoint";
        KVRangeId rangeId = KVRangeIdUtil.generate();
        KVRangeSnapshot snapshot = KVRangeSnapshot.newBuilder()
            .setId(rangeId)
            .setCheckpointId(checkpointId)
            .build();
        SnapshotSyncRequest request = SnapshotSyncRequest.newBuilder()
            .setSessionId(sessionId)
            .setSnapshot(snapshot)
            .build();
        PublishSubject<KVRangeMessage> incomingMsgs = PublishSubject.create();

        when(rangeAccessor.hasCheckpoint(snapshot)).thenReturn(true);
        when(rangeAccessor.open(snapshot)).thenReturn(snapItr);
        when(messenger.receive()).thenReturn(incomingMsgs);

        when(snapItr.isValid()).thenReturn(true, false);
        when(snapItr.key()).thenReturn(ByteString.copyFromUtf8("key"));
        when(snapItr.value()).thenReturn(ByteString.copyFromUtf8("value"));
        KVRangeDumpSession dumpSession = new KVRangeDumpSession(peerStoreId, request, rangeAccessor, messenger,
            MoreExecutors.directExecutor(), Duration.ofMillis(100), 1024, dumpBytesRecorder);
        Thread.sleep(60);
        dumpSession.tick();
        ArgumentCaptor<KVRangeMessage> messageCap = ArgumentCaptor.forClass(KVRangeMessage.class);
        verify(messenger, times(2)).send(messageCap.capture());
        assertEquals(messageCap.getAllValues().get(1), messageCap.getAllValues().get(0));
    }

    @SneakyThrows
    @Test
    public void idle() {
        String peerStoreId = "follower";
        String sessionId = "session";
        String checkpointId = "checkpoint";
        KVRangeId rangeId = KVRangeIdUtil.generate();
        KVRangeSnapshot snapshot = KVRangeSnapshot.newBuilder()
            .setId(rangeId)
            .setCheckpointId(checkpointId)
            .build();
        SnapshotSyncRequest request = SnapshotSyncRequest.newBuilder()
            .setSessionId(sessionId)
            .setSnapshot(snapshot)
            .build();
        PublishSubject<KVRangeMessage> incomingMsgs = PublishSubject.create();

        when(rangeAccessor.hasCheckpoint(snapshot)).thenReturn(true);
        when(rangeAccessor.open(snapshot)).thenReturn(snapItr);
        when(messenger.receive()).thenReturn(incomingMsgs);

        when(snapItr.isValid()).thenReturn(true, false);
        when(snapItr.key()).thenReturn(ByteString.copyFromUtf8("key"));
        when(snapItr.value()).thenReturn(ByteString.copyFromUtf8("value"));
        KVRangeDumpSession dumpSession = new KVRangeDumpSession(peerStoreId, request, rangeAccessor, messenger,
            MoreExecutors.directExecutor(), Duration.ofMillis(10), 1024, dumpBytesRecorder);
        Thread.sleep(20);
        dumpSession.tick();
        verify(messenger, times(1)).send(any());
        assertTrue(dumpSession.awaitDone().toCompletableFuture().isDone());
    }

    @Test
    public void cancel() {
        String peerStoreId = "follower";
        String sessionId = "session";
        String checkpointId = "checkpoint";
        KVRangeId rangeId = KVRangeIdUtil.generate();
        KVRangeSnapshot snapshot = KVRangeSnapshot.newBuilder()
            .setId(rangeId)
            .setCheckpointId(checkpointId)
            .build();
        SnapshotSyncRequest request = SnapshotSyncRequest.newBuilder()
            .setSessionId(sessionId)
            .setSnapshot(snapshot)
            .build();
        PublishSubject<KVRangeMessage> incomingMsgs = PublishSubject.create();

        when(rangeAccessor.hasCheckpoint(snapshot)).thenReturn(true);
        when(rangeAccessor.open(snapshot)).thenReturn(snapItr);
        when(messenger.receive()).thenReturn(incomingMsgs);

        when(snapItr.isValid()).thenReturn(true, false);
        when(snapItr.key()).thenReturn(ByteString.copyFromUtf8("key"));
        when(snapItr.value()).thenReturn(ByteString.copyFromUtf8("value"));
        KVRangeDumpSession dumpSession = new KVRangeDumpSession(peerStoreId, request, rangeAccessor, messenger,
            MoreExecutors.directExecutor(), Duration.ofMillis(10), 1024, dumpBytesRecorder);
        assertFalse(dumpSession.awaitDone().toCompletableFuture().isDone());
        dumpSession.cancel();
        verify(messenger, times(1)).send(any());
        assertTrue(dumpSession.awaitDone().toCompletableFuture().isDone());
    }
}
