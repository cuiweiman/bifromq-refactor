package com.zachary.bifromq.basekv.raft;


import com.zachary.bifromq.basekv.raft.proto.LogEntry;
import com.zachary.bifromq.basekv.raft.proto.RaftNodeSyncState;

import java.util.LinkedList;
import java.util.Optional;


class PeerLogReplicatorStateReplicating extends PeerLogReplicatorState {
    private final LinkedList<Long> inflightAppends;
    private int heartbeatElapsedTick;
    private boolean needHeartbeat;
    private long lastMatchIndex;
    private long catchupRate;
    private int fullElapsedTick = -1;
    private int unconfirmedHeartbeatTick = 0;

    PeerLogReplicatorStateReplicating(String peerId,
                                      RaftConfig config,
                                      IRaftStateStore stateStorage,
                                      long matchIndex,
                                      long nextIndex,
                                      IRaftNodeLogger logger) {
        super(peerId, config, stateStorage, matchIndex, nextIndex, logger);
        lastMatchIndex = matchIndex;
        inflightAppends = new LinkedList<>();
    }

    @Override
    public RaftNodeSyncState state() {
        return RaftNodeSyncState.Replicating;
    }

    @Override
    public PeerLogReplicatorState tick() {
        if (heartbeatElapsedTick >= config.getHeartbeatTimeoutTick()) {
            needHeartbeat = true;
        }
        heartbeatElapsedTick++;
        if (isFull()) {
            if (needHeartbeat) {
                // replicating paused because inflightAppends is full, free up a space from start,
                // so the replicating could move forward,
                // this situation may happen when packets loss from peer
                inflightAppends.removeFirst();
            }
            // increase full state elapsed ticks
            fullElapsedTick++;
        }
        catchupRate = matchIndex - lastMatchIndex;
        lastMatchIndex = matchIndex;
        if ((catchupRate == 0 && fullElapsedTick >= config.getElectionTimeoutTick())
            || unconfirmedHeartbeatTick >= config.getElectionTimeoutTick()) {
            // transit probing state when:
            // 1) peer has not made any progress within one election timeout after paused, or
            // 2) peer has not confirmed any heartbeat within one election timeout
            return new PeerLogReplicatorStateProbing(peerId, config, stateStorage, matchIndex, matchIndex + 1, logger);
        }
        return this;
    }

    @Override
    public long catchupRate() {
        return catchupRate;
    }

    @Override
    public boolean pauseReplicating() {
        return isFull();
    }

    @Override
    public boolean needHeartbeat() {
        return needHeartbeat;
    }

    @Override
    public PeerLogReplicatorState backoff(long peerRejectedIndex, long peerLastIndex) {
        if (peerRejectedIndex < matchIndex) {
            // rejectedIndex <= previously confirmed index,
            // must be an out-of-order reject reply
            return this;
        }
        // peer is out of sync, we need to move to probe or snapshot,
        // if log entry at peerLastIndex is still available
        Optional<LogEntry> prevLogEntry = stateStorage.entryAt(peerLastIndex);
        if (!prevLogEntry.isPresent()) {
            // if prev log entry is unavailable
            logger.logDebug("Entry[index:{}] not available for peer[{}] from "
                    + "tracker[matchIndex:{},nextIndex:{},state:{}], start syncing with snapshot",
                peerLastIndex, peerId, matchIndex, nextIndex, state());
            return new PeerLogReplicatorStateSnapshotSyncing(peerId, config, stateStorage, logger);
        } else {
            // probing from peer's last index
            long probeStartIndex = Math.min(peerLastIndex, matchIndex);
            logger.logDebug("Peer[{}] rejected entries after index[{}] from "
                    + "tracker[matchIndex:{},nextIndex:{},state:{}], start probing",
                peerId, peerLastIndex, matchIndex, nextIndex, state());
            return new PeerLogReplicatorStateProbing(peerId, config,
                stateStorage, probeStartIndex, probeStartIndex + 1, logger);
        }
    }

    @Override
    public PeerLogReplicatorState confirmMatch(long peerLastIndex) {
        if (matchIndex < peerLastIndex) {
            matchIndex = peerLastIndex;
        }
        if (nextIndex < peerLastIndex + 1) {
            nextIndex = peerLastIndex + 1;
        }
        free(peerLastIndex);
        // received reply from peer, stop the counter
        fullElapsedTick = -1;
        return this;
    }

    @Override
    public PeerLogReplicatorState replicateTo(long endIndex) {
        // allowing tracking heartbeat
        assert endIndex + 1 >= nextIndex;
        boolean isHeartbeat = endIndex == (Math.max(nextIndex, stateStorage.firstIndex()) - 1);
        unconfirmedHeartbeatTick = isHeartbeat ? unconfirmedHeartbeatTick + heartbeatElapsedTick : 0;
        heartbeatElapsedTick = 0; // reset heartbeatElapsedTick
        needHeartbeat = false;
        nextIndex = endIndex + 1;
        inflightAppends.add(endIndex);
        if (isFull() && fullElapsedTick == -1) {
            // start full state elapsed tick counter if necessary
            fullElapsedTick = 0;
        }
        return this;
    }

    void free(long belowIndex) {
        while (!inflightAppends.isEmpty() && inflightAppends.peekFirst() <= belowIndex) {
            inflightAppends.removeFirst();
        }
        if (inflightAppends.isEmpty()) {
            unconfirmedHeartbeatTick = 0;
        }
    }

    boolean isFull() {
        return inflightAppends.size() >= config.getMaxInflightAppends();
    }
}
