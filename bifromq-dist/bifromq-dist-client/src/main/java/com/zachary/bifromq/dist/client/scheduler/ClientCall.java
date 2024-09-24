package com.zachary.bifromq.dist.client.scheduler;

import com.zachary.bifromq.type.ClientInfo;
import com.zachary.bifromq.type.Message;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClientCall {
    final ClientInfo publisher;
    final String topic;
    final Message message;
}
