package com.zachary.bifromq.basecrdt.core.internal;


import com.zachary.bifromq.basecrdt.core.api.operation.MVRegOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

class MVRegInflater extends CausalCRDTInflater<IDotFunc, MVRegOperation, MVReg> {
    MVRegInflater(long id, Replica replica, IReplicaStateLattice stateLattice,
                  ScheduledExecutorService executor, Duration inflationInterval) {
        super(id, replica, stateLattice, executor, inflationInterval);
    }

    @Override
    protected MVReg newCRDT(Replica replica, IDotFunc dotStore,
                            CausalCRDT.CRDTOperationExecutor<MVRegOperation> executor) {
        return new MVReg(replica, () -> dotStore, executor);
    }

    @Override
    protected ICoalesceOperation<IDotFunc, MVRegOperation> startCoalescing(MVRegOperation op) {
        return new MVRegCoalesceOperation(id().getId(), op);
    }

    @Override
    protected Class<? extends IDotFunc> dotStoreType() {
        return DotFunc.class;
    }
}
