
package com.zachary.bifromq.basecrdt.core.internal;

import com.google.common.collect.Maps;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.proto.Dot;
import com.zachary.bifromq.basecrdt.proto.StateLattice;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

class DotFunc extends DotStore implements IDotFunc {
    public static final IDotFunc BOTTOM = new DotFunc();
    private final Map<Dot, ByteString> dots = Maps.newConcurrentMap();

    @Override
    public Iterator<Dot> iterator() {
        return dots.keySet().iterator();
    }

    @Override
    public boolean isBottom() {
        return dots.isEmpty();
    }

    @Override
    public Iterable<ByteString> values() {
        return dots.values();
    }

    @Override
    public Optional<ByteString> value(Dot dot) {
        return Optional.ofNullable(dots.get(dot));
    }

    boolean add(StateLattice addState) {
        assert addState.getStateTypeCase() == StateLattice.StateTypeCase.SINGLEVALUE;
        return dots.putIfAbsent(ProtoUtils.dot(addState.getSingleValue().getReplicaId(),
                        addState.getSingleValue().getVer()),
                addState.getSingleValue().getValue()) == null;
    }

    boolean remove(StateLattice removeState) {
        assert removeState.getStateTypeCase() == StateLattice.StateTypeCase.SINGLEVALUE;
        return dots.remove(ProtoUtils.dot(removeState.getSingleValue().getReplicaId(),
                        removeState.getSingleValue().getVer()),
                removeState.getSingleValue().getValue());
    }

    @Override
    public String toString() {
        return "DotFunc{" +
                "dots=" + dots +
                '}';
    }
}
