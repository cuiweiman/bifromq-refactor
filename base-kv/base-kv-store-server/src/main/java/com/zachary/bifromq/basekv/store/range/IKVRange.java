package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.proto.KVRangeDescriptor;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.core.Observable;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

public interface IKVRange {

    KVRangeId id();

    boolean isOccupying(String checkpointId);

    /**
     * If the range is ready to quit from the store. This happens after config changing or merging operation
     *
     * @return
     */
    boolean readyToQuit();

    Observable<KVRangeDescriptor> describe();

    void open(IKVRangeMessenger messenger);

    void tick();

    /**
     * Close the range, all activity of the range will be stopped after closed
     *
     * @return
     */
    CompletionStage<Void> close();

    /**
     * Quit the range from the store, if the range is ready to quit. The range will be closed implicitly.
     *
     * @return
     */
    CompletionStage<Void> quit();

    /**
     * Destroy the range from store, all range related data will be purged from the store. The range will be closed
     * implicitly
     *
     * @return
     */
    CompletionStage<Void> destroy();

    /**
     * Recover the range quorum, if it's unable to do election
     *
     * @return
     */
    CompletionStage<Void> recover();

    CompletionStage<Void> transferLeadership(long ver, String newLeader);

    CompletionStage<Void> changeReplicaConfig(long ver, Set<String> newVoters, Set<String> newLearners);

    CompletionStage<Void> split(long ver, ByteString splitKey);

    CompletionStage<Void> merge(long ver, KVRangeId mergeeId);

    CompletionStage<Boolean> exist(long ver, ByteString key, boolean linearized);

    CompletionStage<Optional<ByteString>> get(long ver, ByteString key, boolean linearized);

    CompletionStage<ByteString> queryCoProc(long ver, ByteString query, boolean linearized);

    CompletionStage<ByteString> put(long ver, ByteString key, ByteString value);

    CompletionStage<ByteString> delete(long ver, ByteString key);

    CompletionStage<ByteString> mutateCoProc(long ver, ByteString mutate);

}
