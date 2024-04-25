package com.zachary.bifromq.basecrdt.core.api.operation;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * @description:
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

    public static RWORSetOperation add(ByteString e) {
        return new RWORSetOperation(Type.Add, e);
    }

    public static RWORSetOperation remove(ByteString e) {
        return new RWORSetOperation(Type.Remove, e);
    }

    public static RWORSetOperation clear() {
        return CLEAR;
    }


}
