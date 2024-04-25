
package com.zachary.bifromq.basecrdt.core.internal;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.operation.DWFlagOperation;
import com.zachary.bifromq.basecrdt.proto.Replacement;

public class DWFlagCoalesceOperation extends CoalesceOperation<IDotSet, DWFlagOperation> {
    DWFlagOperation op;

    DWFlagCoalesceOperation(ByteString replicaId, DWFlagOperation op) {
        super(replicaId);
        coalesce(op);
    }

    @Override
    public void coalesce(DWFlagOperation op) {
        this.op = op;
    }

    @Override
    public Iterable<Replacement> delta(IDotSet current, IEventGenerator eventGenerator) {
        long ver = eventGenerator.nextEvent();
        return switch (op.type) {
            case Disable -> ProtoUtils.replacements(ProtoUtils.dot(replicaId, ver,
                    ProtoUtils.singleDot(replicaId, ver)), current);
            default -> ProtoUtils.replacements(ProtoUtils.dot(replicaId, ver), current);
        };
    }
}
