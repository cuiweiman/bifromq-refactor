package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.proto.KVRangeSnapshot;
import com.zachary.bifromq.basekv.proto.Range;
import com.zachary.bifromq.basekv.proto.State;
import com.zachary.bifromq.basekv.store.api.IKVIterator;
import com.zachary.bifromq.basekv.store.api.IKVRangeReader;
import io.reactivex.rxjava3.core.Observable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * The KVRange State Machine
 */
public interface IKVRangeState {
    @AllArgsConstructor
    @EqualsAndHashCode
    class KVRangeMeta {
        final long ver;
        final State state;
        final Range range;
    }

    /**
     * Make a checkpoint of current state and return a descriptor
     *
     * @return
     */
    KVRangeSnapshot checkpoint();

    /**
     * Check if the given checkpoint exists
     *
     * @param checkpoint
     * @return
     */
    boolean hasCheckpoint(KVRangeSnapshot checkpoint);

    /**
     * Open an iterator for accessing the checkpoint data
     *
     * @param checkpoint
     * @return
     */
    IKVIterator open(KVRangeSnapshot checkpoint);

    /**
     * Borrow a reader for query the latest data. The borrowed the reader must be released once finished use
     *
     * @return
     */
    IKVRangeReader borrow();

    /**
     * Release a borrowed reader, so it will be available for other borrower
     *
     * @param reader
     */
    void returnBorrowed(IKVRangeReader reader);

    IKVRangeReader getReader();

    /**
     * Get a state updater
     *
     * @return
     */
    IKVRangeWriter getWriter();

    Observable<KVRangeMeta> metadata();

    /**
     * Get a state restorer
     *
     * @param checkpoint
     * @return
     */
    IKVRangeRestorer reset(KVRangeSnapshot checkpoint);

    void destroy(boolean includeData);
}
