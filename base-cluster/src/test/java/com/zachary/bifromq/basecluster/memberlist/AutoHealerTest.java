package com.zachary.bifromq.basecluster.memberlist;

import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import com.zachary.bifromq.basecluster.membership.proto.HostMember;
import com.zachary.bifromq.basecluster.messenger.IMessenger;
import com.zachary.bifromq.basecluster.messenger.MessageEnvelope;
import com.zachary.bifromq.basecluster.proto.ClusterMessage;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.schedulers.Timed;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import static com.zachary.bifromq.basecluster.memberlist.Fixtures.LOCAL;
import static com.zachary.bifromq.basecluster.memberlist.Fixtures.LOCAL_ENDPOINT;
import static com.zachary.bifromq.basecluster.memberlist.Fixtures.REMOTE_ADDR_1;
import static com.zachary.bifromq.basecluster.memberlist.Fixtures.REMOTE_HOST_1_ENDPOINT;
import static com.zachary.bifromq.basecluster.memberlist.Fixtures.ZOMBIE_ENDPOINT;
import static com.zachary.bifromq.basecluster.memberlist.Fixtures.endorseMsg;
import static com.zachary.bifromq.basecluster.memberlist.Fixtures.failMsg;
import static com.zachary.bifromq.basecluster.memberlist.Fixtures.joinMsg;
import static com.zachary.bifromq.basecluster.memberlist.Fixtures.quitMsg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AutoHealerTest {
    @Mock
    private IMessenger messenger;
    @Mock
    private IHostMemberList memberList;
    @Mock
    private IHostAddressResolver addressResolver;
    private PublishSubject<Map<HostEndpoint, Integer>> membersSubject = PublishSubject.create();
    private PublishSubject<Timed<MessageEnvelope>> messageSubject = PublishSubject.create();
    private Scheduler scheduler = Schedulers.from(Executors.newSingleThreadScheduledExecutor());
    private Duration healingTimeout = Duration.ofSeconds(1);
    private Duration healingInterval = Duration.ofMillis(100);
    private AutoCloseable closeable;

    @BeforeMethod
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        when(addressResolver.resolve(REMOTE_HOST_1_ENDPOINT)).thenReturn(REMOTE_ADDR_1);
        when(memberList.local()).thenReturn(LOCAL);
        when(messenger.receive()).thenReturn(messageSubject);
        when(memberList.members()).thenReturn(membersSubject);
    }

    @AfterMethod
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void init() {
        AutoHealer healer =
                new AutoHealer(messenger, scheduler, memberList, addressResolver, healingTimeout, healingInterval);
    }

    @Test
    public void healTimeout() {
        AutoHealer healer =
                new AutoHealer(messenger, scheduler, memberList, addressResolver, healingTimeout, healingInterval);
        membersSubject.onNext(new HashMap<>() {{
            put(LOCAL_ENDPOINT, 0);
            put(REMOTE_HOST_1_ENDPOINT, 0);
        }});
        messageSubject.onNext(failMsg(REMOTE_HOST_1_ENDPOINT, 0));

        ArgumentCaptor<ClusterMessage> msgCap = ArgumentCaptor.forClass(ClusterMessage.class);
        ArgumentCaptor<InetSocketAddress> addCap = ArgumentCaptor.forClass(InetSocketAddress.class);
        ArgumentCaptor<Boolean> reliableCap = ArgumentCaptor.forClass(Boolean.class);
        verify(messenger, timeout(1500).atLeast(9)).send(msgCap.capture(), addCap.capture(), reliableCap.capture());
        assertTrue(msgCap.getAllValues().size() <= 10);
    }

    @Test
    public void handleFail() {
        AutoHealer healer =
                new AutoHealer(messenger, scheduler, memberList, addressResolver, healingTimeout, healingInterval);
        membersSubject.onNext(new HashMap<>() {{
            put(LOCAL_ENDPOINT, 0);
            put(REMOTE_HOST_1_ENDPOINT, 0);
        }});
        messageSubject.onNext(failMsg(REMOTE_HOST_1_ENDPOINT, 0));

        ArgumentCaptor<ClusterMessage> msgCap = ArgumentCaptor.forClass(ClusterMessage.class);
        ArgumentCaptor<InetSocketAddress> addCap = ArgumentCaptor.forClass(InetSocketAddress.class);
        ArgumentCaptor<Boolean> reliableCap = ArgumentCaptor.forClass(Boolean.class);
        verify(messenger, timeout(1500).atLeast(1)).send(msgCap.capture(), addCap.capture(), reliableCap.capture());

        assertEquals(msgCap.getValue().getJoin().getMember().getEndpoint(), LOCAL_ENDPOINT);
        assertEquals(msgCap.getValue().getJoin().getMember().getIncarnation(), 0);
        assertEquals(addCap.getValue(), REMOTE_ADDR_1);
        assertTrue(reliableCap.getValue());
    }

    @Test
    public void handleIgnoreFail() {
        AutoHealer healer =
                new AutoHealer(messenger, scheduler, memberList, addressResolver, healingTimeout, healingInterval);
        messageSubject.onNext(failMsg(LOCAL_ENDPOINT, 0));

        assertNoMoreHealingMessages();
    }

    @Test
    public void handleZombieFail() {
        AutoHealer healer =
                new AutoHealer(messenger, scheduler, memberList, addressResolver, healingTimeout, healingInterval);
        messageSubject.onNext(failMsg(ZOMBIE_ENDPOINT, 0));

        assertNoMoreHealingMessages();
    }

    @Test
    public void handleNotKnownFail() {
        AutoHealer healer =
                new AutoHealer(messenger, scheduler, memberList, addressResolver, healingTimeout, healingInterval);
        messageSubject.onNext(failMsg(REMOTE_HOST_1_ENDPOINT, 0));

        assertNoMoreHealingMessages();
    }

    @Test
    public void handleObsoleteFail() {
        AutoHealer healer =
                new AutoHealer(messenger, scheduler, memberList, addressResolver, healingTimeout, healingInterval);
        membersSubject.onNext(new HashMap<>() {{
            put(LOCAL_ENDPOINT, 0);
            put(REMOTE_HOST_1_ENDPOINT, 1); // newer incarnation
        }});

        messageSubject.onNext(failMsg(REMOTE_HOST_1_ENDPOINT, 0));

        assertNoMoreHealingMessages();
    }

    @Test
    public void handleJoin() {
        handleHealingMsg(joinMsg(HostMember.newBuilder()
                .setIncarnation(1)
                .setEndpoint(REMOTE_HOST_1_ENDPOINT)
                .build()));
    }

    @Test
    public void handleObsoleteJoin() {
        handleObsoleteHealingMsg(joinMsg(HostMember.newBuilder()
                .setIncarnation(0) // older incarnation
                .setEndpoint(REMOTE_HOST_1_ENDPOINT)
                .build()));
        handleObsoleteHealingMsg(joinMsg(HostMember.newBuilder()
                .setIncarnation(1) // same incarnation
                .setEndpoint(REMOTE_HOST_1_ENDPOINT)
                .build()));
    }

    @Test
    public void handleQuit() {
        handleHealingMsg(quitMsg(REMOTE_HOST_1_ENDPOINT, 1));
    }

    @Test
    public void handleObsoleteQuit() {
        handleObsoleteHealingMsg(quitMsg(REMOTE_HOST_1_ENDPOINT, 0));
    }

    @Test
    public void handleEndorse() {
        handleHealingMsg(endorseMsg(REMOTE_HOST_1_ENDPOINT, 1));
    }

    @Test
    public void handleObsoleteEndorse() {
        handleObsoleteHealingMsg(endorseMsg(REMOTE_HOST_1_ENDPOINT, 0));
    }

    private void handleHealingMsg(Timed<MessageEnvelope> msg) {
        AutoHealer healer =
                new AutoHealer(messenger, scheduler, memberList, addressResolver, Duration.ofSeconds(2), healingInterval);
        membersSubject.onNext(new HashMap<>() {{
            put(LOCAL_ENDPOINT, 0);
            put(REMOTE_HOST_1_ENDPOINT, 0);
        }});
        messageSubject.onNext(failMsg(REMOTE_HOST_1_ENDPOINT, 0));

        verify(messenger, timeout(200).atLeast(1)).send(any(), any(), anyBoolean());

        messageSubject.onNext(msg);

        ArgumentCaptor<ClusterMessage> msgCap = ArgumentCaptor.forClass(ClusterMessage.class);
        ArgumentCaptor<InetSocketAddress> addCap = ArgumentCaptor.forClass(InetSocketAddress.class);
        ArgumentCaptor<Boolean> reliableCap = ArgumentCaptor.forClass(Boolean.class);
        verify(messenger, timeout(2500).atLeast(0)).send(msgCap.capture(), addCap.capture(), reliableCap.capture());
        assertTrue(msgCap.getAllValues().size() < 9);
    }

    private void handleObsoleteHealingMsg(Timed<MessageEnvelope> msg) {
        AutoHealer healer =
                new AutoHealer(messenger, scheduler, memberList, addressResolver, healingTimeout, healingInterval);
        membersSubject.onNext(new HashMap<>() {{
            put(LOCAL_ENDPOINT, 0);
            put(REMOTE_HOST_1_ENDPOINT, 1);
        }});
        messageSubject.onNext(failMsg(REMOTE_HOST_1_ENDPOINT, 1));

        messageSubject.onNext(msg);

        ArgumentCaptor<ClusterMessage> msgCap = ArgumentCaptor.forClass(ClusterMessage.class);
        ArgumentCaptor<InetSocketAddress> addCap = ArgumentCaptor.forClass(InetSocketAddress.class);
        ArgumentCaptor<Boolean> reliableCap = ArgumentCaptor.forClass(Boolean.class);
        verify(messenger, timeout(1500).atLeast(9)).send(msgCap.capture(), addCap.capture(), reliableCap.capture());
        assertTrue(msgCap.getAllValues().size() <= 10);
    }

    private void assertNoMoreHealingMessages() {
        ArgumentCaptor<ClusterMessage> msgCap = ArgumentCaptor.forClass(ClusterMessage.class);
        ArgumentCaptor<InetSocketAddress> addCap = ArgumentCaptor.forClass(InetSocketAddress.class);
        ArgumentCaptor<Boolean> reliableCap = ArgumentCaptor.forClass(Boolean.class);
        verify(messenger, timeout(1500).atLeast(0)).send(msgCap.capture(), addCap.capture(), reliableCap.capture());
        assertTrue(msgCap.getAllValues().isEmpty());
    }
}
