package com.zachary.bifromq.basekv.store;

import com.zachary.bifromq.basekv.proto.StoreMessage;
import io.reactivex.rxjava3.core.Observable;

public interface IStoreMessenger {
    void send(StoreMessage message);

    Observable<StoreMessage> receive();

    void close();
}
