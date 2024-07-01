package com.zachary.bifromq.dist.server;

import com.zachary.bifromq.plugin.subbroker.DeliveryPack;
import com.zachary.bifromq.plugin.subbroker.DeliveryResult;
import com.zachary.bifromq.type.ClientInfo;
import com.zachary.bifromq.type.QoS;
import com.zachary.bifromq.type.SubInfo;
import com.zachary.bifromq.type.TopicMessagePack;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

@Slf4j
public class DistTest extends DistServiceTest {
    private final String tenantId = "tenantA";

    @SneakyThrows
    @Test(groups = "integration")
    public void distWithNoSub() {
        Mockito.lenient().when(inboxDeliverer.deliver(any()))
            .thenAnswer((Answer<CompletableFuture<Map<SubInfo, DeliveryResult>>>) invocation -> {
                Iterable<DeliveryPack> inboxPacks = invocation.getArgument(0);
                Map<SubInfo, DeliveryResult> resultMap = new HashMap<>();
                for (DeliveryPack inboxWrite : inboxPacks) {
                    for (SubInfo subInfo : inboxWrite.inboxes) {
                        resultMap.put(subInfo, DeliveryResult.OK);
                    }
                }
                return CompletableFuture.completedFuture(resultMap);
            });
        long reqId = System.nanoTime();
        ByteBuffer payload = ByteString.EMPTY.asReadOnlyByteBuffer();
        ClientInfo clientInfo;
        int total = 10;
        CountDownLatch latch = new CountDownLatch(total);

        for (int i = 0; i < total; i++) {
            clientInfo = ClientInfo.newBuilder()
                .setTenantId("trafficA")
                .putMetadata("userId", "user" + i)
                .build();
            try {
                distClient().pub(reqId, "/sport/tennis" + i, QoS.AT_LEAST_ONCE, payload, Integer.MAX_VALUE, clientInfo)
                    .join();
            } catch (Throwable e) {
                fail();
            } finally {
                latch.countDown();
            }
        }
        latch.await();
    }

    @SneakyThrows
    @Test(groups = "integration")
    public void distWithSub() {
        Mockito.lenient().when(inboxDeliverer.deliver(any()))
            .thenAnswer((Answer<CompletableFuture<Map<SubInfo, DeliveryResult>>>) invocation -> {
                Iterable<DeliveryPack> inboxPacks = invocation.getArgument(0);
                Map<SubInfo, DeliveryResult> resultMap = new HashMap<>();
                for (DeliveryPack inboxWrite : inboxPacks) {
                    for (SubInfo subInfo : inboxWrite.inboxes) {
                        resultMap.put(subInfo, DeliveryResult.OK);
                    }
                }
                return CompletableFuture.completedFuture(resultMap);
            });
        long reqId = System.nanoTime();
        ByteBuffer payload = ByteString.EMPTY.asReadOnlyByteBuffer();
        ClientInfo clientInfo;
        int total = 1;
        for (int i = 0; i < total; i++) {
            distClient().sub(reqId, tenantId, "/sport/tennis" + i, QoS.AT_LEAST_ONCE, "inbox" + i, "server1", 0)
                .join();
        }
        CountDownLatch latch = new CountDownLatch(total);
        for (int i = 0; i < total; i++) {
            clientInfo = ClientInfo.newBuilder()
                .setTenantId(tenantId)
                .putMetadata("userId", "user" + i)
                .build();
            try {
                distClient().pub(reqId, "/sport/tennis" + i, QoS.AT_LEAST_ONCE, payload, Integer.MAX_VALUE, clientInfo)
                    .join();
            } catch (Throwable e) {
                fail();
            } finally {
                latch.countDown();
            }
        }
        latch.await();
    }

    @SneakyThrows
    @Test(groups = "integration")
    public void distWithSharedSub() {
        Mockito.lenient().when(inboxDeliverer.deliver(any()))
            .thenAnswer((Answer<CompletableFuture<Map<SubInfo, DeliveryResult>>>) invocation -> {
                Iterable<DeliveryPack> inboxPacks = invocation.getArgument(0);
                Map<SubInfo, DeliveryResult> resultMap = new HashMap<>();
                for (DeliveryPack inboxWrite : inboxPacks) {
                    for (SubInfo subInfo : inboxWrite.inboxes) {
                        resultMap.put(subInfo, DeliveryResult.OK);
                    }
                }
                return CompletableFuture.completedFuture(resultMap);
            });
        long reqId = System.nanoTime();
        ByteBuffer payload = ByteString.EMPTY.asReadOnlyByteBuffer();
        ClientInfo clientInfo;
        int totalSub = 5;
        int totalMsg = 1;
        for (int i = 0; i < totalSub; i++) {
            int subResult =
                distClient().sub(reqId, tenantId, "$share/g1/sport/tennis" + i, QoS.AT_LEAST_ONCE, "inbox" + i,
                    "server1", 0).join();
            assertEquals(subResult, 1);
        }
        CountDownLatch latch = new CountDownLatch(totalMsg);
        for (int i = 0; i < totalMsg; i++) {
            clientInfo = ClientInfo.newBuilder().setTenantId(tenantId)
                .putMetadata("userId", "user" + i)
                .build();
            try {
                distClient().pub(reqId, "/sport/tennis" + i, QoS.AT_LEAST_ONCE, payload, Integer.MAX_VALUE, clientInfo)
                    .join();
            } catch (Throwable e) {
                fail();
            } finally {
                latch.countDown();
            }
        }
        latch.await();
    }

    @SneakyThrows
    @Test(groups = "integration")
    public void distWithFanOutSub() {
        List<Iterable<DeliveryPack>> capturedArguments = new CopyOnWriteArrayList<>();
        when(inboxDeliverer.deliver(any()))
            .thenAnswer((Answer<CompletableFuture<Map<SubInfo, DeliveryResult>>>) invocation -> {
                Iterable<DeliveryPack> inboxPacks = invocation.getArgument(0);
                // the argument object will be reused, so make a clone
                capturedArguments.add(inboxPacks);
                Map<SubInfo, DeliveryResult> resultMap = new HashMap<>();
                for (DeliveryPack inboxWrite : inboxPacks) {
                    for (SubInfo subInfo : inboxWrite.inboxes) {
                        resultMap.put(subInfo, DeliveryResult.OK);
                    }
                }
                return CompletableFuture.completedFuture(resultMap);
            });

        long reqId = System.nanoTime();
        ByteBuffer payload = ByteString.EMPTY.asReadOnlyByteBuffer();
        int totalInbox = 100;
        for (int i = 0; i < totalInbox; i++) {
            distClient().sub(reqId, tenantId, "/sport/tennis", QoS.AT_LEAST_ONCE, "inbox" + i, "server1", 0)
                .join();
        }

        int totalPub = 2;
        ClientInfo pubClient = ClientInfo.newBuilder()
            .setTenantId(tenantId)
            .putMetadata("userId", "pubUser")
            .build();
        for (int i = 0; i < totalPub; i++) {
            distClient().pub(reqId, "/sport/tennis", QoS.AT_LEAST_ONCE, payload, Integer.MAX_VALUE, pubClient).join();
        }

        Thread.sleep(100);

        Set<SubInfo> subInfos = new HashSet<>();
        int msgCount = 0;
        for (Iterable<DeliveryPack> writeReq : capturedArguments) {
            for (DeliveryPack pack : writeReq) {
                TopicMessagePack msgs = pack.messagePack;
                Set<SubInfo> inboxes = Sets.newHashSet(pack.inboxes);
                subInfos.addAll(inboxes);
                msgCount += msgs.getMessageCount() * inboxes.size();
            }
        }
        assertEquals(subInfos.size(), totalInbox);
        assertEquals(msgCount, totalInbox * totalPub);
    }
}
