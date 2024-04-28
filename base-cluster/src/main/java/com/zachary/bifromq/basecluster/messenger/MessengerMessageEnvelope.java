
package com.zachary.bifromq.basecluster.messenger;

import com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage;
import lombok.Builder;

import java.net.InetSocketAddress;

@Builder
class MessengerMessageEnvelope {
    public final MessengerMessage message;
    public final InetSocketAddress recipient;
    public final InetSocketAddress sender; // null if gossip
}
