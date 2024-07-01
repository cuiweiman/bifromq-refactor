package com.zachary.bifromq.basekv.store.range;

import com.zachary.bifromq.basekv.proto.KVRangeMessage;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public interface IKVRangeMessenger {
    void send(KVRangeMessage message);

    Observable<KVRangeMessage> receive();

    CompletableFuture<KVRangeMessage> once(Predicate<KVRangeMessage> condition);
}
