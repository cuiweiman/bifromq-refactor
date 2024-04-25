
package com.zachary.bifromq.basecrdt.core.internal;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;

class ORMapUtil {
    static ByteString parseKey(ByteString typedKey) {
        return typedKey.substring(0, typedKey.size() - 1);
    }

    static ByteString typedKey(ByteString key, CausalCRDTType type) {
        return key.concat(type.id);
    }

    static CausalCRDTType getType(ByteString typedKey) {
        return CausalCRDTType.parse(typedKey.substring(typedKey.size() - 1));
    }
}
