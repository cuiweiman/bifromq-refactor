package com.zachary.bifromq.basecrdt.core.api;

import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.operation.CCounterOperation;

/**
 * @description: 计数器
 * @author: cuiweiman
 * @date: 2024/9/23 17:49
 */
public interface ICCounter extends ICausalCRDT<CCounterOperation> {
    @Override
    default CausalCRDTType type() {
        return CausalCRDTType.cctr;
    }

    /**
     * The final count includes all partial counts
     * 最终计数包括所有部分计数
     *
     * @return result
     */
    long read();
}
