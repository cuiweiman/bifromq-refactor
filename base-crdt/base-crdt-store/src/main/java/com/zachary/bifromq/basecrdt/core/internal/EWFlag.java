
package com.zachary.bifromq.basecrdt.core.internal;


import com.zachary.bifromq.basecrdt.core.api.IEWFlag;
import com.zachary.bifromq.basecrdt.core.api.operation.EWFlagOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;
import com.zachary.bifromq.basecrdt.proto.StateLattice;

/**
 * @description: 带有启用/禁用的标志。启用胜利
 * @author: cuiweiman
 * @date: 2024/9/23 18:10
 */
class EWFlag extends CausalCRDT<IDotSet, EWFlagOperation> implements IEWFlag {
    private volatile boolean flag;

    EWFlag(Replica replica, DotStoreAccessor<IDotSet> dotStoreAccessor,
           CRDTOperationExecutor<EWFlagOperation> executor) {
        super(replica, dotStoreAccessor, executor);
        refresh();
    }

    @Override
    public boolean read() {
        return flag;
    }

    @Override
    protected void handleInflation(Iterable<StateLattice> addEvents, Iterable<StateLattice> removeEvents) {
        refresh();
    }

    private void refresh() {
        flag = !dotStoreAccessor.fetch().isBottom();
    }
}
