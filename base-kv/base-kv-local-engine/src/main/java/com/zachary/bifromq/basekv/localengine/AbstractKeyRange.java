package com.zachary.bifromq.basekv.localengine;

import com.google.protobuf.ByteString;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AbstractKeyRange {
    final int id;
    final String ns;
    final ByteString start;
    final ByteString end;
}
