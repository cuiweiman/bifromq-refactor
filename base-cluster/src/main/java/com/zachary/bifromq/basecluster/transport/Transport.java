package com.zachary.bifromq.basecluster.transport;

import com.google.protobuf.ByteString;
import io.netty.handler.ssl.SslContext;
import io.reactivex.rxjava3.core.Observable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/24 11:58
 */
public class Transport implements ITransport {

    @Getter
    @Setter
    @Builder(toBuilder = true)
    @Accessors(chain = true, fluent = true)
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TransportOptions {
        private long mtu = 1400;
        private TCPTransport.TCPTransportOptions tcpTransportOptions = new TCPTransport.TCPTransportOptions();
    }

    private final ITransport tcpTransport;

    private final ITransport udpTransport;

    private final TransportOptions options;

    private final Observable<PacketEnvelope> sink;

    @Builder
    Transport(InetSocketAddress bindAddr,
              SslContext serverSslContext,
              SslContext clientSslContext,
              String sharedToken,
              TransportOptions options) {
        this.options = options == null ? new TransportOptions() : options.toBuilder().build();
        if (bindAddr == null) {
            bindAddr = new InetSocketAddress(0);
        }
        tcpTransport = TCPTransport.builder()
                .bindAddr(bindAddr)
                .sharedToken(sharedToken)
                .serverSslContext(serverSslContext)
                .clientSslContext(clientSslContext)
                .opts(this.options.tcpTransportOptions)
                .build();
        // bind to same address
        udpTransport = UDPTransport.builder().sharedToken(sharedToken)
                .bindAddr(tcpTransport.bindAddress())
                .build();

        sink = Observable.merge(tcpTransport.receive(), udpTransport.receive());
    }


    @Override
    public InetSocketAddress bindAddress() {
        return tcpTransport.bindAddress();
    }

    public CompletableFuture<Void> send(List<ByteString> data, InetSocketAddress recipient) {
        int size = data.stream().map(ByteString::size).reduce(0, Integer::sum);
        try {
            if (Boolean.TRUE.equals(RELIABLE.get()) || size > options.mtu) {
                return tcpTransport.send(data, recipient);
            } else {
                return udpTransport.send(data, recipient);
            }
        } finally {
            RELIABLE.set(false);
        }
    }

    public Observable<PacketEnvelope> receive() {
        return sink;
    }

    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.allOf(
                tcpTransport.shutdown().exceptionally(e -> null),
                udpTransport.shutdown().exceptionally(e -> null)
        );
    }
}
