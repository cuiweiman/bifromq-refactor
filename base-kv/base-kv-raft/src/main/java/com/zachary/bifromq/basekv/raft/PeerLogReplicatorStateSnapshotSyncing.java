package com.zachary.bifromq.basekv.raft;


import com.zachary.bifromq.basekv.raft.proto.RaftNodeSyncState;

class PeerLogReplicatorStateSnapshotSyncing extends PeerLogReplicatorState {
    private int heartbeatElapsedTick;
    private int installSnapshotElapsedTick;
    private boolean needHeartbeat;
    private boolean snapshotSent;

    PeerLogReplicatorStateSnapshotSyncing(String peerId,
                                          RaftConfig config,
                                          IRaftStateStore stateStorage,
                                          IRaftNodeLogger logger) {
        super(peerId,
            config,
            stateStorage,
            stateStorage.latestSnapshot().getIndex(),
            stateStorage.latestSnapshot().getIndex() + 1, logger);
    }

    @Override
    public RaftNodeSyncState state() {
        return RaftNodeSyncState.SnapshotSyncing;
    }

    @Override
    public PeerLogReplicatorState tick() {
        installSnapshotElapsedTick++;
        if (installSnapshotElapsedTick >= config.getInstallSnapshotTimeoutTick()) {
            // no ack from peer within timeout, restart from probe
            logger.logDebug("Peer[{}] install snapshot timeout from "
                    + "tracker[matchIndex:{},nextIndex:{},state:{}], probing again",
                peerId, matchIndex, nextIndex, state());
            return new PeerLogReplicatorStateProbing(peerId, config, stateStorage, logger);
        }
        if (heartbeatElapsedTick >= config.getHeartbeatTimeoutTick()) {
            heartbeatElapsedTick = 0;
            needHeartbeat = true;
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
        return snapshotSent;
    }

    @Override
    public boolean needHeartbeat() {
        return needHeartbeat;
    }

    @Override
    public PeerLogReplicatorState backoff(long peerRejectedIndex, long peerLastIndex) {
        // peerLastIndex should be the index of last entry in snapshot
        if (matchIndex == peerLastIndex) {
            // peer is still under tracking
            // if peer reports installation failure, snapshot syncing again
            logger.logDebug("Peer[{}] rejected snapshot from "
                    + "tracker[matchIndex:{},nextIndex:{},state:{}], try again",
                peerId, matchIndex, nextIndex, state());
            return new PeerLogReplicatorStateSnapshotSyncing(peerId, config, stateStorage, logger);
        }
        return this;
    }

    @Override
    public PeerLogReplicatorState confirmMatch(long peerLastIndex) {
        // peerLastIndex should be the index of last entry in snapshot
        if (matchIndex == peerLastIndex) {
            // peer is still under tracking
            if (stateStorage.latestSnapshot().getIndex() == matchIndex) {
                // snapshot is still there
                logger.logDebug("Peer[{}] installed snapshot from tracker[matchIndex:{},nextIndex:{},state:{}], "
                    + "start replicating", peerId, matchIndex, nextIndex, state());
                return new PeerLogReplicatorStateReplicating(peerId, config,
                    stateStorage, matchIndex, nextIndex, logger);
            } else {
                logger.logDebug("Peer[{}] installed old snapshot "
                        + "from tracker[matchIndex:{},nextIndex:{},state:{}], try again",
                    peerLastIndex, peerId, matchIndex, nextIndex, state());
                return new PeerLogReplicatorStateSnapshotSyncing(peerId, config, stateStorage, logger);
            }
        }
        return this;
    }

    @Override
    public PeerLogReplicatorState replicateTo(long endIndex) {
        // in snapshotting state, the end index must be matchIndex which is the index of last log entry includes in
        // snapshot
        if (endIndex != matchIndex) {
            return new PeerLogReplicatorStateSnapshotSyncing(peerId, config, stateStorage, logger);
        }
        snapshotSent = true;
        needHeartbeat = false;
        return this;
    }
}
