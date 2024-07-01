package com.zachary.bifromq.basekv.store;

import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.KVRangeMessage;
import com.zachary.bifromq.basekv.proto.StoreMessage;
import com.zachary.bifromq.basekv.store.exception.KVRangeException;
import com.zachary.bifromq.basekv.store.range.IKVRangeMessenger;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class KVRangeMessenger implements IKVRangeMessenger {
    private final String id;
    private final KVRangeId rangeId;
    private final IStoreMessenger messenger;

    public KVRangeMessenger(String id, KVRangeId rangeId, IStoreMessenger messenger) {
        this.id = id;
        this.rangeId = rangeId;
        this.messenger = messenger;
    }

    @Override
    public void send(KVRangeMessage message) {
        messenger.send(StoreMessage.newBuilder()
            .setFrom(id)
            .setSrcRange(rangeId)
            .setPayload(message)
            .build());
    }

    @Override
    public Observable<KVRangeMessage> receive() {
        return messenger.receive().mapOptional(storeMessage -> {
            assert storeMessage.getFrom() != null;
            assert storeMessage.hasSrcRange();
            KVRangeMessage payload = storeMessage.getPayload();
            if (!payload.getHostStoreId().equals(id) || !payload.getRangeId().equals(rangeId)) {
                return Optional.empty();
            }
            // swap the origin
            return Optional.of(payload.toBuilder()
                .setRangeId(storeMessage.getSrcRange())
                .setHostStoreId(storeMessage.getFrom())
                .build());
        });
    }

    @Override
    public CompletableFuture<KVRangeMessage> once(Predicate<KVRangeMessage> condition) {
        CompletableFuture<KVRangeMessage> onDone = new CompletableFuture<>();
        Disposable disposable = receive()
            .mapOptional(msg -> {
                if (condition.test(msg)) {
                    return Optional.of(msg);
                }
                return Optional.empty();
            })
            .firstElement()
            .subscribe(onDone::complete,
                e -> onDone.completeExceptionally(new KVRangeException.TryLater("Once test canceled", e)),
                () -> {
                    if (!onDone.isDone()) {
                        onDone.completeExceptionally(new KVRangeException.TryLater("Try again"));
                    }
                });

        onDone.whenComplete((v, e) -> disposable.dispose());
        return onDone;
    }
}
