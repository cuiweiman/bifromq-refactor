package com.zachary.bifromq.basecrdt.core.api;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.operation.MVRegOperation;

import java.util.Iterator;

public interface IMVReg extends ICausalCRDT<MVRegOperation> {
    @Override
    default CausalCRDTType type() {
        return CausalCRDTType.mvreg;
    }

    Iterator<ByteString> read();
}
