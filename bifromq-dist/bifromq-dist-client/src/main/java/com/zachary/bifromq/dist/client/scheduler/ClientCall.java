package com.zachary.bifromq.dist.client.scheduler;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClientCall {
    final ClientInfo publisher;
    final String topic;
    final Message message;
}
