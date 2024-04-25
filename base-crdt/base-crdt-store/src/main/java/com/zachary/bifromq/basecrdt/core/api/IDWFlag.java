package com.zachary.bifromq.basecrdt.core.api;

import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.operation.DWFlagOperation;

public interface IDWFlag extends ICausalCRDT<DWFlagOperation> {
    @Override
    default CausalCRDTType type() {
        return CausalCRDTType.dwflag;
    }

    boolean read();
}
