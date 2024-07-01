package com.zachary.bifromq.basekv.store.range;

import java.util.concurrent.CompletionStage;

public interface IKVRangeQueryLinearizer {
    CompletionStage<Void> linearize();
}
