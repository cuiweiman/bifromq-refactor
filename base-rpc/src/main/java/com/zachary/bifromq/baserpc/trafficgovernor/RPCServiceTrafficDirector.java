package com.zachary.bifromq.baserpc.trafficgovernor;

import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.baserpc.proto.RPCServer;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Collections.emptySet;

/**
 * React to 3 kinds of events: server announcement,  traffic directive announcement, alive replica changes
 */
@Slf4j
class RPCServiceTrafficDirector extends RPCServiceAnnouncer implements IRPCServiceTrafficDirector {
    private final AtomicBoolean destroyed = new AtomicBoolean();

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final BehaviorSubject<Set<Server>> serverListSubject = BehaviorSubject.createDefault(emptySet());

    public RPCServiceTrafficDirector(String serviceUniqueName, ICRDTService crdtService) {
        super(serviceUniqueName, crdtService);

        disposables.add(Observable.combineLatest(announcedServers(), aliveAnnouncers(),
                this::refreshAliveServerList)
            .subscribe(serverListSubject::onNext));
    }


    @Override
    public Observable<Map<String, Map<String, Integer>>> trafficDirective() {
        return super.trafficDirective();
    }

    @Override
    public Observable<Set<Server>> serverList() {
        return serverListSubject.distinctUntilChanged();
    }

    @Override
    public void destroy() {
        if (destroyed.compareAndSet(false, true)) {
            disposables.dispose();
            super.destroy();
        }
    }

    private Set<Server> refreshAliveServerList(Map<String, RPCServer> announcedServers,
                                               Set<ByteString> aliveAnnouncers) {
        Set<Server> aliveServers = Sets.newHashSet();
        for (RPCServer server : announcedServers.values()) {
            if (aliveAnnouncers.contains(server.getAnnouncerId())) {
                aliveServers.add(new Server(server.getId(), new InetSocketAddress(server.getHost(), server.getPort()),
                    Sets.newHashSet(server.getGroupList()), server.getAttrsMap()));
            } else {
                // this is a side effect: revoke the announcement made by dead announcer
                log.debug("Remove not alive server announcement: {}", server.getId());
                revoke(server.getId());
            }
        }
        return aliveServers;
    }
}
