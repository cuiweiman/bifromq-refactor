package com.zachary.bifromq.basekv.store;

import com.zachary.bifromq.basecrdt.core.api.CRDTURI;
import com.zachary.bifromq.basecrdt.core.api.IORMap;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basekv.proto.KVRangeStoreDescriptor;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class CRDTUtil {
    public static String storeDescriptorMapCRDTURI(String clusterId) {
        return CRDTURI.toURI(CausalCRDTType.ormap, clusterId + "-store-descriptor-map");
    }

    public static Optional<KVRangeStoreDescriptor> getDescriptorFromCRDT(IORMap descriptorMapCRDT,
                                                                         ByteString key) {
        ArrayList<KVRangeStoreDescriptor> l = Lists.newArrayList(
            Iterators.filter(Iterators.transform(descriptorMapCRDT.getMVReg(key).read(), b -> {
                try {
                    return KVRangeStoreDescriptor.parseFrom(b);
                } catch (InvalidProtocolBufferException e) {
                    log.error("Unable to parse KVRangeStoreDescriptor", e);
                    return null;
                }
            }), Objects::nonNull));
        l.sort((a, b) -> Long.compareUnsigned(b.getHlc(), a.getHlc()));
        return Optional.ofNullable(l.isEmpty() ? null : l.get(0));
    }
}
