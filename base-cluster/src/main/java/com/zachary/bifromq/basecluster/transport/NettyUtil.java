package com.zachary.bifromq.basecluster.transport;

import com.zachary.bifromq.baseenv.EnvProvider;
import com.zachary.bifromq.baseenv.IEnvProvider;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueDatagramChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/24 11:10
 */
public class NettyUtil {

    /**
     * 根据系统判断，获取性能最好的 SocketChannel
     * NIO 对于非阻塞 socket 操作的支持的组件，其在 socket 上 封装了一层，
     * 主要是支持了 非阻塞 的读写。同时改进了传统的单向流 API,，Channel同时支持读写。
     * <p>
     * 用来处理网络 I/O 的通道，是一个连接到 TCP 网络套接字的通道。实现了可选择通道，可以被多路复用的。
     *
     * @return SocketChannel
     */
    static Class<? extends SocketChannel> getSocketChannelClass() {
        if (Epoll.isAvailable()) {
            return EpollSocketChannel.class;
        }
        if (KQueue.isAvailable()) {
            return KQueueSocketChannel.class;
        }
        return NioSocketChannel.class;
    }

    /**
     * 根据系统判断，获取性能最好的 ServerSocketChannel
     * ServerSocketChannel 是 面向流的 监听 socket 套接字的 可选择性 的通道。
     * 用于在 服务器端 监听 新的客户端 Socket 连接，而且本身不传输数据。
     *
     * @return ServerSocketChannel
     */
    static Class<? extends ServerSocketChannel> getServerSocketChannelClass() {
        if (Epoll.isAvailable()) {
            return EpollServerSocketChannel.class;
        }
        if (KQueue.isAvailable()) {
            return KQueueServerSocketChannel.class;
        }
        return NioServerSocketChannel.class;
    }

    /**
     * 根据系统判断，获取性能最好的 DatagramChannel
     * DatagramChannel 是一个能收发 UDP 包的通道。
     * 因为 UDP 是无连接的网络协议，所以不能像其它通道那样读取和写入。它发送和接收的是数据包
     *
     * @return DatagramChannel
     */
    static Class<? extends DatagramChannel> getDatagramChannelClass() {
        if (Epoll.isAvailable()) {
            return EpollDatagramChannel.class;
        }
        if (KQueue.isAvailable()) {
            return KQueueDatagramChannel.class;
        }
        return NioDatagramChannel.class;
    }

    /**
     * 根据系统判断，获取性能最好的 EventLoopGroup
     *
     * @param threads 线程数
     * @param name    ThreadFactory 线程工厂名称
     * @return EventLoopGroup
     */
    static EventLoopGroup getEventLoopGroup(int threads, String name) {
        IEnvProvider envProvider = EnvProvider.INSTANCE;
        if (Epoll.isAvailable()) {
            // Netty 是否支持 Epoll（Linux 支持）
            return new EpollEventLoopGroup(threads, envProvider.newThreadFactory(name));
        }
        if (KQueue.isAvailable()) {
            // Netty 是否支持 KQueue（MacOS 支持）
            return new KQueueEventLoopGroup(threads, envProvider.newThreadFactory(name));
        }
        return new NioEventLoopGroup(threads, envProvider.newThreadFactory(name));
    }
}
