
package com.zachary.bifromq.basecrdt.core.internal;


import com.google.common.collect.Iterables;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.proto.Dot;
import com.zachary.bifromq.basecrdt.proto.Replacement;
import com.zachary.bifromq.basecrdt.proto.SingleDot;
import com.zachary.bifromq.basecrdt.proto.SingleMap;
import com.zachary.bifromq.basecrdt.proto.SingleValue;
import com.zachary.bifromq.basecrdt.proto.StateLattice;

import java.nio.ByteBuffer;

import static com.google.protobuf.UnsafeByteOperations.unsafeWrap;
import static java.util.Collections.singleton;

/**
 * @description: Proto 工具
 * @author: cuiweiman
 * @date: 2024/4/20 18:00
 */
class ProtoUtils {
    static ByteString toByteString(long l) {
        return unsafeWrap(ByteBuffer.allocate(Long.BYTES).putLong(l).array());
    }

    static Dot dot(ByteString replicaId, long ver) {
        return Dot.newBuilder().setReplicaId(replicaId).setVer(ver).build();
    }

    static Dot dot(ByteString replicaId, long ver, StateLattice lattice) {
        return Dot.newBuilder().setReplicaId(replicaId).setVer(ver).setLattice(lattice).build();
    }

    /**
     * 将 Dot 数组 封装到 protobuf 对象 Replacement 中
     *
     * @param dots dot 数组
     * @return protobuf Dot 数组
     */
    static Replacement replacement(Dot... dots) {
        Replacement.Builder builder = Replacement.newBuilder();
        for (Dot dot : dots) {
            builder.addDots(dot);
        }
        return builder.build();
    }

    /**
     * replacingDots 不为空时，则 {@link #replacement}进行转换，否则只转换 Dot
     *
     * @param dot           dot
     * @param replacingDots replacingDots
     * @return Iterable<Replacement>
     */
    static Iterable<Replacement> replacements(Dot dot, Iterable<Dot> replacingDots) {
        return replacingDots.iterator().hasNext() ?
                Iterables.transform(replacingDots, replacingDot -> replacement(dot, replacingDot)) :
                singleton(replacement(dot));
    }

    static StateLattice singleDot(ByteString replicaId, long ver) {
        return StateLattice.newBuilder()
                .setSingleDot(SingleDot.newBuilder()
                        .setReplicaId(replicaId)
                        .setVer(ver)
                        .build())
                .build();
    }

    static StateLattice singleValue(ByteString replicaId, long ver, ByteString val) {
        return StateLattice.newBuilder()
                .setSingleValue(SingleValue.newBuilder()
                        .setReplicaId(replicaId)
                        .setVer(ver)
                        .setValue(val)
                        .build())
                .build();
    }

    static StateLattice singleMap(ByteString key, StateLattice val) {
        return StateLattice.newBuilder().setSingleMap(SingleMap.newBuilder().setKey(key).setVal(val).build()).build();
    }
}
