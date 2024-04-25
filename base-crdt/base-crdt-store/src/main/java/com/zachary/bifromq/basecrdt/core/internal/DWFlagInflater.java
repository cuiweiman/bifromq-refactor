
package com.zachary.bifromq.basecrdt.core.internal;

import com.zachary.bifromq.basecrdt.core.api.IDWFlag;
import com.zachary.bifromq.basecrdt.core.api.operation.DWFlagOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

class DWFlagInflater extends CausalCRDTInflater<IDotSet, DWFlagOperation, IDWFlag> {
    DWFlagInflater(long id, Replica replica, IReplicaStateLattice stateLattice,
                   ScheduledExecutorService executor, Duration inflationInterval) {
        super(id, replica, stateLattice, executor, inflationInterval);
    }

    @Override
    protected IDWFlag newCRDT(Replica replica, IDotSet dotStore,
                              CausalCRDT.CRDTOperationExecutor<DWFlagOperation> executor) {
        return new DWFlag(replica, () -> dotStore, executor);
    }

    @Override
    protected ICoalesceOperation<IDotSet, DWFlagOperation> startCoalescing(DWFlagOperation op) {
        return new DWFlagCoalesceOperation(id().getId(), op);
    }

    @Override
    protected Class<? extends IDotSet> dotStoreType() {
        return DotSet.class;
    }
}
