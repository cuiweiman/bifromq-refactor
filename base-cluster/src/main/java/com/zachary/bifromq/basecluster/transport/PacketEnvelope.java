package com.zachary.bifromq.basecluster.transport;

import com.google.protobuf.ByteString;
import lombok.AllArgsConstructor;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/24 11:08
 */
@AllArgsConstructor
public class PacketEnvelope {
    public final List<ByteString> data;
    public final InetSocketAddress recipient;
    public final InetSocketAddress sender;
}
