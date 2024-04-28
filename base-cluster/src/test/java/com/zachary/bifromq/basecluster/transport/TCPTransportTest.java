package com.zachary.bifromq.basecluster.transport;

import org.testng.annotations.Test;

import java.net.InetSocketAddress;

import static org.testng.Assert.assertTrue;

public class TCPTransportTest {
    @Test
    public void bindEphemeralPort() {
        TCPTransport transport = TCPTransport.builder()
            .bindAddr(new InetSocketAddress(0))
            .opts(new TCPTransport.TCPTransportOptions())
            .build();
        InetSocketAddress socketAddress = transport.bindAddress();
        assertTrue(socketAddress.getPort() > 0);
        transport.shutdown().join();
    }
}
