package com.zachary.bifromq.basekv.raft;

import com.zachary.bifromq.basekv.raft.proto.ClusterConfig;
import com.zachary.bifromq.basekv.raft.proto.LogEntry;
import com.zachary.bifromq.basekv.raft.proto.Snapshot;
import com.zachary.bifromq.basekv.raft.proto.Voting;
import com.google.protobuf.ByteString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public final class InMemoryStateStore implements IRaftStateStore {
    private static final StableListener DEFAULT_STABLE_LISTENER = stableListener -> {
    };
    private final String id;
    private final LinkedList<Long> configEntryIndexes = new LinkedList<>();
    private final long flushDelayInMS;
    private final ScheduledExecutorService flusher = Executors.newSingleThreadScheduledExecutor();
    private volatile long stabilizingIndex;
    private final AtomicBoolean flushTaskScheduled = new AtomicBoolean(false);
    private final LinkedList<LogEntry> logEntries = new LinkedList<>(); // log entry start from 1
    private Snapshot latestSnapshot;

    private final AtomicLong currentTerm = new AtomicLong(0);

    private Voting currentVoting;

    private StableListener stableListener = DEFAULT_STABLE_LISTENER;

    public InMemoryStateStore(String id, Snapshot latestSnapshot, long flushDelayInMS) {
        this.id = id;
        this.latestSnapshot = latestSnapshot;
        this.flushDelayInMS = flushDelayInMS;
        // offset 0 is always a dummy entry pointing to last entry in snapshot
        logEntries.add(LogEntry.newBuilder()
            .setTerm(latestSnapshot.getTerm())
            .setIndex(latestSnapshot.getIndex())
            .build());
    }

    public InMemoryStateStore(String id, Snapshot latestSnapshot) {
        this(id, latestSnapshot, 5L);
    }

    @Override
    public String local() {
        return id;
    }

    @Override
    public long currentTerm() {
        return currentTerm.get();
    }

    @Override
    public void saveTerm(long term) {
        assert term >= currentTerm.get();
        currentTerm.set(term);
    }

    @Override
    public Optional<Voting> currentVoting() {
        return Optional.ofNullable(currentVoting);
    }

    @Override
    public void saveVoting(Voting voting) {
        currentVoting = voting;
    }

    @Override
    public ClusterConfig latestClusterConfig() {
        if (configEntryIndexes.isEmpty()) {
            return latestSnapshot.getClusterConfig();
        } else {
            long latestConfigEntryIndex = configEntryIndexes.getLast();
            LogEntry logEntry = logEntries.get(offset(latestConfigEntryIndex));
            assert logEntry.hasConfig();
            return logEntry.getConfig();
        }
    }

    @Override
    public void applySnapshot(Snapshot snapshot) {
        long snapLastIndex = snapshot.getIndex();
        long snapLastTerm = snapshot.getTerm();
        Optional<LogEntry> lastEntryInSS = entryAt(snapLastIndex);
        if (lastEntryInSS.isPresent() && lastEntryInSS.get().getTerm() == snapLastTerm) {
            // the snapshot represents partial history, it happens when compacting
            latestSnapshot = snapshot;
            long truncateBefore = Math.min(lastIndex(), snapLastIndex) + 1;
            while (firstIndex() < truncateBefore) {
                logEntries.remove(0);
            }
            while (!configEntryIndexes.isEmpty() && configEntryIndexes.getFirst() < truncateBefore) {
                configEntryIndexes.removeFirst();
            }
            // update the dummy entry at offset 0
            logEntries.set(0, logEntries.getFirst()
                .toBuilder()
                .setIndex(snapLastIndex)
                .setTerm(snapLastTerm)
                .setData(ByteString.EMPTY)
                .build());
        } else {
            // the snapshot represents a different history, it happens when installing snapshot from leader
            latestSnapshot = snapshot;
            // offset 0 is always a dummy entry pointing to last entry in snapshot
            logEntries.clear();
            configEntryIndexes.clear();
            logEntries.add(LogEntry.newBuilder()
                .setTerm(latestSnapshot.getTerm())
                .setIndex(latestSnapshot.getIndex())
                .build());
        }
    }

    @Override
    public Snapshot latestSnapshot() {
        return latestSnapshot;
    }

    @Override
    public long firstIndex() {
        return logEntries.getFirst().getIndex() + 1;
    }

    @Override
    public long lastIndex() {
        return logEntries.getLast().getIndex();
    }

    @Override
    public Optional<LogEntry> entryAt(long index) {
        if (index < firstIndex() || index > lastIndex()) {
            return Optional.ofNullable(null);
        }
        return Optional.of(logEntries.get(offset(index)));
    }

    @Override
    public Iterator<LogEntry> entries(long lo, long hi, long maxSize) {
        if (lo < firstIndex()) {
            throw new IndexOutOfBoundsException("lo must not be less than firstIndex");
        }
        if (hi > lastIndex() + 1) {
            throw new IndexOutOfBoundsException("hi must not be greater than lastIndex");
        }
        if (maxSize < 0) {
            maxSize = Long.MAX_VALUE;
        }
        List<LogEntry> ret = new ArrayList();
        long size = 0;
        while (lo < hi && size <= maxSize) {
            LogEntry entry = logEntries.get(offset(lo));
            ret.add(entry);
            switch (entry.getTypeCase()) {
                case DATA:
                    size += entry.getData().size();
                    break;
                case CONFIG:
                    size += entry.getConfig().getSerializedSize();
            }
            lo++;
        }
        return ret.iterator();
    }

    @Override
    public void append(List<LogEntry> entries, boolean flush) {
        assert !entries.isEmpty();
        LogEntry startEntry = entries.get(0);
        if (lastIndex() >= firstIndex()) {
            if (firstIndex() > startEntry.getIndex() || lastIndex() + 1 < startEntry.getIndex()) {
                throw new IndexOutOfBoundsException("log index[" + startEntry.getIndex()
                    + "] must be in [" + firstIndex() + "," + lastIndex() + 1 + "]");
            }
        } else {
            if (startEntry.getIndex() != firstIndex()) {
                throw new IndexOutOfBoundsException("log index must start from " + firstIndex());
            }
        }
        long afterIndex = startEntry.getIndex() - 1;
        ListIterator<LogEntry> itr = logEntries.listIterator(offset(afterIndex) + 1);
        while (itr.hasNext()) {
            itr.next();
            itr.remove();
        }
        while (!configEntryIndexes.isEmpty() && configEntryIndexes.get(configEntryIndexes.size() - 1) > afterIndex) {
            configEntryIndexes.remove(configEntryIndexes.size() - 1);
        }
        entries.forEach(entry -> {
            if (entry.hasConfig()) {
                configEntryIndexes.add(entry.getIndex());
            }
            logEntries.add(entry);
        });
        if (flush) {
            immediateFlush(entryAt(lastIndex()).get().getIndex());
        } else {
            stabilizingIndex = lastIndex();
            scheduleFlushTask();
        }
    }

    @Override
    public void addStableListener(StableListener listener) {
        stableListener = listener;
    }

    public CompletableFuture<Void> stop() {
        flusher.shutdown();
        try {
            flusher.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        } finally {
            stableListener = DEFAULT_STABLE_LISTENER;
        }
        return CompletableFuture.completedFuture(null);
    }

    int offset(long index) {
        return (int) (index - firstIndex() + 1);
    }

    void immediateFlush(long index) {
        // flush in calling thread should not alter the semantic
        stableListener.onStabilized(index);
    }

    void flush() {
        stableListener.onStabilized(stabilizingIndex);
        flushTaskScheduled.set(false);
    }

    void scheduleFlushTask() {
        if (flushTaskScheduled.compareAndSet(false, true)) {
            flusher.schedule(this::flush, flushDelayInMS, TimeUnit.MILLISECONDS);
        }
    }
}
