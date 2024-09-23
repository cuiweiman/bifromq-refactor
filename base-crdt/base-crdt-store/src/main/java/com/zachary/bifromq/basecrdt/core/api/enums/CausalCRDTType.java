package com.zachary.bifromq.basecrdt.core.api.enums;

import com.google.protobuf.ByteString;

/**
 * <url http="https://github.com/CBaquero/delta-enabled-crdts">结合delta-enabled-crdts做参考和概念理解</url>
 *
 * @description: 分片类型
 * @author: cuiweiman
 * @date: 2024/4/20 17:09
 */
public enum CausalCRDTType {

    // CCounter: A (causal) counter for map embedding (Optimization over Riak EMCounter)
    // CCounter：用于映射嵌入的（因果）计数器（对 Riak EMCounter 的优化）
    cctr,
    // EWFlag: Flag with enable/disable. Enable wins (Riak Flag inspired)
    // EWFlag：带有启用/禁用的标志。启用胜利（受到 Riak Flag 启发）
    ewflag,
    // DWFlag: Flag with enable/disable. Disable wins (Riak Flag inspired)
    // DWFlag：带有启用/禁用的标志。禁用胜利（受到 Riak Flag 启发）
    dwflag,
    // MVRegister: An optimized multi-value register (new unpublished datatype)
    // MVRegister：优化的多值寄存器（新的未发布的数据类型）
    mvreg,
    // AWORSet：一个 添加获胜优化的观察删除集，允许添加和删除
    // AWORSet: An add-wins optimized observed-remove set that allows adds and removes
    aworset,
    // RWORSet: A remove-wins optimized observed-remove set that allows adds and removes
    // RWORSet：删除获胜优化的观察删除集，允许添加和删除
    rworset,
    // ORMap: Map of keys to causal CRDTs. (spec in common with the Riak Map)
    // ORMap：因果 CRDT 的键映射。 （规格与 Riak 映射相同）
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


    public static void main(String[] args) {
        CausalCRDTType[] values = CausalCRDTType.values();
        System.out.println(values[1].id.byteAt(0));
        for (CausalCRDTType value : values) {
            System.out.printf("ordinal: %s, type: %s%n", value.ordinal(), parse(value.id));
        }

    }
}
