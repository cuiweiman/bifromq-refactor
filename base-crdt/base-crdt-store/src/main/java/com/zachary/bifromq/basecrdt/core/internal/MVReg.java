package com.zachary.bifromq.basecrdt.core.internal;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.IMVReg;
import com.zachary.bifromq.basecrdt.core.api.operation.MVRegOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;

import java.util.Iterator;

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
