package com.zachary.bifromq.basecluster.memberlist;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import com.zachary.bifromq.basecluster.membership.proto.HostMember;
import com.zachary.bifromq.basecrdt.core.api.IMVReg;
import com.zachary.bifromq.basecrdt.core.api.IORMap;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.zachary.bifromq.basecrdt.core.api.CRDTURI.toURI;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.ormap;

@Slf4j
public class CRDTUtil {
    public static final String AGENT_HOST_MAP_URI = toURI(ormap, "AGENT_HOST_MAP");

    public static Optional<HostMember> getHostMember(IORMap hostListCRDT, HostEndpoint endpoint) {
        return parse(hostListCRDT.getMVReg(endpoint.toByteString()));
    }

    public static Iterator<HostMember> iterate(IORMap hostListCRDT) {
        return Iterators.transform(hostListCRDT.keys(), orMapkey -> parse(hostListCRDT.getMVReg(orMapkey.key())).get());
    }

    private static Optional<HostMember> parse(IMVReg value) {
        List<HostMember> agentHostNodes = Lists.newArrayList(Iterators.filter(
                Iterators.transform(value.read(), data -> {
                    try {
                        return HostMember.parseFrom(data);
                    } catch (InvalidProtocolBufferException e) {
                        log.error("Unable to parse agent host node", e);
                        // this exception should not happen
                        return null;
                    }
                }), Objects::nonNull));
        agentHostNodes.sort((a, b) -> b.getIncarnation() - a.getIncarnation());
        return Optional.ofNullable(agentHostNodes.isEmpty() ? null : agentHostNodes.get(0));
    }
}
