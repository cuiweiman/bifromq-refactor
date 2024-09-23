package com.zachary.bifromq.basecrdt.core.api.operation;


import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * 用于映射嵌入的（因果）计数器（对 Riak EMCounter 的优化）
 *
 * @description: A (causal) counter for map embedding (Optimization over Riak EMCounter)
 * @author: cuiweiman
 * @date: 2024/4/20 17:41
 */
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class CCounterOperation implements ICRDTOperation {
    public enum Type {
        /**
         * 新增,预设(预置,预定时间;预先决定;事先安排;)
         */
        Add, Preset
    }

    // 计数器操作类型
    public final Type type;
    // 副本ID
    public final ByteString replicaId;
    // 计数器
    public final long c;

    /**
     * 增加操作
     *
     * @param inc inc
     * @return result
     */
    public static CCounterOperation add(long inc) {
        return new CCounterOperation(Type.Add, null, inc);
    }

    /**
     * 清除操作
     *
     * @return result
     */
    public static CCounterOperation zeroOut() {
        return zeroOut(null);
    }

    /**
     * 清除操作
     *
     * @param replicaId 副本ID
     * @return result
     */
    public static CCounterOperation zeroOut(ByteString replicaId) {
        return new CCounterOperation(Type.Preset, replicaId, 0);
    }

    /**
     * 预设操作
     *
     * @param value value
     * @return result
     */
    public static CCounterOperation preset(long value) {
        return new CCounterOperation(Type.Preset, null, value);
    }
}
