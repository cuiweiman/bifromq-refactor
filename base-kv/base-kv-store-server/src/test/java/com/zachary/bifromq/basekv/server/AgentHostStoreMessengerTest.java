package com.zachary.bifromq.basekv.server;

import com.zachary.bifromq.basecluster.IAgentHost;
import com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr;
import com.zachary.bifromq.basecluster.agent.proto.AgentMessage;
import com.zachary.bifromq.basecluster.memberlist.agent.IAgent;
import com.zachary.bifromq.basecluster.memberlist.agent.IAgentMember;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.KVRangeMessage;
import com.zachary.bifromq.basekv.proto.StoreMessage;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.zachary.bifromq.basekv.server.AgentHostStoreMessenger.agentId;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AgentHostStoreMessengerTest {
    @Mock
    private IAgentHost agentHost;
    @Mock
    private IAgent agent;

    private String clusterId = "testCluster";
    private String srcStore = "store1";

    @Mock
    private IAgentMember srcStoreAgentMember;
    private KVRangeId srcRange;
    private String targetStore = "store2";
    private PublishSubject<AgentMessage> tgtStoreMessageSubject;
    @Mock
    private IAgentMember tgtStoreAgentMember;
    private KVRangeId targetRange;
    private AutoCloseable closeable;
    @BeforeMethod
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        tgtStoreMessageSubject = PublishSubject.create();
        srcRange = KVRangeIdUtil.generate();
        targetRange = KVRangeIdUtil.generate();
        when(agentHost.host(agentId(clusterId))).thenReturn(agent);
        when(agent.register(srcStore)).thenReturn(srcStoreAgentMember);
        when(agent.register(targetStore)).thenReturn(tgtStoreAgentMember);
        when(tgtStoreAgentMember.receive()).thenReturn(tgtStoreMessageSubject);
    }

    @AfterMethod
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void init() {
        when(agentHost.host(agentId(clusterId))).thenReturn(agent);
        AgentHostStoreMessenger messenger = new AgentHostStoreMessenger(agentHost, clusterId, srcStore);

        ArgumentCaptor<String> agentMemberCap = ArgumentCaptor.forClass(String.class);
        verify(agent).register(agentMemberCap.capture());
        assertEquals(agentMemberCap.getValue(), srcStore);
    }

    @Test
    public void send() {
        AgentHostStoreMessenger messenger = new AgentHostStoreMessenger(agentHost, clusterId, srcStore);
        StoreMessage message = StoreMessage.newBuilder()
            .setFrom(srcStore)
            .setSrcRange(srcRange)
            .setPayload(KVRangeMessage.newBuilder().setHostStoreId(targetStore).setRangeId(targetRange).build())
            .build();
        messenger.send(message);
        ArgumentCaptor<String> targetMemberCap = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ByteString> msgCap = ArgumentCaptor.forClass(ByteString.class);
        ArgumentCaptor<Boolean> reliableCap = ArgumentCaptor.forClass(Boolean.class);
        verify(srcStoreAgentMember).multicast(targetMemberCap.capture(), msgCap.capture(),
            reliableCap.capture());

        assertEquals(targetMemberCap.getValue(), targetStore);
        assertEquals(msgCap.getValue(), message.toByteString());
        assertTrue(reliableCap.getValue());
    }

    @Test
    public void broadcast() {
        when(agentHost.host(agentId(clusterId))).thenReturn(agent);
        AgentHostStoreMessenger messenger = new AgentHostStoreMessenger(agentHost, clusterId, srcStore);
        StoreMessage message = StoreMessage.newBuilder()
            .setFrom(srcStore)
            .setSrcRange(srcRange)
            .setPayload(KVRangeMessage.newBuilder().setRangeId(targetRange).build())
            .build();
        messenger.send(message);
        ArgumentCaptor<ByteString> msgCap = ArgumentCaptor.forClass(ByteString.class);
        ArgumentCaptor<Boolean> reliableCap = ArgumentCaptor.forClass(Boolean.class);
        verify(srcStoreAgentMember).broadcast(msgCap.capture(), reliableCap.capture());
        assertEquals(msgCap.getValue(), message.toByteString());
        assertTrue(reliableCap.getValue());
    }

    @Test
    public void receiveSend() {
        AgentHostStoreMessenger messenger = new AgentHostStoreMessenger(agentHost, clusterId, targetStore);
        TestObserver<StoreMessage> testObserver = TestObserver.create();
        messenger.receive().subscribe(testObserver);

        StoreMessage message = StoreMessage.newBuilder()
            .setFrom(srcStore)
            .setSrcRange(srcRange)
            .setPayload(KVRangeMessage.newBuilder().setHostStoreId(targetStore).setRangeId(targetRange).build())
            .build();
        AgentMessage nodeMessage = AgentMessage.newBuilder()
            .setSender(AgentMemberAddr.newBuilder().setName(srcStore).build())
            .setPayload(message.toByteString())
            .build();
        tgtStoreMessageSubject.onNext(nodeMessage);
        testObserver.awaitCount(1);
        assertEquals(testObserver.values().get(0), message);
    }

    @Test
    public void receiveBroadcast() {
        AgentHostStoreMessenger messenger = new AgentHostStoreMessenger(agentHost, clusterId, targetStore);
        TestObserver<StoreMessage> testObserver = TestObserver.create();
        messenger.receive().subscribe(testObserver);

        StoreMessage message = StoreMessage.newBuilder()
            .setFrom(srcStore)
            .setSrcRange(srcRange)
            .setPayload(KVRangeMessage.newBuilder().setRangeId(targetRange).build())
            .build();
        AgentMessage nodeMessage = AgentMessage.newBuilder()
            .setSender(AgentMemberAddr.newBuilder().setName(srcStore).build())
            .setPayload(message.toByteString())
            .build();
        tgtStoreMessageSubject.onNext(nodeMessage);
        testObserver.awaitCount(1);
        assertEquals(testObserver.values().get(0).getPayload().getHostStoreId(), targetStore);
    }
}
