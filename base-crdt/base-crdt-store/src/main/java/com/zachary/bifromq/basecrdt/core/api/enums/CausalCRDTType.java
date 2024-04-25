package com.zachary.bifromq.basecrdt.core.api.enums;

import com.google.protobuf.ByteString;

/**
 * @description: 分片类型
 * @author: cuiweiman
 * @date: 2024/4/20 17:09
 */
public enum CausalCRDTType {

    cctr,
    ewflag,
    dwflag,
    mvreg,
    aworset,
    rworset,
    ormap,

    ;

    public final ByteString id;

    /**
     * {@link Enum#ordinal()} 返回此枚举常量的位置，枚举常量的起始元素位置从 0 开始。
     */
    CausalCRDTType() {
        id = ByteString.copyFrom(new byte[]{(byte) ordinal()});
    }

    public static CausalCRDTType parse(ByteString id) {
        return CausalCRDTType.values()[id.byteAt(0)];
    }

}
