package com.zachary.bifromq.basekv.raft;

import com.zachary.bifromq.basekv.raft.event.RaftEvent;
import com.zachary.bifromq.basekv.raft.proto.ClusterConfig;
import com.zachary.bifromq.basekv.raft.proto.LogEntry;
import com.zachary.bifromq.basekv.raft.proto.RaftMessage;
import com.zachary.bifromq.basekv.raft.proto.RaftNodeStatus;
import com.google.protobuf.ByteString;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface IRaftNode {
    interface IRaftEventListener {
        /**
         * This callback method will be executed in raft thread
         *
         * @param event
         */
        void onEvent(RaftEvent event);
    }

    interface IRaftMessageSender {
        void send(Map<String, List<RaftMessage>> messages);
    }

    interface ISnapshotInstaller {
        /**
         * Application specific async snapshot installation
         *
         * @param fsmSnapshot the snapshot you received from snapshot listener
         */
        CompletableFuture<Void> install(ByteString fsmSnapshot);
    }

    boolean isStarted();

    /**
     * the id of local raft node
     *
     * @return
     */
    String id();

    /**
     * role of the local raft node in current log term
     *
     * @return
     */
    RaftNodeStatus status();

    /**
     * tick from external clock which driving Raft StateMachine to move forward
     */
    void tick();

    /**
     * Propose a app command. The returned future is guaranteed to be completed when the corresponding log entry is
     * COMMITTED or finished with exception indicating if the proposal MAYBE dropped due to timeout or other reason.
     *
     * @param appCommand
     * @return
     */
    CompletableFuture<Void> propose(ByteString appCommand);

    /**
     * Request an index for safely linearizable read. NOTE: The returned will be completed by raft execution thread.
     *
     * @return
     */
    CompletableFuture<Long> readIndex();

    /**
     * Receive raft messages from other Peers and drive local Raft StateMachine to proceed.
     *
     * @param message
     * @return
     */
    void receive(String fromPeer, RaftMessage message);

    /**
     * Compact RAFT logs by removing all log entries below compactIndex(inclusive), using the snapshot from
     * application's state machine. The snapshot is expected to be as light-weight as possible, so it's better to
     * include only some kind of metadata about the complete snapshot data, like file location, url etc. It's
     * application's duty to make sure the snapshot including latest state by applying the log until compactIndex.
     *
     * <br>
     * NOTE: The returned will be completed by raft execution thread.
     *
     * @param fsmSnapshot
     * @param compactIndex
     * @return
     */
    CompletableFuture<Void> compact(ByteString fsmSnapshot, long compactIndex);

    /**
     * Transfer the leadership to new leader. NOTE: the returned future is completed as soon as the current leader is
     * STEP DOWN due to the transfer, NOT when the new leader elected. NOTE: The returned will be completed by raft
     * execution thread. Don't do make heavy work on it.
     *
     * @param newLeader
     * @return
     */
    CompletableFuture<Void> transferLeadership(String newLeader);

    /**
     * Recover if lost quorum because of majority members failed
     *
     * @return
     */
    CompletableFuture<Void> recover();

    /**
     * Returns the latest cluster config of the raft cluster
     *
     * @return
     */
    ClusterConfig latestClusterConfig();

    /**
     * If current node is leader, make it step down as a follower, which means an election is going to happen
     *
     * @return true if it was a leader
     */
    Boolean stepDown();

    /**
     * Returns the latest snapshot in the raft node
     *
     * @return
     */
    ByteString latestSnapshot();

    /**
     * Change cluster membership config NOTE: The returned will be completed by raft execution thread.
     *
     * @param correlateId the id will be embedded in config entry, so caller could use it to associate with a real
     *                    request
     * @param voters
     * @param learners
     * @return
     */
    CompletableFuture<Void> changeClusterConfig(String correlateId, Set<String> voters, Set<String> learners);

    /**
     * Retrieve committed log entries from given index to current commitIndex. Note: maxSize controls max aggregated
     * size returned, so the returned may be fewer.
     *
     * @param fromIndex
     * @param maxSize
     * @return
     */
    CompletableFuture<Iterator<LogEntry>> retrieveCommitted(long fromIndex, long maxSize);

    /**
     * Start the Raft Node with necessary callbacks, this callbacks will be executed in raft's thread.
     *
     * @param sender
     * @param listener
     * @param installer
     */
    void start(IRaftMessageSender sender, IRaftEventListener listener, ISnapshotInstaller installer);

    /**
     * Stop the raft node asynchronously
     */
    CompletableFuture<Void> stop();
}
