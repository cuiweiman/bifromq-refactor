package com.zachary.bifromq.baserpc.trafficgovernor;

import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.baserpc.proto.RPCServer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
final class RPCServiceServerRegister extends RPCServiceAnnouncer implements IRPCServiceServerRegister {
    private final AtomicBoolean started = new AtomicBoolean();
    private final AtomicBoolean stopped = new AtomicBoolean();

    private final CompositeDisposable disposable = new CompositeDisposable();
    private RPCServer localServer;

    public RPCServiceServerRegister(String serviceUniqueName, ICRDTService crdtService) {
        super(serviceUniqueName, crdtService);
    }

    @Override
    public void start(String id, InetSocketAddress hostAddr, Set<String> groupTags, Map<String, String> attrs) {
        assert !stopped.get();
        if (started.compareAndSet(false, true)) {
            localServer = RPCServer.newBuilder()
                .setId(id)
                .setHost(hostAddr.getAddress().getHostAddress())
                .setPort(hostAddr.getPort())
                .addAllGroup(groupTags)
                .putAllAttrs(attrs)
                .setAnnouncerId(id())
                .setAnnouncedTS(System.currentTimeMillis())
                .build();

            // make an announcement via rpcServiceCRDT
            log.debug("Announce local server:{}", localServer);
            announce(localServer).join();

            // enforce the announcement consistent eventually
            disposable.add(announcedServers()
                .subscribe(serverMap -> {
                    if (!serverMap.containsKey(localServer.getId())) {
                        localServer = localServer.toBuilder().setAnnouncedTS(System.currentTimeMillis()).build();
                        log.debug("Re-announce local server: {}", localServer);
                        // refresh announcement time
                        announce(localServer);
                    } else if (localServer.getAnnouncedTS() < serverMap.get(localServer.getId()).getAnnouncedTS()) {
                        localServer = serverMap.get(localServer.getId());
                        log.debug("Update local server from announcement: server={}", localServer);
                    }
                }));
        }
    }

    @Override
    public void stop() {
        assert started.get();
        if (stopped.compareAndSet(false, true)) {
            // stop the announcement
            revoke(localServer.getId()).join();
            disposable.dispose();
            super.destroy();
        }
    }
}
