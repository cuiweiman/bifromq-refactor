package com.zachary.bifromq.baserpc.nameresolver;

import com.zachary.bifromq.baserpc.loadbalancer.Constants;
import com.zachary.bifromq.baserpc.trafficgovernor.IRPCServiceTrafficDirector;
import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import io.grpc.Status;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.zachary.bifromq.baserpc.loadbalancer.Constants.SERVER_GROUP_TAG_ATTR_KEY;

@Slf4j
class TrafficGovernorNameResolver extends NameResolver {
    private final String serviceUniqueName;
    private final IRPCServiceTrafficDirector trafficDirector;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public TrafficGovernorNameResolver(String serviceUniqueName, IRPCServiceTrafficDirector trafficDirector) {
        this.serviceUniqueName = serviceUniqueName;
        this.trafficDirector = trafficDirector;
    }

    @Override
    public String getServiceAuthority() {
        return serviceUniqueName;
    }

    @Override
    public void start(Listener listener) {
        log.info("Starting TrafficGovernorNameResolver for service[{}]", serviceUniqueName);
        disposable.add(Observable.combineLatest(trafficDirector.trafficDirective(),
                trafficDirector.serverList(), (td, sl) ->
                    (Runnable) () -> listener.onAddresses(toAddressGroup(sl), toAttributes(td)))
            .subscribe(Runnable::run, e -> listener.onError(Status.INTERNAL.withCause(e))));
    }

    @Override
    public void shutdown() {
        log.info("Start to shutdown trafficGovernor nameResolver, service={}", serviceUniqueName);
        disposable.dispose();
        trafficDirector.destroy();
    }

    @Override
    public void refresh() {
    }

    private List<EquivalentAddressGroup> toAddressGroup(Set<IRPCServiceTrafficDirector.Server> servers) {
        return servers.stream().map(s -> new EquivalentAddressGroup(s.hostAddr, Attributes.newBuilder()
                .set(Constants.SERVER_ID_ATTR_KEY, s.id)
                .set(SERVER_GROUP_TAG_ATTR_KEY, s.groupTags)
                .build()))
            .collect(Collectors.toList());
    }

    private Attributes toAttributes(Map<String, Map<String, Integer>> td) {
        return Attributes.newBuilder().set(Constants.TRAFFIC_DIRECTIVE_ATTR_KEY, td).build();
    }
}

