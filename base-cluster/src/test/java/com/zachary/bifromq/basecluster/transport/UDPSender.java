package com.zachary.bifromq.basecluster.transport;

import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class UDPSender {
    public static void main(String[] args) {
        InetSocketAddress remote = new InetSocketAddress("127.0.0.1", 2222);
        UDPTransport transport = UDPTransport.builder()
            .bindAddr(new InetSocketAddress("127.0.0.1", 1111))
            .build();
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        AtomicInteger count = new AtomicInteger();
        scheduledExecutorService.scheduleAtFixedRate(() -> transport.send(Arrays.asList(ByteString.EMPTY), remote)
            .thenAccept(v -> log.info("Sent ")), 0, 2, TimeUnit.SECONDS);

        transport.receive()
            .blockingSubscribe(packetEnvelope -> {
                List<ByteString> data = packetEnvelope.data;
                log.info("Data={}, Sender={}", data, packetEnvelope.sender);
                transport.send(data, packetEnvelope.sender);
            });
    }
}
