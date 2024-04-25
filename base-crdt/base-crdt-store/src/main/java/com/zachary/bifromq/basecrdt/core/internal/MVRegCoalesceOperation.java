package com.zachary.bifromq.basecrdt.core.internal;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.operation.MVRegOperation;
import com.zachary.bifromq.basecrdt.proto.Replacement;


public class MVRegCoalesceOperation extends CoalesceOperation<IDotFunc, MVRegOperation> {
    MVRegOperation op;

    MVRegCoalesceOperation(ByteString replicaId, MVRegOperation op) {
        super(replicaId);
        coalesce(op);
    }

    @Override
    public void coalesce(MVRegOperation op) {
        this.op = op;
    }

    @Override
    public Iterable<Replacement> delta(IDotFunc current, IEventGenerator eventGenerator) {
        long ver = eventGenerator.nextEvent();
        return switch (op.type) {
            case Write ->
                    ProtoUtils.replacements(ProtoUtils.dot(replicaId, ver, ProtoUtils.singleValue(replicaId, ver, op.value)), current);
            default -> ProtoUtils.replacements(ProtoUtils.dot(replicaId, ver), current);
        };
    }
}
