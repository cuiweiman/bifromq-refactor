package com.zachary.bifromq.basecluster.messenger;

import com.zachary.bifromq.basecluster.proto.ClusterMessage;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Timed;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/24 11:05
 */
public interface IMessenger {
    InetSocketAddress bindAddress();
    CompletableFuture<Void> send(ClusterMessage message, InetSocketAddress recipient, boolean reliable);

    /**
     * send messages
     *
     * @param message
     * @param piggybackedGossips
     * @param recipient
     * @param reliable
     * @return
     */
    CompletableFuture<Void> send(ClusterMessage message,
                                 List<ClusterMessage> piggybackedGossips,
                                 InetSocketAddress recipient,
                                 boolean reliable);

    /**
     * send messages
     *
     * @param message
     * @param piggybackedGossips
     * @param recipient
     * @param reliable
     * @return
     */
    CompletableFuture<Void> send(ClusterMessage message,
                                 List<ClusterMessage> piggybackedGossips,
                                 InetSocketAddress recipient,
                                 String sender,
                                 boolean reliable);

    /**
     * Spread the message in the cluster
     *
     * @param message
     * @return how long it takes to spread the message within the cluster
     */
    CompletableFuture<Duration> spread(ClusterMessage message);

    Observable<Timed<MessageEnvelope>> receive();

    void start(IRecipientSelector recipientSelector);

    CompletableFuture<Void> shutdown();
}
