
package com.zachary.bifromq.basecrdt.core.internal;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.operation.RWORSetOperation;
import com.zachary.bifromq.basecrdt.proto.Replacement;

import java.util.Set;
import java.util.stream.Collectors;

import static com.zachary.bifromq.basecrdt.core.internal.ProtoUtils.dot;
import static com.zachary.bifromq.basecrdt.core.internal.ProtoUtils.replacement;
import static com.zachary.bifromq.basecrdt.core.internal.ProtoUtils.singleMap;

class RWORSetCoalesceOperation extends CoalesceOperation<IDotMap, RWORSetOperation> {

    boolean clearAtFirst = false;
    private final Set<ByteString> adds = Sets.newConcurrentHashSet();
    private final Set<ByteString> rems = Sets.newConcurrentHashSet();

    RWORSetCoalesceOperation(ByteString replicaId, RWORSetOperation op) {
        super(replicaId);
        coalesce(op);
    }

    @Override
    public void coalesce(RWORSetOperation op) {
        if (op.type == RWORSetOperation.Type.Clear) {
            clearAtFirst = true;
            adds.clear();
            rems.clear();
        } else {
            switch (op.type) {
                case Add:
                    adds.add(op.element);
                    rems.remove(op.element);
                    break;
                case Remove:
                    adds.remove(op.element);
                    rems.add(op.element);
                    break;
            }
        }
    }

    @Override
    public Iterable<Replacement> delta(IDotMap current, IEventGenerator eventGenerator) {
        // DotMap<ByteString, DotMap<TRUE, DotSet>>
        Iterable<Replacement> addElements = adds.stream()
                .map(e -> {
                    long ver = eventGenerator.nextEvent();
                    return replacement(dot(replicaId, ver,
                            singleMap(e, singleMap(RWORSet.TRUE, ProtoUtils.singleDot(replicaId, ver)))));
                })
                .collect(Collectors.toSet());
        // DotMap<ByteString, DotMap<FALSE, DotSet>>
        Iterable<Replacement> remElements = rems.stream()
                .map(e -> {
                    long ver = eventGenerator.nextEvent();
                    return replacement(dot(replicaId, ver,
                            singleMap(e, singleMap(RWORSet.FALSE, ProtoUtils.singleDot(replicaId, ver)))));
                })
                .collect(Collectors.toSet());
        if (clearAtFirst) {
            return Iterables.concat(addElements, remElements,
                    ProtoUtils.replacements(dot(replicaId, eventGenerator.nextEvent()), current));
        } else {
            return Iterables.concat(addElements, remElements,
                    Iterables.transform(Iterables.concat(Sets.union(adds, rems)
                            .stream()
                            .map(e -> current.subDotMap(e).orElse(DotMap.BOTTOM))
                            .collect(Collectors.toSet())), ProtoUtils::replacement));
        }
    }
}

