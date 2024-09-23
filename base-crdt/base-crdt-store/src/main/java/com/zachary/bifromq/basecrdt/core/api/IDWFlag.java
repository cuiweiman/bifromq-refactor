package com.zachary.bifromq.basecrdt.core.api;

import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.operation.DWFlagOperation;

/**
 * @description: 带有启用/禁用的标志。禁用胜利
 * @author: cuiweiman
 * @date: 2024/9/23 17:57
 */
public interface IDWFlag extends ICausalCRDT<DWFlagOperation> {
    @Override
    default CausalCRDTType type() {
        return CausalCRDTType.dwflag;
    }

    boolean read();
}
