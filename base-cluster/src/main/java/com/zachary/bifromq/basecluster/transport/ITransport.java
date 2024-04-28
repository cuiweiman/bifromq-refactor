package com.zachary.bifromq.basecluster.transport;

import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.core.Observable;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/24 11:05
 */
public interface ITransport {

    ThreadLocal<Boolean> RELIABLE = new ThreadLocal<>();

    InetSocketAddress bindAddress();

    /**
     * Send messages to recipient
     * 向 recipient 发送消息
     *
     * @return
     */
    CompletableFuture<Void> send(List<ByteString> data, InetSocketAddress recipient);

    /**
     * HOT observable of incoming Packet wrapped in Envelop where sender and recipient address indicated
     * 可观察到包裹在信封中的传入数据包，其中指示了发件人和收件人地址
     *
     * @return
     */
    Observable<PacketEnvelope> receive();

    CompletableFuture<Void> shutdown();
}
