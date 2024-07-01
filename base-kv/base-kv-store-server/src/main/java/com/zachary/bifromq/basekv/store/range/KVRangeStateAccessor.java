package com.zachary.bifromq.basekv.store.range;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class KVRangeStateAccessor {
    private final AtomicLong stateModVer = new AtomicLong();
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    KVRangeWriterMutator mutator() {
        return new KVRangeWriterMutator();
    }

    KVRangeReaderRefresher refresher() {
        return new KVRangeReaderRefresher();
    }

    public class KVRangeReaderRefresher {
        private final Lock rLock;
        private long readVer;

        KVRangeReaderRefresher() {
            this.rLock = rwLock.readLock();
            this.readVer = stateModVer.get();
        }

        void run(Runnable refresh) {
            rLock.lock();
            try {
                if (readVer == stateModVer.get()) {
                    return;
                }
                readVer = stateModVer.get();
                refresh.run();
            } finally {
                rLock.unlock();
            }
        }

        void lock() {
            rLock.lock();
        }

        void unlock() {
            rLock.unlock();
        }
    }

    public class KVRangeWriterMutator {
        private final Lock wLock;

        KVRangeWriterMutator() {
            this.wLock = rwLock.writeLock();
        }

        void run(Runnable mutation) {
            wLock.lock();
            try {
                mutation.run();
                stateModVer.incrementAndGet();
            } finally {
                wLock.unlock();
            }
        }

    }
}
