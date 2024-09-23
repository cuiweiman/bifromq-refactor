
package com.zachary.bifromq.basecrdt.core.internal;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;

/**
 * @description: 合并操作
 * @author: cuiweiman
 * @date: 2024/9/23 18:09
 */
abstract class CoalesceOperation<T extends IDotStore, O extends ICRDTOperation> implements ICoalesceOperation<T, O> {

    protected final ByteString replicaId;

    protected CoalesceOperation(ByteString replicaId) {
        this.replicaId = replicaId;
    }
}
