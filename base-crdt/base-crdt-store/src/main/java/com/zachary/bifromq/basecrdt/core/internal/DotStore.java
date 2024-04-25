package com.zachary.bifromq.basecrdt.core.internal;

import com.zachary.bifromq.basecrdt.proto.StateLattice;

abstract class DotStore implements IDotStore {
    abstract boolean add(StateLattice addState);

    abstract boolean remove(StateLattice removeState);
}
