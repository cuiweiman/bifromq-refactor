package com.zachary.bifromq.baserpc.nameresolver;

import com.zachary.bifromq.baserpc.trafficgovernor.IRPCServiceTrafficDirector;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@Slf4j
@Builder
public class TrafficGovernorNameResolverProvider extends NameResolverProvider {
    public static final String SCHEME = "tgov";

    private final String serviceUniqueName;

    private final IRPCServiceTrafficDirector trafficDirector;

    @Override
    public NameResolver newNameResolver(URI targetUri, NameResolver.Args args) {
        return new TrafficGovernorNameResolver(serviceUniqueName, trafficDirector);
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 6;
    }

    @Override
    public String getDefaultScheme() {
        return SCHEME;
    }
}
