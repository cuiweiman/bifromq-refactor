package com.zachary.bifromq.basecluster.messenger;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import com.zachary.bifromq.basecluster.membership.proto.Quit;
import com.zachary.bifromq.basecluster.proto.ClusterMessage;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.schedulers.Timed;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;

@Slf4j
public class MessengerFuncTest {
    private IRecipientSelector localRecipientSelector;
    private IRecipientSelector remoteRecipientSelector;
    private Messenger localMessenger;
    private Messenger remoteMessenger;
    private TestObserver<Timed<MessageEnvelope>> localObserver;
    private TestObserver<Timed<MessageEnvelope>> remoteObserver;
    private InetSocketAddress local;
    private InetSocketAddress remote;
    private int maxFanout = 1;
    private int maxFanoutGossips = 5;
    private int retransmitMultiplier = 1;
    private int udpPacketLimit = 1000;
    private int clusterSize = 4;
    private Duration spreadPeriod = Duration.ofMillis(500);
    private Scheduler scheduler = Schedulers.single();
    private ClusterMessage quit;

    @BeforeMethod(alwaysRun = true)
    public void init() {
        MessengerOptions opts = new MessengerOptions();
        opts.maxFanout(maxFanout)
                .maxHealthScore(4)
                .maxFanoutGossips(maxFanoutGossips)
                .retransmitMultiplier(retransmitMultiplier)
                .spreadPeriod(spreadPeriod)
                .transporterOptions().mtu(udpPacketLimit);
        localObserver = TestObserver.create();
        remoteObserver = TestObserver.create();

        localMessenger = Messenger.builder()
                .bindAddr(new InetSocketAddress("localhost", 0))
                .scheduler(scheduler)
                .opts(opts)
                .build();
        local = localMessenger.bindAddress();
        remoteMessenger = Messenger.builder()
                .bindAddr(new InetSocketAddress("localhost", 0))
                .scheduler(scheduler)
                .opts(opts)
                .build();
        remote = remoteMessenger.bindAddress();
        localRecipientSelector = new IRecipientSelector() {
            @Override
            public Collection<? extends IRecipient> selectForSpread(int limit) {
                return Collections.singleton(() -> remote);
            }

            @Override
            public int clusterSize() {
                return clusterSize;
            }
        };
        remoteRecipientSelector = new IRecipientSelector() {
            @Override
            public Collection<? extends IRecipient> selectForSpread(int limit) {
                return Collections.singleton(() -> local);
            }

            @Override
            public int clusterSize() {
                return clusterSize;
            }
        };

        remoteMessenger.receive().subscribe(remoteObserver::onNext);
        remoteMessenger.start(remoteRecipientSelector);

        quit = ClusterMessage.newBuilder()
                .setQuit(Quit.newBuilder()
                        .setEndpoint(HostEndpoint.newBuilder()
                                .setId(ByteString.copyFromUtf8("demo"))
                                .setAddress(local.getAddress().getHostAddress())
                                .setPort(local.getPort())
                                .build())
                        .setIncarnation(1)
                        .build())
                .build();
    }

    @AfterMethod(alwaysRun = true)
    public void close() {
        localMessenger.shutdown().join();
        remoteMessenger.shutdown().join();
    }

    @SneakyThrows
    @Test(groups = "integration")
    public void testGossipHeardFromSelf() {
        localMessenger.receive().subscribe(localObserver::onNext);
        localMessenger.start(localRecipientSelector);
        localMessenger.spread(quit);
        localObserver.awaitCount(1).await(1000, TimeUnit.MILLISECONDS);
        localObserver.assertValueCount(1);
        assertTrue(localObserver.values().get(0).value().message.hasQuit());
        assertTrue(localObserver.values().get(0).value().recipient.equals(local));
    }

    @SneakyThrows
    @Test(groups = "integration")
    public void testSpreadFromRemote() {
        localMessenger.receive().subscribe(localObserver::onNext);
        localMessenger.start(localRecipientSelector);
        remoteMessenger.spread(quit);
        remoteObserver.awaitCount(1).await(1000, TimeUnit.MILLISECONDS);
        remoteObserver.assertValueCount(1);
        localObserver.awaitCount(1).await(1000, TimeUnit.MILLISECONDS);
        localObserver.assertValueCount(1);
        assertTrue(remoteObserver.values().get(0).value().message.hasQuit());
        assertTrue(remoteObserver.values().get(0).value().recipient.equals(remote));
        assertTrue(localObserver.values().get(0).value().message.hasQuit());
        assertTrue(localObserver.values().get(0).value().recipient.equals(local));
    }

    @SneakyThrows
    @Test(groups = "integration")
    public void testDirectMessageFromRemote() {
        localMessenger.receive().subscribe(localObserver::onNext);
        localMessenger.start(localRecipientSelector);
        remoteMessenger.send(quit, local, true);
        localObserver.awaitCount(1).await(1000, TimeUnit.MILLISECONDS);
        localObserver.assertValueCount(1);
        assertTrue(localObserver.values().get(0).value().message.hasQuit());
    }

    @SneakyThrows
    @Test(groups = "integration")
    public void testSendPiggybackedGossips() {
        localMessenger.receive().subscribe(localObserver::onNext);
        localMessenger.start(localRecipientSelector);
        remoteMessenger.send(quit, Collections.singletonList(quit), local, true);
        localObserver.awaitCount(2).await(1000, TimeUnit.MILLISECONDS);
        localObserver.assertValueCount(2);
        assertTrue(localObserver.values().get(0).value().message.hasQuit());
        assertTrue(localObserver.values().get(1).value().message.hasQuit());
    }
}
