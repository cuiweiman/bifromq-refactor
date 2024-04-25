package com.zachary.bifromq.basecrdt.core.api;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.operation.RWORSetOperation;

import java.util.Iterator;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/20 17:36
 */
public interface IRWORSet extends ICausalCRDT<RWORSetOperation> {

    Iterator<ByteString> elements();

    boolean contains(ByteString element);


    default boolean isEmpty() {
        return !elements().hasNext();
    }

    @Override
    default CausalCRDTType type() {
        return CausalCRDTType.rworset;
    }

}
