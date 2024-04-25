
package com.zachary.bifromq.basecrdt.core.internal;


import com.zachary.bifromq.basecrdt.core.api.operation.CCounterOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

class CCounterInflater extends CausalCRDTInflater<IDotMap, CCounterOperation, CCounter> {
    CCounterInflater(long id, Replica replica, IReplicaStateLattice stateLattice,
                     ScheduledExecutorService executor, Duration inflationInterval) {
        super(id, replica, stateLattice, executor, inflationInterval);
    }

    @Override
    protected CCounter newCRDT(Replica replica, IDotMap dotStore,
                               CausalCRDT.CRDTOperationExecutor<CCounterOperation> executor) {
        return new CCounter(replica, () -> dotStore, executor);
    }

    @Override
    protected ICoalesceOperation<IDotMap, CCounterOperation> startCoalescing(CCounterOperation op) {
        return new CCounterCoalesceOperation(id().getId(), op);
    }

    @Override
    protected Class<? extends IDotMap> dotStoreType() {
        return DotMap.class;
    }
}
