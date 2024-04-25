package com.zachary.bifromq.basecrdt.core.internal;

import com.google.common.collect.AbstractIterator;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.IRWORSet;
import com.zachary.bifromq.basecrdt.core.api.operation.RWORSetOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;

import java.util.Iterator;

class RWORSet extends CausalCRDT<IDotMap, RWORSetOperation> implements IRWORSet {
    public static final ByteString TRUE = ByteString.copyFrom(new byte[]{(byte) 0xFF});
    public static final ByteString FALSE = ByteString.copyFrom(new byte[]{(byte) 0x00});


    RWORSet(Replica replica, DotStoreAccessor<IDotMap> dotStoreAccessor,
            CRDTOperationExecutor<RWORSetOperation> executor) {
        super(replica, dotStoreAccessor, executor);
    }

    @Override
    public boolean contains(ByteString element) {
        IDotMap dotStore = dotStoreAccessor.fetch();
        IDotMap valueDotMap = dotStore.subDotMap(element).orElse(DotMap.BOTTOM);
        if (valueDotMap.subDotSet(FALSE).orElse(DotSet.BOTTOM).isBottom()) {
            return !valueDotMap.subDotSet(TRUE).orElse(DotSet.BOTTOM).isBottom();
        }
        return false;
    }

    @Override
    public Iterator<ByteString> elements() {
        return new AbstractIterator<>() {
            private final Iterator<ByteString> dotMapKeys = dotStoreAccessor.fetch().dotMapKeys();

            @Override
            protected ByteString computeNext() {
                if (dotMapKeys.hasNext()) {
                    ByteString element = dotMapKeys.next();
                    if (contains(element)) {
                        return element;
                    } else {
                        return computeNext();
                    }
                }
                return endOfData();
            }
        };
    }
}
