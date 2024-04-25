
package com.zachary.bifromq.basecrdt.core.internal;


import com.zachary.bifromq.basecrdt.core.api.operation.ORMapOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

class ORMapInflater extends CausalCRDTInflater<IDotMap, ORMapOperation, ORMap> {
    ORMapInflater(long id, Replica replica, IReplicaStateLattice stateLattice,
                  ScheduledExecutorService executor, Duration inflationInterval) {
        super(id, replica, stateLattice, executor, inflationInterval);
    }

    @Override
    protected ORMap newCRDT(Replica replica, IDotMap dotStore,
                            CausalCRDT.CRDTOperationExecutor<ORMapOperation> executor) {
        return new ORMap(replica, () -> dotStore, executor);
    }

    @Override
    protected ICoalesceOperation<IDotMap, ORMapOperation> startCoalescing(ORMapOperation op) {
        return new ORMapCoalesceOperation(id().getId(), op);
    }

    @Override
    protected Class<? extends IDotMap> dotStoreType() {
        return DotMap.class;
    }
}
