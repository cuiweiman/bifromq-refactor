package com.zachary.bifromq.basecrdt.core.internal;


import com.google.common.collect.Maps;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.ICCounter;
import com.zachary.bifromq.basecrdt.core.api.operation.CCounterOperation;
import com.zachary.bifromq.basecrdt.proto.Dot;
import com.zachary.bifromq.basecrdt.proto.Replica;
import com.zachary.bifromq.basecrdt.proto.StateLattice;

import java.util.Iterator;
import java.util.Map;

/**
 * Causal Counter 因果计数器
 *
 * @description: 用于映射嵌入的（因果）计数器（对 Riak EMCounter 的优化）
 * @author: cuiweiman
 * @date: 2024/9/23 18:08
 */
class CCounter extends CausalCRDT<IDotMap, CCounterOperation> implements ICCounter {
    private volatile long read = 0;

    CCounter(Replica replica, DotStoreAccessor<IDotMap> dotStoreAccessor,
             CRDTOperationExecutor<CCounterOperation> executor) {
        super(replica, dotStoreAccessor, executor);
        refresh();
    }

    @Override
    public long read() {
        return read;
    }

    @Override
    protected void handleInflation(Iterable<StateLattice> addEvents, Iterable<StateLattice> removeEvents) {
        refresh();
    }

    private void refresh() {
        long total = 0;
        IDotMap dotMap = dotStoreAccessor.fetch();
        Iterator<ByteString> dotFuncKeys = dotMap.dotFuncKeys();
        while (dotFuncKeys.hasNext()) {
            ByteString dotFuncKey = dotFuncKeys.next();
            IDotFunc dotFunc = dotMap.subDotFunc(dotFuncKey).orElse(DotFunc.BOTTOM);
            Map<ByteString, Dot> dots = Maps.newHashMap();
            for (Dot dot : dotFunc) {
                if (!dots.containsKey(dot.getReplicaId())) {
                    total += Varint.decodeLong(dotFunc.value(dot).get());
                    dots.put(dot.getReplicaId(), dot);
                } else if (dots.get(dot.getReplicaId()).getVer() < dot.getVer()) {
                    total -= Varint.decodeLong(dotFunc.value(dots.get(dot.getReplicaId())).get());
                    total += Varint.decodeLong(dotFunc.value(dot).get());
                    dots.put(dot.getReplicaId(), dot);
                }
            }
        }
        read = total;
    }
}
