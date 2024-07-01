package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.proto.Range;
import com.zachary.bifromq.basekv.proto.State;
import io.reactivex.rxjava3.core.Observable;

interface IKVRangeMetadataAccessor {
    long version();

    State state();

    long lastAppliedIndex();

    Range range();

    int dataBoundId();

    Range dataBound();

    Observable<IKVRangeState.KVRangeMeta> source();

    void refresh();

    void destroy(boolean includeData);
}
