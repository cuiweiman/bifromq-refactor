package com.zachary.bifromq.baserpc;

import io.grpc.ConnectivityState;

interface ConnStateListener {
    void onChange(String serverAuthority, ConnectivityState state);
}
