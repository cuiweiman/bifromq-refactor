package com.zachary.bifromq.basekv.localengine;

import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.ReadOptions;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksIterator;
import org.rocksdb.Slice;
import org.rocksdb.Snapshot;

import javax.annotation.Nullable;
import java.lang.ref.Cleaner;

import static com.google.protobuf.UnsafeByteOperations.unsafeWrap;

@Slf4j
class RocksDBKVEngineIterator implements IKVEngineIterator {
    private static final Cleaner CLEANER = Cleaner.create();

    private static class NativeState implements Runnable {
        private final RocksIterator itr;
        private final ReadOptions readOptions;

        private NativeState(RocksIterator itr, ReadOptions readOptions) {
            this.itr = itr;
            this.readOptions = readOptions;
        }

        @Override
        public void run() {
            itr.close();
            readOptions.close();
        }
    }

    private final RocksIterator rocksIterator;
    private final Cleaner.Cleanable onClose;

    RocksDBKVEngineIterator(RocksDB db, ColumnFamilyHandle cfHandle,
                            @Nullable ByteString start, @Nullable ByteString end) {
        this(db, null, cfHandle, start, end);
    }

    RocksDBKVEngineIterator(RocksDB db,
                            Snapshot snapshot,
                            ColumnFamilyHandle cfHandle,
                            @Nullable ByteString start,
                            @Nullable ByteString end) {
        ReadOptions readOptions = new ReadOptions();
        if (snapshot != null) {
            readOptions.setSnapshot(snapshot);
        }
        if (start != null) {
            readOptions.setIterateLowerBound(new Slice(start.toByteArray()));
        }
        if (end != null) {
            readOptions.setIterateUpperBound(new Slice(end.toByteArray()));
        }
        rocksIterator = db.newIterator(cfHandle, readOptions);
        onClose = CLEANER.register(this, new NativeState(rocksIterator, readOptions));
    }

    @Override
    public ByteString key() {
        return unsafeWrap(rocksIterator.key());
    }

    @Override
    public ByteString value() {
        return unsafeWrap(rocksIterator.value());
    }

    @Override
    public boolean isValid() {
        return rocksIterator.isValid();
    }

    @Override
    public void next() {
        rocksIterator.next();
    }

    @Override
    public void prev() {
        rocksIterator.prev();
    }

    @Override
    public void seekToFirst() {
        rocksIterator.seekToFirst();
    }

    @Override
    public void seekToLast() {
        rocksIterator.seekToLast();
    }

    @Override
    public void seek(ByteString target) {
        rocksIterator.seek(target.toByteArray());
    }

    @Override
    public void seekForPrev(ByteString target) {
        rocksIterator.seekForPrev(target.toByteArray());
    }

    @Override
    public void refresh() {
        try {
            rocksIterator.refresh();
        } catch (Throwable e) {
            throw new KVEngineException("Unable to refresh iterator", e);
        }
    }

    @Override
    public void close() {
        onClose.clean();
    }
}
