package com.zachary.bifromq.dist.worker.balance;

import com.zachary.bifromq.basekv.balance.IStoreBalancerFactory;
import com.zachary.bifromq.basekv.balance.StoreBalancer;
import com.zachary.bifromq.basekv.balance.impl.RecoveryBalancer;

import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.DIST_WORKER_RECOVERY_TIMEOUT_MILLIS;


public class RecoveryBalancerFactory implements IStoreBalancerFactory {

    @Override
    public StoreBalancer newBalancer(String localStoreId) {
        return new RecoveryBalancer(localStoreId, DIST_WORKER_RECOVERY_TIMEOUT_MILLIS.get());
    }
}
