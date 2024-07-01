package com.zachary.bifromq.basekv.raft;

import com.zachary.bifromq.basekv.raft.event.CommitEvent;
import com.zachary.bifromq.basekv.raft.event.ElectionEvent;
import com.zachary.bifromq.basekv.raft.event.SnapshotRestoredEvent;
import com.zachary.bifromq.basekv.raft.event.StatusChangedEvent;
import com.zachary.bifromq.basekv.raft.exception.CompactionException;
import com.zachary.bifromq.basekv.raft.exception.DropProposalException;
import com.zachary.bifromq.basekv.raft.proto.AppendEntriesReply;
import com.zachary.bifromq.basekv.raft.proto.ClusterConfig;
import com.zachary.bifromq.basekv.raft.proto.LogEntry;
import com.zachary.bifromq.basekv.raft.proto.RaftMessage;
import com.zachary.bifromq.basekv.raft.proto.RaftNodeStatus;
import com.zachary.bifromq.basekv.raft.proto.RequestPreVote;
import com.zachary.bifromq.basekv.raft.proto.RequestPreVoteReply;
import com.zachary.bifromq.basekv.raft.proto.RequestVoteReply;
import com.zachary.bifromq.basekv.raft.proto.Snapshot;
import com.zachary.bifromq.basekv.raft.proto.Voting;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

abstract class RaftNodeState implements IRaftNodeLogger {
    interface OnSnapshotInstalled {
        void done(ByteString fsmSnapshot, Throwable ex);
    }

    /**
     * The future of Uncommitted propose request
     */
    protected static class ProposeTask {
        final long term;
        final CompletableFuture<Void> future;

        ProposeTask(long term, CompletableFuture<Void> future) {
            this.term = term;
            this.future = future;
        }
    }

    private static final String LOG_DETAILS_SUFFIX =
        ": local={}, state={}, term={}, firstIndex={}, lastIndex={}, commitIndex={}, config={}";
    protected final String id;
    protected final RaftConfig config;
    protected final IRaftStateStore stateStorage;
    protected final Logger log;
    protected final IRaftNode.IRaftMessageSender sender;
    protected final IRaftNode.IRaftEventListener listener;
    protected final IRaftNode.ISnapshotInstaller snapshotInstaller;
    protected final OnSnapshotInstalled onSnapshotInstalled;
    protected final LinkedHashMap<Long, ProposeTask> uncommittedProposals;
    protected final int maxUncommittedProposals;
    protected volatile long commitIndex;

    public RaftNodeState(long currentTerm,
                         long commitIndex,
                         RaftConfig config,
                         IRaftStateStore stateStorage,
                         Logger log,
                         LinkedHashMap<Long, ProposeTask> uncommittedProposals,
                         IRaftNode.IRaftMessageSender sender,
                         IRaftNode.IRaftEventListener listener,
                         IRaftNode.ISnapshotInstaller installer,
                         OnSnapshotInstalled onSnapshotInstalled) {
        this.stateStorage = stateStorage;
        this.id = stateStorage.local();
        this.uncommittedProposals = uncommittedProposals;
        this.maxUncommittedProposals = config.getMaxUncommittedProposals() == 0 ? Integer.MAX_VALUE
            : config.getMaxUncommittedProposals();
        this.commitIndex = commitIndex;
        this.config = config;
        this.log = log;
        this.sender = sender;
        this.listener = listener;
        this.snapshotInstaller = installer;
        this.onSnapshotInstalled = onSnapshotInstalled;
        if (currentTerm > currentTerm()) {
            this.stateStorage.saveTerm(currentTerm);
        }
    }

    final String id() {
        return id;
    }

    abstract RaftNodeState stepDown(CompletableFuture<Boolean> onDone);

    abstract RaftNodeState recover(CompletableFuture<Void> onDone);

    abstract RaftNodeState tick();

    abstract void propose(ByteString fsmCmd, CompletableFuture<Void> onDone);

    abstract RaftNodeState stableTo(long stabledIndex);

    abstract RaftNodeState receive(String fromPeer, RaftMessage message);

    abstract RaftNodeStatus getState();

    abstract String currentLeader();

    abstract void readIndex(CompletableFuture<Long> onDone);

    abstract void transferLeadership(String newLeader, CompletableFuture<Void> onDone);

    abstract void changeClusterConfig(String correlateId,
                                      Set<String> newVoters,
                                      Set<String> newLearners,
                                      CompletableFuture<Void> onDone);

    abstract void onSnapshotRestored(ByteString fsmSnapshot, Throwable ex);

    final long currentTerm() {
        return stateStorage.currentTerm();
    }

    final Optional<String> currentVote() {
        Optional<Voting> voting = stateStorage.currentVoting();
        return voting.map(Voting::getFor);
    }

    final ClusterConfig latestClusterConfig() {
        return stateStorage.latestClusterConfig();
    }

    final ByteString latestSnapshot() {
        return stateStorage.latestSnapshot().getData();
    }

    final void retrieveCommitted(long fromIndex, long maxSize,
                                 CompletableFuture<Iterator<LogEntry>> onDone) {
        if (fromIndex < stateStorage.firstIndex() || fromIndex > stateStorage.lastIndex()) {
            onDone.completeExceptionally(new IllegalArgumentException("Index out of range"));
        } else {
            onDone.complete(stateStorage.entries(fromIndex, commitIndex + 1, maxSize));
        }
    }

    final void entryAt(long index, CompletableFuture<Optional<LogEntry>> onDone) {
        onDone.complete(stateStorage.entryAt(index));
    }

    final void compact(ByteString fsmSnapshot, long compactIndex, CompletableFuture<Void> onDone) {
        Optional<LogEntry> compactEntry = stateStorage.entryAt(compactIndex);
        if (compactEntry.isPresent()) {
            Snapshot newSnapshot = Snapshot.newBuilder()
                .setClusterConfig(stateStorage.latestClusterConfig())
                .setIndex(compactIndex)
                .setTerm(compactEntry.get().getTerm())
                .setData(fsmSnapshot)
                .build();
            try {
                long firstIndex = stateStorage.firstIndex();
                stateStorage.applySnapshot(newSnapshot);
                logDebug("Compacted entries[{},{}]", firstIndex, compactIndex);
                onDone.complete(null);
            } catch (Throwable e) {
                onDone.completeExceptionally(new CompactionException("Failed to apply snapshot", e));
            }
        } else {
            onDone.completeExceptionally(CompactionException.STALE_SNAPSHOT);
        }
    }

    protected Set<String> voters() {
        ClusterConfig clusterConfig = stateStorage.latestClusterConfig();
        Set<String> voters = new HashSet<>(clusterConfig.getVotersList());
        voters.addAll(clusterConfig.getNextVotersList());
        return voters;
    }

    protected boolean promotable() {
        String localId = stateStorage.local();
        return voters().contains(localId);
    }

    protected int randomizeElectionTimeoutTick() {
        return config.getElectionTimeoutTick() +
            ThreadLocalRandom.current().nextInt(1, config.getElectionTimeoutTick() + 1);
    }

    protected Set<String> remoteVoters() {
        ClusterConfig clusterConfig = stateStorage.latestClusterConfig();
        Set<String> all = (new HashSet<>(clusterConfig.getVotersList()));
        all.addAll(clusterConfig.getNextVotersList());
        all.remove(stateStorage.local());
        return all;
    }


    protected void submitRaftMessages(Map<String, List<RaftMessage>> messages) {
        Map<String, List<RaftMessage>> sendMessages = messages.entrySet().stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (!sendMessages.isEmpty()) {
            sender.send(sendMessages);
        }
    }

    protected void submitRaftMessages(String remotePeer, RaftMessage message) {
        submitRaftMessages(new HashMap<>() {{
            put(remotePeer, Collections.singletonList(message));
        }});
    }

    protected void submitRaftMessages(String remotePeer, List<RaftMessage> messages) {
        submitRaftMessages(new HashMap<>() {{
            put(remotePeer, messages);
        }});
    }

    protected void submitSnapshot(ByteString fsmSnapshot) {
        snapshotInstaller.install(fsmSnapshot).whenComplete((v, e) -> onSnapshotRestored(fsmSnapshot, e));
    }

    protected void notifyCommit() {
        logTrace("Notify commit index[{}]", commitIndex);
        for (Iterator<Map.Entry<Long, ProposeTask>> it = uncommittedProposals.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Long, ProposeTask> entry = it.next();
            long proposalIndex = entry.getKey();
            ProposeTask task = entry.getValue();
            long proposalTerm = task.term;
            Optional<LogEntry> proposalEntry = stateStorage.entryAt(proposalIndex);
            if (proposalEntry.isPresent()) {
                // proposal still in logs
                if (proposalIndex <= commitIndex) {
                    // proposal may be committed
                    it.remove();
                    if (proposalTerm == proposalEntry.get().getTerm()) {
                        // proposal has committed
                        task.future.complete(null);
                    } else {
                        // proposal has been overridden
                        task.future.completeExceptionally(DropProposalException.OVERRIDDEN);
                    }
                } else if (proposalTerm < currentTerm()) {
                    // current committed entry has newer term and less index, proposal will be overridden in the future
                    it.remove();
                    task.future.completeExceptionally(DropProposalException.OVERRIDDEN);
                } else {
                    // wait for commit index advancing
                    break;
                }
            } else {
                // proposal not in logs
                it.remove();
                task.future.completeExceptionally(DropProposalException.OVERRIDDEN);
            }
        }
        listener.onEvent(new CommitEvent(id, commitIndex));
    }

    protected void notifyLeaderElected(String leaderId, long term) {
        listener.onEvent(new ElectionEvent(id, leaderId, term));
    }

    protected void notifyStateChanged() {
        listener.onEvent(new StatusChangedEvent(id, getState()));
    }

    protected void notifySnapshotRestored() {
        listener.onEvent(new SnapshotRestoredEvent(id, stateStorage.latestSnapshot()));
    }

    protected boolean isUpToDate(long term, long index) {
        long localLastTerm = stateStorage.latestSnapshot().getTerm();
        long localLastIndex = stateStorage.latestSnapshot().getIndex();
        // if there are log entries
        if (stateStorage.lastIndex() >= stateStorage.firstIndex()) {
            localLastTerm = stateStorage.entryAt(stateStorage.lastIndex()).get().getTerm();
            localLastIndex = stateStorage.lastIndex();
        }
        return term > localLastTerm || (term == localLastTerm && index >= localLastIndex);
    }

    protected boolean isProposeThrottled() {
        return uncommittedProposals.size() > maxUncommittedProposals;
    }

    protected void handlePreVote(String fromPeer, long askedTerm, RequestPreVote request) {
        sendRequestPreVoteReply(fromPeer, askedTerm, isUpToDate(request.getLastLogTerm(), request.getLastLogIndex()));
    }

    protected void sendRequestPreVoteReply(String fromPeer, long term, boolean granted) {
        RaftMessage reply = RaftMessage
            .newBuilder()
            .setTerm(term)
            .setRequestPreVoteReply(
                RequestPreVoteReply
                    .newBuilder()
                    .setVoteCouldGranted(granted)
                    .build())
            .build();
        submitRaftMessages(fromPeer, reply);
    }

    protected void sendRequestVoteReply(String fromPeer, long term, boolean granted) {
        RaftMessage requestVoteReply = RaftMessage
            .newBuilder()
            .setTerm(term)
            .setRequestVoteReply(
                RequestVoteReply
                    .newBuilder()
                    .setVoteGranted(granted)
                    .build())
            .build();
        submitRaftMessages(fromPeer, requestVoteReply);
    }

    protected void handleLowTermMessage(String fromPeer, RaftMessage message) {
        switch (message.getMessageTypeCase()) {
            case APPENDENTRIES:
            case INSTALLSNAPSHOT:
                // probably an isolated server regains connectivity with higher term and receiving request from
                // leader of majority, we need to let the leader acknowledge the higher term and step down, so
                // that the partitioned member could rejoin and be stable
                logDebug("Reply to the leader[{}] of lower term[{}] to let it step down",
                    fromPeer, message.getTerm());
                submitRaftMessages(fromPeer,
                    RaftMessage.newBuilder()
                        // force leader step down and starts election in higher term
                        .setTerm(currentTerm())
                        .setAppendEntriesReply(
                            // it doesn't matter what type of the reply message is actually,
                            // the purpose is to step down the leader of lower term and ignored by
                            // follower.
                            AppendEntriesReply.getDefaultInstance()
                        )
                        .build());
                break;
            case REQUESTPREVOTE:
                // let pre-vote candidate with lower term be aware of the higher term instead of dropping,
                // so that it had a chance to update its term, without this mechanism the cluster may deadlock when
                // pre-vote gradually enable using rolling restart.
                // for details checkout: https://github.com/etcd-io/etcd/issues/8501
                logDebug("Reject pre-vote from candidate[{}] of lower term", fromPeer, message.getTerm());
                submitRaftMessages(fromPeer, RaftMessage.newBuilder()
                    .setTerm(currentTerm())
                    .setRequestPreVoteReply(RequestPreVoteReply.newBuilder().setVoteCouldGranted(false).build())
                    .build());
                break;
            default:
                logDebug("Ignore message[{}] with lower term[{}] from peer[{}]",
                    message.getMessageTypeCase(), message.getTerm(), fromPeer);
                // ignore other messages other than the leader issues
        }
    }

    public final void logTrace(String message, Object... args) {
        if (log.isTraceEnabled()) {
            log.trace(message + LOG_DETAILS_SUFFIX, allLogArgs(args));
        }
    }

    public final void logDebug(String message, Object... args) {
        log.debug(message + LOG_DETAILS_SUFFIX, allLogArgs(args));
    }

    public final void logInfo(String message, Object... args) {
        log.info(message + LOG_DETAILS_SUFFIX, allLogArgs(args));
    }

    public final void logWarn(String message, Object... args) {
        log.warn(message + LOG_DETAILS_SUFFIX, allLogArgs(args));
    }

    public final void logError(String message, Object... args) {
        log.error(message + LOG_DETAILS_SUFFIX, allLogArgs(args));
    }

    private Object[] allLogArgs(Object... args) {
        Object[] allArgs = new Object[args.length + 7];
        int copyLength = args.length;
        if (args.length > 0 && args[args.length - 1] instanceof Throwable) {
            copyLength--;
        }
        System.arraycopy(args, 0, allArgs, 0, copyLength);
        allArgs[copyLength] = stateStorage.local();
        allArgs[copyLength + 1] = getState();
        allArgs[copyLength + 2] = currentTerm();
        allArgs[copyLength + 3] = stateStorage.firstIndex();
        allArgs[copyLength + 4] = stateStorage.lastIndex();
        allArgs[copyLength + 5] = commitIndex;
        allArgs[copyLength + 6] = printClusterConfig(stateStorage.latestClusterConfig());
        if (copyLength < args.length) {
            allArgs[copyLength + 6] = args[args.length - 1]; // throwable
        }
        return allArgs;
    }

    private String printClusterConfig(ClusterConfig clusterConfig) {
        return String.format("[c:%s,v:%s,l:%s,nv:%s,nl:%s]",
            clusterConfig.getCorrelateId(),
            clusterConfig.getVotersList(),
            clusterConfig.getLearnersList(),
            clusterConfig.getNextVotersList(),
            clusterConfig.getNextLearnersList());
    }
}
