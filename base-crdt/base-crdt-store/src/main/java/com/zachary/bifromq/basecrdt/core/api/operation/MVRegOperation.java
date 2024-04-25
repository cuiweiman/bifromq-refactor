package com.zachary.bifromq.basecrdt.core.api.operation;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/20 17:40
 */
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class MVRegOperation implements ICRDTOperation {
    private static final MVRegOperation RESET = new MVRegOperation(Type.Reset, null);

    public enum Type {
        Write, Reset
    }

    public final Type type;
    public final ByteString value;

    public static MVRegOperation write(ByteString val) {
        return new MVRegOperation(Type.Write, val);
    }


    public static MVRegOperation reset() {
        return RESET;
    }
}
