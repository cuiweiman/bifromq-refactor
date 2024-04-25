
package com.zachary.bifromq.basecrdt.core.internal;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.IAWORSet;
import com.zachary.bifromq.basecrdt.core.api.operation.AWORSetOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;

import java.util.Iterator;

class AWORSet extends CausalCRDT<IDotMap, AWORSetOperation> implements IAWORSet {

    AWORSet(Replica replica, DotStoreAccessor<IDotMap> dotStoreAccessor,
            CRDTOperationExecutor<AWORSetOperation> executor) {
        super(replica, dotStoreAccessor, executor);
    }

    @Override
    public boolean contains(ByteString element) {
        return dotStoreAccessor.fetch().subDotSet(element).isPresent();
    }

    @Override
    public Iterator<ByteString> elements() {
        return dotStoreAccessor.fetch().dotSetKeys();
    }
}
