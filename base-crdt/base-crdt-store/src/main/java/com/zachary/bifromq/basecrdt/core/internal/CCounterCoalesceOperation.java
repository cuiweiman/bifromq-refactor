
package com.zachary.bifromq.basecrdt.core.internal;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.operation.CCounterOperation;
import com.zachary.bifromq.basecrdt.proto.Dot;
import com.zachary.bifromq.basecrdt.proto.Replacement;

import java.util.Set;
import java.util.stream.Collectors;

class CCounterCoalesceOperation extends CoalesceOperation<IDotMap, CCounterOperation> {

    private long inc;

    private boolean preset;

    private Set<ByteString> zeroOutReplicaIds = Sets.newHashSet();

    CCounterCoalesceOperation(ByteString replicaId, CCounterOperation op) {
        super(replicaId);
        coalesce(op);
    }

    @Override
    public void coalesce(CCounterOperation op) {
        switch (op.type) {
            case Add:
                inc += op.c;
                break;
            case Preset:
                if (op.replicaId == null) {
                    preset = true;
                    inc = op.c;
                } else {
                    zeroOutReplicaIds.add(op.replicaId);
                }
                break;
            default:
                throw new IllegalStateException("Unknown ccounter operation type: " + op.type);
        }
    }

    @Override
    public Iterable<Replacement> delta(IDotMap current, IEventGenerator eventGenerator) {
        long now = preset ? inc : getPartialCount(current.subDotFunc(replicaId).orElse(DotFunc.BOTTOM)) + inc;
        if (preset && inc == 0) {
            return zeroOut(eventGenerator.nextEvent(),
                current, Sets.union(zeroOutReplicaIds, Sets.<ByteString>newHashSet(replicaId)));
        }
        long ver = eventGenerator.nextEvent();
        if (zeroOutReplicaIds.isEmpty()) {
            return ProtoUtils.replacements(ProtoUtils.dot(replicaId, ver,
                    ProtoUtils.singleMap(replicaId,
                        ProtoUtils.singleValue(replicaId, ver, Varint.encodeLong(now)))),
                current.subDotFunc(replicaId).orElse(DotFunc.BOTTOM));
        }
        return Iterables.concat(ProtoUtils.replacements(
                ProtoUtils.dot(replicaId, ver, ProtoUtils.singleMap(replicaId,
                    ProtoUtils.singleValue(replicaId, ver, Varint.encodeLong(now)))),
                current.subDotFunc(replicaId).orElse(DotFunc.BOTTOM)),
            zeroOut(eventGenerator.nextEvent(), current, zeroOutReplicaIds));
    }

    private Iterable<Replacement> zeroOut(long ver, IDotMap current, Set<ByteString> replicaIds) {
        return ProtoUtils.replacements(ProtoUtils.dot(replicaId, ver), Iterables.concat(replicaIds.stream()
            .map(r -> current.subDotFunc(r).orElse(DotFunc.BOTTOM))
            .collect(Collectors.toSet())));
    }

    private long getPartialCount(IDotFunc df) {
        long maxVer = -1;
        long val = 0;
        for (Dot dot : df) {
            if (dot.getVer() > maxVer) {
                maxVer = dot.getVer();
                val = Varint.decodeLong(df.value(dot).get());
            }
        }
        return val;
    }
}
