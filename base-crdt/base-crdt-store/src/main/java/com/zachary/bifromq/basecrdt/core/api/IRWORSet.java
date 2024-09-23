package com.zachary.bifromq.basecrdt.core.api;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.operation.RWORSetOperation;

import java.util.Iterator;

/**
 * 添加获胜 观察集 接口
 *
 * @description: 添加获胜优化的观察删除集，允许添加和删除
 * @author: cuiweiman
 * @date: 2024/4/20 17:36
 */
public interface IRWORSet extends ICausalCRDT<RWORSetOperation> {

    /**
     * 缓存变量
     *
     * @return 集合
     */
    Iterator<ByteString> elements();

    /**
     * 是否包含
     *
     * @param element 元素
     * @return result
     */
    boolean contains(ByteString element);


    /**
     * 是否为空
     *
     * @return result
     */
    default boolean isEmpty() {
        return !elements().hasNext();
    }

    /**
     * 返回 操作类型
     *
     * @return 因果CRDT类型 CausalCRDTType.rworset
     */
    @Override
    default CausalCRDTType type() {
        return CausalCRDTType.rworset;
    }

}
