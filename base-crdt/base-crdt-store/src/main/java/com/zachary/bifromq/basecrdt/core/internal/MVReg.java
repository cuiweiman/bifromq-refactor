package com.zachary.bifromq.basecrdt.core.internal;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.IMVReg;
import com.zachary.bifromq.basecrdt.core.api.operation.MVRegOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;

import java.util.Iterator;

/**
 * @description: 优化的多值寄存器
 * @author: cuiweiman
 * @date: 2024/9/23 18:11
 */
class MVReg extends CausalCRDT<IDotFunc, MVRegOperation> implements IMVReg {
    MVReg(Replica replica, DotStoreAccessor<IDotFunc> dotStoreAccessor,
          CRDTOperationExecutor<MVRegOperation> executor) {
        super(replica, dotStoreAccessor, executor);
    }

    @Override
    public Iterator<ByteString> read() {
        return dotStoreAccessor.fetch().values().iterator();
    }
}
