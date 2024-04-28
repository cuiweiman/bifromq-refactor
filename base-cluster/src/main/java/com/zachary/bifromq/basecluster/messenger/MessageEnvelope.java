
package com.zachary.bifromq.basecluster.messenger;

import com.zachary.bifromq.basecluster.proto.ClusterMessage;
import lombok.Builder;

import java.net.InetSocketAddress;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/28 14:21
 */
@Builder
public class MessageEnvelope {
    public final ClusterMessage message;
    public final InetSocketAddress recipient;
    /**
     * null if gossip
     * 消息发送者
     */
    public final InetSocketAddress sender;
}
