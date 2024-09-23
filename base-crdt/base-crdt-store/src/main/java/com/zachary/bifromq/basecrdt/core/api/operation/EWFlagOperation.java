package com.zachary.bifromq.basecrdt.core.api.operation;

import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * EWFlag：带有启用/禁用的标志。弃用胜利（受到 Riak Flag 启发）
 *
 * @description: EWFlag: Flag with enable/disable. Enable wins (Riak Flag inspired)
 * @author: cuiweiman
 * @date: 2024/4/20 17:40
 */
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class EWFlagOperation implements ICRDTOperation {

    private static final EWFlagOperation DISABLE = new EWFlagOperation(Type.Disable);
    private static final EWFlagOperation ENABLE = new EWFlagOperation(Type.Enable);
    private static final EWFlagOperation RESET = new EWFlagOperation(Type.Reset);

    public enum Type {
        /**
         * 禁用、启用、重置
         */
        Disable, Enable, Reset
    }

    public final Type type;

    public static EWFlagOperation disable() {
        return DISABLE;
    }

    public static EWFlagOperation enable() {
        return ENABLE;
    }

    public static EWFlagOperation reset() {
        return RESET;
    }
}
