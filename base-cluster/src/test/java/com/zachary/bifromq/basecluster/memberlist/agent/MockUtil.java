package com.zachary.bifromq.basecluster.memberlist.agent;

import com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr;
import com.zachary.bifromq.basecluster.agent.proto.AgentMemberMetadata;
import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.IMVReg;
import com.zachary.bifromq.basecrdt.core.api.IORMap;
import com.zachary.bifromq.basecrdt.core.api.operation.MVRegOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;
import com.zachary.bifromq.basehlc.HLC;
import com.google.common.collect.Iterators;
import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.core.Observable;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

public class MockUtil {
    public static AgentMemberMetadata toAgentMemberMetadata(ByteString value) {
        return AgentMemberMetadata.newBuilder().setValue(value).setHlc(HLC.INST.get()).build();
    }

    public static AgentMemberAddr toAgentMemberAddr(String name, HostEndpoint endpoint) {
        return AgentMemberAddr.newBuilder().setName(name).setEndpoint(endpoint).build();
    }

    public static void mockAgentMemberCRDT(IORMap orMap, Map<AgentMemberAddr, AgentMemberMetadata> members) {
        IORMap.ORMapKey[] keys =
            members.keySet().stream().map(memberAddr -> mvRegKey(memberAddr.toByteString()))
                .toArray(IORMap.ORMapKey[]::new);
        Map<ByteString, AgentMemberMetadata> memberMap =
            members.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toByteString(), e -> e.getValue()));
        when(orMap.keys()).thenReturn(Iterators.forArray(keys));
        for (IORMap.ORMapKey key : keys) {
            when(orMap.getMVReg(key.key())).thenReturn(mvRegValue(memberMap.get(key.key()).toByteString()));
        }
    }

    private static IORMap.ORMapKey mvRegKey(ByteString key) {
        return new IORMap.ORMapKey() {
            @Override
            public ByteString key() {
                return key;
            }

            @Override
            public CausalCRDTType valueType() {
                return CausalCRDTType.mvreg;
            }
        };
    }

    private static IMVReg mvRegValue(ByteString value) {
        return new IMVReg() {
            @Override
            public Iterator<ByteString> read() {
                return Iterators.forArray(value);
            }

            @Override
            public Replica id() {
                return null;
            }

            @Override
            public CompletableFuture<Void> execute(MVRegOperation op) {
                return null;
            }

            @Override
            public Observable<Long> inflation() {
                return null;
            }
        };
    }
}
