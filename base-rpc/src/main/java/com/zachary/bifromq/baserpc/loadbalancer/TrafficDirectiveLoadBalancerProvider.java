package com.zachary.bifromq.baserpc.loadbalancer;

import com.zachary.bifromq.baserpc.BluePrint;
import io.grpc.LoadBalancer;
import io.grpc.LoadBalancerProvider;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TrafficDirectiveLoadBalancerProvider extends LoadBalancerProvider {
    private final BluePrint bluePrint;
    private final IUpdateListener updateListener;

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getPriority() {
        return 6;
    }

    @Override
    public String getPolicyName() {
        return bluePrint.serviceDescriptor().getName() + hashCode();
    }

    @Override
    public LoadBalancer newLoadBalancer(LoadBalancer.Helper helper) {
        return new TrafficDirectiveLoadBalancer(helper, bluePrint, updateListener);
    }
}
