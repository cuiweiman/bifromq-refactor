package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.proto.Range;
import com.zachary.bifromq.basekv.proto.State;
import com.zachary.bifromq.basekv.store.api.IKVWriter;

public interface IKVRangeWriter extends IKVRangeUpdater {
    IKVRangeWriter bumpVer(boolean toOdd);

    IKVRangeWriter resetVer(long ver);

    IKVRangeWriter setLastAppliedIndex(long lastAppliedIndex);

    IKVRangeWriter setRange(Range range);

    IKVRangeWriter setState(State state);

    IKVWriter kvWriter();
}
