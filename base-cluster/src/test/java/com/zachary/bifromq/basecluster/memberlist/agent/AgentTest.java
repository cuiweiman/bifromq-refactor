package com.zachary.bifromq.basecluster.memberlist.agent;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr;
import com.zachary.bifromq.basecluster.agent.proto.AgentMemberMetadata;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

@Slf4j
public class AgentTest {
    private String agentId = "agentA";
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
    private Replica replica = Replica.newBuilder().setUri(CRDTUtil.toAgentURI(agentId)).build();
    @Mock
    private IAgentMessenger agentMessenger;
    private Scheduler scheduler; // make test more deterministic
    @Mock
    private ICRDTStore crdtStore;
    @Mock
    private IAgentHostProvider hostProvider;
    @Mock
    private IORMap orMap;
    private PublishSubject<Long> inflationSubject;
    private PublishSubject<Set<HostEndpoint>> hostsSubjects;
    private PublishSubject<AgentMessageEnvelope> messageSubject;
    private AutoCloseable closeable;

    @BeforeMethod
    public void setup() {

        closeable = MockitoAnnotations.openMocks(this);
        scheduler = Schedulers.from(MoreExecutors.directExecutor());
        inflationSubject = PublishSubject.create();
        hostsSubjects = PublishSubject.create();
        messageSubject = PublishSubject.create();
        when(crdtStore.host(CRDTUtil.toAgentURI(agentId), endpoint1.toByteString())).thenReturn(replica);
        when(crdtStore.get(CRDTUtil.toAgentURI(agentId))).thenReturn(Optional.of(orMap));
        when(orMap.execute(any())).thenReturn(CompletableFuture.completedFuture(null));
        when(orMap.id()).thenReturn(replica);
        when(orMap.inflation()).thenReturn(inflationSubject);
        when(hostProvider.getHostEndpoints()).thenReturn(hostsSubjects);
        when(agentMessenger.receive()).thenReturn(messageSubject);
    }

    @AfterMethod
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void init() {
        Agent agent = new Agent(agentId, endpoint1, agentMessenger, scheduler, crdtStore, hostProvider);
        assertEquals(agent.id(), agentId);
        assertEquals(agent.endpoint(), endpoint1);
    }

    @SneakyThrows
    @Test
    public void register() {
        String agentMemberName = "agentMember1";
        ByteString meta1 = ByteString.EMPTY;
        ByteString meta2 = ByteString.copyFromUtf8("Hello");

        Agent agent = new Agent(agentId, endpoint1, agentMessenger, scheduler, crdtStore, hostProvider);
        IAgentMember agentMember = agent.register(agentMemberName);
        assertEquals(agentMember.address().getName(), agentMemberName);
        assertEquals(agentMember.address().getEndpoint(), endpoint1);

        ArgumentCaptor<ORMapOperation> orMapOpCap = ArgumentCaptor.forClass(ORMapOperation.class);
        verify(orMap, times(1)).execute(orMapOpCap.capture());
        ORMapOperation op = orMapOpCap.getAllValues().get(0);
        assertTrue(op instanceof ORMapOperation.ORMapUpdate);
        assertTrue(((ORMapOperation.ORMapUpdate) op).valueOp instanceof MVRegOperation);
        AgentMemberAddr key = AgentMemberAddr.parseFrom(op.keyPath[0]);
        assertEquals(key.getName(), agentMemberName);
        assertEquals(key.getEndpoint(), endpoint1);

        AgentMemberMetadata agentMemberMetadata1 =
                AgentMemberMetadata.parseFrom(((MVRegOperation) ((ORMapOperation.ORMapUpdate) op).valueOp).value);
        assertEquals(agentMemberMetadata1.getValue(), meta1);
        assertTrue(agentMemberMetadata1.getHlc() > 0);
    }

    @Test
    public void deregister() {
        String agentMemberName = "agentMember1";
        Agent agent = new Agent(agentId, endpoint1, agentMessenger, scheduler, crdtStore, hostProvider);
        IAgentMember agentMember = agent.register(agentMemberName);
        agent.deregister(agentMember).join();
        // nothing should happen
        agent.deregister(agentMember).join();

        // register again should return distinct object
        IAgentMember newAgentMember = agent.register(agentMemberName);
        assertNotEquals(newAgentMember, agentMember);
    }

    @Test
    public void membership() {
        String agentMemberName = "agentMember1";
        Agent agent = new Agent(agentId, endpoint1, agentMessenger, scheduler, crdtStore, hostProvider);
        TestObserver<Map<AgentMemberAddr, AgentMemberMetadata>> testObserver = new TestObserver<>();
        agent.membership().subscribe(testObserver);
        IAgentMember agentMember = agent.register(agentMemberName);
        com.zachary.bifromq.basecluster.agent.proto.AgentMember member =
                com.zachary.bifromq.basecluster.agent.proto.AgentMember.newBuilder()
                        .setAddr(AgentMemberAddr.newBuilder().setName(agentId).setEndpoint(endpoint1).build())
                        .setMetadata(AgentMemberMetadata.newBuilder().setValue(ByteString.EMPTY).build())
                        .build();
        MockUtil.mockAgentMemberCRDT(orMap, Collections.singletonMap(
                MockUtil.toAgentMemberAddr(agentMemberName, endpoint1),
                MockUtil.toAgentMemberMetadata(ByteString.EMPTY)));
        inflationSubject.onNext(System.currentTimeMillis());
        testObserver.awaitCount(2);
        assertEquals(testObserver.values().get(1).size(), 1);
    }

    @Test
    public void hostUpdate() {
        Agent agent = new Agent(agentId, endpoint1, agentMessenger, scheduler, crdtStore, hostProvider);
        MockUtil.mockAgentMemberCRDT(orMap, Collections.emptyMap());
        Set<HostEndpoint> endpoints = Sets.newHashSet(endpoint1, endpoint2);
        hostsSubjects.onNext(endpoints);

        verify(crdtStore).join(replica.getUri(), endpoint1.toByteString(),
                endpoints.stream().map(HostEndpoint::toByteString).collect(
                        Collectors.toSet()));
    }

    @SneakyThrows
    @Test
    public void dropMemberWhileHostUpdate() {
        String agentMember1 = "agentMember1";
        String agentMember2 = "agentMember2";
        Agent agent = new Agent(agentId, endpoint1, agentMessenger, scheduler, crdtStore, hostProvider);
        Map<AgentMemberAddr, AgentMemberMetadata> members = new HashMap<>();
        members.put(MockUtil.toAgentMemberAddr(agentMember1, endpoint1),
                MockUtil.toAgentMemberMetadata(ByteString.EMPTY));
        members.put(MockUtil.toAgentMemberAddr(agentMember2, endpoint2),
                MockUtil.toAgentMemberMetadata(ByteString.EMPTY));
        MockUtil.mockAgentMemberCRDT(orMap, members);
        Set<HostEndpoint> endpoints = Sets.newHashSet(endpoint1, endpoint2);
        hostsSubjects.onNext(endpoints);
        // mock ormap again
        MockUtil.mockAgentMemberCRDT(orMap, members);
        endpoints = Sets.newHashSet(endpoint1);
        hostsSubjects.onNext(endpoints);

        ArgumentCaptor<ORMapOperation> orMapOpCap = ArgumentCaptor.forClass(ORMapOperation.class);
        verify(orMap).execute(orMapOpCap.capture());
        ORMapOperation op = orMapOpCap.getValue();
        assertTrue(op instanceof ORMapOperation.ORMapRemove);
        assertEquals(((ORMapOperation.ORMapRemove) op).valueType, CausalCRDTType.mvreg);
        AgentMemberAddr key = AgentMemberAddr.parseFrom(op.keyPath[0]);
        assertEquals(key.getName(), agentMember2);
        assertEquals(key.getEndpoint(), endpoint2);

        verify(crdtStore).join(replica.getUri(), endpoint1.toByteString(),
                endpoints.stream().map(HostEndpoint::toByteString).collect(
                        Collectors.toSet()));
    }

    @Test
    public void quit() {
        Agent agent = new Agent(agentId, endpoint1, agentMessenger, scheduler, crdtStore, hostProvider);
        IAgentMember agentMember = agent.register("agentMember");
        when(crdtStore.stopHosting(replica.getUri())).thenReturn(CompletableFuture.completedFuture(null));
        TestObserver membersObserver = new TestObserver();
        agent.membership().subscribe(membersObserver);
        agent.quit().join();
        membersObserver.assertComplete();
        try {
            agentMember.broadcast(ByteString.copyFromUtf8("hello"), false);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
        }
        // nothing should happen
        agent.quit().join();
    }
}
