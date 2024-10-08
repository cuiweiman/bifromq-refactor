package com.zachary.bifromq.basecrdt.core.api;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.operation.AWORSetOperation;

import java.util.Iterator;

/**
 * @description: 添加获胜优化的观察删除集，允许添加和删除
 * @author: cuiweiman
 * @date: 2024/4/20 17:48
 */
public interface IAWORSet extends ICausalCRDT<AWORSetOperation> {

    default CausalCRDTType type() {
        return CausalCRDTType.aworset;
    }

    default boolean isEmpty() {
        return !elements().hasNext();
    }

    boolean contains(ByteString element);

    Iterator<ByteString> elements();
}
