package com.zachary.bifromq.basecrdt.core.internal;


import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;
import com.zachary.bifromq.basecrdt.proto.Replacement;

interface ICoalesceOperation<T extends IDotStore, O extends ICRDTOperation> {
    interface IEventGenerator {
        long nextEvent();
    }

    void coalesce(O op);

    Iterable<Replacement> delta(T current, IEventGenerator eventGenerator);
}
