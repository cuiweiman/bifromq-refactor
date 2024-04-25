package com.zachary.bifromq.basecrdt.core.internal;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.operation.EWFlagOperation;
import com.zachary.bifromq.basecrdt.proto.Replacement;

public class EWFlagCoalesceOperation extends CoalesceOperation<IDotSet, EWFlagOperation> {
    EWFlagOperation op;

    EWFlagCoalesceOperation(ByteString replicaId, EWFlagOperation op) {
        super(replicaId);
        coalesce(op);
    }

    @Override
    public void coalesce(EWFlagOperation op) {
        this.op = op;
    }

    @Override
    public Iterable<Replacement> delta(IDotSet current, IEventGenerator eventGenerator) {
        long ver = eventGenerator.nextEvent();
        return switch (op.type) {
            case Enable -> ProtoUtils.replacements(ProtoUtils.dot(replicaId, ver,
                    ProtoUtils.singleDot(replicaId, ver)), current);
            default -> ProtoUtils.replacements(ProtoUtils.dot(replicaId, ver), current);
        };
    }
}
