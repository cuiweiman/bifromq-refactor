package com.zachary.bifromq.basecrdt.core.api.operation;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;
import lombok.ToString;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/20 17:38
 */
@ToString
public final class AWORSetOperation implements ICRDTOperation {

    public enum Type {
        Add, Remove, Clear
    }

    private static final AWORSetOperation CLEAR = new AWORSetOperation(Type.Clear, ByteString.EMPTY);

    public final Type type;
    public final ByteString element;

    private AWORSetOperation(Type type, ByteString element) {
        this.type = type;
        this.element = element;
    }

    public static AWORSetOperation add(ByteString e) {
        return new AWORSetOperation(Type.Add, e);
    }

    public static AWORSetOperation remove(ByteString e) {
        return new AWORSetOperation(Type.Remove, e);
    }

    public static AWORSetOperation clear() {
        return CLEAR;
    }


}
