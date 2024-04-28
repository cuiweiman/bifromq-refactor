package com.zachary.bifromq.basecluster.memberlist;

import com.zachary.bifromq.basecluster.membership.proto.Endorse;
import com.zachary.bifromq.basecluster.membership.proto.Fail;
import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import com.zachary.bifromq.basecluster.membership.proto.HostMember;
import com.zachary.bifromq.basecluster.membership.proto.Join;
import com.zachary.bifromq.basecluster.membership.proto.Quit;
import com.zachary.bifromq.basecluster.messenger.IMessenger;
import com.zachary.bifromq.basecluster.proto.ClusterMessage;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.google.common.collect.Maps;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.benmanes.caffeine.cache.Scheduler.systemScheduler;

@Slf4j
public final class AutoHealer {
    private final Cache<HostEndpoint, Integer> healingMembers;
    private final IMessenger messenger;
    private final Scheduler scheduler;
    private final IHostMemberList memberList;
    private final IHostAddressResolver addressResolver;
    private final Duration healingTimeout;
    private final Duration healingInterval;
    private final AtomicBoolean stopped = new AtomicBoolean();
    private final AtomicBoolean scheduled = new AtomicBoolean();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final Map<HostEndpoint, Integer> alivePeers = new ConcurrentHashMap<>();
    private volatile Disposable healingJob;

    public AutoHealer(IMessenger messenger, Scheduler scheduler, IHostMemberList memberList,
                      IHostAddressResolver addressResolver, Duration healingTimeout, Duration healingInterval) {
        this.messenger = messenger;
        this.scheduler = scheduler;
        this.memberList = memberList;
        this.addressResolver = addressResolver;
        this.healingTimeout = healingTimeout;
        this.healingInterval = healingInterval;
        this.healingMembers = Caffeine.newBuilder()
            .maximumSize(30)
            .expireAfterWrite(healingTimeout)
            .scheduler(systemScheduler())
            .removalListener((RemovalListener<HostEndpoint, Integer>) (key, value, cause) -> {
                if (cause.wasEvicted()) {
                    log.debug("Give up healing host[{}]", key);
                }
            })
            .build();
        disposables.add(messenger.receive().map(m -> m.value().message)
            .observeOn(scheduler)
            .subscribe(this::handleMessage));
        disposables.add(memberList.members()
            .observeOn(scheduler)
            .subscribe(members -> {
                alivePeers.clear();
                alivePeers.putAll(Maps.filterKeys(members, k -> !k.equals(memberList.local())));
            }));
    }

    public void stop() {
        if (stopped.compareAndSet(false, true)) {
            healingMembers.invalidateAll();
            disposables.dispose();
            if (healingJob != null) {
                healingJob.dispose();
            }
        }
    }

    private void handleMessage(ClusterMessage message) {
        switch (message.getClusterMessageTypeCase()) {
            case JOIN:
                handleJoin(message.getJoin());
                break;
            case ENDORSE:
                handleEndorse(message.getEndorse());
                break;
            case QUIT:
                handleQuit(message.getQuit());
                break;
            case FAIL:
                handleFail(message.getFail());
                break;
        }
    }

    private void handleJoin(Join join) {
        HostMember joinMember = join.getMember();
        HostEndpoint joinEndpoint = joinMember.getEndpoint();
        Integer healingMemberIncarnation = healingMembers.getIfPresent(joinEndpoint);
        if (healingMemberIncarnation != null && healingMemberIncarnation < joinMember.getIncarnation()) {
            // the member is alive, no need to heal it anymore
            healingMembers.invalidate(joinEndpoint);
        }
    }

    private void handleEndorse(Endorse endorse) {
        HostEndpoint endpoint = endorse.getEndpoint();
        Integer healingMemberIncarnation = healingMembers.getIfPresent(endpoint);
        if (healingMemberIncarnation != null && healingMemberIncarnation <= endorse.getIncarnation()) {
            // the member is alive, no need to heal it anymore
            healingMembers.invalidate(endpoint);
        }
    }

    private void handleQuit(Quit quit) {
        HostEndpoint quitEndpoint = quit.getEndpoint();
        Integer healingMemberIncarnation = healingMembers.getIfPresent(quitEndpoint);
        if (healingMemberIncarnation != null && healingMemberIncarnation <= quit.getIncarnation()) {
            // the member has quit, no need to heal it anymore
            healingMembers.invalidate(quitEndpoint);
        }
    }

    private void handleFail(Fail fail) {
        HostEndpoint failedEndpoint = fail.getEndpoint();
        // no need to heal self, memberlist will refute it
        Integer inc = alivePeers.get(failedEndpoint);
        if (!failedEndpoint.equals(memberList.local()) &&
            !memberList.isZombie(failedEndpoint) &&
            inc != null && inc <= fail.getIncarnation()) {
            alivePeers.remove(failedEndpoint, inc);
            healingMembers.put(failedEndpoint, fail.getIncarnation());
            scheduleHealing();
        }
    }

    private void scheduleHealing() {
        if (stopped.get()) {
            return;
        }
        if (scheduled.compareAndSet(false, true)) {
            healingJob = scheduler.scheduleDirect(this::heal, healingInterval.toMillis(), TimeUnit.MILLISECONDS);
        }
    }

    private void heal() {
        for (Map.Entry<HostEndpoint, Integer> entry : healingMembers.asMap().entrySet()) {
            log.debug("Send join message to the host[{}] locating at address[{}] for healing the connection",
                entry.getKey(), entry.getValue());
            messenger.send(ClusterMessage.newBuilder()
                .setJoin(Join.newBuilder()
                    .setMember(memberList.local())
                    .setExpectedHost(entry.getKey())
                    .build())
                .build(), addressResolver.resolve(entry.getKey()), true);
        }
        // run a compaction so that expired entries could be cleanup as soon as possible
        healingMembers.cleanUp();
        scheduled.set(false);
        if (!healingMembers.asMap().isEmpty()) {
            scheduleHealing();
        }
    }
}
