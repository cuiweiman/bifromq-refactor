package com.zachary.bifromq.basekv;

import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.Range;
import com.google.protobuf.ByteString;
import java.util.List;
import java.util.Optional;

public interface IKVRangeRouter {
    Optional<KVRangeSetting> findById(KVRangeId id);

    Optional<KVRangeSetting> findByKey(ByteString key);

    List<KVRangeSetting> findByRange(Range range);
}
