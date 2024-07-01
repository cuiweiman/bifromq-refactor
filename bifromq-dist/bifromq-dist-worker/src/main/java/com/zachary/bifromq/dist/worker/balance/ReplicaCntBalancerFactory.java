package com.zachary.bifromq.dist.worker.balance;

import com.zachary.bifromq.basekv.balance.IStoreBalancerFactory;
import com.zachary.bifromq.basekv.balance.StoreBalancer;
import com.zachary.bifromq.basekv.balance.impl.ReplicaCntBalancer;

import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.DIST_WORKER_LEARNER_COUNT;
import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.DIST_WORKER_VOTER_COUNT;

public class ReplicaCntBalancerFactory implements IStoreBalancerFactory {

    @Override
    public StoreBalancer newBalancer(String localStoreId) {
        return new ReplicaCntBalancer(localStoreId, DIST_WORKER_VOTER_COUNT.get(), DIST_WORKER_LEARNER_COUNT.get());
    }
}
