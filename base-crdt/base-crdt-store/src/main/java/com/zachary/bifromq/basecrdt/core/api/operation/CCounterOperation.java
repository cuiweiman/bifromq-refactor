package com.zachary.bifromq.basecrdt.core.api.operation;


import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/20 17:41
 */
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class CCounterOperation implements ICRDTOperation {
    public enum Type {
        Add, Preset
    }

    public final Type type;
    public final ByteString replicaId;
    public final long c;

    public static CCounterOperation add(long inc) {
        return new CCounterOperation(Type.Add, null, inc);
    }

    public static CCounterOperation zeroOut() {
        return zeroOut(null);
    }

    public static CCounterOperation zeroOut(ByteString replicaId) {
        return new CCounterOperation(Type.Preset, replicaId, 0);
    }

    public static CCounterOperation preset(long value) {
        return new CCounterOperation(Type.Preset, null, value);
    }
}
