
package com.zachary.bifromq.basecluster.messenger;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage;
import com.zachary.bifromq.basecluster.transport.ITransport;
import com.zachary.bifromq.basecluster.transport.PacketEnvelope;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Timed;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
final class MessengerTransport {

    private final ITransport transport;

    MessengerTransport(ITransport transport) {
        this.transport = transport;
    }

    InetSocketAddress bindAddress() {
        return transport.bindAddress();
    }

    CompletableFuture<Void> send(List<MessengerMessage> messengerMessages,
                                 InetSocketAddress recipient,
                                 boolean forceTCP) {
        ITransport.RELIABLE.set(forceTCP);
        return transport.send(messengerMessages.stream()
                .map(AbstractMessageLite::toByteString).collect(Collectors.toList()), recipient);
    }

    Observable<Timed<MessengerMessageEnvelope>> receive() {
        return transport.receive().flatMap(this::convert);
    }

    CompletableFuture<Void> shutdown() {
        return transport.shutdown();
    }

    private Observable<Timed<MessengerMessageEnvelope>> convert(PacketEnvelope packetEnvelope) {
        return Observable.fromIterable(packetEnvelope.data.stream().map(b -> {
            MessengerMessageEnvelope.MessengerMessageEnvelopeBuilder messageEnvelopeBuilder =
                    MessengerMessageEnvelope.builder()
                            .recipient(packetEnvelope.recipient);
            try {
                MessengerMessage mm = MessengerMessage.parseFrom(b);
                messageEnvelopeBuilder.message(mm);
                switch (mm.getMessengerMessageTypeCase()) {
                    case DIRECT:
                        return new Timed<MessengerMessageEnvelope>(
                                messageEnvelopeBuilder.sender(packetEnvelope.sender).build(),
                                System.currentTimeMillis(),
                                TimeUnit.MILLISECONDS);
                    case GOSSIP:
                    default:
                        return new Timed<MessengerMessageEnvelope>(
                                messageEnvelopeBuilder.build(),
                                System.currentTimeMillis(),
                                TimeUnit.MILLISECONDS);
                }
            } catch (InvalidProtocolBufferException e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList()));
    }
}
