package com.zachary.bifromq.basecluster.memberlist;

import com.zachary.bifromq.basecluster.fd.DirectProbingInfo;
import com.zachary.bifromq.basecluster.fd.IFailureDetector;
import com.zachary.bifromq.basecluster.fd.IProbingTarget;
import com.zachary.bifromq.basecluster.fd.IProbingTargetSelector;
import com.zachary.bifromq.basecluster.membership.proto.Doubt;
import com.zachary.bifromq.basecluster.membership.proto.Endorse;
import com.zachary.bifromq.basecluster.membership.proto.Fail;
import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import com.zachary.bifromq.basecluster.membership.proto.HostMember;
import com.zachary.bifromq.basecluster.membership.proto.Join;
import com.zachary.bifromq.basecluster.membership.proto.Quit;
import com.zachary.bifromq.basecluster.messenger.IMessenger;
import com.zachary.bifromq.basecluster.proto.ClusterMessage;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Timed;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class AutoDropper {
    private enum State {
        INIT, STARTING, STARTED, STOPPING, STOPPED
    }

    private final AtomicReference<State> state = new AtomicReference<>(State.INIT);
    private final IMessenger messenger;
    private final Scheduler scheduler;
    private final IHostMemberList memberList;
    private final IFailureDetector failureDetector;
    private final IHostAddressResolver addressResolver;
    private final Queue<PriorityProbingTarget> probingQueue = new PriorityQueue<>(
        (a, b) -> Float.compare(a.priority(), b.priority()));
    private final List<PriorityProbingTarget> probingTargets = new ArrayList<>();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final Map<HostEndpoint, Suspicion> suspicions = new HashMap<>();
    private final int suspicionMultiplier;
    private final int suspicionMaxTimeoutMultiplier;
    private final Gauge healthScoreGauge;
    private volatile Map<HostEndpoint, Integer> alivePeers = new HashMap<>();

    public AutoDropper(IMessenger messenger,
                       Scheduler scheduler,
                       IHostMemberList memberList,
                       IFailureDetector failureDetector,
                       IHostAddressResolver addressResolver,
                       int suspicionMultiplier,
                       int suspicionMaxTimeoutMultiplier) {
        this.messenger = messenger;
        this.scheduler = scheduler;
        this.memberList = memberList;
        this.failureDetector = failureDetector;
        this.addressResolver = addressResolver;
        this.suspicionMultiplier = suspicionMultiplier;
        this.suspicionMaxTimeoutMultiplier = suspicionMaxTimeoutMultiplier;
        disposables.add(failureDetector.succeeding()
            .map(Timed::value)
            .observeOn(scheduler)
            .subscribe(this::observeNormalReport));
        disposables.add(failureDetector.suspecting()
            .map(Timed::value)
            .observeOn(scheduler)
            .subscribe(this::observeSuspicionReport));
        disposables.add(messenger.receive()
            .map(m -> m.value().message)
            .observeOn(scheduler)
            .subscribe(this::handleMessage));
        disposables.add(memberList.members()
            .observeOn(scheduler)
            .subscribe(members -> alivePeers = Maps.filterKeys(members, k -> !k.equals(memberList.local()))));
        healthScoreGauge = Gauge.builder("basecluster.crdt.healthScore",
                () -> failureDetector.healthScoring().blockingFirst())
            .tags(Tags.of("replica_addr", memberList.local().getEndpoint().getId().toStringUtf8()))
            .register(Metrics.globalRegistry);
    }

    public void start() {
        if (state.compareAndSet(State.INIT, State.STARTING)) {
            failureDetector.start(new IProbingTargetSelector() {
                @Override
                public DirectProbingInfo targetForProbe() {
                    PriorityProbingTarget target = probingQueue.poll();
                    if (target == null) {
                        prepareForProbing();
                    }
                    // poll again
                    target = probingQueue.poll();
                    if (target == null) {
                        return new DirectProbingInfo(Optional.empty(), Collections.emptyList());
                    }


                    if (!alivePeers.containsKey(target.endpoint())) {
                        // host is not alive now
                        return targetForProbe();
                    }
                    List<ClusterMessage> piggybacked = new ArrayList<>();
                    if (suspicions.containsKey(target.endpoint())) {
                        // prepare piggybacked msg
                        piggybacked.add(ClusterMessage.newBuilder()
                            .setDoubt(Doubt.newBuilder()
                                .setEndpoint(target.endpoint())
                                .setIncarnation(target.incarnation())
                                .setReporter(memberList.local().getEndpoint())
                                .build())
                            .build());
                    }
                    return new DirectProbingInfo(Optional.of(target), piggybacked);
                }

                @Override
                public Collection<IProbingTarget> targetForIndirectProbes(IProbingTarget skip, int num) {
                    return randomProbingTargets(num, member -> !member.id().equals(skip.id()));
                }
            });
            state.set(State.STARTED);
        }
    }

    public void stop() {
        if (state.compareAndSet(State.STARTED, State.STOPPING)) {
            disposables.dispose();
            failureDetector.shutdown();
            Metrics.globalRegistry.remove(healthScoreGauge);
            state.set(State.STOPPED);
        }
    }

    private void prepareForProbing() {
        probingTargets.clear();
        for (Map.Entry<HostEndpoint, Integer> entry : alivePeers.entrySet()) {
            PriorityProbingTarget target = new PriorityProbingTarget(entry.getKey(), entry.getValue());
            probingQueue.add(target);
            probingTargets.add(target);
        }
    }

    private Collection<IProbingTarget> randomProbingTargets(int limit, Predicate<IProbingTarget> filter) {
        Set<IProbingTarget> selected = Sets.newHashSet();
        if (limit >= probingTargets.size()) {
            return probingTargets.stream()
                .filter(t -> alivePeers.containsKey(t.endpoint()))
                .collect(Collectors.toList());
        }
        while (limit > 0) {
            if (limit >= probingTargets.size()) {
                return randomProbingTargets(limit, filter);
            }
            int randomIdx = ThreadLocalRandom.current().nextInt(probingTargets.size());
            PriorityProbingTarget target = probingTargets.get(randomIdx);
            if (!alivePeers.containsKey(target.endpoint())) {
                probingTargets.remove(randomIdx);
                continue;
            }
            if (filter.test(target)) {
                selected.add(target);
                limit--;
            }
        }
        return selected;
    }

    private void observeNormalReport(IProbingTarget healthyTarget) {
        try {
            HostEndpoint endpoint = HostEndpoint.parseFrom(healthyTarget.id());
            Suspicion suspicion = suspicions.remove(endpoint);
            if (suspicion != null) {
                suspicion.task.dispose();
                Integer incarnation = alivePeers.get(endpoint);
                if (incarnation != null) {
                    // help others stop suspicion about the healthy target
                    messenger.spread(ClusterMessage.newBuilder()
                        .setEndorse(Endorse.newBuilder()
                            .setEndpoint(endpoint)
                            .setIncarnation(incarnation)
                            .setReporter(memberList.local().getEndpoint())
                            .build())
                        .build());
                }
            }
        } catch (InvalidProtocolBufferException e) {
            log.error("Unexpected exception", e);
        }
    }

    private void observeSuspicionReport(IProbingTarget suspected) {
        try {
            HostEndpoint suspectedEndpoint = HostEndpoint.parseFrom(suspected.id());
            Integer incarnation = alivePeers.get(suspectedEndpoint);
            if (incarnation == null) {
                log.debug("Ignore the suspicion about a non exist member[{}]", suspectedEndpoint);
                return;
            }

            if (suspicions.containsKey(suspectedEndpoint)) {
                log.debug("Member[{}] is already under suspicion", suspectedEndpoint);
                return;
            }

            log.debug("Got suspicion report[id={}, incarnation={}, reporterId={}] from failure detector",
                suspectedEndpoint, incarnation, memberList.local());
            Doubt doubt = Doubt.newBuilder()
                .setEndpoint(suspectedEndpoint)
                .setIncarnation(incarnation)
                .setReporter(memberList.local().getEndpoint())
                .build();
            startSuspicion(doubt);
            // spread the suspect among members including myself, so everybody could start a suspicion
            ClusterMessage suspectMsg = ClusterMessage.newBuilder().setDoubt(doubt).build();
            log.debug("Spread the suspicion:\n{}", doubt);
            messenger.spread(suspectMsg);
        } catch (InvalidProtocolBufferException e) {
            log.error("Unexpected exception", e);
        }
    }

    private void startSuspicion(Doubt doubt) {
        log.debug("Start death suspicion of the endpoint[{}] from reporter[{}]",
            doubt.getEndpoint(), doubt.getReporter());
        // confirmed should contain local
        Set<HostEndpoint> confirmed = new HashSet<>();
        confirmed.add(doubt.getReporter());
        final int k = Math.max(probingTargets.size(), 1) < suspicionMultiplier ? 0 : suspicionMultiplier - 2;
        long minInMS = calculateSuspicionTimeout();
        long maxInMS = suspicionMaxTimeoutMultiplier * minInMS;
        long start = Instant.now().toEpochMilli();

        Observable<Long> suspicionTimer = Observable.<Long>create(emitter -> {
            double coe = Math.log10(confirmed.size() + 1) / Math.log10(k + 1);
            long suspicionTimeout =
                k < 1 ? minInMS : Math.max(minInMS, Math.round(maxInMS - (maxInMS - minInMS) * coe));
            long elapsed = Instant.now().toEpochMilli() - start;
            long remaining = suspicionTimeout - elapsed;
            emitter.onNext(remaining);
        }).switchMap(remain -> {
            if (remain > 0) {
                return Observable.timer(remain, TimeUnit.MILLISECONDS);
            } else {
                return Observable.just(0L);
            }
        });
        Observable<Long> confirmStream = messenger
            .receive()
            .observeOn(scheduler)
            .filter(msg -> {
                ClusterMessage clusterMessage = msg.value().message;
                if (!clusterMessage.hasDoubt()) {
                    return false;
                }
                Doubt s = clusterMessage.getDoubt();
                if (s.getEndpoint().equals(doubt.getEndpoint())
                    && s.getIncarnation() >= doubt.getIncarnation()) {
                    if (confirmed.contains(s.getReporter())) {
                        return false;
                    } else if (confirmed.size() < k) {
                        confirmed.add(s.getReporter());
                        // this is a side-effect that re-gossip first k doubt done by reporter
                        if (memberList.local().equals(s.getReporter())) {
                            messenger.spread(clusterMessage);
                        }
                        return true;
                    }
                }
                return false;
            })
            .map(msg -> 0L)
            .switchMap(ignore -> Observable.error(new RuntimeException("Independent Suspicion Confirm")));
        suspicions.put(doubt.getEndpoint(),
            new Suspicion(doubt.getIncarnation(), Observable.merge(suspicionTimer, confirmStream)
                .retry()
                .observeOn(scheduler)
                .subscribe(ignore -> {
                    // suspicion timed out
                    suspicions.remove(doubt.getEndpoint());
                    log.debug("Suspicion timeout, dead peer[{}] confirmed by {} peers",
                        doubt, confirmed.size());
                    Integer suspectIncarnation = alivePeers.get(doubt.getEndpoint());
                    if (suspectIncarnation == null) {
                        return;
                    }
                    if (suspectIncarnation <= doubt.getIncarnation()) {
                        ClusterMessage msg;
                        if (memberList.isZombie(doubt.getEndpoint())) {
                            msg = ClusterMessage.newBuilder()
                                .setQuit(Quit.newBuilder()
                                    .setEndpoint(doubt.getEndpoint())
                                    .setIncarnation(doubt.getIncarnation())
                                    .build())
                                .build();
                        } else {
                            msg = ClusterMessage.newBuilder()
                                .setFail(Fail.newBuilder()
                                    .setEndpoint(doubt.getEndpoint())
                                    .setIncarnation(suspectIncarnation)
                                    .build())
                                .build();
                        }
                        log.debug("Spread message:\n{}", msg);
                        messenger.spread(msg);
                    }
                })));
    }

    private void handleMessage(ClusterMessage clusterMessage) {
        switch (clusterMessage.getClusterMessageTypeCase()) {
            case JOIN:
                handleJoin(clusterMessage.getJoin());
                break;
            case QUIT:
                handleQuit(clusterMessage.getQuit());
                break;
            case FAIL:
                handleFail(clusterMessage.getFail());
                break;
            case DOUBT:
                handleDoubt(clusterMessage.getDoubt());
                break;
            case ENDORSE:
                handleEndorse(clusterMessage.getEndorse());
                break;
        }
    }

    private void handleJoin(Join join) {
        HostMember joinMember = join.getMember();
        if (memberList.isZombie(joinMember.getEndpoint())) {
            // never stop suspecting a zombie member
            return;
        }
        stopSuspectIfNeeded(joinMember.getEndpoint(), joinMember.getIncarnation());
    }

    private void handleEndorse(Endorse endorse) {
        HostEndpoint endpoint = endorse.getEndpoint();
        if (memberList.isZombie(endpoint)) {
            // never stop suspecting a zombie member
            return;
        }
        stopSuspectIfNeeded(endpoint, endorse.getIncarnation());
    }

    private void handleQuit(Quit quit) {
        stopSuspectIfNeeded(quit.getEndpoint(), quit.getIncarnation());
    }

    private void handleFail(Fail fail) {
        stopSuspectIfNeeded(fail.getEndpoint(), fail.getIncarnation());
    }

    private void stopSuspectIfNeeded(HostEndpoint endpoint, int incarnation) {
        Suspicion suspicion = suspicions.get(endpoint);
        if (suspicion != null && suspicion.incarnation <= incarnation) {
            log.debug("Suspected member[{}, {}] is alive, stop suspecting it", endpoint, incarnation);
            suspicions.remove(endpoint, suspicion);
            suspicion.task.dispose();
        }
    }

    private void handleDoubt(Doubt doubt) {
        HostEndpoint suspectedEndpoint = doubt.getEndpoint();
        if (suspicions.containsKey(doubt.getEndpoint())) {
            if (!doubt.getReporter().equals(memberList.local())) {
                log.debug("Ignore suspect[{}] of reporter[{}], member already under suspicion",
                    doubt, doubt.getReporter());
            }
            return;
        }
        Integer suspectedIncarnation = alivePeers.get(suspectedEndpoint);
        if (suspectedIncarnation == null) {
            // ignore obsolete suspect
            log.debug("Ignore suspect[{}] by member[{}], no member found", doubt, doubt.getReporter());
            return;
        }
        if (doubt.getIncarnation() < suspectedIncarnation) {
            log.debug("Ignore suspect[{}] by member[{}], obsolete incarnation",
                doubt.getEndpoint(), doubt.getReporter());
            return;
        }
        if (memberList.local().equals(doubt.getEndpoint())) {
            // may be unhealthy
            failureDetector.penaltyHealth();
        } else {
            // start a suspicion if no one exist
            startSuspicion(doubt);
        }
    }

    private int clusterSize() {
        return Math.max(probingTargets.size(), 1);
    }

    private long calculateSuspicionTimeout() {
        double scale = Math.max(1.0, Math.log10(Math.max(clusterSize(), 1.0)));
        return Math.round(suspicionMultiplier * scale * failureDetector.baseProbeInterval().toMillis());
    }

    @AllArgsConstructor
    private static class Suspicion {
        final int incarnation;
        final Disposable task;
    }

    @EqualsAndHashCode
    private class PriorityProbingTarget implements IProbingTarget {
        @EqualsAndHashCode.Exclude
        final float p = ThreadLocalRandom.current().nextFloat();
        private final HostEndpoint endpoint;
        private final int incarnation;
        private final InetSocketAddress address;

        PriorityProbingTarget(HostEndpoint endpoint, int incarnation) {
            this.endpoint = endpoint;
            this.incarnation = incarnation;
            this.address = addressResolver.resolve(endpoint);
        }

        public float priority() {
            return p;
        }

        public HostMember member() {
            return HostMember.newBuilder().setEndpoint(endpoint).setIncarnation(incarnation).build();
        }

        public HostEndpoint endpoint() {
            return endpoint;
        }

        public int incarnation() {
            return incarnation;
        }

        @Override
        public ByteString id() {
            return endpoint.toByteString();
        }

        @Override
        public InetSocketAddress addr() {
            return address;
        }
    }
}
