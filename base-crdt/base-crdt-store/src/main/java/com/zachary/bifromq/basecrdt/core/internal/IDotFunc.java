package com.zachary.bifromq.basecrdt.core.internal;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.proto.Dot;

import java.util.Optional;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/20 16:45
 */
public interface IDotFunc extends IDotStore {

    Iterable<ByteString> values();

    Optional<ByteString> value(Dot dot);

}
