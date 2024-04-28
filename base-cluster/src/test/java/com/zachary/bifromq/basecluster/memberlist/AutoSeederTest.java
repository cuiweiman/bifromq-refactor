package com.zachary.bifromq.basecluster.memberlist;

import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import com.zachary.bifromq.basecluster.messenger.IMessenger;
import com.zachary.bifromq.basecluster.messenger.MessageEnvelope;
import com.zachary.bifromq.basecluster.proto.ClusterMessage;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.schedulers.Timed;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.SneakyThrows;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import static com.zachary.bifromq.basecluster.memberlist.Fixtures.LOCAL;
import static com.zachary.bifromq.basecluster.memberlist.Fixtures.LOCAL_ENDPOINT;
import static com.zachary.bifromq.basecluster.memberlist.Fixtures.REMOTE_ADDR_1;
import static com.zachary.bifromq.basecluster.memberlist.Fixtures.REMOTE_HOST_1_ENDPOINT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class AutoSeederTest {
    @Mock
    private IMessenger messenger;
    @Mock
    private IHostMemberList memberList;
    @Mock
    private IHostAddressResolver addressResolver;
    private Scheduler scheduler = Schedulers.from(Executors.newSingleThreadScheduledExecutor());
    private PublishSubject<Map<HostEndpoint, Integer>> membersSubject = PublishSubject.create();
    private PublishSubject<Timed<MessageEnvelope>> messageSubject = PublishSubject.create();
    private Duration joinTimeout = Duration.ofSeconds(1);
    private Duration joinInterval = Duration.ofMillis(100);
    private AutoCloseable closeable;
    @BeforeMethod
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        when(addressResolver.resolve(REMOTE_HOST_1_ENDPOINT)).thenReturn(REMOTE_ADDR_1);
        when(memberList.local()).thenReturn(LOCAL);
        when(memberList.members()).thenReturn(membersSubject);
    }

    @AfterMethod
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void join() {
        AutoSeeder seeder =
            new AutoSeeder(messenger, scheduler, memberList, addressResolver, joinTimeout, joinInterval);

        try {
            seeder.join(Collections.singleton(REMOTE_ADDR_1)).join();
            fail();
        } catch (Throwable e) {
            ArgumentCaptor<ClusterMessage> msgCap = ArgumentCaptor.forClass(ClusterMessage.class);
            ArgumentCaptor<InetSocketAddress> addCap = ArgumentCaptor.forClass(InetSocketAddress.class);
            ArgumentCaptor<Boolean> reliableCap = ArgumentCaptor.forClass(Boolean.class);

            verify(messenger, atMost(10)).send(msgCap.capture(), addCap.capture(), reliableCap.capture());

            assertEquals(msgCap.getValue().getJoin().getMember().getEndpoint(), LOCAL_ENDPOINT);
            assertEquals(msgCap.getValue().getJoin().getMember().getIncarnation(), 0);
            assertFalse(msgCap.getValue().getJoin().hasExpectedHost());
            assertEquals(addCap.getValue(), REMOTE_ADDR_1);
            assertTrue(reliableCap.getValue());
        }
    }

    @Test
    public void joinKnownEndpoint() {
        AutoSeeder seeder =
            new AutoSeeder(messenger, scheduler, memberList, addressResolver, joinTimeout, joinInterval);
        membersSubject.onNext(new HashMap<>() {{
            put(REMOTE_HOST_1_ENDPOINT, 0);
        }});
        try {
            seeder.join(Collections.singleton(REMOTE_ADDR_1)).join();
            verify(messenger, atMost(0)).send(any(), any(), anyBoolean());
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    public void stopJoinEarlier() {
        AutoSeeder seeder =
            new AutoSeeder(messenger, scheduler, memberList, addressResolver, joinTimeout, joinInterval);
        CompletableFuture<Void> joinResult = seeder.join(Collections.singleton(REMOTE_ADDR_1));
        verify(messenger, timeout(200).atLeast(1)).send(any(), any(), anyBoolean());
        membersSubject.onNext(new HashMap<>() {{
            put(REMOTE_HOST_1_ENDPOINT, 0);
        }});
        joinResult.join();
    }

    @SneakyThrows
    @Test
    public void joinDomain() {
        String domain = "test.domain";
        InetAddress addr = InetAddress.getByName(REMOTE_HOST_1_ENDPOINT.getAddress());
        try (MockedStatic<InetAddress> mockAgent = mockStatic(InetAddress.class)) {
            mockAgent.when(() -> InetAddress.getByName(any())).thenReturn(addr);
            mockAgent.when(() -> InetAddress.getAllByName(domain))
                .thenReturn(new InetAddress[] {addr});
            AutoSeeder seeder =
                new AutoSeeder(messenger, scheduler, memberList, addressResolver, joinTimeout, joinInterval);
            seeder.join(domain, REMOTE_HOST_1_ENDPOINT.getPort()).join();
        } catch (Throwable e) {
            ArgumentCaptor<ClusterMessage> msgCap = ArgumentCaptor.forClass(ClusterMessage.class);
            ArgumentCaptor<InetSocketAddress> addCap = ArgumentCaptor.forClass(InetSocketAddress.class);
            ArgumentCaptor<Boolean> reliableCap = ArgumentCaptor.forClass(Boolean.class);
            verify(messenger, atLeast(1)).send(msgCap.capture(), addCap.capture(), reliableCap.capture());
            assertEquals(msgCap.getValue().getJoin().getMember().getEndpoint(), LOCAL_ENDPOINT);
            assertEquals(msgCap.getValue().getJoin().getMember().getIncarnation(), 0);
            assertFalse(msgCap.getValue().getJoin().hasExpectedHost());
            assertEquals(addCap.getValue(), REMOTE_ADDR_1);
            assertTrue(reliableCap.getValue());
        }
    }
}
