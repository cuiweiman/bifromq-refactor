
package com.zachary.bifromq.basecluster.transport;

import com.google.protobuf.InvalidProtocolBufferException;
import com.zachary.bifromq.basecluster.transport.proto.Packet;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.DatagramPacketEncoder;
import io.netty.handler.codec.compression.FastLzFrameDecoder;
import io.netty.handler.codec.compression.FastLzFrameEncoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.subjects.CompletableSubject;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

import static com.zachary.bifromq.basecluster.transport.NettyUtil.getDatagramChannelClass;
import static com.zachary.bifromq.basecluster.transport.NettyUtil.getEventLoopGroup;


@Slf4j
public final class UDPTransport extends AbstractTransport {
    @ChannelHandler.Sharable
    private class Bridger extends SimpleChannelInboundHandler<DatagramPacket> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket dp) {
            // ctx.channel().remoteAddress() is null when DatagramChannel is not in 'connected' mode
            recvBytes.increment(dp.content().readableBytes());
            try {
                byte[] data = new byte[dp.content().readableBytes()];
                dp.content().readBytes(data);
                Packet packet = Packet.parseFrom(data);
                doReceive(packet, dp.sender(), dp.recipient());
            } catch (InvalidProtocolBufferException e) {
                log.error("Unable to decode packet, just ignore");
            }
        }
    }

    private final Counter sendBytes = Metrics.counter("cluster.send.bytes", "proto", "udp");
    private final Counter recvBytes = Metrics.counter("cluster.recv.bytes", "proto", "udp");

    private final EventLoopGroup elg;

    private final Channel channel;

    private final Bridger bridger;
    private final InetSocketAddress localAddress;


    @Builder
    UDPTransport(String sharedToken, InetSocketAddress bindAddr) {
        super(sharedToken);
        try {
            bridger = new Bridger();
            elg = getEventLoopGroup(4, "cluster-udp-transport");
            Bootstrap bootstrap = new Bootstrap();
            channel = bootstrap.group(elg)
                    .channel(getDatagramChannelClass())
                    .localAddress(bindAddr)
                    .handler(new ChannelInitializer<DatagramChannel>() {
                        @Override
                        protected void initChannel(DatagramChannel channel) {
                            channel.pipeline()
                                    .addLast("compressor", new FastLzFrameDecoder())
                                    .addLast("decompressor", new FastLzFrameEncoder())
                                    .addLast("udpEncoder", new DatagramPacketEncoder<>(new ProtobufEncoder()))
                                    .addLast("Bridger", bridger);
                        }
                    })
                    .bind()
                    .sync()
                    .channel();
            localAddress = (InetSocketAddress) channel.localAddress();
            log.debug("Creating udp transport: bindAddr={}", localAddress);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize udp transport", e);
        }
    }

    @Override
    public InetSocketAddress bindAddress() {
        return localAddress;
    }

    @Override
    protected CompletableFuture<Void> doSend(Packet packet, InetSocketAddress recipient) {

        log.trace("Sending packet via udp: size={}, packet#={}, recipient={}",
                packet.getSerializedSize(), packet.hashCode(), recipient);
        sendBytes.increment(packet.getSerializedSize());
        CompletableFuture<Void> ret = new CompletableFuture<>();
        channel.writeAndFlush(new DefaultAddressedEnvelope<>(packet, recipient)).addListener(future -> {
            if (!future.isSuccess()) {
                log.warn("failed to send packet via udp, recipient={}", recipient, future.cause());
            }
            ret.complete(null);
        });
        return ret;
    }

    @Override
    protected Completable doShutdown() {
        log.debug("Closing udp transport");
        CompletableSubject doneSignal = CompletableSubject.create();
        Completable.concatArrayDelayError(
                        Completable.fromFuture(channel.close()),
                        Completable.fromFuture(elg.shutdownGracefully()),
                        Completable.fromRunnable(() -> {
                            Metrics.globalRegistry.remove(sendBytes);
                            Metrics.globalRegistry.remove(recvBytes);
                        }))
                .onErrorComplete()
                .subscribe(doneSignal::onComplete);
        return doneSignal;
    }
}
