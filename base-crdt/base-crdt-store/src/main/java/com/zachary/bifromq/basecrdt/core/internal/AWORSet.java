
package com.zachary.bifromq.basecrdt.core.internal;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.IAWORSet;
import com.zachary.bifromq.basecrdt.core.api.operation.AWORSetOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;

import java.util.Iterator;

/**
 * @description: 添加获胜优化的观察删除集，允许添加和删除
 * @author: cuiweiman
 * @date: 2024/9/23 17:16
 */
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
