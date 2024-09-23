package com.zachary.bifromq.basecrdt.core.api.operation;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import lombok.ToString;

/**
 * 因果 CRDT 的键映射。
 *
 * @description: Map of keys to causal CRDTs. (spec in common with the Riak Map)
 * @author: cuiweiman
 * @date: 2024/9/23 16:55
 */
@ToString
public abstract class ORMapOperation implements ICRDTOperation {
    /**
     * 更新操作接口
     */
    public interface ORMapUpdater {
        ORMapUpdate with(AWORSetOperation op);

        ORMapUpdate with(RWORSetOperation op);

        ORMapUpdate with(DWFlagOperation op);

        ORMapUpdate with(EWFlagOperation op);

        ORMapUpdate with(MVRegOperation op);

        ORMapUpdate with(CCounterOperation op);

        ORMapUpdate with(ORMapOperation op);
    }

    /**
     * 移除操作接口
     */
    public interface ORMapRemover {
        ORMapRemove of(CausalCRDTType valueType);
    }

    /**
     * 操作类型
     */
    public enum Type {
        /**
         * 更新、移除、清空
         */
        UpdateKey,
        RemoveKey,
        Clear
    }

    public final Type type;
    public final ByteString[] keyPath;

    ORMapOperation(Type type, ByteString[] keyPath) {
        this.type = type;
        this.keyPath = keyPath;
    }

    /**
     * 移除操作接口 方法实现
     */
    public static ORMapRemover remove(ByteString... keyPath) {
        return new ORMapRemover() {
            @Override
            public ORMapRemove of(CausalCRDTType valueType) {
                return new ORMapRemove(Type.RemoveKey, keyPath, valueType);
            }
        };
    }

    /**
     * 更新操作接口 方法实现
     */
    public static ORMapUpdater update(ByteString... keyPath) {
        return new ORMapUpdater() {
            @Override
            public ORMapUpdate with(AWORSetOperation op) {
                return new ORMapUpdate(Type.UpdateKey, keyPath, op);
            }

            @Override
            public ORMapUpdate with(RWORSetOperation op) {
                return new ORMapUpdate(Type.UpdateKey, keyPath, op);
            }

            @Override
            public ORMapUpdate with(DWFlagOperation op) {
                return new ORMapUpdate(Type.UpdateKey, keyPath, op);
            }

            @Override
            public ORMapUpdate with(EWFlagOperation op) {
                return new ORMapUpdate(Type.UpdateKey, keyPath, op);
            }

            @Override
            public ORMapUpdate with(MVRegOperation op) {
                return new ORMapUpdate(Type.UpdateKey, keyPath, op);
            }

            @Override
            public ORMapUpdate with(CCounterOperation op) {
                return new ORMapUpdate(Type.UpdateKey, keyPath, op);
            }

            @Override
            public ORMapUpdate with(ORMapOperation op) {
                return new ORMapUpdate(Type.UpdateKey, keyPath, op);
            }

        };
    }

    /**
     * 继承抽象类 ORMapOperation
     * 更新操作
     */
    public static class ORMapUpdate extends ORMapOperation {
        public final ICRDTOperation valueOp;

        private ORMapUpdate(Type type, ByteString[] keyPath, ICRDTOperation valueOp) {
            super(type, keyPath);
            this.valueOp = valueOp;
        }
    }

    /**
     * 继承抽象类 ORMapOperation
     * 移除操作
     */
    public static class ORMapRemove extends ORMapOperation {
        public CausalCRDTType valueType;

        private ORMapRemove(Type type, ByteString[] keyPath, CausalCRDTType valueType) {
            super(type, keyPath);
            this.valueType = valueType;
        }
    }
}
