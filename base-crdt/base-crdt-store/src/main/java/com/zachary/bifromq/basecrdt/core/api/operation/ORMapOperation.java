package com.zachary.bifromq.basecrdt.core.api.operation;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import lombok.ToString;

@ToString
public abstract class ORMapOperation implements ICRDTOperation {
    public interface ORMapUpdater {
        ORMapUpdate with(AWORSetOperation op);

        ORMapUpdate with(RWORSetOperation op);

        ORMapUpdate with(DWFlagOperation op);

        ORMapUpdate with(EWFlagOperation op);

        ORMapUpdate with(MVRegOperation op);

        ORMapUpdate with(CCounterOperation op);

        ORMapUpdate with(ORMapOperation op);
    }

    public interface ORMapRemover {
        ORMapRemove of(CausalCRDTType valueType);
    }

    public enum Type {
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

    public static ORMapRemover remove(ByteString... keyPath) {
        return new ORMapRemover() {
            @Override
            public ORMapRemove of(CausalCRDTType valueType) {
                return new ORMapRemove(Type.RemoveKey, keyPath, valueType);
            }
        };
    }

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

    public static class ORMapUpdate extends ORMapOperation {
        public final ICRDTOperation valueOp;

        private ORMapUpdate(Type type, ByteString[] keyPath, ICRDTOperation valueOp) {
            super(type, keyPath);
            this.valueOp = valueOp;
        }
    }

    public static class ORMapRemove extends ORMapOperation {
        public CausalCRDTType valueType;

        private ORMapRemove(Type type, ByteString[] keyPath, CausalCRDTType valueType) {
            super(type, keyPath);
            this.valueType = valueType;
        }
    }
}
