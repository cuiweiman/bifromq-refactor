package com.zachary.bifromq.basecrdt.core.api.operation;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * 删除获胜优化的观察删除集，允许添加和删除
 *
 * @description: A remove-wins optimized observed-remove set that allows adds and removes
 * @author: cuiweiman
 * @date: 2024/4/20 17:18
 */
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RWORSetOperation implements ICRDTOperation {

    public enum Type {
        /**
         * 新增，移除，清空
         */
        Add, Remove, Clear
    }

    private static final RWORSetOperation CLEAR = new RWORSetOperation(Type.Clear, ByteString.EMPTY);

    public final Type type;
    public final ByteString element;

    /**
     * 新增操作
     *
     * @param e 新增内容
     * @return result
     */
    public static RWORSetOperation add(ByteString e) {
        return new RWORSetOperation(Type.Add, e);
    }

    /**
     * 移除操作
     *
     * @param e 移除内容
     * @return result
     */
    public static RWORSetOperation remove(ByteString e) {
        return new RWORSetOperation(Type.Remove, e);
    }

    /**
     * 清空操作
     *
     * @return result
     */
    public static RWORSetOperation clear() {
        return CLEAR;
    }


}
