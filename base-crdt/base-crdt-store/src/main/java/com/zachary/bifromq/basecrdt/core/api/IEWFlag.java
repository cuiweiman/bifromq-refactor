

package com.zachary.bifromq.basecrdt.core.api;

import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.operation.EWFlagOperation;

/**
 * @description: 带有启用/禁用的标志。启用胜利
 * @author: cuiweiman
 * @date: 2024/9/23 17:56
 */
public interface IEWFlag extends ICausalCRDT<EWFlagOperation> {
    @Override
    default CausalCRDTType type() {
        return CausalCRDTType.ewflag;
    }

    boolean read();
}
