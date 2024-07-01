package com.zachary.bifromq.basekv.store.wal;

import com.zachary.bifromq.basekv.proto.KVRangeCommand;
import com.zachary.bifromq.basekv.proto.KVRangeSnapshot;
import com.zachary.bifromq.basekv.raft.event.ElectionEvent;
import com.zachary.bifromq.basekv.raft.event.SnapshotRestoredEvent;
import com.zachary.bifromq.basekv.raft.proto.ClusterConfig;
import com.zachary.bifromq.basekv.raft.proto.LogEntry;
import com.zachary.bifromq.basekv.raft.proto.RaftMessage;
import com.zachary.bifromq.basekv.raft.proto.RaftNodeStatus;
import com.zachary.bifromq.basekv.raft.proto.RaftNodeSyncState;
import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.core.Observable;
import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

public interface IKVRangeWAL {
    @AllArgsConstructor
    class SnapshotInstallTask {
        public final ByteString snapshot;
        public final CompletableFuture<Void> onDone = new CompletableFuture<>();
    }

    String id();

    boolean isLeader();

    Optional<String> currentLeader();

    RaftNodeStatus currentState();

    Observable<RaftNodeStatus> state();

    Observable<ElectionEvent> election();

    ClusterConfig clusterConfig();

    KVRangeSnapshot latestSnapshot();

    CompletableFuture<Void> propose(KVRangeCommand command);

    Observable<Map<String, RaftNodeSyncState>> replicationStatus();

    IKVRangeWALSubscription subscribe(long startIndex, IKVRangeWALSubscriber subscriber, Executor executor);

    CompletableFuture<LogEntry> once(long startIndex, Predicate<LogEntry> condition, Executor executor);

    Observable<Long> commitIndex();

    Observable<SnapshotRestoredEvent> snapshotRestoreEvent();

    CompletableFuture<Iterator<LogEntry>> retrieveCommitted(long fromIndex, long maxSize);

    CompletableFuture<Long> readIndex();

    CompletableFuture<Void> transferLeadership(String peerId);

    boolean stepDown();

    CompletableFuture<Void> changeClusterConfig(String correlateId, Set<String> voters, Set<String> learners);

    CompletableFuture<Void> compact(KVRangeSnapshot snapshot);

    Observable<SnapshotInstallTask> snapshotInstallTask();

    Observable<Map<String, List<RaftMessage>>> peerMessages();

    CompletableFuture<Void> recover();

    void receivePeerMessages(String peerId, List<RaftMessage> messages);

    long logDataSize();

    void tick();

    void start();

    CompletableFuture<Void> close();

    void destroy();
}
