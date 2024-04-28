package com.zachary.bifromq.basecluster.memberlist;

import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import com.zachary.bifromq.basecluster.membership.proto.Join;
import com.zachary.bifromq.basecluster.messenger.IMessenger;
import com.zachary.bifromq.basecluster.proto.ClusterMessage;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.google.common.collect.Sets;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
public final class AutoSeeder {
    private final IMessenger messenger;
    private final Scheduler scheduler;
    private final IHostMemberList memberList;
    private final IHostAddressResolver addressResolver;
    private final Duration joinInterval;
    private final LoadingCache<InetSocketAddress, CompletableFuture<Void>> joiningSeeds;
    private final AtomicBoolean stopped = new AtomicBoolean();
    private final AtomicBoolean scheduled = new AtomicBoolean();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private volatile Set<HostEndpoint> aliveMembers = new HashSet<>();
    private volatile Disposable job;

    public AutoSeeder(IMessenger messenger,
                      Scheduler scheduler,
                      IHostMemberList memberList,
                      IHostAddressResolver addressResolver,
                      Duration joinTimeout,
                      Duration joinInterval) {
        this.messenger = messenger;
        this.scheduler = scheduler;
        this.memberList = memberList;
        this.addressResolver = addressResolver;
        this.joinInterval = joinInterval;
        joiningSeeds = Caffeine.newBuilder()
            .maximumSize(30)
            .expireAfterWrite(joinTimeout)
            .removalListener((RemovalListener<InetSocketAddress, CompletableFuture<Void>>) (key, value, cause) -> {
                if (value == null) {
                    return;
                }
                if (cause.wasEvicted()) {
                    log.debug("Stop trying to join seed address[{}]", key);
                    value.completeExceptionally(new UnknownHostException(key + " is unreachable"));
                } else {
                    if (stopped.get()) {
                        log.debug("Abort joining seed address[{}]", key);
                        value.completeExceptionally(new IllegalStateException("Seeding has stopped"));
                    } else {
                        log.debug("Join seed address[{}] success", key);
                        value.complete(null);
                    }
                }
            })
            .build(k -> new CompletableFuture<>());
        disposables.add(memberList.members().observeOn(scheduler).subscribe(m -> this.clearJoined(m.keySet())));
        disposables.add(memberList.members()
            .observeOn(scheduler)
            .subscribe(members -> aliveMembers = members.keySet()));
    }

    public CompletableFuture<Void> join(String domainName, int port) {
        try {
            InetAddress[] addrs = InetAddress.getAllByName(domainName);
            Set<InetSocketAddress> peers = Arrays.stream(addrs)
                // peer must be serving on same port
                .map(addr -> new InetSocketAddress(addr.getHostAddress(), port))
                .collect(Collectors.toSet());
            log.trace("resolved all peers: {} from domain: {}", peers.stream()
                .map(InetSocketAddress::toString).collect(Collectors.joining(", ")), domainName);
            return join(peers);
        } catch (Throwable e) {
            log.error("Cannot resolve cluster domain name: {}", domainName, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<Void> join(Set<InetSocketAddress> seeds) {
        if (stopped.get()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Seeder has stopped"));
        }
        Set<InetSocketAddress> known = new HashSet<>();
        for (HostEndpoint endpoint : aliveMembers) {
            InetSocketAddress addr = addressResolver.resolve(endpoint);
            if (seeds.contains(addr)) {
                known.add(addr);
            }
        }
        Set<InetSocketAddress> newSeeds = Sets.difference(seeds, known);
        CompletableFuture<Void>[] joinFutures = joiningSeeds.getAll(newSeeds)
            .values()
            .toArray(CompletableFuture[]::new);
        schedule(0);
        return CompletableFuture.allOf(joinFutures);
    }

    public void stop() {
        if (stopped.compareAndSet(false, true)) {
            joiningSeeds.invalidateAll();
            disposables.dispose();
            if (job != null) {
                job.dispose();
            }
        }
    }

    private void schedule(long delayInMS) {
        if (!stopped.get() && scheduled.compareAndSet(false, true)) {
            job = scheduler.scheduleDirect(this::run, delayInMS, TimeUnit.MILLISECONDS);
        }
    }

    private Set<InetSocketAddress> clearJoined(Set<HostEndpoint> endpoints) {
        Set<InetSocketAddress> knownAddresses = new HashSet<>();
        Set<InetSocketAddress> allJoiningSeeds = Sets.newHashSet(joiningSeeds.asMap().keySet());
        endpoints.forEach(endpoint -> knownAddresses.add(addressResolver.resolve(endpoint)));
        joiningSeeds.invalidateAll(Sets.intersection(knownAddresses, allJoiningSeeds));
        // run a compaction so that expired entries could be cleanup as soon as possible
        joiningSeeds.cleanUp();
        return Sets.difference(allJoiningSeeds, knownAddresses);
    }

    private void run() {
        Set<InetSocketAddress> toJoinSeeds = clearJoined(aliveMembers);
        for (InetSocketAddress seedAddr : toJoinSeeds) {
            log.debug("Send join message to address[{}]", seedAddr);
            messenger.send(ClusterMessage.newBuilder()
                .setJoin(Join.newBuilder()
                    .setMember(memberList.local())
                    .build())
                .build(), seedAddr, true);
        }
        scheduled.set(false);
        if (stopped.get()) {
            joiningSeeds.invalidateAll();
        } else if (!joiningSeeds.asMap().isEmpty()) {
            schedule(joinInterval.toMillis());
        }
    }
}
