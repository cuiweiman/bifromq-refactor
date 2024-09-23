package com.zachary.bifromq.basecrdt.core.api;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.operation.ORMapOperation;

import java.util.Iterator;

/**
 * @description: 因果 CRDT 的键映射 接口
 * @author: cuiweiman
 * @date: 2024/4/20 17:37
 */
public interface IORMap extends ICausalCRDT<ORMapOperation>{

    interface ORMapKey {
        ByteString key();

        CausalCRDTType valueType();
    }

    @Override
    default CausalCRDTType type() {
        return CausalCRDTType.ormap;
    }

    /**
     * Non-bottom keys
     *
     * @return
     */
    Iterator<ORMapKey> keys();

    IAWORSet getAWORSet(ByteString... keys);

    IRWORSet getRWORSet(ByteString... keys);

    ICCounter getCCounter(ByteString... keys);

    IMVReg getMVReg(ByteString... keys);

    IDWFlag getDWFlag(ByteString... keys);

    IEWFlag getEWFlag(ByteString... keys);

    IORMap getORMap(ByteString... keys);
}
