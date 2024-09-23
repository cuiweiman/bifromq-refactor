
package com.zachary.bifromq.basecrdt.core.internal;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.IAWORSet;
import com.zachary.bifromq.basecrdt.core.api.ICCounter;
import com.zachary.bifromq.basecrdt.core.api.IDWFlag;
import com.zachary.bifromq.basecrdt.core.api.IEWFlag;
import com.zachary.bifromq.basecrdt.core.api.IMVReg;
import com.zachary.bifromq.basecrdt.core.api.IORMap;
import com.zachary.bifromq.basecrdt.core.api.IRWORSet;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.operation.ORMapOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;
import com.zachary.bifromq.basecrdt.proto.StateLattice;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.aworset;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.cctr;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.dwflag;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.ewflag;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.mvreg;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.ormap;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.rworset;


/**
 * @description: 因果 CRDT 的键映射
 * @author: cuiweiman
 * @date: 2024/9/23 18:11
 */
@Slf4j
class ORMap extends CausalCRDT<IDotMap, ORMapOperation> implements IORMap {
    private final Cache<ByteString, CausalCRDT> subCRDTMap = Caffeine.newBuilder().weakValues().build();


    ORMap(Replica replica, DotStoreAccessor<IDotMap> dotStoreAccessor,
          CRDTOperationExecutor<ORMapOperation> executor) {
        super(replica, dotStoreAccessor, executor);
    }

    @Override
    public Iterator<ORMapKey> keys() {
        IDotMap dotMap = dotStoreAccessor.fetch();
        return new AbstractIterator<>() {
            private final Iterator<ByteString> keyItr = Iterators
                    .concat(dotMap.dotSetKeys(), dotMap.dotFuncKeys(), dotMap.dotMapKeys());

            @Override
            protected ORMapKey computeNext() {
                if (keyItr.hasNext()) {
                    ByteString typedKey = keyItr.next();
                    return new ORMapKey() {
                        @Override
                        public ByteString key() {
                            return ORMapUtil.parseKey(typedKey);
                        }

                        @Override
                        public CausalCRDTType valueType() {
                            return ORMapUtil.getType(typedKey);
                        }
                    };
                }
                return endOfData();
            }
        };
    }

    @Override
    public IAWORSet getAWORSet(ByteString... keys) {
        if (keys.length == 0) {
            throw new IllegalArgumentException("No key specified");
        }
        if (keys.length == 1) {
            return (IAWORSet) subCRDTMap.get(ORMapUtil.typedKey(keys[0], aworset), key ->
                    new AWORSet(replica, fetchSubDotMap(key), op -> execute(ORMapOperation.update(keys).with(op))));
        } else {
            return getORMap(keys[0]).getAWORSet(Arrays.copyOfRange(keys, 1, keys.length));
        }
    }

    @Override
    public IRWORSet getRWORSet(ByteString... keys) {
        if (keys.length == 0) {
            throw new IllegalArgumentException("No key specified");
        }
        if (keys.length == 1) {
            return (IRWORSet) subCRDTMap.get(ORMapUtil.typedKey(keys[0], rworset), key ->
                    new RWORSet(replica, fetchSubDotMap(key), op -> execute(ORMapOperation.update(keys).with(op))));
        } else {
            return getORMap(keys[0]).getRWORSet(Arrays.copyOfRange(keys, 1, keys.length));
        }
    }

    @Override
    public ICCounter getCCounter(ByteString... keys) {
        if (keys.length == 0) {
            throw new IllegalArgumentException("No key specified");
        }
        if (keys.length == 1) {
            return (ICCounter) subCRDTMap.get(ORMapUtil.typedKey(keys[0], cctr), key ->
                    new CCounter(replica, fetchSubDotMap(key), op -> execute(ORMapOperation.update(keys).with(op))));
        } else {
            return getORMap(keys[0]).getCCounter(Arrays.copyOfRange(keys, 1, keys.length));
        }
    }

    @Override
    public IMVReg getMVReg(ByteString... keys) {
        if (keys.length == 0) {
            throw new IllegalArgumentException("No key specified");
        }
        if (keys.length == 1) {
            return (IMVReg) subCRDTMap.get(ORMapUtil.typedKey(keys[0], mvreg), key ->
                    new MVReg(replica, fetchSubDotFunc(key), op -> execute(ORMapOperation.update(keys).with(op))));
        } else {
            return getORMap(keys[0]).getMVReg(Arrays.copyOfRange(keys, 1, keys.length));
        }
    }

    @Override
    public IDWFlag getDWFlag(ByteString... keys) {
        if (keys.length == 0) {
            throw new IllegalArgumentException("No key specified");
        }
        if (keys.length == 1) {
            return (IDWFlag) subCRDTMap.get(ORMapUtil.typedKey(keys[0], dwflag), key ->
                    new DWFlag(replica, fetchSubDotSet(key), op -> execute(ORMapOperation.update(keys).with(op))));
        } else {
            return getORMap(keys[0]).getDWFlag(Arrays.copyOfRange(keys, 1, keys.length));
        }
    }

    @Override
    public IEWFlag getEWFlag(ByteString... keys) {
        if (keys.length == 0) {
            throw new IllegalArgumentException("No key specified");
        }
        if (keys.length == 1) {
            return (IEWFlag) subCRDTMap.get(ORMapUtil.typedKey(keys[0], ewflag), key ->
                    new EWFlag(replica, fetchSubDotSet(key), op -> execute(ORMapOperation.update(keys).with(op))));
        } else {
            return getORMap(keys[0]).getEWFlag(Arrays.copyOfRange(keys, 1, keys.length));
        }
    }

    @Override
    public IORMap getORMap(ByteString... keys) {
        if (keys.length == 0) {
            throw new IllegalArgumentException("No key specified");
        }
        if (keys.length == 1) {
            return (IORMap) subCRDTMap.get(ORMapUtil.typedKey(keys[0], ormap), key ->
                    new ORMap(replica, fetchSubDotMap(key), op -> execute(ORMapOperation.update(keys).with(op))));
        } else {
            return getORMap(keys[0]).getORMap(Arrays.copyOfRange(keys, 1, keys.length));
        }
    }

    @Override
    protected void handleInflation(Iterable<StateLattice> adds, Iterable<StateLattice> rems) {
        Map<ByteString, List<List<StateLattice>>> eventsByKey = Maps.newHashMap();
        for (StateLattice stateLattice : adds) {
            assert stateLattice.getStateTypeCase() == StateLattice.StateTypeCase.SINGLEMAP;
            ByteString key = stateLattice.getSingleMap().getKey();
            StateLattice val = stateLattice.getSingleMap().getVal();
            eventsByKey.computeIfAbsent(key, k -> Lists.newArrayList(Lists.newArrayList(), Lists.newArrayList()));
            eventsByKey.get(key).get(0).add(val);
        }
        for (StateLattice stateLattice : rems) {
            assert stateLattice.getStateTypeCase() == StateLattice.StateTypeCase.SINGLEMAP;
            ByteString key = stateLattice.getSingleMap().getKey();
            StateLattice val = stateLattice.getSingleMap().getVal();
            eventsByKey.computeIfAbsent(key, k -> Lists.newArrayList(Lists.newArrayList(), Lists.newArrayList()));
            eventsByKey.get(key).get(1).add(val);
        }
        eventsByKey.forEach((k, v) -> {
            CausalCRDT subCRDT = subCRDTMap.getIfPresent(k);
            if (subCRDT != null) {
                subCRDT.afterInflation(v.get(0), v.get(1));
            }
        });
    }

    private DotStoreAccessor<IDotSet> fetchSubDotSet(ByteString key) {
        AtomicReference<IDotSet> ref = new AtomicReference<>();
        return () -> ref.updateAndGet(v -> {
            if (v == null || v.isBottom()) {
                v = dotStoreAccessor.fetch().subDotSet(key).orElse(DotSet.BOTTOM);
            }
            return v;
        });
    }

    private DotStoreAccessor<IDotFunc> fetchSubDotFunc(ByteString key) {
        AtomicReference<IDotFunc> ref = new AtomicReference<>();
        return () -> ref.updateAndGet(v -> {
            if (v == null || v.isBottom()) {
                v = dotStoreAccessor.fetch().subDotFunc(key).orElse(DotFunc.BOTTOM);
            }
            return v;
        });
    }

    private DotStoreAccessor<IDotMap> fetchSubDotMap(ByteString key) {
        AtomicReference<IDotMap> ref = new AtomicReference<>();
        return () -> ref.updateAndGet(v -> {
            if (v == null || v.isBottom()) {
                v = dotStoreAccessor.fetch().subDotMap(key).orElse(DotMap.BOTTOM);
            }
            return v;
        });
    }
}
