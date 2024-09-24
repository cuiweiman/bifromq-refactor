
package com.zachary.bifromq.basecrdt.core.internal;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.operation.AWORSetOperation;
import com.zachary.bifromq.basecrdt.proto.Replacement;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 合并操作
 *
 * @description: 添加获胜优化的观察删除集，允许添加和删除
 * @author: cuiweiman
 * @date: 2024/9/23 18:06
 */
class AWORSetCoalesceOperation extends CoalesceOperation<IDotMap, AWORSetOperation> {
    private boolean clearAtFirst = false;
    private final Set<ByteString> adds = Sets.newConcurrentHashSet();
    private final Set<ByteString> rems = Sets.newConcurrentHashSet();

    AWORSetCoalesceOperation(ByteString replicaId, AWORSetOperation op) {
        super(replicaId);
        coalesce(op);
    }

    @Override
    public void coalesce(AWORSetOperation op) {
        if (op.type == AWORSetOperation.Type.Clear) {
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
    public Iterable<Replacement> delta(IDotMap current, ICoalesceOperation.IEventGenerator eventGenerator) {
        Iterable<Replacement> addDots = adds.stream().map(e -> {
                    long ver = eventGenerator.nextEvent();
                    return ProtoUtils.replacement(ProtoUtils.dot(replicaId, ver,
                            ProtoUtils.singleMap(e, ProtoUtils.singleDot(replicaId, ver))));
                })
                .collect(Collectors.toSet());
        if (clearAtFirst && !current.isBottom()) {
            // Iterables.concat 将多个 list 中的元素 合并到 一个 list 中
            return Iterables.concat(addDots,
                    ProtoUtils.replacements(ProtoUtils.dot(replicaId, eventGenerator.nextEvent()), current));
        } else {
            return Iterables.concat(addDots, Iterables.transform(Iterables.concat(rems.stream()
                    .map(e -> current.subDotSet(e).orElse(DotSet.BOTTOM))
                    .collect(Collectors.toSet())), ProtoUtils::replacement));
        }
    }
}

