package com.zachary.bifromq.basecrdt.core.api.operation;

import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;


/**
 * 带有启用/禁用的标志。禁用胜利（受到 Riak Flag 启发）
 *
 * @description: Flag with enable/disable. Disable wins (Riak Flag inspired)
 * @author: cuiweiman
 * @date: 2024/4/20 17:39
 */
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class DWFlagOperation implements ICRDTOperation {
    private static final DWFlagOperation DISABLE = new DWFlagOperation(Type.Disable);
    private static final DWFlagOperation ENABLE = new DWFlagOperation(Type.Enable);

    public enum Type {
        /**
         * 禁用、启用 操作类型
         */
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
