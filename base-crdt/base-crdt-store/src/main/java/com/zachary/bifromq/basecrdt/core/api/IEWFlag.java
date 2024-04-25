

package com.zachary.bifromq.basecrdt.core.api;

import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.operation.EWFlagOperation;

public interface IEWFlag extends ICausalCRDT<EWFlagOperation> {
    @Override
    default CausalCRDTType type() {
        return CausalCRDTType.ewflag;
    }

    boolean read();
}
