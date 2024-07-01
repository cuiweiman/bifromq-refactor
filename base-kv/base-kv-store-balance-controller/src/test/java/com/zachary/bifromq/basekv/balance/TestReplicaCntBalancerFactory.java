package com.zachary.bifromq.basekv.balance;

import com.zachary.bifromq.basekv.balance.impl.ReplicaCntBalancer;

public class TestReplicaCntBalancerFactory implements IStoreBalancerFactory {

    @Override
    public StoreBalancer newBalancer(String localStoreId) {
        return new ReplicaCntBalancer(localStoreId, 3, 3);
    }
}
