package com.zachary.bifromq.basekv.store;

import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.KVRangeStoreDescriptor;
import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.core.Observable;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

public interface IKVRangeStore {
    String id();

    boolean isStarted();

    void start(IStoreMessenger messenger);

    void stop();

    /**
     * Bootstrap the first replica of the very first KVRange which covers full key range in current store, it's caller's
     * responsibility to guarantee this method is invoked on exactly only ONE store node within a SINGLE base kv cluster
     * deployment.
     *
     * @return true if bootstrap is success on current store and the 'genesis' KVRange is generated
     */
    boolean bootstrap();

    /**
     * Recover unwritable ranges hosted in current store
     *
     * @return
     */
    CompletionStage<Void> recover();

    Observable<KVRangeStoreDescriptor> describe();

    /**
     * Transfer the leadership of specified KVRange to other KVRangeStore
     *
     * @param id the id of the KVRange
     * @return
     */
    CompletionStage<Void> transferLeadership(long ver, KVRangeId id, String newLeader);

    /**
     * Change a KVRange's config, this method must be invoked on the leader of specified KVRange, and there is no
     * un-stabilized range settings
     *
     * @param id
     * @return
     */
    CompletionStage<Void> changeReplicaConfig(long ver, KVRangeId id, Set<String> newVoters, Set<String> newLearners);

    CompletionStage<Void> split(long ver, KVRangeId id, ByteString splitKey);

    CompletionStage<Void> merge(long ver, KVRangeId mergerId, KVRangeId mergeeId);

    CompletionStage<Boolean> exist(long ver, KVRangeId id, ByteString key, boolean linearized);

    CompletionStage<Optional<ByteString>> get(long ver, KVRangeId id, ByteString key, boolean linearized);

    CompletionStage<ByteString> queryCoProc(long ver, KVRangeId id, ByteString query, boolean linearized);

    CompletionStage<ByteString> put(long ver, KVRangeId id, ByteString key, ByteString value);

    CompletionStage<ByteString> delete(long ver, KVRangeId id, ByteString key);

    CompletionStage<ByteString> mutateCoProc(long ver, KVRangeId id, ByteString mutate);
}
