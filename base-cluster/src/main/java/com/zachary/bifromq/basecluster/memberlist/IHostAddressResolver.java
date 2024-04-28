package com.zachary.bifromq.basecluster.memberlist;


import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;

import java.net.InetSocketAddress;

public interface IHostAddressResolver {
    InetSocketAddress resolve(HostEndpoint endpoint);
}
