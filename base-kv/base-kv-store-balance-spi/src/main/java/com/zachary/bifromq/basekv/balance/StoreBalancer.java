package com.zachary.bifromq.basekv.balance;

import com.zachary.bifromq.basekv.balance.command.BalanceCommand;
import com.zachary.bifromq.basekv.proto.KVRangeStoreDescriptor;

import java.util.Optional;
import java.util.Set;

public abstract class StoreBalancer {
    protected final String localStoreId;

    public StoreBalancer(String localStoreId) {
        this.localStoreId = localStoreId;
    }

    public abstract void update(Set<KVRangeStoreDescriptor> storeDescriptors);

    public abstract Optional<BalanceCommand> balance();

}
