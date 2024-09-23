package com.zachary.bifromq.basecrdt.core.api;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.operation.MVRegOperation;

import java.util.Iterator;

/**
 * @description: 优化的多值寄存器（新的未发布的数据类型）接口
 * @author: cuiweiman
 * @date: 2024/9/23 17:53
 */
public interface IMVReg extends ICausalCRDT<MVRegOperation> {
    @Override
    default CausalCRDTType type() {
        return CausalCRDTType.mvreg;
    }

    Iterator<ByteString> read();
}
