

package com.zachary.bifromq.basecrdt.core.internal;


import com.zachary.bifromq.basecrdt.core.api.IEWFlag;
import com.zachary.bifromq.basecrdt.core.api.operation.EWFlagOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

class EWFlagInflater extends CausalCRDTInflater<IDotSet, EWFlagOperation, IEWFlag> {
    EWFlagInflater(long id, Replica replica, IReplicaStateLattice stateLattice,
                   ScheduledExecutorService executor, Duration inflationInterval) {
        super(id, replica, stateLattice, executor, inflationInterval);
    }

    @Override
    protected IEWFlag newCRDT(Replica replica, IDotSet dotStore,
                              CausalCRDT.CRDTOperationExecutor<EWFlagOperation> executor) {
        return new EWFlag(replica, () -> dotStore, executor);
    }

    @Override
    protected ICoalesceOperation<IDotSet, EWFlagOperation> startCoalescing(EWFlagOperation op) {
        return new EWFlagCoalesceOperation(id().getId(), op);
    }

    @Override
    protected Class<? extends IDotSet> dotStoreType() {
        return DotSet.class;
    }
}
