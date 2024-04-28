package com.zachary.bifromq.basecluster.memberlist.agent;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr;
import com.zachary.bifromq.basecluster.agent.proto.AgentMemberMetadata;
import com.zachary.bifromq.basecrdt.core.api.IMVReg;
import com.zachary.bifromq.basecrdt.core.api.IORMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.zachary.bifromq.basecrdt.core.api.CRDTURI.toURI;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.mvreg;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.ormap;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class CRDTUtil {

    private static final String AGENTKEY_PREFIX = "A";

    static String toAgentURI(String agentId) {
        return toURI(ormap, AGENTKEY_PREFIX.concat(agentId));
    }

    static Map<AgentMemberAddr, AgentMemberMetadata> toAgentMemberMap(IORMap agentCRDT) {
        Map<AgentMemberAddr, AgentMemberMetadata> agentMemberMap = new HashMap<>();
        Iterator<IORMap.ORMapKey> orMapKeyItr = agentCRDT.keys();
        while (orMapKeyItr.hasNext()) {
            IORMap.ORMapKey orMapKey = orMapKeyItr.next();
            agentMemberMap.put(parseAgentMemberAddr(orMapKey), parseMetadata(agentCRDT.getMVReg(orMapKey.key())).get());
        }
        return agentMemberMap;
    }

    static Optional<AgentMemberMetadata> getAgentMemberMetadata(IORMap agentCRDT, AgentMemberAddr addr) {
        return parseMetadata(agentCRDT.getMVReg(addr.toByteString()));
    }

    @SneakyThrows
    private static AgentMemberAddr parseAgentMemberAddr(IORMap.ORMapKey key) {
        assert key.valueType() == mvreg;
        return AgentMemberAddr.parseFrom(key.key());
    }

    private static Optional<AgentMemberMetadata> parseMetadata(IMVReg value) {
        List<AgentMemberMetadata> metaList = Lists.newArrayList(Iterators.filter(
                Iterators.transform(value.read(), data -> {
                    try {
                        return AgentMemberMetadata.parseFrom(data);
                    } catch (InvalidProtocolBufferException e) {
                        log.error("Unable to parse agent host node", e);
                        // this exception should not happen
                        return null;
                    }
                }), Objects::nonNull));
        metaList.sort(Comparator.comparingLong(AgentMemberMetadata::getHlc));
        return Optional.ofNullable(metaList.isEmpty() ? null : metaList.get(0));
    }
}
