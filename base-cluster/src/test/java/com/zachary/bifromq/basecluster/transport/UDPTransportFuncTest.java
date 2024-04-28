package com.zachary.bifromq.basecluster.transport;

import com.zachary.bifromq.basecluster.transport.proto.Packet;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

@Slf4j
public class UDPTransportFuncTest {

    InetSocketAddress address = new InetSocketAddress("127.0.0.1", 12345);

    @Test
    public void testSendAndReceive() {
        UDPTransport transport = UDPTransport.builder().bindAddr(address).build();
        Packet packet = Packet.newBuilder()
            .addMessages(ByteString.copyFrom("test", Charset.defaultCharset()))
            .build();
        transport.send(packet.getMessagesList(), address).join();
        transport.shutdown();
    }
}
