package com.zachary.bifromq.basecluster.transport;

import org.testng.annotations.Test;

import java.net.InetSocketAddress;

import static org.testng.Assert.assertTrue;

public class UDPTransportTest {
    @Test
    public void bindEphemeralPort() {
        UDPTransport transport = UDPTransport.builder()
            .bindAddr(new InetSocketAddress(0)).build();
        assertTrue(transport.bindAddress().getPort() > 0);
    }
}
