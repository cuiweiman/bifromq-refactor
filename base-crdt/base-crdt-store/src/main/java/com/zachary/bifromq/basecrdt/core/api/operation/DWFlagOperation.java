package com.zachary.bifromq.basecrdt.core.api.operation;

import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;


/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/20 17:39
 */
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class DWFlagOperation implements ICRDTOperation {
    private static final DWFlagOperation DISABLE = new DWFlagOperation(Type.Disable);
    private static final DWFlagOperation ENABLE = new DWFlagOperation(Type.Enable);

    public enum Type {
        Disable, Enable
    }

    public final Type type;

    public static DWFlagOperation disable() {
        return DISABLE;
    }

    public static DWFlagOperation enable() {
        return ENABLE;
    }
}
