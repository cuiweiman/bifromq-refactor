package com.zachary.bifromq.basecrdt.core.internal;

import com.google.protobuf.ByteString;

import java.util.Iterator;
import java.util.Optional;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/20 16:51
 */
public interface IDotMap extends IDotStore {

    Optional<IDotSet> subDotSet(ByteString... keys);

    Optional<IDotFunc> subDotFunc(ByteString... keys);

    Optional<IDotMap> subDotMap(ByteString... keys);

    Iterator<ByteString> dotSetKeys();

    Iterator<ByteString> dotFuncKeys();

    Iterator<ByteString> dotMapKeys(ByteString... keys);


}
