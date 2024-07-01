package com.zachary.bifromq.basekv.store.api;

import com.zachary.bifromq.basekv.proto.Range;
import com.google.protobuf.ByteString;

public interface IKVWriter {

    void delete(ByteString key);

    void deleteRange(Range range);

    /**
     * Insert a non-exist key value pair, if the key is already exist, the result is undefined.
     *
     * @param key
     * @param value
     */
    void insert(ByteString key, ByteString value);

    /**
     * Put a key value pair, if the key is existed, its value will be overridden.
     *
     * @param key
     * @param value
     */
    void put(ByteString key, ByteString value);
}
