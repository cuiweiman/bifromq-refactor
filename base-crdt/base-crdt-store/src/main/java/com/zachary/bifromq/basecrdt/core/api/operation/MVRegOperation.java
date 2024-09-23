package com.zachary.bifromq.basecrdt.core.api.operation;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * 优化的多值寄存器（新的未发布的数据类型）
 *
 * @description: An optimized multi-value register (new unpublished datatype)
 * @author: cuiweiman
 * @date: 2024/4/20 17:40
 */
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class MVRegOperation implements ICRDTOperation {
    private static final MVRegOperation RESET = new MVRegOperation(Type.Reset, null);

    public enum Type {
        /**
         * 写操作、重置操作
         */
        Write, Reset
    }

    public final Type type;
    public final ByteString value;

    /**
     * 写操作
     *
     * @param val 写的内容
     * @return result
     */
    public static MVRegOperation write(ByteString val) {
        return new MVRegOperation(Type.Write, val);
    }


    public static MVRegOperation reset() {
        return RESET;
    }
}
