package com.zachary.bifromq.basekv.localengine;

import com.google.protobuf.ByteString;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.LongAdder;

public final class InMemoryKVEngineIterator implements IKVEngineIterator {
    private Map.Entry<ByteString, ByteString> currentEntry;
    private final LongAdder readKeys;
    private final LongAdder readBytes;
    private final ConcurrentSkipListMap<ByteString, ByteString> origData;
    private final ByteString startKey;
    private final ByteString endKey;
    private ConcurrentNavigableMap<ByteString, ByteString> dataSource;

    public InMemoryKVEngineIterator(ConcurrentSkipListMap<ByteString, ByteString> data,
                                    @Nullable ByteString start, @Nullable ByteString end,
                                    LongAdder readKeys,
                                    LongAdder readBytes) {
        origData = data;
        startKey = start;
        endKey = end;
        this.readKeys = readKeys;
        this.readBytes = readBytes;
        refresh();
    }

    @Override
    public ByteString key() {
        return currentEntry.getKey();
    }

    @Override
    public ByteString value() {
        ByteString value = currentEntry.getValue();
        readKeys.increment();
        readBytes.add(value.size());
        return value;
    }

    @Override
    public boolean isValid() {
        return currentEntry != null;
    }

    @Override
    public void next() {
        currentEntry = dataSource.higherEntry(currentEntry.getKey());
    }

    @Override
    public void prev() {
        currentEntry = dataSource.lowerEntry(currentEntry.getKey());
    }

    @Override
    public void seekToFirst() {
        currentEntry = dataSource.firstEntry();
    }

    @Override
    public void seekToLast() {
        currentEntry = dataSource.lastEntry();
    }

    @Override
    public void seek(ByteString target) {
        currentEntry = dataSource.ceilingEntry(target);
    }

    @Override
    public void seekForPrev(ByteString target) {
        currentEntry = dataSource.floorEntry(target);
    }

    @Override
    public void refresh() {
        ConcurrentSkipListMap<ByteString, ByteString> data = origData.clone();
        if (startKey == null && endKey == null) {
            dataSource = data;
        } else if (startKey == null) {
            dataSource = data.headMap(endKey);
        } else if (endKey == null) {
            dataSource = data.tailMap(startKey);
        } else {
            dataSource = data.subMap(startKey, endKey);
        }
        currentEntry = dataSource.firstEntry();
    }

    @Override
    public void close() {
        currentEntry = null;
    }
}
