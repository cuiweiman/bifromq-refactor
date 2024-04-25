
package com.zachary.bifromq.basecrdt.core.internal;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.util.LatticeIndexUtil;
import com.zachary.bifromq.basecrdt.core.util.Log;
import com.zachary.bifromq.basecrdt.proto.Dot;
import com.zachary.bifromq.basecrdt.proto.Replacement;
import com.zachary.bifromq.basecrdt.proto.StateLattice;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static java.util.Collections.emptyNavigableMap;
import static java.util.Collections.singleton;

@Slf4j
class InMemReplicaStateLattice implements IReplicaStateLattice {
    private final ByteString ownerReplicaId;
    private final AtomicLong event = new AtomicLong(0);

    // index into the events containing lattices which are populating current dot store
    private final ConcurrentMap<ByteString, NavigableMap<Long, Long>> latticeIndex = Maps.newConcurrentMap();
    // index into the historical events which was populating dot store
    private final ConcurrentMap<ByteString, NavigableMap<Long, Long>> historyIndex = Maps.newConcurrentMap();
    private final Map<Event, EventInfo> eventDAG = Maps.newHashMapWithExpectedSize(1024);
    private final Duration historyExpire;
    private final long maxCompactionDuration;

    InMemReplicaStateLattice(ByteString ownerReplicaId, Duration historyExpire, Duration maxCompactionTime) {
        this.ownerReplicaId = ownerReplicaId;
        this.historyExpire = historyExpire;
        this.maxCompactionDuration = maxCompactionTime.toNanos();
    }

    @Override
    public int size() {
        return eventDAG.size();
    }

    @Override
    public Duration historyDuration() {
        return historyExpire;
    }

    @Override
    public long nextEvent() {
        return event.incrementAndGet();
    }

    @Override
    public Iterator<StateLattice> lattices() {
        return new AbstractIterator<>() {
            private final Iterator<Event> eventItr = Iterators.concat(
                latticeIndex.entrySet()
                    .stream()
                    .map(e -> from(e.getKey(), e.getValue()))
                    .iterator());

            @Override
            protected StateLattice computeNext() {
                if (eventItr.hasNext()) {
                    Event event = eventItr.next();
                    return eventDAG.get(event).lattice.get();
                }
                return endOfData();
            }
        };
    }

    @Override
    public JoinDiff join(Iterable<Replacement> deltas) {
        Set<StateLattice> adds = Sets.newHashSet();
        Set<StateLattice> removes = Sets.newHashSet();
        join(deltas, adds, removes);
        return new JoinDiff() {
            @Override
            public Iterable<StateLattice> adds() {
                return adds;
            }

            @Override
            public Iterable<StateLattice> removes() {
                return removes;
            }
        };
    }

    private void join(Iterable<Replacement> delta, Set<StateLattice> adds, Set<StateLattice> rems) {
        for (Replacement replacement : delta) {
            assert replacement.getDotsCount() > 0;
            Dot dot = replacement.getDots(0);
            Event event = Event.from(dot);
            EventInfo eventInfo = eventDAG.get(event);
            if (eventInfo == null) {
                // new event
                eventInfo = EventInfo.from(dot);
                eventDAG.put(event, eventInfo);
                if (dot.hasLattice()) {
                    adds.add(dot.getLattice());
                    LatticeIndexUtil.remember(latticeIndex, dot.getReplicaId(), dot.getVer());
                } else {
                    LatticeIndexUtil.remember(historyIndex, dot.getReplicaId(), dot.getVer());
                }
            } else if (eventInfo.lattice.isPresent()) {
                if (!dot.hasLattice()) {
                    rems.add(eventInfo.lattice.get());
                    eventInfo.eol();
                    EventHistoryUtil.forget(latticeIndex, dot.getReplicaId(), dot.getVer());
                    LatticeIndexUtil.remember(historyIndex, dot.getReplicaId(), dot.getVer());
                }
            }
            // update the relation
            for (int i = 1; i < replacement.getDotsCount(); i++) {
                Event prevEvent = Event.from(replacement.getDots(i - 1));
                EventInfo prevInfo = eventDAG.get(prevEvent);
                Dot currentDot = replacement.getDots(i);
                Event currentEvent = Event.from(currentDot);
                EventInfo currentInfo = eventDAG.get(currentEvent);
                if (currentInfo == null) {
                    currentInfo = EventInfo.from(currentDot);
                    currentInfo.eol();
                    eventDAG.put(currentEvent, currentInfo);
                    LatticeIndexUtil.remember(historyIndex, currentEvent.replicaId, currentEvent.ver);
                } else if (currentInfo.lattice.isPresent()) {
                    rems.add(currentInfo.lattice.get());
                    currentInfo.eol();
                    EventHistoryUtil.forget(latticeIndex, currentEvent.replicaId, currentEvent.ver);
                    LatticeIndexUtil.remember(historyIndex, currentEvent.replicaId, currentEvent.ver);
                }
                prevInfo.replacing.add(currentEvent);
                currentInfo.replacedBy.add(prevEvent);
            }
        }
    }

    @Override
    public Optional<Iterable<Replacement>> delta(Map<ByteString, NavigableMap<Long, Long>> coveredLatticeIndex,
                                                 Map<ByteString, NavigableMap<Long, Long>> coveredHistoryIndex,
                                                 int maxLattices) {
        Set<Replacement> replacements = Sets.newHashSet();
        stopNewLattice:
        for (ByteString replicaId : randomize(latticeIndex.keySet())) {
            NavigableMap<Long, Long> latticeRanges = latticeIndex.get(replicaId);
            NavigableMap<Long, Long> coveredLattices = coveredLatticeIndex.getOrDefault(replicaId, emptyNavigableMap());
            NavigableMap<Long, Long> coveredHistory = coveredHistoryIndex.getOrDefault(replicaId, emptyNavigableMap());

            // excludes lattices already removed by peer
            NavigableMap<Long, Long> newLatticeRanges =
                EventHistoryUtil.diff(EventHistoryUtil.diff(latticeRanges, coveredLattices), coveredHistory);
            Iterator<Event> itr = from(replicaId, newLatticeRanges);
            while (itr.hasNext()) {
                Event event = itr.next();
                if (maxLattices > 0) {
                    EventInfo eventInfo = eventDAG.get(event);
                    assert eventInfo != null && eventInfo.lattice.isPresent();
                    subDAG(event, replacements, coveredLatticeIndex, coveredHistoryIndex);
                    maxLattices--;
                } else {
                    break stopNewLattice;
                }
            }
        }
        stopRemLattice:
        for (Map.Entry<ByteString, NavigableMap<Long, Long>> entry : randomize(coveredLatticeIndex.entrySet())) {
            ByteString replicaId = entry.getKey();
            NavigableMap<Long, Long> coveredLattices = entry.getValue();
            NavigableMap<Long, Long> latticeRanges = latticeIndex.getOrDefault(replicaId, emptyNavigableMap());
            NavigableMap<Long, Long> remLatticeRanges = EventHistoryUtil.diff(coveredLattices, latticeRanges);
            Iterator<Event> itr = from(replicaId, remLatticeRanges);
            while (itr.hasNext()) {
                Event event = itr.next();
                EventInfo eventInfo = eventDAG.get(event);
                if (eventInfo != null) {
                    assert !eventInfo.lattice.isPresent();
                    for (Event replacedByEvent : replacedByEvents(event)) {
                        if (!eventDAG.get(replacedByEvent).lattice.isPresent() && maxLattices > 0) {
                            subDAG(replacedByEvent, replacements, coveredLatticeIndex, coveredHistoryIndex);
                            maxLattices--;
                        } else {
                            break stopRemLattice;
                        }
                    }
                }
            }
        }
        if (!replacements.isEmpty()) {
            return Optional.of(replacements);
        }
        return Optional.empty();
    }

    @Override
    public Map<ByteString, NavigableMap<Long, Long>> latticeIndex() {
        return latticeIndex;
    }

    @Override
    public Map<ByteString, NavigableMap<Long, Long>> historyIndex() {
        return historyIndex;
    }

    public boolean compact() {
        try {
            boolean nextCompactNeeded = false;
            long start = System.nanoTime();
//            int total = eventDAG.size();
            for (ByteString replicaId : historyIndex.keySet()) {
                if (historyIndex.containsKey(replicaId)) {
                    Iterator<Event> eventItr = from(replicaId, Maps.newTreeMap(historyIndex.get(replicaId)));
                    while (eventItr.hasNext()) {
                        Event event = eventItr.next();
                        EventInfo eventInfo = eventDAG.get(event);
                        if (eventInfo == null) {
                            continue;
                        }
                        if (truncate(event)) {
                            nextCompactNeeded = true;
                        }
                        if (System.nanoTime() - start > maxCompactionDuration) {
                            nextCompactNeeded = true;
                            break;
                        }
                    }
                }
            }
            return nextCompactNeeded;
        } catch (Throwable e) {
            Log.error(log, "Replica {} compaction error", ownerReplicaId, e);
            return false;
        }
    }

    private <E> Iterable<E> randomize(Iterable<E> iterable) {
        List<E> shuffled = Lists.newArrayList(iterable);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    private boolean truncate(Event event) {
        EventInfo eventInfo = eventDAG.get(event);
        if (eventInfo == null) {
            return false;
        }
        if (!truncatable(event)) {
            return false;
        }
        if (eventInfo.ts < System.nanoTime() - historyExpire.toNanos()) {
            eventDAG.remove(event);
            EventHistoryUtil.forget(historyIndex, event.replicaId, event.ver);
            List<Event> relateEvents = new ArrayList<>();
            boolean moreToTruncate = false;
            for (Event replacedByEvent : eventInfo.replacedBy) {
                eventDAG.get(replacedByEvent).replacing.remove(event);
                relateEvents.add(replacedByEvent);
            }
            for (Event replacingEvent : eventInfo.replacing) {
                eventDAG.get(replacingEvent).replacedBy.remove(event);
                relateEvents.add(replacingEvent);
            }
            for (Event relate : relateEvents) {
                if (truncate(relate)) {
                    moreToTruncate = true;
                }
            }
            return moreToTruncate;
        } else {
            return true;
        }
    }

    private boolean truncatable(Event event) {
        EventInfo eventInfo = eventDAG.get(event);
        if (eventInfo.lattice.isPresent()) {
            // containing lattice
            return false;
        }
        for (Event replacedByEvent : eventInfo.replacedBy) {
            EventInfo replacedByEventInfo = eventDAG.get(replacedByEvent);
            if (replacedByEventInfo.lattice.isPresent()) {
                // replaced by lattice event
                return false;
            }
            if (replacedByEventInfo.replacedBy.isEmpty()) {
                // replaced by non-intermediate event
                return false;
            }
        }
        return true;
    }

    private Iterator<Event> from(ByteString replicaId, NavigableMap<Long, Long> ranges) {
        return Iterators.transform(Iterators.concat(Maps.newTreeMap(ranges).entrySet()
                .stream()
                .map(r -> LongStream.rangeClosed(r.getKey(), r.getValue()).iterator()).iterator()),
            i -> Event.from(replicaId, i));
    }

    private void subDAG(Event event, Set<Replacement> replacements,
                        Map<ByteString, NavigableMap<Long, Long>> coveredLatticeIndex,
                        Map<ByteString, NavigableMap<Long, Long>> coveredHistoryIndex) {
        LinkedList<Event> toVisits = Lists.newLinkedList(singleton(event));
        LinkedList<Event> events = Lists.newLinkedList();// events in replacing order
        while (!toVisits.isEmpty()) {
            Event current = toVisits.remove(0);
            EventInfo eventInfo = eventDAG.get(current);

            events.add(current);

            if (eventInfo.replacing.isEmpty()
                || EventHistoryUtil.remembering(coveredLatticeIndex, current.replicaId, current.ver)
                || EventHistoryUtil.remembering(coveredHistoryIndex, current.replicaId, current.ver)) {
                // build path
                replacements.add(Replacement.newBuilder()
                    .addAllDots(events.stream().map(e -> {
                        EventInfo info = eventDAG.get(e);
                        if (info.lattice.isPresent()) {
                            return ProtoUtils.dot(e.replicaId, e.ver, info.lattice.get());
                        }
                        return ProtoUtils.dot(e.replicaId, e.ver);
                    }).collect(Collectors.toList()))
                    .build());
                // rewind path until next visit event is replaced by the last event in the path
                if (!toVisits.isEmpty()) {
                    Event nextVisitEvent = toVisits.getFirst();
                    EventInfo nextVisitEventInfo = eventDAG.get(nextVisitEvent);
                    while (!events.isEmpty()) {
                        events.removeLast();
                        Event lastEvent = events.getLast();
                        if (nextVisitEventInfo.replacedBy.contains(lastEvent)) {
                            break;
                        }
                    }
                }
            } else {
                eventInfo.replacing.forEach(toVisits::addFirst);
            }
        }
    }

    private Iterable<Event> replacedByEvents(Event event) {
        EventInfo eventInfo = eventDAG.get(event);
        assert eventInfo != null;
        if (eventInfo.replacedBy.isEmpty()) {
            return singleton(event);
        }
        return Iterables.concat(eventInfo.replacedBy.stream().map(this::replacedByEvents).collect(Collectors.toSet()));
    }

    @EqualsAndHashCode
    private static class Event {
        final ByteString replicaId;
        final long ver;

        private Event(ByteString replicaId, long ver) {
            this.replicaId = replicaId;
            this.ver = ver;
        }

        private Event(Dot dot) {
            replicaId = dot.getReplicaId();
            ver = dot.getVer();
        }

        static Event from(ByteString replicaId, long ver) {
            return new Event(replicaId, ver);
        }

        static Event from(Dot dot) {
            return new Event(dot);
        }
    }

    private static class EventInfo {
        final Set<Event> replacing = Sets.newHashSet();
        final Set<Event> replacedBy = Sets.newHashSet();
        Optional<StateLattice> lattice;
        long ts;

        private EventInfo() {
            this.lattice = Optional.empty();
            ts = System.nanoTime();
        }

        private EventInfo(StateLattice lattice) {
            this.lattice = Optional.of(lattice);
            ts = Long.MAX_VALUE;
        }

        void eol() {
            lattice = Optional.empty();
            ts = System.nanoTime();
        }

        static EventInfo from(Dot dot) {
            return dot.hasLattice() ? new EventInfo(dot.getLattice()) : new EventInfo();
        }
    }
}
