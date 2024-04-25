
package com.zachary.bifromq.basecrdt.core.internal;

import com.google.common.collect.Sets;
import com.zachary.bifromq.basecrdt.proto.Dot;
import com.zachary.bifromq.basecrdt.proto.StateLattice;

import java.util.Iterator;
import java.util.Set;

class DotSet extends DotStore implements IDotSet {
    public static final IDotSet BOTTOM = new DotSet();
    private final Set<Dot> dots = Sets.newConcurrentHashSet();

    @Override
    public Iterator<Dot> iterator() {
        return dots.iterator();
    }

    @Override
    public boolean isBottom() {
        return dots.isEmpty();
    }

    @Override
    boolean add(StateLattice addState) {
        assert addState.getStateTypeCase() == StateLattice.StateTypeCase.SINGLEDOT;
        return dots.add(ProtoUtils.dot(addState.getSingleDot().getReplicaId(), addState.getSingleDot().getVer()));
    }

    @Override
    boolean remove(StateLattice removeState) {
        assert removeState.getStateTypeCase() == StateLattice.StateTypeCase.SINGLEDOT;
        return dots.remove(ProtoUtils.dot(removeState.getSingleDot().getReplicaId(),
            removeState.getSingleDot().getVer()));
    }

    @Override
    public String toString() {
        return "DotSet{" +
            "dots=" + dots +
            '}';
    }
}
