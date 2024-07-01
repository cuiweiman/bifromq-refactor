package com.zachary.bifromq.basekv.raft;


import com.zachary.bifromq.basekv.raft.proto.RaftNodeSyncState;

class PeerLogReplicatorStateProbing extends PeerLogReplicatorState {
    private int heartbeatElapsedTick = 0;
    private boolean probeSent = false;

    PeerLogReplicatorStateProbing(String peerId,
                                  RaftConfig config,
                                  IRaftStateStore stateStorage,
                                  IRaftNodeLogger logger) {
        this(peerId,
            config,
            stateStorage,
            stateStorage.latestSnapshot().getIndex(),
            stateStorage.lastIndex() + 1,
            logger);
    }

    PeerLogReplicatorStateProbing(String peerId,
                                  RaftConfig config,
                                  IRaftStateStore stateStorage,
                                  long matchIndex,
                                  long nextIndex,
                                  IRaftNodeLogger logger) {
        super(peerId, config, stateStorage, matchIndex, nextIndex, logger);
    }

    @Override
    public RaftNodeSyncState state() {
        return RaftNodeSyncState.Probing;
    }

    @Override
    public PeerLogReplicatorState tick() {
        if (heartbeatElapsedTick >= config.getHeartbeatTimeoutTick()) {
            heartbeatElapsedTick = 0;
            probeSent = false;
        }
        heartbeatElapsedTick++;
        return this;
    }

    @Override
    public long catchupRate() {
        return 0;
    }

    @Override
    public boolean pauseReplicating() {
        return probeSent;
    }

    @Override
    public boolean needHeartbeat() {
        return !probeSent;
    }

    @Override
    public PeerLogReplicatorState backoff(long peerRejectedIndex, long peerLastIndex) {
        // nextIndex may be staled after compaction
        if (stateStorage.entryAt(nextIndex).isEmpty() || nextIndex == peerRejectedIndex + 1) {
            // follower rejected the AppendEntries request for probing
            // backoff 1 until matched or switch to snapshot sync mode
            nextIndex = Math.max(1, Math.min(peerRejectedIndex, peerLastIndex + 1)); // nextIndex >= 1
            matchIndex = Math.min(matchIndex, nextIndex - 1);
            if (nextIndex > 0 && stateStorage.entryAt(nextIndex).isEmpty()) {
                // if prev log entry is unavailable, send follower the latest snapshot
                logger.logDebug("Entry[index:{}] not available for peer[{}] from "
                        + "tracker[matchIndex:{},nextIndex:{},state:{}], start syncing with snapshot",
                    nextIndex, peerId, matchIndex, nextIndex, state());
                return new PeerLogReplicatorStateSnapshotSyncing(peerId, config, stateStorage, logger);
            }
            probeSent = false;
        }
        return this;
    }

    @Override
    public PeerLogReplicatorState confirmMatch(long peerLastIndex) {
        // replicator state may transit to replicating because of receiving "heartbeat" reply from follower
        // in this case matchIndex is same as peerLastIndex(reported by follower)
        if (matchIndex <= peerLastIndex) {
            // switch to replicating mode
            logger.logDebug("Peer[{}] accepted entry[{}] from tracker[matchIndex:{},nextIndex:{},state:{}], "
                    + "start replicating",
                peerId, peerLastIndex, matchIndex, nextIndex, state());
            return new PeerLogReplicatorStateReplicating(peerId, config,
                stateStorage, peerLastIndex, peerLastIndex + 1, logger);
        }
        // ignore outdated messages
        return this;
    }

    @Override
    public PeerLogReplicatorState replicateTo(long endIndex) {
        // zero or more log entries sent for probing
        assert endIndex + 1 >= nextIndex;
        probeSent = true;
        return this;
    }
}
