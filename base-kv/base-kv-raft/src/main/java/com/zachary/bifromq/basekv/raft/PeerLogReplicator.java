package com.zachary.bifromq.basekv.raft;


import com.zachary.bifromq.basekv.raft.proto.RaftNodeSyncState;

final class PeerLogReplicator implements IPeerLogReplicator {
    private PeerLogReplicatorState state;


    PeerLogReplicator(String peerId, RaftConfig config, IRaftStateStore stateStorage, IRaftNodeLogger logger) {
        this.state = new PeerLogReplicatorStateProbing(peerId, config, stateStorage, logger);
    }

    @Override
    public long matchIndex() {
        return this.state.matchIndex();
    }

    @Override
    public long nextIndex() {
        return this.state.nextIndex();
    }

    @Override
    public RaftNodeSyncState status() {
        return this.state.state();
    }

    @Override
    public boolean tick() {
        PeerLogReplicatorState newState = this.state.tick();
        if (this.state != newState) {
            this.state = newState;
            return true;
        }
        return false;
    }

    @Override
    public long catchupRate() {
        return this.state.catchupRate();
    }

    @Override
    public boolean pauseReplicating() {
        return this.state.pauseReplicating();
    }

    @Override
    public boolean needHeartbeat() {
        return this.state.needHeartbeat();
    }

    @Override
    public boolean backoff(long peerRejectedIndex, long peerLastIndex) {
        PeerLogReplicatorState newState = this.state.backoff(peerRejectedIndex, peerLastIndex);
        if (this.state != newState) {
            this.state = newState;
            return true;
        }
        return false;
    }

    @Override
    public boolean confirmMatch(long peerLastIndex) {
        PeerLogReplicatorState newState = this.state.confirmMatch(peerLastIndex);
        if (this.state != newState) {
            this.state = newState;
            return true;
        }
        return false;
    }

    @Override
    public boolean replicateBy(long endIndex) {
        PeerLogReplicatorState newState = this.state.replicateTo(endIndex);
        if (this.state != newState) {
            this.state = newState;
            return true;
        }
        return false;
    }
}
