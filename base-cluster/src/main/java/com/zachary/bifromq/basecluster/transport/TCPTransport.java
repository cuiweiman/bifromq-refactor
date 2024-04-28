package com.zachary.bifromq.basecluster.transport;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.zachary.bifromq.basecluster.transport.proto.Packet;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.ssl.SslContext;
import io.netty.util.concurrent.GenericFutureListener;
import io.reactivex.rxjava3.core.Completable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/24 11:59
 */
@Slf4j
public class TCPTransport extends AbstractTransport {

    /**
     * TCP 连接 配置信息
     */
    @Getter
    @Setter
    @Builder(toBuilder = true)
    @Accessors(chain = true, fluent = true)
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TCPTransportOptions {
        /**
         * 连接超时 默认 5000ms 即 5s
         */
        private int connTimeoutInMS = 5000;
        /**
         * 心跳超时 默认 5s
         */
        private int idleTimeoutInSec = 5;
        /**
         * 每个节点的最大 channel 数量 默认 5 个
         */
        private int maxChannelsPerHost = 5;
        /**
         * 最大缓冲区大小（以字节为单位） 默认 64 * 1024 即 64kb
         */
        private int maxBufferSizeInBytes = WriteBufferWaterMark.DEFAULT.high();
    }


    /**
     * 客户桥接器
     * <p>
     * {@link ChannelHandler.Sharable} 表示这个 Handler 是可以共享的，需要自己保证线程安全的
     */
    @ChannelHandler.Sharable
    private static class ClientBridger extends ChannelInboundHandlerAdapter {

        public ClientBridger() {
            super();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            trace("Outbound channel active: remote={}", ctx.channel().remoteAddress());
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            trace("Outbound channel inactive: remote={}", ctx.channel().remoteAddress());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            log.warn("Outbound channel failure: remote={}, cause={}", ctx.channel().remoteAddress(),
                    cause.getMessage());
            ctx.close();
        }
    }

    /**
     * 服务端 桥接器
     */
    @ChannelHandler.Sharable
    private class ServerBridger extends SimpleChannelInboundHandler<Packet> {

        public ServerBridger() {
            super();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Packet msg) {
            // 记录日志
            trace("Received message: remote={}", ctx.channel().remoteAddress());
            // 计数器 增加计数
            recvBytes.increment(msg.getSerializedSize());
            doReceive(msg, (InetSocketAddress) ctx.channel().remoteAddress(),
                    (InetSocketAddress) ctx.channel().localAddress());
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            trace("Inbound channel active: remote={}", ctx.channel().remoteAddress());
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            trace("Inbound channel inactive: remote={}", ctx.channel().remoteAddress());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            log.warn("Inbound channel failure: remote={}, cause={}", ctx.channel().remoteAddress(), cause.getMessage());
            ctx.close();
        }
    }

    /**
     * 集群 proto 数据包 收发计数
     */
    private final Counter sendBytes = Metrics.counter("cluster.send.bytes", "proto", "tcp");
    private final Counter recvBytes = Metrics.counter("cluster.recv.bytes", "proto", "tcp");
    /**
     * 服务端 和 客户端 的 桥接器
     */
    private final ClientBridger clientBridger = new ClientBridger();
    private final ServerBridger serverBridger = new ServerBridger();

    /**
     * 双级缓存 ConcurrentMap 下 存储了 {@link InetSocketAddress} key 和 一级缓存 {@link LoadingCache} value
     * 一级缓存 {@link LoadingCache} value 存储了 Integer 类型的 key 和 ChannelFuture 类型的 value
     * <a href="https://www.jb51.net/program/308687k1r.htm">{@link LoadingCache} 自动加载缓存的工具</a>
     */
    private final ConcurrentMap<InetSocketAddress, LoadingCache<Integer, ChannelFuture>> channelMaps = new ConcurrentHashMap<>();
    /**
     * 线程私有变量 threadChannelKey
     */
    private final ThreadLocal<Integer> threadChannelKey = new ThreadLocal<>();
    private final EventLoopGroup elg;
    private final TCPTransportOptions opts;
    /**
     * 记录 客户端的连接数？
     */
    private final AtomicInteger nextChannelKey = new AtomicInteger(0);
    private final Bootstrap clientBootstrap;
    private final ChannelFuture tcpListeningChannel;
    private final InetSocketAddress localAddress;

    @Builder
    TCPTransport(String sharedToken, InetSocketAddress bindAddr, SslContext serverSslContext,
                 SslContext clientSslContext, TCPTransportOptions opts) {
        // 调用 父级 的构造器
        super(sharedToken);
        try {
            // 参数校验
            Preconditions.checkArgument(opts.connTimeoutInMS > 0, "connTimeoutInMS must be a positive number");
            Preconditions.checkArgument(opts.maxBufferSizeInBytes > 0,
                    "maxBufferSizeInBytes must be a positive number");
            Preconditions.checkArgument(opts.maxChannelsPerHost > 0, "maxChannelsPerHost must be a positive number");
            this.opts = opts.toBuilder().build();
            elg = NettyUtil.getEventLoopGroup(4, "cluster-tcp-transport");
            clientBootstrap = setupTcpClient(clientSslContext);
            tcpListeningChannel = setupTcpServer(bindAddr, serverSslContext);
            localAddress = (InetSocketAddress) tcpListeningChannel.channel().localAddress();
            log.debug("Creating tcp transport: bindAddr={}", localAddress);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize tcp transport", e);
        }
    }

    /**
     * 配置 TcpClient
     */
    private Bootstrap setupTcpClient(SslContext sslContext) {
        Bootstrap clientBootstrap = new Bootstrap();
        clientBootstrap.group(elg)
                .channel(NettyUtil.getSocketChannelClass())
                // 禁用 Nagle 算法，小数据包即时传输，降低 TCP 数据包 的 传输延迟。
                // Nagle 算法试图通过减少 TCP 包的数量来降低结构性开销, 即它会将 多个较小的包 组合成 较大的包 后再进行发送。
                // 关键是 Nagle 算法受 TCP 延迟 ack 确认影响, 会导致相继两次向连接发送请求包, 读数据时会有一个最多达500毫秒的延时。
                .option(ChannelOption.TCP_NODELAY, true)
                // 探活，默认 2小时 内没有数据通信时，TCP 会自动发送一个 探活数据 报文。
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 设置 缓冲区 的高水位（缓冲区容量），默认 低水位是 32kb，高水位是 64kb。
                // 如果 缓冲区容量 低于低水位，可写状态；大于高水位，不可写状态。
                .option(ChannelOption.WRITE_BUFFER_WATER_MARK,
                        new WriteBufferWaterMark(opts.maxBufferSizeInBytes / 2, opts.maxBufferSizeInBytes))
                // 配置 TCP 连接超时时间，默认是 5000ms， 即 5s
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, opts.connTimeoutInMS);
        // 配置 处理器链
        clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) {
                if (sslContext != null) {
                    channel.pipeline().addLast("ssl", sslContext.newHandler(channel.alloc()));
                }
                channel.pipeline()
                        .addLast("probe", new ProbeHandler())
                        .addLast("frameDecoder", new ProtobufVarint32FrameDecoder())
                        .addLast("protoBufDecoder", new ProtobufDecoder(Packet.getDefaultInstance()))
                        .addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender())
                        .addLast("protoBufEncoder", new ProtobufEncoder())
                        // 客户端 桥接 处理器
                        .addLast("bridger", clientBridger);
            }
        });
        return clientBootstrap;
    }

    /**
     * {@link SneakyThrows} 将当前方法抛出的异常, 包装成 RuntimeException, 骗过编译器, 使调用点 不用显示处理异常信息。
     */
    @SneakyThrows
    private ChannelFuture setupTcpServer(InetSocketAddress serverAddr, SslContext sslContext) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        return serverBootstrap.group(elg)
                .channel(NettyUtil.getServerSocketChannelClass())
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .localAddress(serverAddr)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        // SSL 连接
                        if (sslContext != null) {
                            channel.pipeline().addLast("ssl", sslContext.newHandler(channel.alloc()));
                        }
                        channel.pipeline()
                                .addLast("probe", new ProbeHandler())
                                .addLast("frameDecoder", new ProtobufVarint32FrameDecoder())
                                .addLast("protoBufDecoder", new ProtobufDecoder(Packet.getDefaultInstance()))
                                .addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender())
                                .addLast("protoBufEncoder", new ProtobufEncoder())
                                // 服务端 桥接 处理器
                                .addLast("Bridger", serverBridger);
                    }
                })
                .bind()
                .sync();
    }

    @Override
    public InetSocketAddress bindAddress() {
        // Channel 在 本地机器 的 地址
        return (InetSocketAddress) tcpListeningChannel.channel().localAddress();
    }

    @Override
    protected CompletableFuture<Void> doSend(Packet packet, InetSocketAddress recipient) {
        CompletableFuture<Void> onDone = new CompletableFuture<>();
        getChannel(recipient).whenComplete((ch, e) -> {
            if (e != null) {
                onDone.completeExceptionally(e);
            } else {
                if (ch.isWritable()) {
                    long packetLength = packet.getSerializedSize();
                    sendBytes.increment(packetLength);
                    ch.writeAndFlush(packet);
                    onDone.complete(null);
                } else {
                    onDone.completeExceptionally(new RuntimeException("Channel is not writable"));
                }
            }
        });
        return onDone;
    }

    /**
     * {@link VisibleForTesting} 表示一个成员变量、方法或类 仅为了使其对测试代码可见而存在。
     * 对 非public 方法 单元测试时，需要保证 被测方法 的可见性，可以使用 此注解。
     */
    @VisibleForTesting
    CompletableFuture<Channel> getChannel(InetSocketAddress recipient) {
        if (threadChannelKey.get() == null) {
            threadChannelKey.set(nextChannelKey.getAndUpdate(k -> (k + 1) % opts.maxChannelsPerHost));
        }
        Integer channelKey = threadChannelKey.get();
        // compute(new Key, (old Key,old Value) -> new Value) 计算并更新 Value
        // 遍历 channelMaps 中的所有 InetSocketAddress，生成对应的 LoadingCache 缓存
        // 若 LoadingCache 为空，则创建新的 Caffeine 并 根据 InetSocketAddress 建立 客户端 Socket 连接
        // 最后从 LoadingCache 中 获取 channelKey 对应的 ChannelFuture
        ChannelFuture cf = channelMaps.compute(recipient, (inetSocketAddress, loadingCache) -> {
            // 连接节点的 ChannelFuture 先存入 loadingCache
            if (loadingCache == null) {
                loadingCache = Caffeine.newBuilder()
                        // 在 缓存构建器 中 指定一个 调度线程
                        .scheduler(Scheduler.systemScheduler())
                        // 最后一次被访问（读或者写）后，间隔 opts.idleTimeoutInSec 时间后 失效
                        .expireAfterAccess(Duration.ofSeconds(opts.idleTimeoutInSec))
                        // 缓存移除监听器，元素被移出时触发 事件执行
                        .removalListener((Integer k, ChannelFuture v, RemovalCause cause) -> {
                            if (v != null && v.isDone() && v.channel().isActive()) {
                                trace("Closing #{} channel: remote={}, cause={}", k, v.channel().remoteAddress(), cause);
                                v.channel().close();
                            }
                        })
                        .build((Integer k) -> {
                            trace("Setup #{} channel: remote={}", k, recipient);
                            // 连接 副本节点
                            return clientBootstrap.connect(recipient);
                        });
            }
            return loadingCache;
        }).get(channelKey);

        // ChannelFuture 保存 Channel 异步操作的结果，这里用于 等待 上面 channelMaps.compute 操作的执行结束
        // 在 Netty 中是通过 ChannelFuture 来实现 异步结果 的监听，当 操作执行 成功或者失败时 监听 会自动触发 注册的监听事件。
        if (cf.isDone()) {
            if (cf.channel().isActive()) {
                return CompletableFuture.completedFuture(cf.channel());
            } else {
                // channel is inactive rebuild one
                // channel 断开连接 了，重新
                synchronized (cf) {
                    if (cf == channelMaps.get(recipient).get(channelKey)) {
                        // 记录日志，删除 channelMaps#loadingCache 中 channelKey 对应的 value 缓存
                        log.debug("Rebuild #{} channel: remote={}", channelKey, recipient);
                        channelMaps.get(recipient).invalidate(channelKey);
                    }
                }
                // 重新 递归调用 {@link getChannel} 方法，直到 获取到 正常的 Channel，并封装成 CompletableFuture
                return getChannel(recipient);
            }
        } else {
            // channelMaps.compute 操作执行结束
            CompletableFuture<Channel> f = new CompletableFuture<>();
            // 为 cf 操作添加 监听器 ，操作完成时 触发
            cf.addListener(new GenericFutureListener<ChannelFuture>() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess() && future.channel().isActive()) {
                        // future 获取成功，并且 channel 激活，则封装成 CompletableFuture 并返回
                        f.complete(future.channel());
                    } else {
                        try {
                            future.get();
                        } catch (Exception e) {
                            f.completeExceptionally(new RuntimeException("Failed to connect to address: " + recipient, e));
                        }
                    }
                }
            });
            return f;
        }
    }

    @Override
    protected Completable doShutdown() {
        log.debug("Closing tcp transport");
        return Completable.concatArrayDelayError(
                Completable.fromRunnable(() -> channelMaps.forEach((r, cm) -> cm.invalidateAll())),
                Completable.fromFuture(tcpListeningChannel.channel().close()),
                Completable.fromFuture(elg.shutdownGracefully()), Completable.fromRunnable(() -> {
                    Metrics.globalRegistry.remove(sendBytes);
                    Metrics.globalRegistry.remove(recvBytes);
                })).onErrorComplete();
    }

    private static void trace(String format, Object... args) {
        // 先判断 Trace 级别的日志是否被允许输出，用于系统性能优化操作
        if (log.isTraceEnabled()) {
            log.trace(format, args);
        }
    }
}
