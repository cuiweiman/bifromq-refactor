
package com.zachary.bifromq.basecrdt.core.internal;

import com.zachary.bifromq.basecrdt.core.api.operation.RWORSetOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

class RWORSetInflater extends CausalCRDTInflater<IDotMap, RWORSetOperation, RWORSet> {
    RWORSetInflater(long id, Replica replica, IReplicaStateLattice stateLattice,
                    ScheduledExecutorService executor, Duration inflationInterval) {
        super(id, replica, stateLattice, executor, inflationInterval);
    }

    @Override
    protected RWORSet newCRDT(Replica replica, IDotMap dotStore,
                              CausalCRDT.CRDTOperationExecutor<RWORSetOperation> executor) {
        return new RWORSet(replica, () -> dotStore, executor);
    }

    @Override
    protected ICoalesceOperation<IDotMap, RWORSetOperation> startCoalescing(RWORSetOperation op) {
        return new RWORSetCoalesceOperation(id().getId(), op);
    }

    @Override
    protected Class<? extends IDotMap> dotStoreType() {
        return DotMap.class;
    }
}
