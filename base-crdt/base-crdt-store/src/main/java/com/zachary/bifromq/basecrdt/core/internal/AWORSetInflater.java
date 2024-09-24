package com.zachary.bifromq.basecrdt.core.internal;


import com.zachary.bifromq.basecrdt.core.api.operation.AWORSetOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/9/23 18:06
 */
class AWORSetInflater extends CausalCRDTInflater<IDotMap, AWORSetOperation, AWORSet> {
    AWORSetInflater(long engineId, Replica replica, IReplicaStateLattice stateLattice,
                    ScheduledExecutorService executor, Duration inflationInterval) {
        super(engineId, replica, stateLattice, executor, inflationInterval);
    }

    @Override
    protected AWORSet newCRDT(Replica replica, IDotMap dotStore,
                              CausalCRDT.CRDTOperationExecutor<AWORSetOperation> executor) {
        return new AWORSet(replica, () -> dotStore, executor);
    }

    /**
     * 开始合并
     *
     * @param op AWORSetOperation
     * @return 合并结果
     */
    @Override
    protected ICoalesceOperation<IDotMap, AWORSetOperation> startCoalescing(AWORSetOperation op) {
        return new AWORSetCoalesceOperation(id().getId(), op);
    }

    @Override
    protected Class<? extends IDotMap> dotStoreType() {
        return DotMap.class;
    }
}
