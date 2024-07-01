package com.zachary.bifromq.baserpc.loadbalancer;

import io.grpc.MethodDescriptor;

import java.util.Optional;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/29 17:25
 */
public interface IUpdateListener {

    interface IServerSelector {
        boolean direct(String tenantId, String serverId, MethodDescriptor<?, ?> methodDescriptor);

        Optional<String> hashing(String tenantId, String key, MethodDescriptor<?, ?> methodDescriptor);

        Optional<String> roundRobin(String tenantId, MethodDescriptor<?, ?> methodDescriptor);

        Optional<String> random(String tenantId, MethodDescriptor<?, ?> methodDescriptor);
    }

    void onUpdate(IServerSelector selector);

}
