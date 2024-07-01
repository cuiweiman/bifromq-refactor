package com.zachary.bifromq.basekv.balance;

import com.zachary.bifromq.basekv.balance.impl.RecoveryBalancer;

public class TestRecoveryBalancerFactory implements IStoreBalancerFactory {

    @Override
    public StoreBalancer newBalancer(String localStoreId) {
        return new RecoveryBalancer(localStoreId, 1000L);
    }
}
