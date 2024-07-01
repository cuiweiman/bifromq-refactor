package com.zachary.bifromq.basekv.store;

import com.zachary.bifromq.basekv.proto.KVRangeStoreDescriptor;

public interface IKVRangeStoreDescriptorReporter {

    void start();

    void stop();

    void report(KVRangeStoreDescriptor storeDescriptor);

}
