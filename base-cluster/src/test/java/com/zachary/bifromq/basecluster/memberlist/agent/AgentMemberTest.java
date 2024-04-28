package com.zachary.bifromq.basecluster.memberlist.agent;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr;
import com.zachary.bifromq.basecluster.agent.proto.AgentMemberMetadata;
import com.zachary.bifromq.basecluster.agent.proto.AgentMessage;
import com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope;
import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import com.zachary.bifromq.basecrdt.core.api.IORMap;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.operation.MVRegOperation;
import com.zachary.bifromq.basecrdt.core.api.operation.ORMapOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;
import com.zachary.bifromq.basecrdt.store.ICRDTStore;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.UnknownHostException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.zachary.bifromq.basecluster.memberlist.agent.MockUtil.toAgentMemberAddr;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

@Slf4j
public class AgentMemberTest {
    private String agentId = "agentId";
    private ByteString hostId = ByteString.copyFromUtf8("host1");
    private HostEndpoint endpoint1 = HostEndpoint.newBuilder()
            .setId(hostId)
            .setAddress("localhost")
            .setPort(1111)
            .build();
    private HostEndpoint endpoint2 = HostEndpoint.newBuilder()
            .setId(hostId)
            .setAddress("localhost")
            .setPort(2222)
            .build();
    private String agentMemberName = "agentMemberName";
    private AgentMemberAddr agentMemberAddr = AgentMemberAddr.newBuilder()
            .setName(agentMemberName)
            .setEndpoint(endpoint1)
            .build();
    private Replica replica = Replica.newBuilder().setUri(CRDTUtil.toAgentURI(agentId)).build();
    @Mock
    private IAgentMessenger agentMessenger;
    private Scheduler scheduler = Schedulers.from(MoreExecutors.directExecutor()); // make test more deterministic
    @Mock
    private ICRDTStore crdtStore;
    @Mock
    private Supplier<Set<AgentMemberAddr>> memberAddresses;
    @Mock
    private IAgentHostProvider hostProvider;
    @Mock
    private IORMap orMap;
    private PublishSubject<Long> inflationSubject = PublishSubject.create();
    private PublishSubject<Set<HostEndpoint>> hostsSubjects = PublishSubject.create();
    private PublishSubject<AgentMessageEnvelope> messageSubject = PublishSubject.create();
    private AutoCloseable closeable;

    @BeforeMethod
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        when(orMap.execute(any())).thenReturn(CompletableFuture.completedFuture(null));
        when(orMap.inflation()).thenReturn(inflationSubject);
        when(agentMessenger.send(any(), any(), anyBoolean())).thenReturn(CompletableFuture.completedFuture(null));
        when(agentMessenger.receive()).thenReturn(messageSubject);
    }

    @AfterMethod
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @SneakyThrows
    @Test
    public void updateMetadata() {
        ByteString meta2 = ByteString.copyFromUtf8("Hello");

        AgentMember agentMember = new AgentMember(agentMemberAddr, orMap, agentMessenger, scheduler, memberAddresses);
        AgentMemberMetadata metadata = agentMember.metadata();
        assertEquals(metadata.getValue(), ByteString.EMPTY);

        agentMember.metadata(ByteString.EMPTY); // nothing will happen to crdt
        assertEquals(agentMember.metadata(), metadata);

        agentMember.metadata(meta2);

        ArgumentCaptor<ORMapOperation> orMapOpCap = ArgumentCaptor.forClass(ORMapOperation.class);
        verify(orMap, times(2)).execute(orMapOpCap.capture());
        ORMapOperation op = orMapOpCap.getAllValues().get(0);
        assertTrue(op instanceof ORMapOperation.ORMapUpdate);
        assertTrue(((ORMapOperation.ORMapUpdate) op).valueOp instanceof MVRegOperation);
        AgentMemberAddr key = AgentMemberAddr.parseFrom(op.keyPath[0]);
        assertEquals(key.getName(), agentMemberName);
        assertEquals(key.getEndpoint(), endpoint1);

        AgentMemberMetadata agentMemberMetadata1 =
                AgentMemberMetadata.parseFrom(((MVRegOperation) ((ORMapOperation.ORMapUpdate) op).valueOp).value);
        assertEquals(agentMemberMetadata1.getValue(), ByteString.EMPTY);
        assertTrue(agentMemberMetadata1.getHlc() > 0);

        op = orMapOpCap.getAllValues().get(1);
        AgentMemberMetadata agentMemberMetadata2 =
                AgentMemberMetadata.parseFrom(((MVRegOperation) ((ORMapOperation.ORMapUpdate) op).valueOp).value);
        assertEquals(agentMemberMetadata2.getValue(), meta2);
        assertTrue(agentMemberMetadata1.getHlc() < agentMemberMetadata2.getHlc());
    }

    @Test
    public void broadcast() {
        AgentMember agentMember = new AgentMember(agentMemberAddr, orMap, agentMessenger, scheduler, memberAddresses);

        when(memberAddresses.get()).thenReturn(Sets.newHashSet(agentMemberAddr));
        agentMember.broadcast(ByteString.EMPTY, true).join();

        ArgumentCaptor<AgentMessage> msgCap = ArgumentCaptor.forClass(AgentMessage.class);
        ArgumentCaptor<AgentMemberAddr> addrCap = ArgumentCaptor.forClass(AgentMemberAddr.class);
        ArgumentCaptor<Boolean> reliableCap = ArgumentCaptor.forClass(Boolean.class);
        verify(agentMessenger, times(1)).send(msgCap.capture(), addrCap.capture(), reliableCap.capture());

        assertEquals(msgCap.getValue().getPayload(), ByteString.EMPTY);
        assertEquals(msgCap.getValue().getSender(), agentMemberAddr);
        assertEquals(addrCap.getValue(), agentMemberAddr);
        assertTrue(reliableCap.getValue());
    }

    @Test
    public void illegalStateAfterDestroy() {
        illegalStateAfterDestroy(agentMember -> agentMember.broadcast(ByteString.EMPTY, true));
        illegalStateAfterDestroy(
                agentMember -> agentMember.send(AgentMemberAddr.getDefaultInstance(), ByteString.EMPTY, true));
        illegalStateAfterDestroy(agentMember -> agentMember.multicast("ReceiverGroup", ByteString.EMPTY, true));
    }

    private void illegalStateAfterDestroy(Consumer<IAgentMember> test) {
        AgentMember agentMember = new AgentMember(agentMemberAddr, orMap, agentMessenger, scheduler, memberAddresses);
        agentMember.destroy().join();
        try {
            test.accept(agentMember);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
        }
    }

    @Test
    public void sendToUnknownTarget() {
        String agentMemberName2 = "agentMember2";
        AgentMember agentMember = new AgentMember(agentMemberAddr, orMap, agentMessenger, scheduler, memberAddresses);
        try {
            when(memberAddresses.get()).thenReturn(Sets.newHashSet(agentMemberAddr));
            agentMember.send(AgentMemberAddr.newBuilder().setName(agentMemberName2).setEndpoint(endpoint1).build(),
                    ByteString.EMPTY, false).join();
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof UnknownHostException);
        }
    }

    @Test
    public void send() {
        String agentMemberName2 = "agentMember2";
        AgentMember agentMember1 = new AgentMember(agentMemberAddr, orMap, agentMessenger, scheduler, memberAddresses);
        AgentMemberAddr target = toAgentMemberAddr(agentMemberName2, endpoint1);
        when(memberAddresses.get()).thenReturn(Sets.newHashSet(agentMemberAddr, target));
        agentMember1.send(target, ByteString.EMPTY, false);

        ArgumentCaptor<AgentMessage> msgCap = ArgumentCaptor.forClass(AgentMessage.class);
        ArgumentCaptor<AgentMemberAddr> addrCap = ArgumentCaptor.forClass(AgentMemberAddr.class);
        ArgumentCaptor<Boolean> reliableCap = ArgumentCaptor.forClass(Boolean.class);
        verify(agentMessenger, times(1)).send(msgCap.capture(), addrCap.capture(), reliableCap.capture());

        assertEquals(msgCap.getValue().getPayload(), ByteString.EMPTY);
        assertEquals(msgCap.getValue().getSender().getName(), agentMemberName);
        assertEquals(msgCap.getValue().getSender().getEndpoint(), endpoint1);
        assertEquals(addrCap.getValue().getName(), agentMemberName2);
        assertEquals(addrCap.getValue().getEndpoint(), endpoint1);
        assertFalse(reliableCap.getValue());
    }

    @Test
    public void multicast() {
        String senderName = "sender";
        String receiverGroupName = "receiverGroup";
        AgentMember sender = new AgentMember(toAgentMemberAddr(senderName, endpoint1),
                orMap, agentMessenger, scheduler, memberAddresses);

        when(memberAddresses.get()).thenReturn(Sets.newHashSet(
                toAgentMemberAddr(receiverGroupName, endpoint1),
                toAgentMemberAddr(receiverGroupName, endpoint2)));

        when(agentMessenger.send(any(), any(), anyBoolean())).thenReturn(CompletableFuture.completedFuture(null));
        sender.multicast(receiverGroupName, ByteString.EMPTY, false);

        ArgumentCaptor<AgentMessage> msgCap = ArgumentCaptor.forClass(AgentMessage.class);
        ArgumentCaptor<AgentMemberAddr> addrCap = ArgumentCaptor.forClass(AgentMemberAddr.class);
        ArgumentCaptor<Boolean> reliableCap = ArgumentCaptor.forClass(Boolean.class);
        verify(agentMessenger, times(2)).send(msgCap.capture(), addrCap.capture(), reliableCap.capture());

        assertEquals(msgCap.getAllValues().get(0).getPayload(), ByteString.EMPTY);
        assertEquals(Sets.newHashSet(msgCap.getAllValues()).size(), 1);

        assertEquals(msgCap.getAllValues().get(0).getSender().getName(), senderName);
        assertEquals(msgCap.getAllValues().get(1).getSender().getName(), senderName);
        assertEquals(msgCap.getAllValues().get(0).getSender().getEndpoint(), endpoint1);
        assertEquals(msgCap.getAllValues().get(1).getSender().getEndpoint(), endpoint1);

        Set<HostEndpoint> receiverEndpoints = addrCap.getAllValues().stream().map(AgentMemberAddr::getEndpoint).collect(
                Collectors.toSet());
        assertTrue(receiverEndpoints.contains(endpoint1));
        assertTrue(receiverEndpoints.contains(endpoint2));

        assertFalse(reliableCap.getAllValues().get(0));
        assertFalse(reliableCap.getAllValues().get(1));
    }

    @Test
    public void receive() {
        String agentMemberName2 = "agentMember2";
        AgentMember agentMember1 = new AgentMember(agentMemberAddr, orMap, agentMessenger, scheduler, memberAddresses);
        AgentMember agentMember2 = new AgentMember(toAgentMemberAddr(agentMemberName2, endpoint1),
                orMap, agentMessenger, scheduler, memberAddresses);


        TestObserver<AgentMessage> testObserver = new TestObserver<>();
        agentMember1.receive().subscribe(testObserver);
        AgentMessage message1 = AgentMessage.newBuilder()
                .setSender(AgentMemberAddr.newBuilder().setName(agentMemberName2).setEndpoint(endpoint2).build())
                .setPayload(ByteString.copyFromUtf8("hello"))
                .build();
        AgentMessage message2 = AgentMessage.newBuilder()
                .setSender(AgentMemberAddr.newBuilder().setName(agentMemberName2).setEndpoint(endpoint2).build())
                .setPayload(ByteString.copyFromUtf8("world"))
                .build();
        messageSubject.onNext(AgentMessageEnvelope.newBuilder()
                .setReceiver(AgentMemberAddr.newBuilder().setName(agentMemberName).setEndpoint(endpoint2))
                .setMessage(message1)
                .build());
        messageSubject.onNext(AgentMessageEnvelope.newBuilder()
                .setReceiver(AgentMemberAddr.newBuilder().setName(agentMemberName).setEndpoint(endpoint1))
                .setMessage(message2)
                .build());
        testObserver.awaitCount(1);
        assertEquals(testObserver.values().get(0), message2);
    }

    @SneakyThrows
    @Test
    public void destroy() {
        AgentMember agentMember = new AgentMember(agentMemberAddr, orMap, agentMessenger, scheduler, memberAddresses);

        TestObserver<AgentMessage> msgObserver = new TestObserver<>();
        agentMember.receive().subscribe(msgObserver);
        agentMember.destroy().join();

        msgObserver.assertComplete();

        ArgumentCaptor<ORMapOperation> orMapOpCap = ArgumentCaptor.forClass(ORMapOperation.class);
        verify(orMap, atLeast(1)).execute(orMapOpCap.capture());
        ORMapOperation op = orMapOpCap.getAllValues().get(orMapOpCap.getAllValues().size() - 1);
        assertTrue(op instanceof ORMapOperation.ORMapRemove);
        assertEquals(((ORMapOperation.ORMapRemove) op).valueType, CausalCRDTType.mvreg);
        AgentMemberAddr key = AgentMemberAddr.parseFrom(op.keyPath[0]);
        assertEquals(agentMember.address(), key);
    }
}
