package com.zachary.bifromq.basecrdt.core.api;

import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.operation.CCounterOperation;

public interface ICCounter extends ICausalCRDT<CCounterOperation> {
    @Override
    default CausalCRDTType type() {
        return CausalCRDTType.cctr;
    }

    /**
     * The final count includes all partial counts
     *
     * @return
     */
    long read();
}
